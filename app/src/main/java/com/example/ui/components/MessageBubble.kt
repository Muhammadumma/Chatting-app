package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Message
import com.example.model.MessageStatus
import com.example.model.MessageType
import com.example.model.ReplyPreview
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenContainer
import com.example.ui.theme.WhatsAppGreenDeep
import com.example.ui.theme.WhatsAppIncomingBubble
import com.example.ui.theme.WhatsAppTextPrimary
import com.example.ui.theme.WhatsAppTextSecondary
import com.example.ui.theme.WhatsAppTickBlue

@Composable
fun MessageBubble(
  message: Message,
  onReactionSelected: (String) -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showReactionPicker by remember { mutableStateOf(false) }
  var isAudioPlaying by remember { mutableStateOf(false) }

  val bubbleShape = if (message.isOutgoing) {
    // Modern pill rounded bubble with subtle anchor corner
    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
  } else {
    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
  }

  val bubbleColor = if (message.isOutgoing) WhatsAppGreenContainer else WhatsAppIncomingBubble

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 3.dp),
    horizontalAlignment = if (message.isOutgoing) Alignment.End else Alignment.Start
  ) {
    // Quick emoji reaction bar popover
    AnimatedVisibility(visible = showReactionPicker) {
      ReactionPickerBar(
        onSelect = { emoji ->
          onReactionSelected(emoji)
          showReactionPicker = false
        },
        onDismiss = { showReactionPicker = false }
      )
    }

    Box(
      modifier = Modifier
        .widthIn(min = 60.dp, max = 310.dp)
        .shadow(
          elevation = 1.dp,
          shape = bubbleShape,
          ambientColor = Color(0x15000000),
          spotColor = Color(0x10000000)
        )
        .clip(bubbleShape)
        .background(bubbleColor)
        .clickable { showReactionPicker = !showReactionPicker }
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .testTag("message_bubble_${message.id}")
    ) {
      Column {
        // Quoted reply snippet if present
        if (message.replyTo != null) {
          QuotedReplyView(reply = message.replyTo)
          Spacer(modifier = Modifier.height(6.dp))
        }

        // Multimedia content
        when (message.type) {
          MessageType.PHOTO -> {
            MultimediaPhotoBubble(caption = message.mediaCaption ?: message.text)
          }
          MessageType.AUDIO -> {
            VoiceNoteBubble(
              duration = message.audioDuration ?: "0:24",
              isPlaying = isAudioPlaying,
              onTogglePlay = { isAudioPlaying = !isAudioPlaying }
            )
          }
          else -> {
            // Text Message
            Text(
              text = message.text,
              style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                lineHeight = 21.sp,
                color = WhatsAppTextPrimary
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Bottom row: Timestamp + Delivery ticks
        Row(
          modifier = Modifier.align(Alignment.End),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = message.timestamp,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              color = WhatsAppTextSecondary
            )
          )

          if (message.isOutgoing) {
            Spacer(modifier = Modifier.width(3.dp))
            when (message.status) {
              MessageStatus.READ -> {
                Icon(
                  imageVector = Icons.Default.DoneAll,
                  contentDescription = "Read",
                  tint = WhatsAppTickBlue,
                  modifier = Modifier.size(15.dp)
                )
              }
              MessageStatus.DELIVERED -> {
                Icon(
                  imageVector = Icons.Default.DoneAll,
                  contentDescription = "Delivered",
                  tint = WhatsAppTextSecondary,
                  modifier = Modifier.size(15.dp)
                )
              }
              else -> {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Sent",
                  tint = WhatsAppTextSecondary,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }
    }

    // Reaction pills underneath bubble
    if (message.reactions.isNotEmpty()) {
      Spacer(modifier = Modifier.height(2.dp))
      Row(
        modifier = Modifier
          .offset(y = (-6).dp)
          .clip(CircleShape)
          .background(Color(0xF0FFFFFF))
          .border(1.dp, Color(0x20000000), CircleShape)
          .padding(horizontal = 6.dp, vertical = 2.dp)
          .clickable { showReactionPicker = !showReactionPicker }
          .testTag("reaction_badge_${message.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        message.reactions.take(3).forEach { emoji ->
          Text(text = emoji, fontSize = 12.sp)
        }
        if (message.reactions.size > 1) {
          Text(
            text = message.reactions.size.toString(),
            fontSize = 11.sp,
            color = WhatsAppTextSecondary,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun QuotedReplyView(reply: ReplyPreview) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(Color(0x1800A884))
      .border(
        width = 3.dp,
        color = WhatsAppGreen,
        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
      )
      .padding(horizontal = 8.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = reply.senderName,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = WhatsAppGreenDeep,
          fontSize = 12.sp
        )
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = reply.messageSnippet,
        style = MaterialTheme.typography.bodySmall.copy(
          color = WhatsAppTextSecondary,
          fontSize = 12.sp
        ),
        maxLines = 1
      )
    }
  }
}

@Composable
fun MultimediaPhotoBubble(caption: String) {
  Column {
    // Rounded image preview card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFF263238),
              Color(0xFF37474F),
              Color(0xFF455A64)
            )
          )
        ),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = Icons.Default.Photo,
          contentDescription = "Photo message",
          tint = Color.White.copy(alpha = 0.8f),
          modifier = Modifier.size(42.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Preview • 1.4 MB",
          color = Color.White.copy(alpha = 0.8f),
          fontSize = 12.sp
        )
      }
    }

    if (caption.isNotBlank()) {
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = caption,
        style = MaterialTheme.typography.bodyLarge.copy(
          fontSize = 14.sp,
          color = WhatsAppTextPrimary
        )
      )
    }
  }
}

@Composable
fun VoiceNoteBubble(
  duration: String,
  isPlaying: Boolean,
  onTogglePlay: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Play / Pause round button
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(CircleShape)
        .background(WhatsAppGreen)
        .clickable(onClick = onTogglePlay),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
        contentDescription = if (isPlaying) "Pause" else "Play",
        tint = Color.White,
        modifier = Modifier.size(22.dp)
      )
    }

    Spacer(modifier = Modifier.width(10.dp))

    // Animated waveform bars representation
    Column(modifier = Modifier.weight(1f)) {
      AudioWaveformVisualizer(isPlaying = isPlaying)
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = if (isPlaying) "Playing • $duration" else duration,
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 11.sp,
          color = WhatsAppTextSecondary
        )
      )
    }
  }
}

@Composable
fun AudioWaveformVisualizer(isPlaying: Boolean) {
  val barHeights = listOf(
    12.dp, 20.dp, 8.dp, 16.dp, 24.dp, 14.dp, 10.dp, 22.dp,
    18.dp, 12.dp, 26.dp, 16.dp, 10.dp, 20.dp, 14.dp, 8.dp,
    22.dp, 16.dp, 12.dp, 18.dp
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(26.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(2.5.dp)
  ) {
    barHeights.forEachIndexed { index, height ->
      val barColor = if (isPlaying && index < 10) WhatsAppGreen else Color(0xFFB0BEC5)
      Box(
        modifier = Modifier
          .width(2.5.dp)
          .height(height)
          .clip(RoundedCornerShape(2.dp))
          .background(barColor)
      )
    }
  }
}

@Composable
fun ReactionPickerBar(
  onSelect: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val emojis = listOf("❤️", "👍", "😂", "😮", "😢", "🙏", "🔥")

  Surface(
    modifier = Modifier
      .padding(bottom = 6.dp)
      .shadow(6.dp, CircleShape)
      .testTag("reaction_picker_bar"),
    shape = CircleShape,
    color = Color.White,
    tonalElevation = 6.dp
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      emojis.forEach { emoji ->
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable { onSelect(emoji) },
          contentAlignment = Alignment.Center
        ) {
          Text(text = emoji, fontSize = 20.sp)
        }
      }
    }
  }
}
