package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentConfirmationDao {
  @Query("SELECT * FROM payment_confirmations ORDER BY timestampMillis DESC")
  fun observeAllConfirmations(): Flow<List<PaymentConfirmationEntity>>

  @Query("SELECT * FROM payment_confirmations WHERE id = :id LIMIT 1")
  fun observeById(id: Int): Flow<PaymentConfirmationEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertConfirmation(entity: PaymentConfirmationEntity): Long

  @Query("DELETE FROM payment_confirmations WHERE id = :id")
  suspend fun deleteById(id: Int)

  @Query("UPDATE payment_confirmations SET isUnlocked = 0")
  suspend fun markAllLocked()
}
