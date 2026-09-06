package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Chat
import com.example.ui.components.ChatListItem
import com.example.ui.components.MetaAIRingIcon
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppTextPrimary
import com.example.ui.theme.WhatsAppTextSecondary

@Composable
fun SearchOverlay(
  allChats: List<Chat>,
  onChatSelected: (Chat) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredChats = remember(searchQuery, allChats) {
    if (searchQuery.isBlank()) {
      allChats
    } else {
      allChats.filter { chat ->
        chat.contact.name.contains(searchQuery, ignoreCase = true) ||
          chat.lastMessage.text.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  val searchCategories = listOf("Unread", "Photos", "Videos", "Links", "GIFs", "Audio", "Docs")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .statusBarsPadding()
      .testTag("search_overlay")
  ) {
    // Top Search Bar with Meta AI ring
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      shape = RoundedCornerShape(26.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onClose,
          modifier = Modifier.testTag("search_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Box(
          modifier = Modifier.weight(1f)
        ) {
          if (searchQuery.isEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Search or ask Meta AI",
                style = TextStyle(
                  fontSize = 16.sp,
                  color = WhatsAppTextSecondary
                )
              )
            }
          }
          BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            textStyle = TextStyle(
              fontSize = 16.sp,
              color = WhatsAppTextPrimary
            ),
            cursorBrush = SolidColor(WhatsAppGreen),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("search_input_field")
          )
        }

        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Clear search",
              tint = WhatsAppTextSecondary
            )
          }
        } else {
          // Meta AI Gradient Ring
          MetaAIRingIcon(
            modifier = Modifier
              .padding(end = 12.dp)
              .testTag("meta_ai_ring")
          )
        }
      }
    }

    // Filter Chips Row
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(searchCategories) { category ->
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { searchQuery = category }
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(
            text = category,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Search Results List
    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        Text(
          text = if (searchQuery.isBlank()) "Recent Chats" else "Results (${filteredChats.size})",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = WhatsAppTextSecondary,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
      }

      if (filteredChats.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No chats or messages found for '$searchQuery'",
              color = WhatsAppTextSecondary,
              fontSize = 15.sp
            )
          }
        }
      } else {
        items(filteredChats, key = { it.id }) { chat ->
          ChatListItem(
            chat = chat,
            onClick = {
              onChatSelected(chat)
              onClose()
            }
          )
        }
      }
    }
  }
}
