package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CallDirection
import com.example.model.CallLog
import com.example.model.CallType
import com.example.model.User
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppRed
import com.example.ui.theme.WhatsAppTextSecondary

@Composable
fun CallsScreen(
  callLogs: List<CallLog>,
  onStartCall: (User, CallType) -> Unit,
  onCameraClick: () -> Unit,
  onSearchClick: () -> Unit,
  onNewCallClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .testTag("calls_screen")
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top App Bar
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Calls",
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          Spacer(modifier = Modifier.weight(1f))

          IconButton(onClick = onCameraClick) {
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = "Camera",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(23.dp)
            )
          }

          IconButton(onClick = onSearchClick) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }

          IconButton(onClick = { /* Menu */ }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "More",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }
        }
      }

      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        // "Create call link" card
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { /* Create call link */ }
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(WhatsAppGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
              Text(
                text = "Create call link",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 16.sp
                )
              )
              Text(
                text = "Share a link for your WhatsApp call",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = WhatsAppTextSecondary,
                  fontSize = 13.sp
                )
              )
            }
          }
        }

        // Recent Calls Header
        item {
          Text(
            text = "Recent",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = WhatsAppTextSecondary,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
          )
        }

        // Call Logs List
        items(callLogs, key = { it.id }) { call ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onStartCall(call.contact, call.type) }
              .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(call.contact.avatarColor),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = call.contact.initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = call.contact.name,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 15.sp
                )
              )

              Row(verticalAlignment = Alignment.CenterVertically) {
                // Call direction arrow
                when (call.direction) {
                  CallDirection.MISSED -> {
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.CallMissed,
                      contentDescription = "Missed call",
                      tint = WhatsAppRed,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                  CallDirection.INCOMING -> {
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.CallReceived,
                      contentDescription = "Incoming call",
                      tint = WhatsAppGreen,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                  CallDirection.OUTGOING -> {
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.CallMade,
                      contentDescription = "Outgoing call",
                      tint = WhatsAppGreen,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                  text = "${call.timestamp} • ${call.duration}",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = WhatsAppTextSecondary,
                    fontSize = 13.sp
                  )
                )
              }
            }

            // Quick Call Action Icon (Audio or Video)
            IconButton(onClick = { onStartCall(call.contact, call.type) }) {
              Icon(
                imageVector = if (call.type == CallType.VIDEO) Icons.Default.Videocam else Icons.Default.Call,
                contentDescription = "Initiate call",
                tint = WhatsAppGreen,
                modifier = Modifier.size(22.dp)
              )
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(90.dp))
        }
      }
    }

    // Floating Action Button (New Call)
    Box(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(bottom = 90.dp, end = 20.dp)
        .shadow(
          elevation = 8.dp,
          shape = RoundedCornerShape(20.dp),
          ambientColor = Color(0x2200A884),
          spotColor = Color(0x3500A884)
        )
        .clip(RoundedCornerShape(20.dp))
        .background(WhatsAppGreen)
        .clickable(onClick = onNewCallClick)
        .padding(16.dp)
        .testTag("new_call_fab"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Call,
        contentDescription = "Start call",
        tint = Color.White,
        modifier = Modifier.size(24.dp)
      )
    }
  }
}
