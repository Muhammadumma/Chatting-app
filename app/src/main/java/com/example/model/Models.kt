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

// ==================== KICK SOCIAL GRAPH & HORIZON MODELS ====================

enum class DispatchType {
  BRIEF,
  AUDIO_MEMO,
  ARTIFACT,
  INQUIRY,
  DEEP_DIVE,
  OPPORTUNITY
}

data class Dispatch(
  val id: String,
  val authorName: String,
  val authorHandle: String,
  val authorAvatarColor: Color,
  val authorFacet: String,
  val socialDistance: Int, // 1 = Direct, 2 = 2° Extended, 3 = 3° Interest
  val distanceBadge: String,
  val timeAgo: String,
  val contentType: DispatchType,
  val title: String? = null,
  val content: String,
  val audioDuration: String? = null,
  val artifactTitle: String? = null,
  val artifactSubtitle: String? = null,
  val artifactBadge: String? = null,
  val artifactUrl: String? = null,
  val inquiryBounty: String? = null,
  val vouchCount: Int,
  val hasVouched: Boolean = false,
  val attestationSnippet: String,
  val intentRepliesCount: Int = 0,
  val targetNodeId: String? = null
)

data class LatticeNode(
  val id: String,
  val name: String,
  val handle: String,
  val facet: String,
  val orbitDistance: Int, // 1 = Direct, 2 = Horizon (2°), 3 = Interest Nebula (3°)
  val clusterCategory: String,
  val avatarColor: Color,
  val activeDispatchesCount: Int,
  val vouchesCount: Int,
  val radialAngleDeg: Float,
  val normalizedRadius: Float, // 0.0 to 1.0 from center
  val recentTopic: String,
  val isConnectedDirectly: Boolean = false
)

enum class ConduitStatus {
  PENDING_BRIDGE,
  FACILITATED,
  DECLINED
}

data class ConduitIntroRequest(
  val id: String,
  val targetName: String,
  val targetHandle: String,
  val targetFacet: String,
  val category: String,
  val intentStatement: String,
  val timestamp: String,
  val status: ConduitStatus = ConduitStatus.PENDING_BRIDGE,
  val isIncomingForBridge: Boolean = false,
  val requesterName: String? = null,
  val targetCandidateName: String? = null,
  val requesterHandle: String? = null
)

enum class SynchronyType {
  LIVE_ROUNDTABLE,
  ACTIVE_INQUIRY,
  BREAKING_CLUSTER
}

data class SynchronyItem(
  val id: String,
  val title: String,
  val type: SynchronyType,
  val participantCount: Int,
  val snippet: String,
  val urgencyHoursLeft: Int? = null,
  val socialDistanceContext: String
)

data class PrismFacet(
  val name: String,
  val headline: String,
  val tags: List<String>,
  val visibility: String
)

data class PrismProfile(
  val id: String,
  val name: String,
  val handle: String,
  val phoneStatus: String = "Cryptographically Sealed (Hidden)",
  val totalVouchesReceived: Int,
  val dailyVouchesRemaining: Int,
  val facets: List<PrismFacet>,
  val activeEndeavors: List<String>,
  val avatarColor: Color
)

data class Guild(
  val id: String,
  val name: String,
  val headline: String,
  val memberCount: String,
  val trustRequirement: String,
  val activeVoiceUsers: Int,
  val iconColor: Color,
  val activeDiscussion: String,
  val subChannels: List<String>,
  val isMember: Boolean = false
)

data class ScoutQuery(
  val id: String,
  val prompt: String,
  val responseSummary: String,
  val matchedCount: Int,
  val suggestedAction: String
)

data class HorizonVideoComment(
  val id: String,
  val authorName: String,
  val authorHandle: String,
  val authorDistance: String,
  val text: String,
  val timeAgo: String,
  val vouchCount: Int
)

data class HorizonVideo(
  val id: String,
  val creatorName: String,
  val creatorHandle: String,
  val creatorDistance: String, // "1° Direct", "2° Extended Horizon", "3° Interest Mesh"
  val creatorFacet: String,
  val creatorAvatarColor: Color,
  val videoUrl: String,
  val fallbackThumbnailUrl: String,
  val caption: String,
  val tags: List<String>,
  val audioTrackTitle: String,
  val vouchCount: Int,
  val isVouched: Boolean = false,
  val commentCount: Int,
  val shareCount: Int,
  val comments: List<HorizonVideoComment> = emptyList(),
  val attestationProof: String = "zk-SNARK Trust Vicinity Verified"
)

enum class AuthStep {
  PHONE_ENTRY,
  OTP_VERIFICATION,
  PROFILE_SETUP,
  AUTHENTICATED
}

data class AuthState(
  val step: AuthStep = AuthStep.AUTHENTICATED,
  val countryCode: String = "+1",
  val phoneNumber: String = "",
  val otpCode: String = "",
  val otpError: String? = null,
  val resendCountdown: Int = 30,
  val isLoggedIn: Boolean = true
)

