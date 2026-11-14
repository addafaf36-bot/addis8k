package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.PaymentAccount
import com.example.ui.PaymentUiState
import com.example.ui.ReferenceValidationError
import com.example.ui.components.BankAccountCard
import com.example.ui.components.UnlockStatusBanner
import com.example.ui.theme.JetBrainsMonoFamily
import com.example.ui.theme.UnlockedEmerald

@Composable
fun PaymentScreen(
  uiState: PaymentUiState,
  onSelectAccount: (PaymentAccount) -> Unit,
  onCopyAccount: (PaymentAccount) -> Unit,
  onReferenceChanged: (String) -> Unit,
  onPasteReference: () -> Unit,
  onConfirmPayment: () -> Unit,
  onBackAction: () -> Unit,
  onViewLatestReceipt: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val scrollState = rememberScrollState()

  BoxWithConstraints(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    val isWideScreen = maxWidth >= 660.dp

    Column(
      modifier =
        Modifier.widthIn(max = 980.dp)
          .fillMaxWidth()
          .verticalScroll(scrollState)
          .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      UnlockStatusBanner(
        isUnlocked = uiState.isDeviceUnlocked,
        onViewReceiptClick =
          uiState.latestUnlockedConfirmation?.let { latest ->
            { onViewLatestReceipt(latest.id) }
          },
      )

      if (isWideScreen) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.Top,
        ) {
          BankAccountsSection(
            accounts = uiState.accounts,
            selectedAccount = uiState.selectedAccount,
            copiedAccountId = uiState.copiedAccountId,
            onSelectAccount = onSelectAccount,
            onCopyAccount = onCopyAccount,
            modifier = Modifier.weight(1.05f),
          )
          PaymentConfirmationForm(
            uiState = uiState,
            onSelectAccount = onSelectAccount,
            onReferenceChanged = onReferenceChanged,
            onPasteReference = onPasteReference,
            onConfirmPayment = onConfirmPayment,
            onBackAction = onBackAction,
            modifier = Modifier.weight(0.95f),
          )
        }
      } else {
        BankAccountsSection(
          accounts = uiState.accounts,
          selectedAccount = uiState.selectedAccount,
          copiedAccountId = uiState.copiedAccountId,
          onSelectAccount = onSelectAccount,
          onCopyAccount = onCopyAccount,
        )
        PaymentConfirmationForm(
          uiState = uiState,
          onSelectAccount = onSelectAccount,
          onReferenceChanged = onReferenceChanged,
          onPasteReference = onPasteReference,
          onConfirmPayment = onConfirmPayment,
          onBackAction = onBackAction,
        )
      }

      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}

@Composable
private fun BankAccountsSection(
  accounts: List<PaymentAccount>,
  selectedAccount: PaymentAccount,
  copiedAccountId: String?,
  onSelectAccount: (PaymentAccount) -> Unit,
  onCopyAccount: (PaymentAccount) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = stringResource(R.string.section_bank_accounts),
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onBackground,
    )

    accounts.forEach { account ->
      BankAccountCard(
        account = account,
        isSelected = account.id == selectedAccount.id,
        isCopied = account.id == copiedAccountId,
        onSelect = { onSelectAccount(account) },
        onCopy = { onCopyAccount(account) },
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentConfirmationForm(
  uiState: PaymentUiState,
  onSelectAccount: (PaymentAccount) -> Unit,
  onReferenceChanged: (String) -> Unit,
  onPasteReference: () -> Unit,
  onConfirmPayment: () -> Unit,
  onBackAction: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var dropdownExpanded by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  val errorMessage =
    when (uiState.validationError) {
      ReferenceValidationError.EMPTY -> stringResource(R.string.error_empty_ref)
      ReferenceValidationError.TOO_SHORT -> stringResource(R.string.error_short_ref)
      null -> null
    }

  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.large,
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 1.dp,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(18.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Text(
        text = stringResource(R.string.section_confirm_payment),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface,
      )

      // Payment Method Dropdown
      ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = !dropdownExpanded },
      ) {
        OutlinedTextField(
          value = uiState.selectedAccount.methodOption,
          onValueChange = {},
          readOnly = true,
          label = { Text(stringResource(R.string.label_payment_method)) },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
          modifier =
            Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .fillMaxWidth()
              .testTag("payment_method_dropdown"),
          shape = MaterialTheme.shapes.small,
          singleLine = true,
        )

        ExposedDropdownMenu(
          expanded = dropdownExpanded,
          onDismissRequest = { dropdownExpanded = false },
        ) {
          uiState.accounts.forEach { account ->
            DropdownMenuItem(
              text = {
                Text(
                  text = account.methodOption,
                  style = MaterialTheme.typography.bodyLarge,
                )
              },
              onClick = {
                onSelectAccount(account)
                dropdownExpanded = false
              },
              modifier = Modifier.testTag("dropdown_option_${account.id}"),
            )
          }
        }
      }

      // Transaction Reference Input
      OutlinedTextField(
        value = uiState.referenceInput,
        onValueChange = onReferenceChanged,
        label = { Text(stringResource(R.string.label_transaction_ref)) },
        placeholder = { Text(stringResource(R.string.placeholder_transaction_ref)) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Filled.ReceiptLong,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
          )
        },
        trailingIcon = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            if (uiState.referenceInput.isNotEmpty()) {
              IconButton(
                onClick = { onReferenceChanged("") },
                modifier = Modifier.minimumInteractiveComponentSize().testTag("clear_ref_button"),
              ) {
                Icon(
                  imageVector = Icons.Filled.Clear,
                  contentDescription = stringResource(R.string.cd_clear_input),
                )
              }
            } else {
              IconButton(
                onClick = onPasteReference,
                modifier = Modifier.minimumInteractiveComponentSize().testTag("paste_ref_button"),
              ) {
                Icon(
                  imageVector = Icons.Filled.ContentPaste,
                  contentDescription = stringResource(R.string.cd_paste_input),
                  tint = MaterialTheme.colorScheme.primary,
                )
              }
            }
          }
        },
        isError = errorMessage != null,
        supportingText =
          errorMessage?.let { msg ->
            { Text(text = msg, color = MaterialTheme.colorScheme.error) }
          },
        textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = JetBrainsMonoFamily),
        singleLine = true,
        keyboardOptions =
          KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Done,
          ),
        keyboardActions =
          KeyboardActions(
            onDone = {
              focusManager.clearFocus()
              onConfirmPayment()
            }
          ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth().testTag("transaction_ref_input"),
      )

      // Confirm & Unlock Button
      Button(
        onClick = {
          focusManager.clearFocus()
          onConfirmPayment()
        },
        enabled = !uiState.isSubmitting,
        modifier =
          Modifier.fillMaxWidth()
            .minimumInteractiveComponentSize()
            .testTag("confirm_payment_button"),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(vertical = 15.dp, horizontal = 16.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = UnlockedEmerald,
            contentColor = MaterialTheme.colorScheme.onPrimary,
          ),
      ) {
        if (uiState.isSubmitting) {
          CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.onPrimary,
          )
        } else {
          Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = stringResource(R.string.btn_confirm_unlock),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          )
        }
      }

      // Back / Reset Button (ተመለስ)
      FilledTonalButton(
        onClick = {
          focusManager.clearFocus()
          onBackAction()
        },
        modifier =
          Modifier.fillMaxWidth()
            .minimumInteractiveComponentSize()
            .testTag("back_action_button"),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
        colors =
          ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
          ),
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = stringResource(R.string.btn_back),
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }
  }
}
