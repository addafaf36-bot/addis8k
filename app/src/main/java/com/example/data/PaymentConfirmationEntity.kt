package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_confirmations")
data class PaymentConfirmationEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val methodOption: String,
  val accountNumber: String,
  val recipientName: String,
  val referenceNumber: String,
  val timestampMillis: Long = System.currentTimeMillis(),
  val isUnlocked: Boolean = true,
)
