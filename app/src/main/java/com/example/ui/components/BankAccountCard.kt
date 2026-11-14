package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.BankProvider
import com.example.data.PaymentAccount
import com.example.ui.theme.AbyssiniaAccent
import com.example.ui.theme.AbyssiniaTint
import com.example.ui.theme.CbeAccent
import com.example.ui.theme.CbeTint
import com.example.ui.theme.MonoAccountTextStyle
import com.example.ui.theme.TelebirrAccent
import com.example.ui.theme.TelebirrTint
import com.example.ui.theme.UnlockedEmerald

@Composable
fun BankAccountCard(
  account: PaymentAccount,
  isSelected: Boolean,
  isCopied: Boolean,
  onSelect: () -> Unit,
  onCopy: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val (accentColor, tintColor, icon) = providerStyle(account.provider)

  val borderColor by
    animateColorAsState(
      targetValue =
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outlineVariant,
      animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
      label = "card_border",
    )

  val containerColor by
    animateColorAsState(
      targetValue =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
        else MaterialTheme.colorScheme.surface,
      animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
      label = "card_bg",
    )

  Surface(
    onClick = onSelect,
    modifier = modifier.fillMaxWidth().testTag("bank_card_${account.id}"),
    shape = MaterialTheme.shapes.medium,
    color = containerColor,
    tonalElevation = if (isSelected) 3.dp else 1.dp,
    border = BorderStroke(width = if (isSelected) 1.5.dp else 1.dp, color = borderColor),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // Provider Emblem
      Box(
        modifier =
          Modifier.size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(tintColor),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = stringResource(R.string.cd_bank_icon),
          tint = accentColor,
          modifier = Modifier.size(24.dp),
        )
      }

      // Account Details
      Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          Text(
            text = account.displayName,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = accentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          if (isSelected) {
            Box(
              modifier =
                Modifier.size(7.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
            )
          }
        }

        Text(
          text = account.accountNumber,
          style = MonoAccountTextStyle,
          color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(1.dp))

        Text(
          text = stringResource(R.string.owner_prefix, account.ownerName),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }

      // Tactile Copy Button
      val copyContainer by
        animateColorAsState(
          targetValue =
            if (isCopied) UnlockedEmerald.copy(alpha = 0.16f)
            else MaterialTheme.colorScheme.surfaceVariant,
          label = "copy_btn_bg",
        )
      val copyContent by
        animateColorAsState(
          targetValue =
            if (isCopied) UnlockedEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
          label = "copy_btn_fg",
        )

      FilledTonalButton(
        onClick = onCopy,
        modifier =
          Modifier.minimumInteractiveComponentSize().testTag("copy_btn_${account.accountNumber}"),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        colors =
          ButtonDefaults.filledTonalButtonColors(
            containerColor = copyContainer,
            contentColor = copyContent,
          ),
      ) {
        AnimatedContent(
          targetState = isCopied,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "copy_icon_text",
        ) { copied ->
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (copied) Icons.Filled.Check else Icons.Filled.ContentCopy,
              contentDescription = stringResource(R.string.cd_copy_icon),
              modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = stringResource(if (copied) R.string.btn_copied else R.string.btn_copy),
              style = MaterialTheme.typography.labelLarge,
            )
          }
        }
      }
    }
  }
}

private data class ProviderVisualStyle(
  val accent: Color,
  val tint: Color,
  val icon: ImageVector,
)

@Composable
private fun providerStyle(provider: BankProvider): ProviderVisualStyle =
  when (provider) {
    BankProvider.TELEBIRR ->
      ProviderVisualStyle(
        accent = TelebirrAccent,
        tint = TelebirrTint,
        icon = Icons.Filled.PhoneAndroid,
      )
    BankProvider.CBE ->
      ProviderVisualStyle(
        accent = CbeAccent,
        tint = CbeTint,
        icon = Icons.Filled.AccountBalance,
      )
    BankProvider.ABYSSINIA ->
      ProviderVisualStyle(
        accent = AbyssiniaAccent,
        tint = AbyssiniaTint,
        icon = Icons.Outlined.AccountBalanceWallet,
      )
  }
