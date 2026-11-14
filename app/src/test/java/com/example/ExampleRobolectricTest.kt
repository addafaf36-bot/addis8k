package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.PaymentAccountsCatalog
import com.example.data.PaymentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ክፍያ ማረጋገጫ", appName)
  }

  @Test
  fun `accounts catalog contains all four Ethiopian accounts`() {
    val accounts = PaymentAccountsCatalog.accounts
    assertEquals(4, accounts.size)
    assertEquals("0942320888", accounts[0].accountNumber)
    assertEquals("0925792262", accounts[1].accountNumber)
    assertEquals("1000282540634", accounts[2].accountNumber)
    assertEquals("66833787", accounts[3].accountNumber)
    assertTrue(accounts.all { it.ownerName == "አዲሴ አራጋው" })
  }

  @Test
  fun `submit confirmation persists unlocked record in Room`() = runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db =
      Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    try {
      val repo = PaymentRepository(db.paymentConfirmationDao())
      val cbeAccount = PaymentAccountsCatalog.accounts[2]
      val insertedId = repo.submitConfirmation(cbeAccount, "FT241128991")

      val records = repo.confirmations.first()
      assertEquals(1, records.size)
      assertEquals(insertedId, records.first().id)
      assertEquals("FT241128991", records.first().referenceNumber)
      assertEquals("ንግድ ባንክ (CBE)", records.first().methodOption)
      assertTrue(records.first().isUnlocked)
    } finally {
      db.close()
    }
  }
}
