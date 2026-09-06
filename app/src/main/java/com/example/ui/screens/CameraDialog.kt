package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.WhatsAppGreen
import kotlinx.coroutines.delay

@Composable
fun CameraDialog(
  onDismiss: () -> Unit,
  onPhotoCaptured: () -> Unit = {}
) {
  var flashState by remember { mutableStateOf(0) } // 0: Auto, 1: On, 2: Off
  var isFrontCamera by remember { mutableStateOf(false) }
  var flashTriggered by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .testTag("camera_viewfinder_dialog")
    ) {
      // Camera Viewfinder Background Simulation
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            brush = Brush.verticalGradient(
              colors = if (isFrontCamera) {
                listOf(Color(0xFF2C3E50), Color(0xFF1A252F), Color(0xFF0F1419))
              } else {
                listOf(Color(0xFF141E30), Color(0xFF243B55), Color(0xFF0F1A24))
              }
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = if (isFrontCamera) "Front Viewfinder" else "Primary Ultra-Wide Lens",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Tap shutter to capture photo or hold for video",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 13.sp
          )
        }
      }

      // Flash feedback effect
      AnimatedVisibility(
        visible = flashTriggered,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        )
      }

      // Top Control Bar: Close, Flash, Filter
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("camera_close_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close camera",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
          )
        }

        IconButton(
          onClick = { flashState = (flashState + 1) % 3 }
        ) {
          val flashIcon = when (flashState) {
            1 -> Icons.Default.FlashOn
            2 -> Icons.Default.FlashOff
            else -> Icons.Default.FlashAuto
          }
          Icon(
            imageVector = flashIcon,
            contentDescription = "Toggle flash",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
          )
        }
      }

      // Bottom Control Area: Gallery, Shutter, Flip Camera
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .navigationBarsPadding()
          .padding(bottom = 28.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Mode Selector: Photo / Video
        Row(
          horizontalArrangement = Arrangement.spacedBy(20.dp),
          modifier = Modifier.padding(bottom = 20.dp)
        ) {
          Text(
            text = "PHOTO",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Text(
            text = "VIDEO",
            color = Color.White.copy(alpha = 0.6f),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Gallery thumbnail preview
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color.White.copy(alpha = 0.2f))
              .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
              .clickable { /* Select from gallery */ },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PhotoLibrary,
              contentDescription = "Gallery",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          // Shutter Button
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .border(4.dp, Color.White, CircleShape)
              .padding(6.dp)
              .clip(CircleShape)
              .background(WhatsAppGreen)
              .clickable {
                flashTriggered = true
                onPhotoCaptured()
                onDismiss()
              }
              .testTag("camera_shutter_button")
          )

          // Flip Camera
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.2f))
              .clickable { isFrontCamera = !isFrontCamera }
              .testTag("camera_flip_button"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Cameraswitch,
              contentDescription = "Flip camera",
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }
        }
      }
    }
  }
}
