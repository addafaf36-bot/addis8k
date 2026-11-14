package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PaymentAccount
import com.example.data.PaymentAccountsCatalog
import com.example.data.PaymentConfirmationEntity
import com.example.data.PaymentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ReferenceValidationError {
  EMPTY,
  TOO_SHORT,
}

data class PaymentUiState(
  val accounts: List<PaymentAccount> = PaymentAccountsCatalog.accounts,
  val selectedAccount: PaymentAccount = PaymentAccountsCatalog.accounts.first(),
  val referenceInput: String = "",
  val validationError: ReferenceValidationError? = null,
  val copiedAccountId: String? = null,
  val isSubmitting: Boolean = false,
  val confirmations: List<PaymentConfirmationEntity> = emptyList(),
) {
  val latestUnlockedConfirmation: PaymentConfirmationEntity?
    get() = confirmations.firstOrNull { it.isUnlocked }

  val isDeviceUnlocked: Boolean
    get() = latestUnlockedConfirmation != null
}

private data class FormDraftState(
  val selectedAccount: PaymentAccount = PaymentAccountsCatalog.accounts.first(),
  val referenceInput: String = "",
  val validationError: ReferenceValidationError? = null,
  val copiedAccountId: String? = null,
  val isSubmitting: Boolean = false,
)

class PaymentViewModel(private val repository: PaymentRepository) : ViewModel() {

  private val draftState = MutableStateFlow(FormDraftState())
  private var copyResetJob: Job? = null

  val uiState: StateFlow<PaymentUiState> =
    combine(draftState, repository.confirmations) { draft, confirmations ->
        PaymentUiState(
          accounts = repository.accounts,
          selectedAccount = draft.selectedAccount,
          referenceInput = draft.referenceInput,
          validationError = draft.validationError,
          copiedAccountId = draft.copiedAccountId,
          isSubmitting = draft.isSubmitting,
          confirmations = confirmations,
        )
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PaymentUiState(accounts = repository.accounts),
      )

  fun selectAccount(account: PaymentAccount) {
    draftState.update { it.copy(selectedAccount = account) }
  }

  fun onReferenceChanged(value: String) {
    draftState.update { it.copy(referenceInput = value, validationError = null) }
  }

  fun markAccountCopied(account: PaymentAccount) {
    copyResetJob?.cancel()
    draftState.update { it.copy(copiedAccountId = account.id, selectedAccount = account) }
    copyResetJob =
      viewModelScope.launch {
        delay(2_200)
        draftState.update { state ->
          if (state.copiedAccountId == account.id) state.copy(copiedAccountId = null) else state
        }
      }
  }

  fun confirmPayment(onSuccess: (Int) -> Unit) {
    val current = draftState.value
    val trimmed = current.referenceInput.trim()

    val error =
      when {
        trimmed.isEmpty() -> ReferenceValidationError.EMPTY
        trimmed.length < 4 -> ReferenceValidationError.TOO_SHORT
        else -> null
      }

    if (error != null) {
      draftState.update { it.copy(validationError = error) }
      return
    }

    if (current.isSubmitting) return

    viewModelScope.launch {
      draftState.update { it.copy(isSubmitting = true, validationError = null) }
      val insertedId = repository.submitConfirmation(current.selectedAccount, trimmed)
      draftState.update {
        it.copy(
          referenceInput = "",
          validationError = null,
          isSubmitting = false,
        )
      }
      onSuccess(insertedId)
    }
  }

  fun resetForm() {
    draftState.update {
      it.copy(
        selectedAccount = repository.accounts.first(),
        referenceInput = "",
        validationError = null,
      )
    }
  }

  fun observeReceipt(id: Int): Flow<PaymentConfirmationEntity?> =
    repository.observeConfirmation(id)

  fun deleteConfirmation(id: Int) {
    viewModelScope.launch { repository.deleteConfirmation(id) }
  }

  fun resetLockStatus() {
    viewModelScope.launch { repository.resetDeviceLock() }
  }

  class Factory(private val repository: PaymentRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
        return PaymentViewModel(repository) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
  }
}
