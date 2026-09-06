package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Chat
import com.example.model.Message
import com.example.ui.components.FloatingInputBar
import com.example.ui.components.MessageBubble
import com.example.ui.theme.WhatsAppChatBackground
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppTextSecondary

@Composable
fun ConversationScreen(
  chat: Chat,
  onBack: () -> Unit,
  onSendMessage: (String) -> Unit,
  onAudioCall: () -> Unit,
  onVideoCall: () -> Unit,
  onCameraClick: () -> Unit,
  onReactionSelected: (messageId: String, emoji: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var inputText by remember { mutableStateOf("") }
  var showMenu by remember { mutableStateOf(false) }
  val listState = rememberLazyListState()

  // Auto-scroll to latest message when messages change
  LaunchedEffect(chat.messages.size) {
    if (chat.messages.isNotEmpty()) {
      listState.animateScrollToItem(chat.messages.size - 1)
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(WhatsAppChatBackground)
      .testTag("conversation_screen")
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      // Top Navigation Bar (Header): Liquid Glass translucent dynamic header
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .shadow(
            elevation = 4.dp,
            ambientColor = Color(0x15000000),
            spotColor = Color(0x1F00A884)
          ),
        color = Color(0xF2FFFFFF), // Liquid Glass frosted surface
        tonalElevation = 2.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Back button
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("chat_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }

          // Contact Avatar
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(chat.contact.avatarColor),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = chat.contact.initials,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          // Contact Name & Status ("online" / "typing...")
          Column(
            modifier = Modifier
              .weight(1f)
              .clickable { /* View contact */ }
          ) {
            Text(
              text = chat.contact.name,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              ),
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1
            )
            Text(
              text = if (chat.isTyping) "typing..." else chat.contact.lastSeen,
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = if (chat.isTyping || chat.contact.isOnline) WhatsAppGreen else WhatsAppTextSecondary
              ),
              maxLines = 1
            )
          }

          // Video Call Icon
          IconButton(
            onClick = onVideoCall,
            modifier = Modifier.testTag("chat_video_call_button")
          ) {
            Icon(
              imageVector = Icons.Default.Videocam,
              contentDescription = "Video call",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }

          // Audio Call Icon
          IconButton(
            onClick = onAudioCall,
            modifier = Modifier.testTag("chat_audio_call_button")
          ) {
            Icon(
              imageVector = Icons.Default.Call,
              contentDescription = "Audio call",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(22.dp)
            )
          }

          // Overflow Menu (Three Dots)
          Box {
            IconButton(onClick = { showMenu = true }) {
              Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Chat menu",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
              )
            }

            DropdownMenu(
              expanded = showMenu,
              onDismissRequest = { showMenu = false }
            ) {
              DropdownMenuItem(
                text = { Text("View contact") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Media, links, and docs") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Search") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Mute notifications") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Disappearing messages") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Wallpaper") },
                onClick = { showMenu = false }
              )
            }
          }
        }
      }

      // Chat Messages List with Rounded Pill Bubbles
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .testTag("conversation_messages_list")
      ) {
        // Top Security & Encryption Notice Pill
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
          ) {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xDEFFF4C2), // Warm soft security banner
              shadowElevation = 1.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = "Encrypted",
                  tint = Color(0xFF6B5800),
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Messages and calls are end-to-end encrypted. No one outside of this chat, not even WhatsApp, can read or listen to them.",
                  fontSize = 11.sp,
                  color = Color(0xFF524400),
                  textAlign = TextAlign.Center,
                  lineHeight = 15.sp
                )
              }
            }
          }
        }

        // Date Pill
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xD8FFFFFF),
              shadowElevation = 1.dp
            ) {
              Text(
                text = "TODAY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsAppTextSecondary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }
        }

        // Pill Message Bubbles
        items(chat.messages, key = { it.id }) { message ->
          MessageBubble(
            message = message,
            onReactionSelected = { emoji ->
              onReactionSelected(message.id, emoji)
            }
          )
        }

        // Space at bottom for floating input bar
        item {
          Spacer(modifier = Modifier.height(76.dp))
        }
      }
    }

    // A. The New Floating Input Bar: Floats with frosted translucent Liquid Glass effect
    FloatingInputBar(
      text = inputText,
      onTextChanged = { inputText = it },
      onSend = { text ->
        if (text.isNotBlank()) {
          onSendMessage(text)
          inputText = ""
        }
      },
      onCameraClick = onCameraClick,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}
