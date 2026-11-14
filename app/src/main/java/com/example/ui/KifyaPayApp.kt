package com.example.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.ReceiptScreen
import kotlinx.coroutines.launch

const val ROUTE_PAYMENT = "payment"
const val ROUTE_HISTORY = "history"
const val ROUTE_RECEIPT = "receipt/{receiptId}"

fun receiptRoute(receiptId: Int): String = "receipt/$receiptId"

@Composable
fun KifyaPayApp(viewModel: PaymentViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val navController = rememberNavController()
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route

  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  fun showMessage(message: String) {
    scope.launch {
      snackbarHostState.currentSnackbarData?.dismiss()
      snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
    }
  }

  val showTopLevelNav = currentRoute == ROUTE_PAYMENT || currentRoute == ROUTE_HISTORY

  BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val useNavRail = maxWidth >= 660.dp

    Scaffold(
      modifier = Modifier.fillMaxSize(),
      contentWindowInsets = WindowInsets.safeDrawing,
      containerColor = MaterialTheme.colorScheme.background,
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      bottomBar = {
        if (showTopLevelNav && !useNavRail) {
          NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
            NavigationBarItem(
              selected = currentRoute == ROUTE_PAYMENT,
              onClick = {
                if (currentRoute != ROUTE_PAYMENT) {
                  navController.navigate(ROUTE_PAYMENT) {
                    popUpTo(ROUTE_PAYMENT) { inclusive = true }
                    launchSingleTop = true
                  }
                }
              },
              icon = {
                Icon(
                  imageVector =
                    if (currentRoute == ROUTE_PAYMENT) Icons.Filled.AccountBalanceWallet
                    else Icons.Outlined.AccountBalanceWallet,
                  contentDescription = stringResource(R.string.nav_payment),
                )
              },
              label = { Text(stringResource(R.string.nav_payment)) },
              modifier = Modifier.testTag("nav_payment"),
            )
            NavigationBarItem(
              selected = currentRoute == ROUTE_HISTORY,
              onClick = {
                if (currentRoute != ROUTE_HISTORY) {
                  navController.navigate(ROUTE_HISTORY) {
                    popUpTo(ROUTE_PAYMENT) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                  }
                }
              },
              icon = {
                BadgedBox(
                  badge = {
                    if (uiState.confirmations.isNotEmpty()) {
                      Badge { Text("${uiState.confirmations.size}") }
                    }
                  }
                ) {
                  Icon(
                    imageVector =
                      if (currentRoute == ROUTE_HISTORY) Icons.Filled.History
                      else Icons.Outlined.History,
                    contentDescription = stringResource(R.string.nav_history),
                  )
                }
              },
              label = { Text(stringResource(R.string.nav_history)) },
              modifier = Modifier.testTag("nav_history"),
            )
          }
        }
      },
    ) { innerPadding ->
      Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        if (showTopLevelNav && useNavRail) {
          NavigationRail(containerColor = MaterialTheme.colorScheme.surface) {
            NavigationRailItem(
              selected = currentRoute == ROUTE_PAYMENT,
              onClick = {
                if (currentRoute != ROUTE_PAYMENT) {
                  navController.navigate(ROUTE_PAYMENT) {
                    popUpTo(ROUTE_PAYMENT) { inclusive = true }
                    launchSingleTop = true
                  }
                }
              },
              icon = {
                Icon(
                  imageVector =
                    if (currentRoute == ROUTE_PAYMENT) Icons.Filled.AccountBalanceWallet
                    else Icons.Outlined.AccountBalanceWallet,
                  contentDescription = stringResource(R.string.nav_payment),
                )
              },
              label = { Text(stringResource(R.string.nav_payment)) },
              modifier = Modifier.testTag("nav_rail_payment"),
            )
            NavigationRailItem(
              selected = currentRoute == ROUTE_HISTORY,
              onClick = {
                if (currentRoute != ROUTE_HISTORY) {
                  navController.navigate(ROUTE_HISTORY) {
                    popUpTo(ROUTE_PAYMENT) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                  }
                }
              },
              icon = {
                BadgedBox(
                  badge = {
                    if (uiState.confirmations.isNotEmpty()) {
                      Badge { Text("${uiState.confirmations.size}") }
                    }
                  }
                ) {
                  Icon(
                    imageVector =
                      if (currentRoute == ROUTE_HISTORY) Icons.Filled.History
                      else Icons.Outlined.History,
                    contentDescription = stringResource(R.string.nav_history),
                  )
                }
              },
              label = { Text(stringResource(R.string.nav_history)) },
              modifier = Modifier.testTag("nav_rail_history"),
            )
          }
        }

        NavHost(
          navController = navController,
          startDestination = ROUTE_PAYMENT,
          modifier = Modifier.weight(1f).fillMaxSize(),
        ) {
          composable(ROUTE_PAYMENT) {
            PaymentScreen(
              uiState = uiState,
              onSelectAccount = viewModel::selectAccount,
              onCopyAccount = { account ->
                clipboardManager.setText(AnnotatedString(account.accountNumber))
                viewModel.markAccountCopied(account)
                showMessage(
                  context.getString(
                    R.string.snackbar_copied,
                    account.displayName,
                    account.accountNumber,
                  )
                )
              },
              onReferenceChanged = viewModel::onReferenceChanged,
              onPasteReference = {
                val clipText = clipboardManager.getText()?.text?.trim().orEmpty()
                if (clipText.isNotEmpty()) {
                  viewModel.onReferenceChanged(clipText)
                  showMessage(context.getString(R.string.snackbar_pasted))
                } else {
                  showMessage(context.getString(R.string.snackbar_clipboard_empty))
                }
              },
              onConfirmPayment = {
                viewModel.confirmPayment { insertedId ->
                  navController.navigate(receiptRoute(insertedId))
                }
              },
              onBackAction = {
                if (uiState.referenceInput.isNotEmpty() || uiState.validationError != null) {
                  viewModel.resetForm()
                  showMessage(context.getString(R.string.snackbar_form_reset))
                } else if (uiState.confirmations.isNotEmpty()) {
                  navController.navigate(ROUTE_HISTORY) {
                    popUpTo(ROUTE_PAYMENT) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                  }
                } else {
                  viewModel.resetForm()
                  showMessage(context.getString(R.string.snackbar_form_reset))
                }
              },
              onViewLatestReceipt = { receiptId ->
                navController.navigate(receiptRoute(receiptId))
              },
            )
          }

          composable(ROUTE_HISTORY) {
            HistoryScreen(
              confirmations = uiState.confirmations,
              isDeviceUnlocked = uiState.isDeviceUnlocked,
              onOpenReceipt = { receiptId ->
                navController.navigate(receiptRoute(receiptId))
              },
              onDeleteConfirmation = viewModel::deleteConfirmation,
              onResetLockStatus = viewModel::resetLockStatus,
            )
          }

          composable(
            route = ROUTE_RECEIPT,
            arguments = listOf(navArgument("receiptId") { type = NavType.IntType }),
          ) { entry ->
            val receiptId = entry.arguments?.getInt("receiptId") ?: 0
            val receipt by
              viewModel.observeReceipt(receiptId).collectAsStateWithLifecycle(initialValue = null)

            ReceiptScreen(
              receipt = receipt,
              onCopyReference = { ref ->
                clipboardManager.setText(AnnotatedString(ref))
                showMessage(context.getString(R.string.snackbar_copied, "Ref", ref))
              },
              onBack = { navController.popBackStack() },
            )
          }
        }
      }
    }
  }
}
