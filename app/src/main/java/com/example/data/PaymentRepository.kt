package com.example.data

import kotlinx.coroutines.flow.Flow

class PaymentRepository(private val dao: PaymentConfirmationDao) {
  val accounts: List<PaymentAccount> = PaymentAccountsCatalog.accounts

  val confirmations: Flow<List<PaymentConfirmationEntity>> = dao.observeAllConfirmations()

  fun observeConfirmation(id: Int): Flow<PaymentConfirmationEntity?> = dao.observeById(id)

  suspend fun submitConfirmation(
    account: PaymentAccount,
    referenceNumber: String,
  ): Int {
    val entity =
      PaymentConfirmationEntity(
        methodOption = account.methodOption,
        accountNumber = account.accountNumber,
        recipientName = account.ownerName,
        referenceNumber = referenceNumber.trim(),
        timestampMillis = System.currentTimeMillis(),
        isUnlocked = true,
      )
    return dao.insertConfirmation(entity).toInt()
  }

  suspend fun deleteConfirmation(id: Int) {
    dao.deleteById(id)
  }

  suspend fun resetDeviceLock() {
    dao.markAllLocked()
  }
}
