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
import com.example.model.Message
import com.example.model.MessageStatus
import com.example.model.MessageType
import com.example.model.StatusStory
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
  val currentTab: NavTab = NavTab.CHATS,
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
  val communities: List<Community> = SampleData.sampleCommunities
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
}
