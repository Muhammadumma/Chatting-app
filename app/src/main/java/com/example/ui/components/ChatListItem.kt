package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Chat
import com.example.model.MessageStatus
import com.example.model.MessageType
import com.example.ui.theme.WhatsAppBadgeGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppTickBlue

@Composable
fun ChatListItem(
  chat: Chat,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 10.dp)
      .testTag("chat_item_${chat.id}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Contact Avatar
    Box(
      modifier = Modifier.size(52.dp),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(chat.contact.avatarColor),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = chat.contact.initials,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      }

      // Online presence indicator
      if (chat.contact.isOnline) {
        Box(
          modifier = Modifier
            .size(13.dp)
            .align(Alignment.BottomEnd)
            .clip(CircleShape)
            .background(WhatsAppBadgeGreen)
            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
        )
      }
    }

    Spacer(modifier = Modifier.width(14.dp))

    // Middle Content: Name and snippet
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = chat.contact.name,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
          ),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f, fill = false)
        )

        if (chat.contact.isVerified) {
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Verified account",
            tint = WhatsAppGreen,
            modifier = Modifier.size(15.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(3.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Message status ticks if outgoing
        if (chat.lastMessage.isOutgoing) {
          when (chat.lastMessage.status) {
            MessageStatus.READ -> {
              Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Read",
                tint = WhatsAppTickBlue,
                modifier = Modifier.size(16.dp)
              )
            }
            MessageStatus.DELIVERED -> {
              Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Delivered",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
              )
            }
            else -> {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Sent",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.size(15.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(4.dp))
        }

        // Multimedia indicator icon
        when (chat.lastMessage.type) {
          MessageType.PHOTO -> {
            Icon(
              imageVector = Icons.Default.PhotoCamera,
              contentDescription = "Photo",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
          }
          MessageType.AUDIO -> {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = "Audio message",
              tint = WhatsAppGreen,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
          }
          else -> {}
        }

        // Snippet text
        val snippetText = if (chat.isTyping) "typing..." else chat.lastMessage.text
        Text(
          text = snippetText,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 14.sp,
            color = if (chat.isTyping) WhatsAppGreen else MaterialTheme.colorScheme.onSurfaceVariant
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Right Content: Timestamp, unread pill, pin/mute icons
    Column(
      horizontalAlignment = Alignment.End
    ) {
      Text(
        text = chat.lastMessage.timestamp,
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 11.sp,
          color = if (chat.unreadCount > 0) WhatsAppGreen else MaterialTheme.colorScheme.onSurfaceVariant
        )
      )

      Spacer(modifier = Modifier.height(5.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (chat.isMuted) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeOff,
            contentDescription = "Muted",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
        }

        if (chat.isPinned) {
          Icon(
            imageVector = Icons.Default.PushPin,
            contentDescription = "Pinned",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
        }

        if (chat.unreadCount > 0) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(WhatsAppBadgeGreen)
              .padding(horizontal = 6.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = chat.unreadCount.toString(),
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
