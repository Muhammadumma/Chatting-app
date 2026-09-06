package com.example.data

import androidx.compose.ui.graphics.Color
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
import com.example.model.ReplyPreview
import com.example.model.StatusStory
import com.example.model.User

object SampleData {
  val currentUser = User(
    id = "user_me",
    name = "You",
    initials = "ME",
    avatarColor = Color(0xFF00A884),
    isOnline = true
  )

  val userAlex = User(
    id = "user_alex",
    name = "Alex Rivera",
    initials = "AR",
    avatarColor = Color(0xFF1E88E5),
    isOnline = true,
    lastSeen = "online",
    phone = "+1 (555) 382-9012"
  )

  val userSarah = User(
    id = "user_sarah",
    name = "Sarah Connor",
    initials = "SC",
    avatarColor = Color(0xFF9C27B0),
    isOnline = true,
    lastSeen = "online",
    phone = "+1 (555) 749-2184"
  )

  val userDevGroup = User(
    id = "user_dev_team",
    name = "Mobile Architecture Guild ⚡",
    initials = "MG",
    avatarColor = Color(0xFF00897B),
    isOnline = false,
    lastSeen = "Elena, Maya, Alex, You",
    phone = "Group • 18 members"
  )

  val userDesignSprint = User(
    id = "user_design_sprint",
    name = "Liquid Glass UI Core ✨",
    initials = "LG",
    avatarColor = Color(0xFFE91E63),
    isOnline = true,
    lastSeen = "Marcus typing...",
    phone = "Group • 8 members"
  )

  val userTechSupport = User(
    id = "user_tech_support",
    name = "CloudSync Cloud Services",
    initials = "CS",
    avatarColor = Color(0xFF0288D1),
    isOnline = true,
    lastSeen = "Official Business Account",
    isVerified = true,
    phone = "+1 (800) 555-0199"
  )

  val userEmma = User(
    id = "user_emma",
    name = "Emma Watson",
    initials = "EW",
    avatarColor = Color(0xFFFB8C00),
    isOnline = false,
    lastSeen = "yesterday at 10:14 PM",
    phone = "+1 (555) 890-1234"
  )

  val userLiam = User(
    id = "user_liam",
    name = "Liam Parker",
    initials = "LP",
    avatarColor = Color(0xFF43A047),
    isOnline = false,
    lastSeen = "today at 8:45 AM",
    phone = "+1 (555) 678-9012"
  )

  val userDavid = User(
    id = "user_david",
    name = "David Miller",
    initials = "DM",
    avatarColor = Color(0xFF5E35B1),
    isOnline = false,
    lastSeen = "today at 1:12 PM",
    phone = "+1 (555) 432-1098"
  )

  val initialChats: List<Chat> = listOf(
    Chat(
      id = "chat_alex",
      contact = userAlex,
      unreadCount = 2,
      isPinned = true,
      category = ChatFilter.PERSONAL,
      lastMessage = Message(
        id = "m_alex_latest",
        senderId = "user_alex",
        text = "The new Liquid Glass navigation bar looks incredible! Have you tested the frosted blur effect yet?",
        timestamp = "11:42 AM",
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      messages = listOf(
        Message(
          id = "m_alex_1",
          senderId = "user_alex",
          text = "Hey! Did you check out WhatsApp's latest Android beta update?",
          timestamp = "11:35 AM",
          isOutgoing = false,
          status = MessageStatus.READ
        ),
        Message(
          id = "m_alex_2",
          senderId = "user_me",
          text = "Yes! The pill-shaped message bubbles and floating input bar feel so much more ergonomic.",
          timestamp = "11:37 AM",
          isOutgoing = true,
          status = MessageStatus.READ,
          reactions = listOf("🔥")
        ),
        Message(
          id = "m_alex_3",
          senderId = "user_alex",
          text = "Voice message",
          timestamp = "11:39 AM",
          isOutgoing = false,
          status = MessageStatus.READ,
          type = MessageType.AUDIO,
          audioDuration = "0:28"
        ),
        Message(
          id = "m_alex_4",
          senderId = "user_me",
          text = "Totally agree with the audio feedback. Loving the rounded corners everywhere.",
          timestamp = "11:40 AM",
          isOutgoing = true,
          status = MessageStatus.READ,
          replyTo = ReplyPreview("Alex Rivera", "Voice message (0:28)")
        ),
        Message(
          id = "m_alex_5",
          senderId = "user_alex",
          text = "The new Liquid Glass navigation bar looks incredible! Have you tested the frosted blur effect yet?",
          timestamp = "11:42 AM",
          isOutgoing = false,
          status = MessageStatus.READ,
          reactions = listOf("❤️", "👍")
        )
      )
    ),
    Chat(
      id = "chat_design_sprint",
      contact = userDesignSprint,
      unreadCount = 5,
      isPinned = true,
      category = ChatFilter.GROUPS,
      lastMessage = Message(
        id = "m_ds_latest",
        senderId = "user_marcus",
        text = "Marcus: Check out the new rounded pill bubbles prototype screenshot!",
        timestamp = "11:15 AM",
        isOutgoing = false,
        status = MessageStatus.DELIVERED
      ),
      messages = listOf(
        Message(
          id = "m_ds_1",
          senderId = "user_sarah",
          text = "Team, we are moving the status updates directly under the top app bar!",
          timestamp = "10:50 AM",
          isOutgoing = false,
          status = MessageStatus.READ
        ),
        Message(
          id = "m_ds_2",
          senderId = "user_me",
          text = "That will drastically improve story discovery right from the Chats tab.",
          timestamp = "10:55 AM",
          isOutgoing = true,
          status = MessageStatus.READ
        ),
        Message(
          id = "m_ds_3",
          senderId = "user_marcus",
          text = "Design mockup",
          timestamp = "11:15 AM",
          isOutgoing = false,
          status = MessageStatus.DELIVERED,
          type = MessageType.PHOTO,
          mediaCaption = "Liquid Glass floating input bar & pill bubbles render"
        )
      )
    ),
    Chat(
      id = "chat_sarah",
      contact = userSarah,
      unreadCount = 0,
      isPinned = false,
      category = ChatFilter.PERSONAL,
      lastMessage = Message(
        id = "m_sarah_latest",
        senderId = "user_me",
        text = "See you at the coffee shop at 2:00 PM!",
        timestamp = "10:30 AM",
        isOutgoing = true,
        status = MessageStatus.READ
      ),
      messages = listOf(
        Message(
          id = "m_sarah_1",
          senderId = "user_sarah",
          text = "Are we still on for lunch or coffee this afternoon?",
          timestamp = "10:25 AM",
          isOutgoing = false,
          status = MessageStatus.READ
        ),
        Message(
          id = "m_sarah_2",
          senderId = "user_me",
          text = "See you at the coffee shop at 2:00 PM!",
          timestamp = "10:30 AM",
          isOutgoing = true,
          status = MessageStatus.READ,
          reactions = listOf("👍")
        )
      )
    ),
    Chat(
      id = "chat_tech_support",
      contact = userTechSupport,
      unreadCount = 1,
      isPinned = false,
      category = ChatFilter.BUSINESS,
      lastMessage = Message(
        id = "m_ts_latest",
        senderId = "user_tech_support",
        text = "Your account verification is complete. Welcome to CloudSync Pro! 🚀",
        timestamp = "9:18 AM",
        isOutgoing = false,
        status = MessageStatus.DELIVERED
      ),
      messages = listOf(
        Message(
          id = "m_ts_1",
          senderId = "user_me",
          text = "Hi, I just upgraded my team subscription.",
          timestamp = "9:15 AM",
          isOutgoing = true,
          status = MessageStatus.READ
        ),
        Message(
          id = "m_ts_2",
          senderId = "user_tech_support",
          text = "Your account verification is complete. Welcome to CloudSync Pro! 🚀",
          timestamp = "9:18 AM",
          isOutgoing = false,
          status = MessageStatus.DELIVERED
        )
      )
    ),
    Chat(
      id = "chat_dev_group",
      contact = userDevGroup,
      unreadCount = 0,
      isPinned = false,
      category = ChatFilter.GROUPS,
      lastMessage = Message(
        id = "m_dev_latest",
        senderId = "user_liam",
        text = "Liam: Merged the edge-to-edge Compose window insets fix.",
        timestamp = "Yesterday",
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      messages = listOf(
        Message(
          id = "m_dev_1",
          senderId = "user_liam",
          text = "Liam: Merged the edge-to-edge Compose window insets fix.",
          timestamp = "Yesterday",
          isOutgoing = false,
          status = MessageStatus.READ
        )
      )
    ),
    Chat(
      id = "chat_emma",
      contact = userEmma,
      unreadCount = 0,
      isPinned = false,
      category = ChatFilter.PERSONAL,
      lastMessage = Message(
        id = "m_emma_latest",
        senderId = "user_emma",
        text = "Thanks for sending the photos over! They look fantastic 📸",
        timestamp = "Yesterday",
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      messages = listOf(
        Message(
          id = "m_emma_1",
          senderId = "user_me",
          text = "Here are the photos from yesterday's summit!",
          timestamp = "Yesterday",
          isOutgoing = true,
          status = MessageStatus.READ
        ),
        Message(
          id = "m_emma_2",
          senderId = "user_emma",
          text = "Thanks for sending the photos over! They look fantastic 📸",
          timestamp = "Yesterday",
          isOutgoing = false,
          status = MessageStatus.READ,
          reactions = listOf("❤️")
        )
      )
    ),
    Chat(
      id = "chat_liam",
      contact = userLiam,
      unreadCount = 0,
      isPinned = false,
      category = ChatFilter.PERSONAL,
      lastMessage = Message(
        id = "m_liam_latest",
        senderId = "user_me",
        text = "Sent the updated design specs.",
        timestamp = "Wednesday",
        isOutgoing = true,
        status = MessageStatus.READ
      ),
      messages = listOf(
        Message(
          id = "m_liam_1",
          senderId = "user_me",
          text = "Sent the updated design specs.",
          timestamp = "Wednesday",
          isOutgoing = true,
          status = MessageStatus.READ
        )
      )
    ),
    Chat(
      id = "chat_david",
      contact = userDavid,
      unreadCount = 0,
      isPinned = false,
      category = ChatFilter.PERSONAL,
      lastMessage = Message(
        id = "m_david_latest",
        senderId = "user_david",
        text = "Let me know when you are free to discuss the project roadmap.",
        timestamp = "Tuesday",
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      messages = listOf(
        Message(
          id = "m_david_1",
          senderId = "user_david",
          text = "Let me know when you are free to discuss the project roadmap.",
          timestamp = "Tuesday",
          isOutgoing = false,
          status = MessageStatus.READ
        )
      )
    )
  )

  val sampleStories: List<StatusStory> = listOf(
    StatusStory(
      id = "story_alex",
      contact = userAlex,
      timestamp = "45m ago",
      caption = "Morning sprint review at the terrace cafe! ☕🥐 Enjoying the crisp autumn air.",
      backgroundColors = listOf(Color(0xFF1E88E5), Color(0xFF00ACC1)),
      isViewed = false,
      emojiAsset = "☕"
    ),
    StatusStory(
      id = "story_sarah",
      contact = userSarah,
      timestamp = "2h ago",
      caption = "Prototyping fluid micro-animations with Jetpack Compose & Liquid Glass ✨",
      backgroundColors = listOf(Color(0xFF8E24AA), Color(0xFFE91E63)),
      isViewed = false,
      emojiAsset = "🎨"
    ),
    StatusStory(
      id = "story_marcus",
      contact = User("user_marcus", "Marcus Vance", "MV", Color(0xFF43A047)),
      timestamp = "4h ago",
      caption = "Golden hour bike ride through the city trail 🚴‍♂️🌄",
      backgroundColors = listOf(Color(0xFFFF8F00), Color(0xFFFF3D00)),
      isViewed = false,
      emojiAsset = "🚴"
    ),
    StatusStory(
      id = "story_emma",
      contact = userEmma,
      timestamp = "7h ago",
      caption = "Finished reading 'Designing Data-Intensive Applications'. Highly recommended! 📚",
      backgroundColors = listOf(Color(0xFF3949AB), Color(0xFF1E88E5)),
      isViewed = true,
      emojiAsset = "📖"
    ),
    StatusStory(
      id = "story_liam",
      contact = userLiam,
      timestamp = "Yesterday",
      caption = "Sunset vibes from Lisbon 🌊🇵🇹",
      backgroundColors = listOf(Color(0xFF00897B), Color(0xFF26A69A)),
      isViewed = true,
      emojiAsset = "🌅"
    )
  )

  val sampleCalls: List<CallLog> = listOf(
    CallLog(
      id = "call_1",
      contact = userAlex,
      type = CallType.VIDEO,
      direction = CallDirection.INCOMING,
      timestamp = "Today, 11:20 AM",
      duration = "14m 20s"
    ),
    CallLog(
      id = "call_2",
      contact = userSarah,
      type = CallType.AUDIO,
      direction = CallDirection.OUTGOING,
      timestamp = "Today, 9:45 AM",
      duration = "6m 12s"
    ),
    CallLog(
      id = "call_3",
      contact = userDavid,
      type = CallType.AUDIO,
      direction = CallDirection.MISSED,
      timestamp = "Yesterday, 6:15 PM",
      duration = "Missed"
    ),
    CallLog(
      id = "call_4",
      contact = userEmma,
      type = CallType.VIDEO,
      direction = CallDirection.INCOMING,
      timestamp = "Yesterday, 3:30 PM",
      duration = "22m 05s"
    ),
    CallLog(
      id = "call_5",
      contact = userLiam,
      type = CallType.AUDIO,
      direction = CallDirection.OUTGOING,
      timestamp = "September 4, 8:10 PM",
      duration = "2m 44s"
    )
  )

  val sampleChannels: List<Channel> = listOf(
    Channel(
      id = "channel_whatsapp",
      name = "WhatsApp",
      followers = "174M followers",
      isVerified = true,
      avatarColor = Color(0xFF00A884),
      isFollowing = true,
      recentPost = "We're testing a fresh look for WhatsApp on Android, featuring sleek Liquid Glass translucency and pill message bubbles.",
      recentPostTime = "2h ago",
      category = "Official"
    ),
    Channel(
      id = "channel_natgeo",
      name = "National Geographic",
      followers = "21.4M followers",
      isVerified = true,
      avatarColor = Color(0xFFFFB300),
      isFollowing = false,
      recentPost = "Photographers capture rare deep-sea bioluminescent species off the Monterey trench.",
      recentPostTime = "4h ago",
      category = "Nature & Science"
    ),
    Channel(
      id = "channel_techcrunch",
      name = "TechCrunch",
      followers = "8.2M followers",
      isVerified = true,
      avatarColor = Color(0xFF00C853),
      isFollowing = true,
      recentPost = "The shift towards ergonomic thumb-zone mobile UI: Why bottom navigation won.",
      recentPostTime = "6h ago",
      category = "Technology"
    ),
    Channel(
      id = "channel_f1",
      name = "Formula 1",
      followers = "14.9M followers",
      isVerified = true,
      avatarColor = Color(0xFFE53935),
      isFollowing = false,
      recentPost = "Track action highlights from Monza qualifying: tenths separating the top 4!",
      recentPostTime = "8h ago",
      category = "Sports"
    )
  )

  val sampleCommunities: List<Community> = listOf(
    Community(
      id = "comm_silicon",
      name = "Android & Kotlin Architecture Hub",
      description = "Official community for Android engineers, UI craftspersons, and Compose enthusiasts.",
      memberCount = "1,420 members",
      announcementSnippet = "Welcome everyone! Check out the Liquid Glass M3 specification guidelines pinned in resources.",
      announcementTime = "Today, 10:00 AM",
      subGroups = listOf("Announcements 📢", "Jetpack Compose Showcase 🎨", "Architecture & Room DB 🏛️", "Job Board & Meetups 💼"),
      iconColor = Color(0xFF00A884)
    ),
    Community(
      id = "comm_neighborhood",
      name = "Sunnyvale Community & Residents",
      description = "Local community board for events, neighborhood safety, and community garden updates.",
      memberCount = "384 members",
      announcementSnippet = "Annual neighborhood autumn block party scheduled for next Saturday!",
      announcementTime = "Yesterday",
      subGroups = listOf("Community Announcements 📢", "Neighborhood Watch 🛡️", "Lost & Found 🐾", "Buy & Sell 🏷️"),
      iconColor = Color(0xFF1E88E5)
    )
  )
}
