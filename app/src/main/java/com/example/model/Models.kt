package com.example.model

import androidx.compose.ui.graphics.Color

data class User(
  val id: String,
  val name: String,
  val initials: String,
  val avatarColor: Color,
  val isOnline: Boolean = false,
  val lastSeen: String = "online",
  val isVerified: Boolean = false,
  val phone: String = "+1 234 567 8900"
)

enum class MessageStatus {
  SENDING,
  SENT,
  DELIVERED,
  READ
}

enum class MessageType {
  TEXT,
  PHOTO,
  AUDIO,
  DOCUMENT
}

data class ReplyPreview(
  val senderName: String,
  val messageSnippet: String
)

data class Message(
  val id: String,
  val senderId: String,
  val text: String,
  val timestamp: String,
  val isOutgoing: Boolean,
  val status: MessageStatus = MessageStatus.READ,
  val type: MessageType = MessageType.TEXT,
  val mediaCaption: String? = null,
  val audioDuration: String? = null,
  val reactions: List<String> = emptyList(),
  val replyTo: ReplyPreview? = null
)

enum class ChatFilter {
  ALL,
  UNREAD,
  PERSONAL,
  BUSINESS,
  GROUPS
}

data class Chat(
  val id: String,
  val contact: User,
  val lastMessage: Message,
  val unreadCount: Int = 0,
  val isPinned: Boolean = false,
  val isMuted: Boolean = false,
  val category: ChatFilter = ChatFilter.PERSONAL,
  val messages: List<Message> = emptyList(),
  val isTyping: Boolean = false
)

data class StatusStory(
  val id: String,
  val contact: User,
  val timestamp: String,
  val caption: String,
  val backgroundColors: List<Color>,
  val isViewed: Boolean = false,
  val emojiAsset: String = "✨"
)

enum class CallType {
  AUDIO,
  VIDEO
}

enum class CallDirection {
  INCOMING,
  OUTGOING,
  MISSED
}

data class CallLog(
  val id: String,
  val contact: User,
  val type: CallType,
  val direction: CallDirection,
  val timestamp: String,
  val duration: String
)

data class Channel(
  val id: String,
  val name: String,
  val followers: String,
  val isVerified: Boolean = true,
  val avatarColor: Color,
  val isFollowing: Boolean = false,
  val recentPost: String,
  val recentPostTime: String,
  val category: String
)

data class Community(
  val id: String,
  val name: String,
  val description: String,
  val memberCount: String,
  val announcementSnippet: String,
  val announcementTime: String,
  val subGroups: List<String>,
  val iconColor: Color
)
