package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CallType
import com.example.model.User
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppRed

@Composable
fun CallOverlayDialog(
  contact: User,
  callType: CallType,
  onEndCall: () -> Unit
) {
  var isMuted by remember { mutableStateOf(false) }
  var isVideoOff by remember { mutableStateOf(callType == CallType.AUDIO) }
  var isSpeakerOn by remember { mutableStateOf(true) }

  val transition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by transition.animateFloat(
    initialValue = 1f,
    targetValue = 1.14f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Dialog(
    onDismissRequest = onEndCall,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFF0F2027),
              Color(0xFF203A43),
              Color(0xFF2C5364)
            )
          )
        )
        .testTag("call_overlay_dialog")
    ) {
      // Top header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(top = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = if (callType == CallType.VIDEO) "WhatsApp Video Call" else "WhatsApp Audio Call",
          color = Color.White.copy(alpha = 0.75f),
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = contact.name,
          style = MaterialTheme.typography.headlineMedium.copy(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Ringing...",
          color = WhatsAppGreen,
          fontSize = 15.sp,
          fontWeight = FontWeight.Medium
        )
      }

      // Center Avatar with glowing pulse
      Box(
        modifier = Modifier.align(Alignment.Center),
        contentAlignment = Alignment.Center
      ) {
        // Outer pulsing ring
        Box(
          modifier = Modifier
            .size(170.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
        )
        Box(
          modifier = Modifier
            .size(140.dp)
            .scale(pulseScale * 0.95f)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.12f))
        )

        // Main Contact Avatar
        Box(
          modifier = Modifier
            .size(110.dp)
            .clip(CircleShape)
            .background(contact.avatarColor)
            .border(3.dp, Color.White, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = contact.initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
          )
        }
      }

      // Bottom Call Controls Bar
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .navigationBarsPadding()
          .padding(bottom = 36.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Feature control buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          CallControlButton(
            icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
            label = if (isMuted) "Unmute" else "Mute",
            isActive = isMuted,
            onClick = { isMuted = !isMuted }
          )

          CallControlButton(
            icon = if (isVideoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
            label = if (isVideoOff) "Turn On" else "Video",
            isActive = !isVideoOff,
            onClick = { isVideoOff = !isVideoOff }
          )

          CallControlButton(
            icon = Icons.AutoMirrored.Filled.VolumeUp,
            label = if (isSpeakerOn) "Speaker" else "Earpiece",
            isActive = isSpeakerOn,
            onClick = { isSpeakerOn = !isSpeakerOn }
          )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Red End Call Button
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(WhatsAppRed)
            .clickable(onClick = onEndCall)
            .testTag("end_call_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CallEnd,
            contentDescription = "End call",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun CallControlButton(
  icon: ImageVector,
  label: String,
  isActive: Boolean,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .size(52.dp)
        .clip(CircleShape)
        .background(if (isActive) Color.White else Color.White.copy(alpha = 0.2f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = if (isActive) Color.Black else Color.White,
        modifier = Modifier.size(24.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      color = Color.White.copy(alpha = 0.85f),
      fontSize = 12.sp
    )
  }
}
