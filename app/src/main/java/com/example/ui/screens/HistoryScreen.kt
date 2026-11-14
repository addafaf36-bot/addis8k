package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.PaymentConfirmationEntity
import com.example.ui.theme.JetBrainsMonoFamily
import com.example.ui.theme.LockedAmber
import com.example.ui.theme.UnlockedEmerald
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
  confirmations: List<PaymentConfirmationEntity>,
  isDeviceUnlocked: Boolean,
  onOpenReceipt: (Int) -> Unit,
  onDeleteConfirmation: (Int) -> Unit,
  onResetLockStatus: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    Column(
      modifier = Modifier.widthIn(max = 760.dp).fillMaxSize().padding(horizontal = 16.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = stringResource(R.string.history_title),
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onBackground,
        )

        if (isDeviceUnlocked) {
          FilledTonalButton(
            onClick = onResetLockStatus,
            modifier =
              Modifier.minimumInteractiveComponentSize().testTag("reset_lock_status_button"),
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            colors =
              ButtonDefaults.filledTonalButtonColors(
                containerColor = LockedAmber.copy(alpha = 0.16f),
                contentColor = LockedAmber,
              ),
          ) {
            Icon(
              imageVector = Icons.Filled.LockReset,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = stringResource(R.string.history_reset_lock),
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            )
          }
        }
      }

      if (confirmations.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize().padding(24.dp).testTag("history_empty_state"),
          contentAlignment = Alignment.Center,
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Icon(
              imageVector = Icons.Outlined.ReceiptLong,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.65f),
              modifier = Modifier.size(64.dp),
            )
            Text(
              text = stringResource(R.string.history_empty_title),
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground,
              textAlign = TextAlign.Center,
            )
            Text(
              text = stringResource(R.string.history_empty_desc),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize().testTag("history_list"),
          contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          items(items = confirmations, key = { it.id }) { item ->
            HistoryItemCard(
              item = item,
              onClick = { onOpenReceipt(item.id) },
              onDelete = { onDeleteConfirmation(item.id) },
            )
          }
        }
      }
    }
  }
}

@Composable
private fun HistoryItemCard(
  item: PaymentConfirmationEntity,
  onClick: () -> Unit,
  onDelete: () -> Unit,
) {
  Surface(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth().testTag("history_card_${item.id}"),
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Surface(
        color = UnlockedEmerald.copy(alpha = 0.14f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(44.dp),
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Filled.Verified,
            contentDescription = null,
            tint = UnlockedEmerald,
            modifier = Modifier.size(22.dp),
          )
        }
      }

      Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
          text = item.referenceNumber,
          style =
            MaterialTheme.typography.titleSmall.copy(
              fontFamily = JetBrainsMonoFamily,
              fontWeight = FontWeight.Bold,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = item.methodOption,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
          text = formatTimestamp(item.timestampMillis),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        )
      }

      IconButton(
        onClick = onDelete,
        modifier = Modifier.minimumInteractiveComponentSize().testTag("delete_history_${item.id}"),
      ) {
        Icon(
          imageVector = Icons.Filled.DeleteOutline,
          contentDescription = stringResource(R.string.history_delete_desc),
          tint = MaterialTheme.colorScheme.error,
        )
      }
    }
  }
}

private fun formatTimestamp(timestampMillis: Long): String {
  val formatter = SimpleDateFormat("MMM dd • HH:mm", Locale.getDefault())
  return formatter.format(Date(timestampMillis))
}
