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
import com.example.ui.dialogs.ConduitIntroDialog
import com.example.ui.dialogs.ConduitTrayDialog
import com.example.ui.dialogs.PrismProfileDialog
import com.example.ui.dialogs.PublishDispatchDialog
import com.example.ui.dialogs.ScoutAssistantDialog
import com.example.ui.dialogs.SynchronyDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CallOverlayDialog
import com.example.ui.screens.CallsScreen
import com.example.ui.screens.CameraDialog
import com.example.ui.screens.ChatsScreen
import com.example.ui.screens.CommunitiesScreen
import com.example.ui.screens.ConversationScreen
import com.example.ui.screens.GuildsScreen
import com.example.ui.screens.HorizonScreen
import com.example.ui.screens.LatticeScreen
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

  // Full onboarding authentication gate: Phone -> Mock OTP 123456 -> Prism Persona Setup -> BOOM!
  if (!uiState.isLoggedIn) {
    AuthScreen(
      onCompleteAuth = { name, handle, facet, bio, avatarColor, phone ->
        viewModel.completeLogin(name, handle, facet, bio, avatarColor, phone)
      },
      onBypass = { viewModel.bypassLogin() }
    )
    return
  }

  // Handle system back navigation if inside a conversation or modal
  BackHandler(
    enabled = uiState.activeChat != null ||
      uiState.isSearchOpen ||
      uiState.isPrismProfileOpen ||
      uiState.isConduitTrayOpen ||
      uiState.isSynchronyOpen ||
      uiState.isScoutOpen ||
      uiState.isPublishDispatchOpen ||
      uiState.activeConduitDispatch != null ||
      uiState.activeConduitNode != null ||
      uiState.activeConduitVideo != null
  ) {
    when {
      uiState.isSearchOpen -> viewModel.closeSearch()
      uiState.activeChat != null -> viewModel.closeChat()
      uiState.isPrismProfileOpen -> viewModel.setPrismProfileOpen(false)
      uiState.isConduitTrayOpen -> viewModel.setConduitTrayOpen(false)
      uiState.isSynchronyOpen -> viewModel.setSynchronyOpen(false)
      uiState.isScoutOpen -> viewModel.setScoutOpen(false)
      uiState.isPublishDispatchOpen -> viewModel.setPublishDispatchOpen(false)
      uiState.activeConduitDispatch != null || uiState.activeConduitNode != null || uiState.activeConduitVideo != null -> viewModel.closeConduitIntro()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .testTag("kick_app_root")
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
        // Main Hub with Radial Horizon Navigation Bar
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
              NavTab.DIALOGS -> {
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
                  },
                  onOpenPrismProfile = { viewModel.setPrismProfileOpen(true) },
                  onOpenConduitTray = { viewModel.setConduitTrayOpen(true) }
                )
              }
              NavTab.HORIZON -> {
                HorizonScreen(
                  videos = uiState.horizonVideos,
                  dailyVouchesRemaining = uiState.prismProfile.dailyVouchesRemaining,
                  onVouchVideo = { viewModel.vouchVideo(it) },
                  onAddComment = { videoId, comment -> viewModel.addVideoComment(videoId, comment) },
                  onRequestConduitIntro = { viewModel.openConduitIntroForVideo(it) },
                  onOpenSynchrony = { viewModel.setSynchronyOpen(true) },
                  onOpenScout = { viewModel.setScoutOpen(true) },
                  onPublishVideo = { viewModel.setPublishDispatchOpen(true) }
                )
              }
              NavTab.LATTICE -> {
                LatticeScreen(
                  nodes = uiState.latticeNodes,
                  onNodeDirectMessage = { node ->
                    val existingChat = uiState.chats.find { it.contact.name == node.name }
                    if (existingChat != null) {
                      viewModel.openChat(existingChat)
                    } else {
                      val firstChat = uiState.chats.firstOrNull()
                      if (firstChat != null) viewModel.openChat(firstChat)
                    }
                  },
                  onNodeRequestConduit = { viewModel.openConduitIntroForNode(it) }
                )
              }
              NavTab.GUILDS -> {
                GuildsScreen(
                  guilds = uiState.guilds,
                  onEnterGuild = { /* View guild commons */ },
                  onJoinVoiceTable = { viewModel.setSynchronyOpen(true) }
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

    // Modal: Double-Blind Conduit Warm Intro Dialog
    if (uiState.activeConduitDispatch != null || uiState.activeConduitNode != null || uiState.activeConduitVideo != null) {
      val targetName = uiState.activeConduitDispatch?.authorName
        ?: uiState.activeConduitNode?.name
        ?: uiState.activeConduitVideo?.creatorName
        ?: ""
      val targetHandle = uiState.activeConduitDispatch?.authorHandle
        ?: uiState.activeConduitNode?.handle
        ?: uiState.activeConduitVideo?.creatorHandle
        ?: ""
      val targetFacet = uiState.activeConduitDispatch?.authorFacet
        ?: uiState.activeConduitNode?.facet
        ?: uiState.activeConduitVideo?.creatorFacet
        ?: ""
      val targetAvatarColor = uiState.activeConduitDispatch?.authorAvatarColor
        ?: uiState.activeConduitNode?.avatarColor
        ?: uiState.activeConduitVideo?.creatorAvatarColor
        ?: com.example.ui.theme.KickCyan

      ConduitIntroDialog(
        targetName = targetName,
        targetHandle = targetHandle,
        targetFacet = targetFacet,
        targetAvatarColor = targetAvatarColor,
        onSendRequest = { category, intent ->
          viewModel.sendConduitRequest(category, intent)
        },
        onDismiss = { viewModel.closeConduitIntro() }
      )
    }

    // Modal: Conduit Inbox & Facilitation Tray
    if (uiState.isConduitTrayOpen) {
      ConduitTrayDialog(
        requests = uiState.conduitRequests,
        onFacilitate = { viewModel.facilitateBridge(it) },
        onDecline = { viewModel.declineBridge(it) },
        onDismiss = { viewModel.setConduitTrayOpen(false) }
      )
    }

    // Modal: Scout AI Private Network Navigator
    if (uiState.isScoutOpen) {
      ScoutAssistantDialog(
        initialQueries = uiState.scoutQueries,
        onExecuteAction = {
          viewModel.setScoutOpen(false)
          viewModel.setTab(NavTab.HORIZON)
        },
        onDismiss = { viewModel.setScoutOpen(false) }
      )
    }

    // Modal: Synchrony Real-Time Resonance
    if (uiState.isSynchronyOpen) {
      SynchronyDialog(
        items = uiState.synchronyItems,
        onJoinRoundtable = { /* Joined audio session */ },
        onDismiss = { viewModel.setSynchronyOpen(false) }
      )
    }

    // Modal: The Prism Profile Contextual Persona
    if (uiState.isPrismProfileOpen) {
      PrismProfileDialog(
        profile = uiState.prismProfile,
        onDismiss = { viewModel.setPrismProfileOpen(false) },
        onLogout = {
          viewModel.setPrismProfileOpen(false)
          viewModel.logout()
        }
      )
    }

    // Modal: Publish Dispatch to Horizon
    if (uiState.isPublishDispatchOpen) {
      PublishDispatchDialog(
        onPublish = { title, content, type ->
          viewModel.publishDispatch(title, content, type)
        },
        onDismiss = { viewModel.setPublishDispatchOpen(false) }
      )
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
