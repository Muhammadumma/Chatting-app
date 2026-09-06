package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.StatusStory
import com.example.ui.theme.WhatsAppGreen

@Composable
fun StatusViewerDialog(
  story: StatusStory,
  onDismiss: () -> Unit,
  onReply: (String) -> Unit
) {
  val progress = remember { Animatable(0f) }
  var isPaused by remember { mutableStateOf(false) }
  var replyText by remember { mutableStateOf("") }

  LaunchedEffect(isPaused) {
    if (!isPaused) {
      val remaining = 1f - progress.value
      val durationMs = (remaining * 5000).toInt()
      if (durationMs > 0) {
        progress.animateTo(
          targetValue = 1f,
          animationSpec = tween(durationMillis = durationMs, easing = LinearEasing)
        )
        onDismiss()
      }
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(story.backgroundColors)
        )
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = {
            // Tap to advance or exit
            onDismiss()
          }
        )
        .testTag("status_viewer_fullscreen")
    ) {
      // Content Center: Big emoji asset + caption
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = story.emojiAsset,
          fontSize = 72.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
          text = story.caption,
          style = MaterialTheme.typography.headlineSmall.copy(
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 32.sp
          ),
          textAlign = TextAlign.Center
        )
      }

      // Top Overlay: Segmented Progress Bar + Contact Info + Close
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(top = 12.dp, start = 12.dp, end = 12.dp)
      ) {
        // Progress bar line
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(alpha = 0.3f))
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(fraction = progress.value)
              .height(3.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(Color.White)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Contact info header
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(story.contact.avatarColor),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = story.contact.initials,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = story.contact.name,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
            Text(
              text = story.timestamp,
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 12.sp
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close status",
              tint = Color.White
            )
          }
        }
      }

      // Bottom Reply Bar + Emoji Quick Reactions
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .navigationBarsPadding()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        // Quick reaction emojis
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          listOf("❤️", "😂", "😮", "😢", "👏", "🔥").forEach { emoji ->
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable {
                  onReply(emoji)
                  onDismiss()
                },
              contentAlignment = Alignment.Center
            ) {
              Text(text = emoji, fontSize = 20.sp)
            }
          }
        }

        // Reply field
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
              onReply("Awesome update!")
              onDismiss()
            },
          color = Color.Black.copy(alpha = 0.45f),
          shape = RoundedCornerShape(24.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Reply to ${story.contact.name}...",
              color = Color.White.copy(alpha = 0.7f),
              fontSize = 15.sp,
              modifier = Modifier.weight(1f)
            )
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send reply",
              tint = Color.White.copy(alpha = 0.8f),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
