package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.CallDirection
import com.example.model.CallLog
import com.example.model.CallType
import com.example.model.Channel
import com.example.model.Chat
import com.example.model.ChatFilter
import com.example.model.Community
import com.example.model.ConduitIntroRequest
import com.example.model.ConduitStatus
import com.example.model.Dispatch
import com.example.model.DispatchType
import com.example.model.Guild
import com.example.model.LatticeNode
import com.example.model.Message
import com.example.model.MessageStatus
import com.example.model.MessageType
import com.example.model.PrismProfile
import com.example.model.ScoutQuery
import com.example.model.StatusStory
import com.example.model.SynchronyItem
import com.example.model.User
import com.example.ui.components.NavTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class WhatsAppUiState(
  val currentTab: NavTab = NavTab.HORIZON,
  val selectedFilter: ChatFilter = ChatFilter.ALL,
  val activeChat: Chat? = null,
  val activeStory: StatusStory? = null,
  val activeCallContact: Pair<User, CallType>? = null,
  val isCameraOpen: Boolean = false,
  val isSearchOpen: Boolean = false,
  val chats: List<Chat> = SampleData.initialChats,
  val stories: List<StatusStory> = SampleData.sampleStories,
  val calls: List<CallLog> = SampleData.sampleCalls,
  val channels: List<Channel> = SampleData.sampleChannels,
  val communities: List<Community> = SampleData.sampleCommunities,
  // Kick Social Graph & Horizon State
  val isLoggedIn: Boolean = false,
  val horizonVideos: List<com.example.model.HorizonVideo> = SampleData.sampleHorizonVideos,
  val dispatches: List<Dispatch> = SampleData.sampleDispatches,
  val latticeNodes: List<LatticeNode> = SampleData.sampleLatticeNodes,
  val conduitRequests: List<ConduitIntroRequest> = SampleData.sampleConduitRequests,
  val synchronyItems: List<SynchronyItem> = SampleData.sampleSynchronyItems,
  val prismProfile: PrismProfile = SampleData.samplePrismProfile,
  val guilds: List<Guild> = SampleData.sampleGuilds,
  val scoutQueries: List<ScoutQuery> = SampleData.sampleScoutQueries,
  val activeConduitDispatch: Dispatch? = null,
  val activeConduitNode: LatticeNode? = null,
  val activeConduitVideo: com.example.model.HorizonVideo? = null,
  val isConduitTrayOpen: Boolean = false,
  val isSynchronyOpen: Boolean = false,
  val isScoutOpen: Boolean = false,
  val isPrismProfileOpen: Boolean = false,
  val isPublishDispatchOpen: Boolean = false
)

class WhatsAppViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(WhatsAppUiState())
  val uiState: StateFlow<WhatsAppUiState> = _uiState.asStateFlow()

  private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

  fun setTab(tab: NavTab) {
    _uiState.update { it.copy(currentTab = tab) }
  }

  fun setFilter(filter: ChatFilter) {
    _uiState.update { it.copy(selectedFilter = filter) }
  }

  fun openChat(chat: Chat) {
    // Clear unread count when opened
    val updatedChats = _uiState.value.chats.map {
      if (it.id == chat.id) it.copy(unreadCount = 0) else it
    }
    val targetChat = updatedChats.find { it.id == chat.id } ?: chat.copy(unreadCount = 0)
    _uiState.update {
      it.copy(
        activeChat = targetChat,
        chats = updatedChats
      )
    }
  }

  fun closeChat() {
    _uiState.update { it.copy(activeChat = null) }
  }

  fun openStory(story: StatusStory) {
    _uiState.update { it.copy(activeStory = story) }
  }

  fun closeStory() {
    _uiState.update { it.copy(activeStory = null) }
  }

  fun openCall(contact: User, type: CallType) {
    _uiState.update { it.copy(activeCallContact = contact to type) }
  }

  fun endCall() {
    val callData = _uiState.value.activeCallContact
    if (callData != null) {
      val (contact, type) = callData
      val newLog = CallLog(
        id = UUID.randomUUID().toString(),
        contact = contact,
        type = type,
        direction = CallDirection.OUTGOING,
        timestamp = "Just now",
        duration = "32s"
      )
      _uiState.update {
        it.copy(
          activeCallContact = null,
          calls = listOf(newLog) + it.calls
        )
      }
    } else {
      _uiState.update { it.copy(activeCallContact = null) }
    }
  }

  fun openCamera() {
    _uiState.update { it.copy(isCameraOpen = true) }
  }

  fun closeCamera() {
    _uiState.update { it.copy(isCameraOpen = false) }
  }

  fun openSearch() {
    _uiState.update { it.copy(isSearchOpen = true) }
  }

  fun closeSearch() {
    _uiState.update { it.copy(isSearchOpen = false) }
  }

  fun sendMessage(text: String) {
    val currentActive = _uiState.value.activeChat ?: return
    val timestamp = timeFormat.format(Date())

    val outgoingMsg = Message(
      id = UUID.randomUUID().toString(),
      senderId = "user_me",
      text = text,
      timestamp = timestamp,
      isOutgoing = true,
      status = MessageStatus.READ
    )

    val updatedMessages = currentActive.messages + outgoingMsg
    val updatedChat = currentActive.copy(
      messages = updatedMessages,
      lastMessage = outgoingMsg
    )

    val updatedChatList = _uiState.value.chats.map {
      if (it.id == updatedChat.id) updatedChat else it
    }

    _uiState.update {
      it.copy(
        activeChat = updatedChat,
        chats = updatedChatList
      )
    }

    // Trigger realistic typing and automated intelligent reply after delay
    triggerContactReply(updatedChat, text)
  }

  private fun triggerContactReply(chat: Chat, userText: String) {
    viewModelScope.launch {
      delay(800)
      // Set typing indicator
      val typingChat = chat.copy(isTyping = true)
      _uiState.update { state ->
        state.copy(
          activeChat = if (state.activeChat?.id == chat.id) typingChat else state.activeChat,
          chats = state.chats.map { if (it.id == chat.id) typingChat else it }
        )
      }

      delay(1400)

      val replyText = generateReply(chat.contact.name, userText)
      val replyTimestamp = timeFormat.format(Date())

      val incomingMsg = Message(
        id = UUID.randomUUID().toString(),
        senderId = chat.contact.id,
        text = replyText,
        timestamp = replyTimestamp,
        isOutgoing = false,
        status = MessageStatus.READ
      )

      val finalMessages = typingChat.messages + incomingMsg
      val finalizedChat = typingChat.copy(
        messages = finalMessages,
        lastMessage = incomingMsg,
        isTyping = false
      )

      _uiState.update { state ->
        state.copy(
          activeChat = if (state.activeChat?.id == chat.id) finalizedChat else state.activeChat,
          chats = state.chats.map { if (it.id == chat.id) finalizedChat else it }
        )
      }
    }
  }

  private fun generateReply(contactName: String, prompt: String): String {
    val lower = prompt.lowercase()
    return when {
      lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ->
        "Hey! Great to hear from you. Have you checked out the new Liquid Glass design?"
      lower.contains("liquid glass") || lower.contains("design") ->
        "The frosted glass input bar and pill bubbles look super clean on Android! 🌟"
      lower.contains("call") || lower.contains("talk") ->
        "Sure, let's jump on a quick WhatsApp call whenever you're ready! 📞"
      lower.contains("meet") || lower.contains("lunch") || lower.contains("coffee") ->
        "Sounds like a plan! Let me know the place and time. ☕"
      lower.contains("photo") || lower.contains("picture") ->
        "Love the new rounded preview frames. Everything feels much more cohesive now."
      else ->
        "Got it! That sounds great. Really enjoying the new interface speed and responsiveness! 💬"
    }
  }

  fun addReaction(messageId: String, emoji: String) {
    val activeChat = _uiState.value.activeChat ?: return
    val updatedMessages = activeChat.messages.map { msg ->
      if (msg.id == messageId) {
        val current = msg.reactions.toMutableList()
        if (current.contains(emoji)) {
          current.remove(emoji)
        } else {
          current.add(emoji)
        }
        msg.copy(reactions = current)
      } else {
        msg
      }
    }

    val updatedChat = activeChat.copy(messages = updatedMessages)
    _uiState.update { state ->
      state.copy(
        activeChat = updatedChat,
        chats = state.chats.map { if (it.id == updatedChat.id) updatedChat else it }
      )
    }
  }

  fun replyToStory(text: String) {
    val story = _uiState.value.activeStory ?: return
    val targetChat = _uiState.value.chats.find { it.contact.id == story.contact.id }
    if (targetChat != null) {
      openChat(targetChat)
      sendMessage("Replying to story: $text")
    }
  }

  // ==================== KICK SIGNATURE ACTIONS ====================

  fun vouchDispatch(dispatchId: String) {
    val currentProfile = _uiState.value.prismProfile
    _uiState.update { state ->
      val updatedDispatches = state.dispatches.map { d ->
        if (d.id == dispatchId) {
          val willVouch = !d.hasVouched
          val newCount = if (willVouch) d.vouchCount + 1 else (d.vouchCount - 1).coerceAtLeast(0)
          d.copy(hasVouched = willVouch, vouchCount = newCount)
        } else {
          d
        }
      }
      val dailyLeft = (currentProfile.dailyVouchesRemaining - 1).coerceAtLeast(0)
      state.copy(
        dispatches = updatedDispatches,
        prismProfile = currentProfile.copy(dailyVouchesRemaining = dailyLeft)
      )
    }
  }

  fun publishDispatch(title: String, content: String, type: DispatchType) {
    val newDispatch = Dispatch(
      id = "disp_${System.currentTimeMillis()}",
      authorName = _uiState.value.prismProfile.name,
      authorHandle = _uiState.value.prismProfile.handle,
      authorAvatarColor = _uiState.value.prismProfile.avatarColor,
      authorFacet = "Distributed Architect & Craft",
      socialDistance = 1,
      distanceBadge = "1° Direct Author",
      timeAgo = "Just now",
      contentType = type,
      title = title.ifBlank { null },
      content = content,
      vouchCount = 1,
      hasVouched = true,
      attestationSnippet = "Directly authored • Encrypted distribution"
    )
    _uiState.update { state ->
      state.copy(dispatches = listOf(newDispatch) + state.dispatches)
    }
  }

  fun openConduitIntro(dispatch: Dispatch) {
    _uiState.update { it.copy(activeConduitDispatch = dispatch, activeConduitNode = null) }
  }

  fun openConduitIntroForNode(node: LatticeNode) {
    _uiState.update { it.copy(activeConduitNode = node, activeConduitDispatch = null) }
  }

  fun closeConduitIntro() {
    _uiState.update { it.copy(activeConduitDispatch = null, activeConduitNode = null) }
  }

  fun sendConduitRequest(category: String, intentStatement: String) {
    val dispatch = _uiState.value.activeConduitDispatch
    val node = _uiState.value.activeConduitNode
    val video = _uiState.value.activeConduitVideo

    val targetName = dispatch?.authorName ?: node?.name ?: video?.creatorName ?: "Contact"
    val targetHandle = dispatch?.authorHandle ?: node?.handle ?: video?.creatorHandle ?: "@user"
    val targetFacet = dispatch?.authorFacet ?: node?.facet ?: video?.creatorFacet ?: "Builder"

    val newRequest = ConduitIntroRequest(
      id = "req_${System.currentTimeMillis()}",
      targetName = targetName,
      targetHandle = targetHandle,
      targetFacet = targetFacet,
      category = category,
      intentStatement = intentStatement,
      timestamp = "Just now",
      status = ConduitStatus.PENDING_BRIDGE,
      isIncomingForBridge = false
    )

    _uiState.update { state ->
      state.copy(
        conduitRequests = listOf(newRequest) + state.conduitRequests,
        activeConduitDispatch = null,
        activeConduitNode = null,
        activeConduitVideo = null
      )
    }
  }

  fun setConduitTrayOpen(isOpen: Boolean) {
    _uiState.update { it.copy(isConduitTrayOpen = isOpen) }
  }

  fun facilitateBridge(requestId: String) {
    _uiState.update { state ->
      state.copy(
        conduitRequests = state.conduitRequests.filter { it.id != requestId }
      )
    }
  }

  fun declineBridge(requestId: String) {
    _uiState.update { state ->
      state.copy(
        conduitRequests = state.conduitRequests.filter { it.id != requestId }
      )
    }
  }

  fun setSynchronyOpen(isOpen: Boolean) {
    _uiState.update { it.copy(isSynchronyOpen = isOpen) }
  }

  fun setScoutOpen(isOpen: Boolean) {
    _uiState.update { it.copy(isScoutOpen = isOpen) }
  }

  fun setPrismProfileOpen(isOpen: Boolean) {
    _uiState.update { it.copy(isPrismProfileOpen = isOpen) }
  }

  fun openConduitIntroForVideo(video: com.example.model.HorizonVideo) {
    _uiState.update { it.copy(activeConduitVideo = video, activeConduitDispatch = null, activeConduitNode = null) }
  }

  fun vouchVideo(videoId: String) {
    val currentProfile = _uiState.value.prismProfile
    _uiState.update { state ->
      val updatedVideos = state.horizonVideos.map { v ->
        if (v.id == videoId) {
          val willVouch = !v.isVouched
          val newCount = if (willVouch) v.vouchCount + 1 else (v.vouchCount - 1).coerceAtLeast(0)
          v.copy(isVouched = willVouch, vouchCount = newCount)
        } else {
          v
        }
      }
      val dailyLeft = (currentProfile.dailyVouchesRemaining - 1).coerceAtLeast(0)
      state.copy(
        horizonVideos = updatedVideos,
        prismProfile = currentProfile.copy(dailyVouchesRemaining = dailyLeft)
      )
    }
  }

  fun addVideoComment(videoId: String, text: String) {
    if (text.isBlank()) return
    val profile = _uiState.value.prismProfile
    val newComment = com.example.model.HorizonVideoComment(
      id = "c_${System.currentTimeMillis()}",
      authorName = profile.name,
      authorHandle = profile.handle,
      authorDistance = "1° Direct",
      text = text.trim(),
      timeAgo = "Just now",
      vouchCount = 0
    )
    _uiState.update { state ->
      val updatedVideos = state.horizonVideos.map { v ->
        if (v.id == videoId) {
          v.copy(
            commentCount = v.commentCount + 1,
            comments = listOf(newComment) + v.comments
          )
        } else {
          v
        }
      }
      state.copy(horizonVideos = updatedVideos)
    }
  }

  fun completeLogin(
    name: String,
    handle: String,
    facet: String,
    bio: String,
    avatarColor: androidx.compose.ui.graphics.Color,
    phone: String
  ) {
    val newFacet = com.example.model.PrismFacet(
      name = facet,
      headline = bio,
      tags = listOf("Primary Orbit", "Verified", "Kick Core"),
      visibility = "Open to 2° Circle"
    )
    val updatedProfile = _uiState.value.prismProfile.copy(
      name = name,
      handle = handle,
      avatarColor = avatarColor,
      phoneStatus = "Sealed via ZK ($phone)",
      facets = listOf(newFacet) + _uiState.value.prismProfile.facets.filter { it.name != facet }
    )
    _uiState.update {
      it.copy(
        isLoggedIn = true,
        prismProfile = updatedProfile
      )
    }
  }

  fun logout() {
    _uiState.update { it.copy(isLoggedIn = false) }
  }

  fun bypassLogin() {
    _uiState.update { it.copy(isLoggedIn = true) }
  }

  fun setPublishDispatchOpen(isOpen: Boolean) {
    _uiState.update { it.copy(isPublishDispatchOpen = isOpen) }
  }
}
