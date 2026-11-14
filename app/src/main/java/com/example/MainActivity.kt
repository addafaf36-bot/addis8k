package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.PaymentRepository
import com.example.ui.KifyaPayApp
import com.example.ui.PaymentViewModel
import com.example.ui.theme.KifyaPayTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = AppDatabase.getInstance(applicationContext)
    val repository = PaymentRepository(database.paymentConfirmationDao())

    setContent {
      KifyaPayTheme {
        val paymentViewModel: PaymentViewModel =
          viewModel(factory = PaymentViewModel.Factory(repository))
        KifyaPayApp(viewModel = paymentViewModel)
      }
    }
  }
}
