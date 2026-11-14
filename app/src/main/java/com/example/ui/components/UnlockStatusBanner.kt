package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.LockedAmber
import com.example.ui.theme.UnlockedEmerald

@Composable
fun UnlockStatusBanner(
  isUnlocked: Boolean,
  onViewReceiptClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth().testTag("unlock_status_banner"),
    shape = MaterialTheme.shapes.large,
    tonalElevation = 4.dp,
    shadowElevation = 2.dp,
  ) {
    Box(modifier = Modifier.fillMaxWidth().height(152.dp)) {
      Image(
        painter = painterResource(id = R.drawable.img_unlock_banner),
        contentDescription = stringResource(R.string.cd_banner_art),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
      )

      val scrimColors =
        if (isUnlocked) {
          listOf(Color(0xE6064E3B), Color(0xCC0E6B4E))
        } else {
          listOf(Color(0xEB0B2920), Color(0xD6123B2E))
        }

      Box(
        modifier =
          Modifier.fillMaxSize()
            .background(Brush.linearGradient(colors = scrimColors))
            .padding(18.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            // Status Pill
            Surface(
              color =
                if (isUnlocked) UnlockedEmerald.copy(alpha = 0.25f)
                else LockedAmber.copy(alpha = 0.24f),
              shape = RoundedCornerShape(50),
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                Icon(
                  imageVector = if (isUnlocked) Icons.Filled.LockOpen else Icons.Filled.Lock,
                  contentDescription = stringResource(R.string.cd_lock_icon),
                  tint = Color.White,
                  modifier = Modifier.size(16.dp),
                )
                AnimatedContent(
                  targetState = isUnlocked,
                  transitionSpec = { fadeIn() togetherWith fadeOut() },
                  label = "status_pill_text",
                ) { unlocked ->
                  Text(
                    text =
                      stringResource(
                        if (unlocked) R.string.status_unlocked_title else R.string.status_locked_title
                      ),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                  )
                }
              }
            }

            // Recipient Pill
            Surface(
              color = Color.White.copy(alpha = 0.14f),
              shape = RoundedCornerShape(50),
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
              ) {
                Icon(
                  imageVector = Icons.Filled.VerifiedUser,
                  contentDescription = null,
                  tint = Color(0xFFFDE68A),
                  modifier = Modifier.size(14.dp),
                )
                Text(
                  text = stringResource(R.string.recipient_label),
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                  color = Color.White,
                )
              }
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = stringResource(R.string.header_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text =
                  stringResource(
                    if (isUnlocked) R.string.status_unlocked_desc else R.string.status_locked_desc
                  ),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.88f),
              )
            }

            if (isUnlocked && onViewReceiptClick != null) {
              Surface(
                onClick = onViewReceiptClick,
                modifier = Modifier.clip(CircleShape).testTag("view_latest_receipt_chip"),
                color = Color.White,
                shape = CircleShape,
              ) {
                Text(
                  text = stringResource(R.string.receipt_status_label),
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF064E3B),
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                )
              }
            }
          }
        }
      }
    }
  }
}
