package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.PaymentConfirmationEntity
import com.example.ui.theme.JetBrainsMonoFamily
import com.example.ui.theme.UnlockedEmerald
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReceiptScreen(
  receipt: PaymentConfirmationEntity?,
  onCopyReference: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var revealed by remember { mutableStateOf(false) }
  LaunchedEffect(Unit) { revealed = true }

  val iconScale by
    animateFloatAsState(
      targetValue = if (revealed) 1f else 0.72f,
      animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
      label = "receipt_badge_scale",
    )

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    Column(
      modifier =
        Modifier.widthIn(max = 560.dp)
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 20.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      // Celebratory Icon
      Box(
        modifier =
          Modifier.scale(iconScale)
            .size(88.dp)
            .clip(CircleShape)
            .background(UnlockedEmerald.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Filled.LockOpen,
          contentDescription = stringResource(R.string.cd_lock_icon),
          tint = UnlockedEmerald,
          modifier = Modifier.size(44.dp),
        )
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Text(
          text = stringResource(R.string.receipt_verified_title),
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.onBackground,
          textAlign = TextAlign.Center,
        )
        Text(
          text = stringResource(R.string.receipt_verified_subtitle),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
        )
      }

      if (receipt != null) {
        Surface(
          modifier = Modifier.fillMaxWidth().testTag("receipt_detail_card"),
          shape = MaterialTheme.shapes.large,
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          shadowElevation = 1.dp,
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = stringResource(R.string.receipt_status_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Surface(
                color = UnlockedEmerald.copy(alpha = 0.14f),
                shape = RoundedCornerShape(50),
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  Icon(
                    imageVector = Icons.Filled.Verified,
                    contentDescription = null,
                    tint = UnlockedEmerald,
                    modifier = Modifier.size(16.dp),
                  )
                  Text(
                    text = stringResource(R.string.receipt_status_verified),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = UnlockedEmerald,
                  )
                }
              }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            ReceiptRow(
              label = stringResource(R.string.receipt_method_label),
              value = receipt.methodOption,
            )
            ReceiptRow(
              label = stringResource(R.string.receipt_ref_label),
              value = receipt.referenceNumber,
              isMonospace = true,
            )
            ReceiptRow(
              label = stringResource(R.string.receipt_recipient_label),
              value = "${receipt.recipientName} (${receipt.accountNumber})",
            )
            ReceiptRow(
              label = stringResource(R.string.receipt_time_label),
              value = formatReceiptDate(receipt.timestampMillis),
            )
          }
        }

        FilledTonalButton(
          onClick = { onCopyReference(receipt.referenceNumber) },
          modifier =
            Modifier.fillMaxWidth()
              .minimumInteractiveComponentSize()
              .testTag("copy_receipt_ref_button"),
          shape = MaterialTheme.shapes.medium,
          contentPadding = PaddingValues(vertical = 14.dp),
        ) {
          Icon(
            imageVector = Icons.Filled.ContentCopy,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = stringResource(R.string.btn_copy_ref),
            style = MaterialTheme.typography.labelLarge,
          )
        }
      }

      Button(
        onClick = onBack,
        modifier =
          Modifier.fillMaxWidth()
            .minimumInteractiveComponentSize()
            .testTag("receipt_back_button"),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(vertical = 15.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
          ),
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = stringResource(R.string.btn_back),
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        )
      }

      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}

@Composable
private fun ReceiptRow(
  label: String,
  value: String,
  isMonospace: Boolean = false,
) {
  Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(
      text = value,
      style =
        if (isMonospace) {
          MaterialTheme.typography.titleMedium.copy(
            fontFamily = JetBrainsMonoFamily,
            fontWeight = FontWeight.Bold,
          )
        } else {
          MaterialTheme.typography.titleMedium
        },
      color = MaterialTheme.colorScheme.onSurface,
    )
  }
}

private fun formatReceiptDate(timestampMillis: Long): String {
  val formatter = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
  return formatter.format(Date(timestampMillis))
}
