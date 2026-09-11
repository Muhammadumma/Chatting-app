package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.SynchronyItem
import com.example.model.SynchronyType
import com.example.ui.theme.KickAmber
import com.example.ui.theme.KickBorder
import com.example.ui.theme.KickCard
import com.example.ui.theme.KickCyan
import com.example.ui.theme.KickDeep
import com.example.ui.theme.KickObsidian
import com.example.ui.theme.KickTextMuted
import com.example.ui.theme.KickTextPrimary
import com.example.ui.theme.KickTextSecondary

@Composable
fun SynchronyDialog(
  items: List<SynchronyItem>,
  onJoinRoundtable: (SynchronyItem) -> Unit,
  onDismiss: () -> Unit
) {
  var activeListeningItem by remember { mutableStateOf<SynchronyItem?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(24.dp))
        .testTag("synchrony_dialog"),
      color = KickCard
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(KickCyan.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Radio,
                contentDescription = null,
                tint = KickCyan,
                modifier = Modifier.size(18.dp)
              )
            }
            Column {
              Text(
                text = "SYNCHRONY",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = KickCyan
              )
              Text(
                text = "Live Network Resonance",
                fontSize = 10.sp,
                color = KickTextSecondary
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = KickTextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Active Listening Banner (if joined)
        activeListeningItem?.let { item ->
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(KickCyan.copy(alpha = 0.15f))
              .border(1.dp, KickCyan, RoundedCornerShape(14.dp))
              .padding(12.dp),
            color = Color.Transparent
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Headphones,
                  contentDescription = null,
                  tint = KickCyan,
                  modifier = Modifier.size(20.dp)
                )
                Column {
                  Text(
                    text = "Connected to Live Audio Table",
                    color = KickCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = item.title,
                    color = KickTextPrimary,
                    fontSize = 10.sp,
                    maxLines = 1
                  )
                }
              }

              Button(
                onClick = { activeListeningItem = null },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x30FFFFFF))
              ) {
                Text(text = "Leave", color = KickTextPrimary, fontSize = 10.sp)
              }
            }
          }
          Spacer(modifier = Modifier.height(12.dp))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          items(items, key = { it.id }) { item ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, KickBorder, RoundedCornerShape(14.dp)),
              color = KickObsidian
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(
                        if (item.type == SynchronyType.LIVE_ROUNDTABLE) KickCyan.copy(alpha = 0.15f)
                        else KickAmber.copy(alpha = 0.15f)
                      )
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = if (item.type == SynchronyType.LIVE_ROUNDTABLE) "LIVE AUDIO ROUNDTABLE" else "ACTIVE INQUIRY",
                      color = if (item.type == SynchronyType.LIVE_ROUNDTABLE) KickCyan else KickAmber,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }

                  Text(
                    text = "${item.participantCount} active",
                    color = KickTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = item.title,
                  color = KickTextPrimary,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = item.snippet,
                  color = KickTextSecondary,
                  fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = item.socialDistanceContext,
                    color = KickTextMuted,
                    fontSize = 10.sp
                  )

                  Button(
                    onClick = {
                      activeListeningItem = item
                      onJoinRoundtable(item)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
                  ) {
                    Text(
                      text = if (item.type == SynchronyType.LIVE_ROUNDTABLE) "Listen In" else "Respond",
                      color = Color(0xFF080D12),
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
