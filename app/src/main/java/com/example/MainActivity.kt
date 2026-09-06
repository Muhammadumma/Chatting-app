package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.CallType
import com.example.ui.components.BottomNavBar
import com.example.ui.components.NavTab
import com.example.ui.screens.CallOverlayDialog
import com.example.ui.screens.CallsScreen
import com.example.ui.screens.CameraDialog
import com.example.ui.screens.ChatsScreen
import com.example.ui.screens.CommunitiesScreen
import com.example.ui.screens.ConversationScreen
import com.example.ui.screens.SearchOverlay
import com.example.ui.screens.StatusViewerDialog
import com.example.ui.screens.UpdatesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.WhatsAppViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        WhatsAppApp()
      }
    }
  }
}

@Composable
fun WhatsAppApp(
  viewModel: WhatsAppViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  // Handle system back navigation if inside a conversation or search
  BackHandler(enabled = uiState.activeChat != null || uiState.isSearchOpen) {
    when {
      uiState.isSearchOpen -> viewModel.closeSearch()
      uiState.activeChat != null -> viewModel.closeChat()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .testTag("whatsapp_app_root")
  ) {
    // Primary Navigation: Inside Chat vs Main Hub (Tabs)
    AnimatedContent(
      targetState = uiState.activeChat,
      transitionSpec = {
        if (targetState != null) {
          slideInHorizontally { it } togetherWith slideOutHorizontally { -it / 3 }
        } else {
          slideInHorizontally { -it / 3 } togetherWith slideOutHorizontally { it }
        }
      },
      label = "chat_transition"
    ) { activeChat ->
      if (activeChat != null) {
        // Individual Conversation Screen
        ConversationScreen(
          chat = activeChat,
          onBack = { viewModel.closeChat() },
          onSendMessage = { text -> viewModel.sendMessage(text) },
          onAudioCall = { viewModel.openCall(activeChat.contact, CallType.AUDIO) },
          onVideoCall = { viewModel.openCall(activeChat.contact, CallType.VIDEO) },
          onCameraClick = { viewModel.openCamera() },
          onReactionSelected = { messageId, emoji ->
            viewModel.addReaction(messageId, emoji)
          }
        )
      } else {
        // Main Hub with Bottom Navigation Bar
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            BottomNavBar(
              currentTab = uiState.currentTab,
              onTabSelected = { tab -> viewModel.setTab(tab) },
              unreadChatsCount = uiState.chats.count { it.unreadCount > 0 }
            )
          }
        ) { innerPadding ->
          Crossfade(
            targetState = uiState.currentTab,
            label = "tab_crossfade",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding() / 2f)
          ) { tab ->
            when (tab) {
              NavTab.CHATS -> {
                ChatsScreen(
                  chats = uiState.chats,
                  stories = uiState.stories,
                  selectedFilter = uiState.selectedFilter,
                  onFilterSelected = { filter -> viewModel.setFilter(filter) },
                  onChatClick = { chat -> viewModel.openChat(chat) },
                  onStoryClick = { story -> viewModel.openStory(story) },
                  onCameraClick = { viewModel.openCamera() },
                  onSearchClick = { viewModel.openSearch() },
                  onNewChatClick = {
                    val firstChat = uiState.chats.firstOrNull()
                    if (firstChat != null) viewModel.openChat(firstChat)
                  }
                )
              }
              NavTab.UPDATES -> {
                UpdatesScreen(
                  stories = uiState.stories,
                  channels = uiState.channels,
                  onStoryClick = { story -> viewModel.openStory(story) },
                  onCameraClick = { viewModel.openCamera() },
                  onSearchClick = { viewModel.openSearch() }
                )
              }
              NavTab.COMMUNITIES -> {
                CommunitiesScreen(
                  communities = uiState.communities,
                  onNewCommunityClick = { /* New community */ },
                  onCommunityClick = { /* Open community */ },
                  onCameraClick = { viewModel.openCamera() }
                )
              }
              NavTab.CALLS -> {
                CallsScreen(
                  callLogs = uiState.calls,
                  onStartCall = { contact, type -> viewModel.openCall(contact, type) },
                  onCameraClick = { viewModel.openCamera() },
                  onSearchClick = { viewModel.openSearch() },
                  onNewCallClick = {
                    val contact = uiState.chats.firstOrNull()?.contact
                    if (contact != null) viewModel.openCall(contact, CallType.AUDIO)
                  }
                )
              }
            }
          }
        }
      }
    }

    // Modal: Full-screen Status Story Viewer
    uiState.activeStory?.let { story ->
      StatusViewerDialog(
        story = story,
        onDismiss = { viewModel.closeStory() },
        onReply = { text -> viewModel.replyToStory(text) }
      )
    }

    // Modal: Full-screen Call Overlay
    uiState.activeCallContact?.let { (contact, type) ->
      CallOverlayDialog(
        contact = contact,
        callType = type,
        onEndCall = { viewModel.endCall() }
      )
    }

    // Modal: Camera Viewfinder
    if (uiState.isCameraOpen) {
      CameraDialog(
        onDismiss = { viewModel.closeCamera() },
        onPhotoCaptured = {
          // If in active chat, could send captured photo
          if (uiState.activeChat != null) {
            viewModel.sendMessage("📷 [Photo captured]")
          }
        }
      )
    }

    // Modal: Search Overlay
    if (uiState.isSearchOpen) {
      SearchOverlay(
        allChats = uiState.chats,
        onChatSelected = { chat -> viewModel.openChat(chat) },
        onClose = { viewModel.closeSearch() }
      )
    }
  }
}

// For unit testing backward compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}
