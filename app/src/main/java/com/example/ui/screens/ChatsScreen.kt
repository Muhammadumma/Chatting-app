package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Chat
import com.example.model.ChatFilter
import com.example.model.StatusStory
import com.example.ui.components.ChatFilterTabs
import com.example.ui.components.ChatListItem
import com.example.ui.components.StatusRow
import com.example.ui.components.WhatsAppLogo
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenContainer

@Composable
fun ChatsScreen(
  chats: List<Chat>,
  stories: List<StatusStory>,
  selectedFilter: ChatFilter,
  onFilterSelected: (ChatFilter) -> Unit,
  onChatClick: (Chat) -> Unit,
  onStoryClick: (StatusStory) -> Unit,
  onCameraClick: () -> Unit,
  onSearchClick: () -> Unit,
  onNewChatClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showMenu by remember { mutableStateOf(false) }

  val filteredChats = remember(chats, selectedFilter) {
    when (selectedFilter) {
      ChatFilter.ALL -> chats
      ChatFilter.UNREAD -> chats.filter { it.unreadCount > 0 }
      ChatFilter.PERSONAL -> chats.filter { it.category == ChatFilter.PERSONAL }
      ChatFilter.BUSINESS -> chats.filter { it.category == ChatFilter.BUSINESS }
      ChatFilter.GROUPS -> chats.filter { it.category == ChatFilter.GROUPS }
    }
  }

  val totalUnreadCount = remember(chats) {
    chats.count { it.unreadCount > 0 }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      // Top App Bar (Header): Clean white top bar with iconic green WhatsApp logo
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
          // Iconic WhatsApp logo (speech bubble with phone) + optional subtle title
          WhatsAppLogo(
            modifier = Modifier.testTag("whatsapp_header_logo"),
            size = 36.dp,
            showText = true // Shows WhatsApp title alongside iconic logo
          )

          Spacer(modifier = Modifier.weight(1f))

          // 1. Camera Icon
          IconButton(
            onClick = onCameraClick,
            modifier = Modifier.testTag("header_camera_button")
          ) {
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = "Camera",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(23.dp)
            )
          }

          // 2. Search Icon
          IconButton(
            onClick = onSearchClick,
            modifier = Modifier.testTag("header_search_button")
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }

          // 3. Overflow Menu (Three dots)
          Box {
            IconButton(
              onClick = { showMenu = true },
              modifier = Modifier.testTag("header_overflow_menu")
            ) {
              Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
              )
            }

            DropdownMenu(
              expanded = showMenu,
              onDismissRequest = { showMenu = false }
            ) {
              DropdownMenuItem(
                text = { Text("New group") },
                onClick = { showMenu = false; onNewChatClick() }
              )
              DropdownMenuItem(
                text = { Text("New broadcast") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Linked devices") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Starred messages") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Payments") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Settings") },
                onClick = { showMenu = false }
              )
            }
          }
        }
      }

      // Main Conversation Scroll Area
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .testTag("chats_list")
      ) {
        // B. Status Updates Row: Direct integration of status stories in Chats screen
        item {
          StatusRow(
            stories = stories,
            onStoryClick = onStoryClick,
            onMyStatusClick = onCameraClick
          )
        }

        // C. Chat Filter Tabs: Smart pill filters (All, Unread, Personal, Business, Groups)
        item {
          ChatFilterTabs(
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected,
            unreadCount = totalUnreadCount
          )
          Spacer(modifier = Modifier.height(4.dp))
        }

        // D. Chats List
        items(filteredChats, key = { it.id }) { chat ->
          ChatListItem(
            chat = chat,
            onClick = { onChatClick(chat) }
          )
        }

        // Bottom spacer to avoid bottom nav clipping
        item {
          Spacer(modifier = Modifier.height(84.dp))
        }
      }
    }

    // E. Floating Action Button (FAB): Rounded Liquid Glass aesthetic with green tint
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
        .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(20.dp))
        .clickable(onClick = onNewChatClick)
        .padding(16.dp)
        .testTag("new_chat_fab"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.Chat,
        contentDescription = "New chat",
        tint = Color.White,
        modifier = Modifier.size(24.dp)
      )
    }
  }
}
