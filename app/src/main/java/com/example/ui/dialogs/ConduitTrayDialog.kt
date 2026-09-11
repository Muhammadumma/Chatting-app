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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.model.ConduitIntroRequest
import com.example.model.ConduitStatus
import com.example.ui.theme.KickAmber
import com.example.ui.theme.KickBlue
import com.example.ui.theme.KickBorder
import com.example.ui.theme.KickCard
import com.example.ui.theme.KickCyan
import com.example.ui.theme.KickDeep
import com.example.ui.theme.KickObsidian
import com.example.ui.theme.KickTextMuted
import com.example.ui.theme.KickTextPrimary
import com.example.ui.theme.KickTextSecondary

@Composable
fun ConduitTrayDialog(
  requests: List<ConduitIntroRequest>,
  onFacilitate: (String) -> Unit,
  onDecline: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) } // 0 = Facilitations (Incoming), 1 = Outgoing

  val incoming = requests.filter { it.isIncomingForBridge }
  val outgoing = requests.filter { !it.isIncomingForBridge }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(24.dp))
        .testTag("conduit_tray_dialog"),
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
                imageVector = Icons.Default.CompareArrows,
                contentDescription = null,
                tint = KickCyan,
                modifier = Modifier.size(18.dp)
              )
            }
            Column {
              Text(
                text = "CONDUIT INBOX",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = KickCyan
              )
              Text(
                text = "Warm Introductions & Bridge Requests",
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

        // Tabs
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (selectedTab == 0) KickCyan.copy(alpha = 0.2f) else KickObsidian)
              .border(
                1.dp,
                if (selectedTab == 0) KickCyan else Color(0x20FFFFFF),
                RoundedCornerShape(12.dp)
              )
              .clickable { selectedTab = 0 }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Bridge Requests (${incoming.size})",
              color = if (selectedTab == 0) KickCyan else KickTextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (selectedTab == 1) KickCyan.copy(alpha = 0.2f) else KickObsidian)
              .border(
                1.dp,
                if (selectedTab == 1) KickCyan else Color(0x20FFFFFF),
                RoundedCornerShape(12.dp)
              )
              .clickable { selectedTab = 1 }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "My Inquiries (${outgoing.size})",
              color = if (selectedTab == 1) KickCyan else KickTextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
          // Incoming Bridge Requests where user is mutual contact
          if (incoming.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "No pending bridge facilitation requests",
                color = KickTextSecondary,
                fontSize = 12.sp
              )
            }
          } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              items(incoming, key = { it.id }) { req ->
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
                          .background(KickAmber.copy(alpha = 0.2f))
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = "BRIDGE FACILITATION",
                          color = KickAmber,
                          fontSize = 9.sp,
                          fontWeight = FontWeight.Bold
                        )
                      }
                      Text(text = req.timestamp, color = KickTextMuted, fontSize = 10.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                      text = "${req.requesterName} wants to connect with ${req.targetName}",
                      color = KickTextPrimary,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                      text = "Intent: \"${req.intentStatement}\"",
                      color = KickTextSecondary,
                      fontSize = 11.sp,
                      fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      OutlinedButton(
                        onClick = { onDecline(req.id) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                      ) {
                        Text(text = "Decline Silently", color = KickTextSecondary, fontSize = 11.sp)
                      }

                      Button(
                        onClick = { onFacilitate(req.id) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
                      ) {
                        Text(
                          text = "Facilitate Intro",
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
        } else {
          // Outgoing Requests
          LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(outgoing, key = { it.id }) { req ->
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
                    Text(
                      text = req.targetName,
                      color = KickTextPrimary,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(KickCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = "BRIDGE PENDING",
                        color = KickCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "${req.targetHandle} • ${req.category}",
                    color = KickTextSecondary,
                    fontSize = 11.sp
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "Your intent: \"${req.intentStatement}\"",
                    color = KickTextSecondary.copy(alpha = 0.8f),
                    fontSize = 10.sp
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
