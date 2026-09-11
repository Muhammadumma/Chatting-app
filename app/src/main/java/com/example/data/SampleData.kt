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

  // ==================== KICK SIGNATURE DATASETS ====================

  val sampleDispatches: List<com.example.model.Dispatch> = listOf(
    com.example.model.Dispatch(
      id = "disp_1",
      authorName = "Fatima Al-Mansoor",
      authorHandle = "@fatima.arch",
      authorAvatarColor = Color(0xFF00E5B8),
      authorFacet = "Distributed Systems Architect",
      socialDistance = 2,
      distanceBadge = "2° Extended Network",
      timeAgo = "18m ago",
      contentType = com.example.model.DispatchType.ARTIFACT,
      title = "Open Sourcing PebbleSync: Local-first ZK Database",
      content = "We just open-sourced PebbleSync—a zero-knowledge Raft consensus engine for mobile client synchronization. Designed to run offline-first with differential privacy guarantees.",
      artifactTitle = "PebbleSync Core Engine (v0.9)",
      artifactSubtitle = "github.com/pebblesync/core • Rust + C FFI",
      artifactBadge = "Open Source Artifact",
      artifactUrl = "https://github.com",
      vouchCount = 42,
      attestationSnippet = "Vouched by 4 people in your direct circle",
      intentRepliesCount = 11,
      targetNodeId = "node_fatima"
    ),
    com.example.model.Dispatch(
      id = "disp_2",
      authorName = "Elena Rostova",
      authorHandle = "@elena.robotics",
      authorAvatarColor = Color(0xFFFA8231),
      authorFacet = "Autonomous Robotics Lead",
      socialDistance = 2,
      distanceBadge = "2° Extended Network",
      timeAgo = "42m ago",
      contentType = com.example.model.DispatchType.INQUIRY,
      title = "Priority Inquiry: Neuromorphic Sensor Counsel",
      content = "Looking for a patent attorney based in Berlin or Munich who deeply understands event-based neuromorphic vision sensors and embedded edge compute IP.",
      inquiryBounty = "$500 intro bounty",
      vouchCount = 28,
      attestationSnippet = "Vouched by 3 people in your circle",
      intentRepliesCount = 7,
      targetNodeId = "node_elena"
    ),
    com.example.model.Dispatch(
      id = "disp_3",
      authorName = "David Chen",
      authorHandle = "@dchen.sound",
      authorAvatarColor = Color(0xFFA55EEA),
      authorFacet = "Acoustic Synthesist & Audio Eng",
      socialDistance = 2,
      distanceBadge = "2° Extended Network",
      timeAgo = "1h ago",
      contentType = com.example.model.DispatchType.AUDIO_MEMO,
      title = "Spatial Binaural Micro-Oscillation (Field Test)",
      content = "Recorded this 432Hz ambient binaural acoustic resonance in the Alps yesterday. The analog filtering produces a natural diaphragmatic calm loop.",
      audioDuration = "1:48 • Spatial Audio",
      vouchCount = 35,
      attestationSnippet = "Vouched by 2 people in your circle",
      intentRepliesCount = 4,
      targetNodeId = "node_david"
    ),
    com.example.model.Dispatch(
      id = "disp_4",
      authorName = "Marcus Vance",
      authorHandle = "@marcus.v",
      authorAvatarColor = Color(0xFF4B7BEC),
      authorFacet = "Product Partner @ Kinetic",
      socialDistance = 1,
      distanceBadge = "1° Direct Circle",
      timeAgo = "2h ago",
      contentType = com.example.model.DispatchType.DEEP_DIVE,
      title = "The Inverse Square Law of Social Signal",
      content = "Broadcasting to millions of anonymous users has failed human communication. High-signal discovery only works when filtered by social distance: 1 degree carries 100% trust, 2 degrees carries 50%, 3 degrees carries topical affinity. Anything beyond is ambient noise.",
      vouchCount = 68,
      attestationSnippet = "Direct Contact • 12 mutual vouches",
      intentRepliesCount = 19,
      targetNodeId = "node_marcus"
    ),
    com.example.model.Dispatch(
      id = "disp_5",
      authorName = "Sophia Sterling",
      authorHandle = "@sophia.climate",
      authorAvatarColor = Color(0xFF20BF6B),
      authorFacet = "Decentralized Energy Syndicate",
      socialDistance = 3,
      distanceBadge = "3° Interest Constellation",
      timeAgo = "4h ago",
      contentType = com.example.model.DispatchType.OPPORTUNITY,
      title = "Microgrid Solar Arbitrage Syndicate",
      content = "Allocating 4 slots for strategic angels in our decentralized microgrid battery storage project deployed in Southern Iberia. High dividend yields backed by physical infrastructure.",
      vouchCount = 51,
      attestationSnippet = "Surfaced via Clean Energy Interest Nebula",
      intentRepliesCount = 14,
      targetNodeId = "node_sophia"
    )
  )

  val sampleLatticeNodes: List<com.example.model.LatticeNode> = listOf(
    com.example.model.LatticeNode(
      id = "node_marcus",
      name = "Marcus Vance",
      handle = "@marcus.v",
      facet = "Product Strategist",
      orbitDistance = 1,
      clusterCategory = "Direct Contacts",
      avatarColor = Color(0xFF4B7BEC),
      activeDispatchesCount = 4,
      vouchesCount = 68,
      radialAngleDeg = 45f,
      normalizedRadius = 0.28f,
      recentTopic = "Social Signal Laws",
      isConnectedDirectly = true
    ),
    com.example.model.LatticeNode(
      id = "node_alex",
      name = "Alex Rivera",
      handle = "@alex.r",
      facet = "Full-Stack Engineer",
      orbitDistance = 1,
      clusterCategory = "Direct Contacts",
      avatarColor = Color(0xFF1E88E5),
      activeDispatchesCount = 2,
      vouchesCount = 45,
      radialAngleDeg = 160f,
      normalizedRadius = 0.32f,
      recentTopic = "Compose Multiplatform",
      isConnectedDirectly = true
    ),
    com.example.model.LatticeNode(
      id = "node_fatima",
      name = "Fatima Al-Mansoor",
      handle = "@fatima.arch",
      facet = "Distributed Systems Architect",
      orbitDistance = 2,
      clusterCategory = "Systems Engineering",
      avatarColor = Color(0xFF00E5B8),
      activeDispatchesCount = 6,
      vouchesCount = 42,
      radialAngleDeg = 20f,
      normalizedRadius = 0.62f,
      recentTopic = "PebbleSync Local-First DB"
    ),
    com.example.model.LatticeNode(
      id = "node_elena",
      name = "Elena Rostova",
      handle = "@elena.robotics",
      facet = "Autonomous Robotics Lead",
      orbitDistance = 2,
      clusterCategory = "Robotics & Hardware",
      avatarColor = Color(0xFFFA8231),
      activeDispatchesCount = 3,
      vouchesCount = 28,
      radialAngleDeg = 100f,
      normalizedRadius = 0.58f,
      recentTopic = "Neuromorphic Sensor IP"
    ),
    com.example.model.LatticeNode(
      id = "node_david",
      name = "David Chen",
      handle = "@dchen.sound",
      facet = "Acoustic Synthesist",
      orbitDistance = 2,
      clusterCategory = "Sound Design & Media",
      avatarColor = Color(0xFFA55EEA),
      activeDispatchesCount = 5,
      vouchesCount = 35,
      radialAngleDeg = 220f,
      normalizedRadius = 0.65f,
      recentTopic = "Binaural Field Recordings"
    ),
    com.example.model.LatticeNode(
      id = "node_sophia",
      name = "Sophia Sterling",
      handle = "@sophia.climate",
      facet = "Climate Capital Syndicate",
      orbitDistance = 3,
      clusterCategory = "Clean Energy & Climate",
      avatarColor = Color(0xFF20BF6B),
      activeDispatchesCount = 4,
      vouchesCount = 51,
      radialAngleDeg = 290f,
      normalizedRadius = 0.88f,
      recentTopic = "Microgrid Solar Arbitrage"
    ),
    com.example.model.LatticeNode(
      id = "node_tariq",
      name = "Dr. Tariq Hussain",
      handle = "@tariq.quant",
      facet = "Applied Cryptographer",
      orbitDistance = 3,
      clusterCategory = "Zero-Knowledge Proofs",
      avatarColor = Color(0xFF00C6FF),
      activeDispatchesCount = 3,
      vouchesCount = 74,
      radialAngleDeg = 330f,
      normalizedRadius = 0.85f,
      recentTopic = "SNARK Folding Schemes"
    )
  )

  val sampleConduitRequests: List<com.example.model.ConduitIntroRequest> = listOf(
    com.example.model.ConduitIntroRequest(
      id = "req_outgoing_1",
      targetName = "Fatima Al-Mansoor",
      targetHandle = "@fatima.arch",
      targetFacet = "Distributed Systems Architect",
      category = "Technical Collaboration",
      intentStatement = "Building a peer-to-peer raft mesh on Android and would love to exchange notes on PebbleSync log compaction.",
      timestamp = "Yesterday",
      status = com.example.model.ConduitStatus.PENDING_BRIDGE,
      isIncomingForBridge = false
    ),
    com.example.model.ConduitIntroRequest(
      id = "req_incoming_1",
      targetName = "Marcus Vance",
      targetHandle = "@marcus.v",
      targetFacet = "Product Partner",
      category = "Advisory & Product",
      intentStatement = "Looking for advice on enterprise trust metrics for our seed round deck.",
      timestamp = "2h ago",
      status = com.example.model.ConduitStatus.PENDING_BRIDGE,
      isIncomingForBridge = true,
      requesterName = "Elena Rostova",
      requesterHandle = "@elena.robotics",
      targetCandidateName = "Marcus Vance"
    )
  )

  val sampleSynchronyItems: List<com.example.model.SynchronyItem> = listOf(
    com.example.model.SynchronyItem(
      id = "sync_1",
      title = "Kernel-level Memory Safety & Raft Concurrency",
      type = com.example.model.SynchronyType.LIVE_ROUNDTABLE,
      participantCount = 48,
      snippet = "Fatima Al-Mansoor & 2 other 2° connections discussing edge consensus",
      socialDistanceContext = "3 people in your 2° network active"
    ),
    com.example.model.SynchronyItem(
      id = "sync_2",
      title = "Urgent: Neuromorphic Sensor Counsel in Europe",
      type = com.example.model.SynchronyType.ACTIVE_INQUIRY,
      participantCount = 7,
      snippet = "Elena Rostova • $500 intro reward offered",
      urgencyHoursLeft = 4,
      socialDistanceContext = "2° Hardware & Robotics cluster"
    )
  )

  val samplePrismProfile: com.example.model.PrismProfile = com.example.model.PrismProfile(
    id = "prism_me",
    name = "Muhammad",
    handle = "@muhammad.val",
    phoneStatus = "Cryptographically Sealed (Hidden)",
    totalVouchesReceived = 42,
    dailyVouchesRemaining = 5,
    facets = listOf(
      com.example.model.PrismFacet(
        name = "Architecture & Systems",
        headline = "Staff Mobile & Distributed Systems Architect",
        tags = listOf("Kotlin", "Local-First", "Zero-Knowledge", "Compose"),
        visibility = "Horizon (2° Trust Mesh)"
      ),
      com.example.model.PrismFacet(
        name = "Acoustic Craft",
        headline = "Analog Sound Designer & Synthesis Collector",
        tags = listOf("Binaural", "Eurorack", "Field Recording"),
        visibility = "Interest Constellation (3°)"
      ),
      com.example.model.PrismFacet(
        name = "Civic & Neighborhood",
        headline = "Clean Energy & Neighborhood Organizer",
        tags = listOf("Solar Microgrids", "Community Board"),
        visibility = "Local Mesh"
      )
    ),
    activeEndeavors = listOf(
      "Architecting Kick's trust-distance traversal engine",
      "Testing PebbleSync Raft consensus on mobile nodes",
      "Assembling an analog generative soundscape generator"
    ),
    avatarColor = Color(0xFF00E5B8)
  )

  val sampleGuilds: List<com.example.model.Guild> = listOf(
    com.example.model.Guild(
      id = "guild_dist",
      name = "Distributed Systems Guild",
      headline = "Consensus protocols, local-first databases, and peer-to-peer synchronization.",
      memberCount = "142 builders",
      trustRequirement = "Vouched by 2 members",
      activeVoiceUsers = 4,
      iconColor = Color(0xFF00E5B8),
      activeDiscussion = "Paxos vs Raft vs CRDTs in 2026",
      subChannels = listOf("The Commons 💬", "Dispatches 📜", "Live Voice Table 🎙️", "Paper Library 📚"),
      isMember = true
    ),
    com.example.model.Guild(
      id = "guild_sound",
      name = "Acoustic & Modular Collective",
      headline = "Analog hardware, binaural field recording, and generative psychoacoustics.",
      memberCount = "88 sound artists",
      trustRequirement = "Open to 2° of Members",
      activeVoiceUsers = 2,
      iconColor = Color(0xFFA55EEA),
      activeDiscussion = "432Hz spatial resonance experiments",
      subChannels = listOf("General Synthesis 💬", "Patches & Schematics 🎛️", "Live Jam Table 🎧"),
      isMember = false
    ),
    com.example.model.Guild(
      id = "guild_climate",
      name = "Clean Energy Syndicate",
      headline = "Decentralized microgrids, solar arbitrage, and battery storage co-ops.",
      memberCount = "210 operators",
      trustRequirement = "Vouched by 3 members",
      activeVoiceUsers = 0,
      iconColor = Color(0xFF20BF6B),
      activeDiscussion = "European microgrid capacity filings",
      subChannels = listOf("Syndicate Hub 💬", "Deal Flow 💼", "Regulatory Research 📄"),
      isMember = false
    )
  )

  val sampleScoutQueries: List<com.example.model.ScoutQuery> = listOf(
    com.example.model.ScoutQuery(
      id = "sq_1",
      prompt = "Find people in my 2° network working on distributed databases",
      responseSummary = "Identified Fatima Al-Mansoor (@fatima.arch) who recently open-sourced PebbleSync, and 2 other engineers in your 2° circle. Their relationship paths are private.",
      matchedCount = 3,
      suggestedAction = "Prepare Conduit Introduction to Fatima"
    ),
    com.example.model.ScoutQuery(
      id = "sq_2",
      prompt = "Summarize trending discussions around my network",
      responseSummary = "Active consensus is forming around 'Local-First SQLite Architecture' (12 vouched dispatches) and 'Neuromorphic Sensors' (active bounty by Elena Rostova).",
      matchedCount = 14,
      suggestedAction = "Explore The Horizon Dispatches"
    ),
    com.example.model.ScoutQuery(
      id = "sq_3",
      prompt = "Show me active communities related to sound synthesis",
      responseSummary = "The 'Acoustic & Modular Collective' is active with 2 members in the live voice drop-in table right now. You are eligible to join via 2° trust.",
      matchedCount = 1,
      suggestedAction = "Enter Acoustic & Modular Guild"
    )
  )

  val sampleHorizonVideos: List<com.example.model.HorizonVideo> = listOf(
    com.example.model.HorizonVideo(
      id = "vid_1",
      creatorName = "Elena Rostova",
      creatorHandle = "@elena_zk",
      creatorDistance = "2° Extended Horizon",
      creatorFacet = "Zero-Knowledge Cryptography",
      creatorAvatarColor = Color(0xFF00E5B8),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&q=80",
      caption = "Demonstrating recursive STARK verification in 12ms on client hardware. Zero centralized sequencer required. ⚡️",
      tags = listOf("#ZKRollups", "#ZeroKnowledge", "#KickHorizon", "#Privacy"),
      audioTrackTitle = "Elena's Research Memo • Audio Track #01",
      vouchCount = 342,
      isVouched = true,
      commentCount = 28,
      shareCount = 89,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_1", "Marcus Chen", "@marcus_robotics", "1° Direct", "The verification speed on ARM is phenomenal. Can we run this on drone flight controllers?", "2h ago", 14),
        com.example.model.HorizonVideoComment("c_2", "Fatima Al-Mansoor", "@fatima_arch", "2° Horizon", "Incredible work Elena. Sent a Conduit inquiry regarding distributed ledger synchronization.", "1h ago", 8)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_2",
      creatorName = "Marcus Chen",
      creatorHandle = "@marcus_robotics",
      creatorDistance = "1° Direct Circle",
      creatorFacet = "Autonomous Quadruped Kinematics",
      creatorAvatarColor = Color(0xFFFF9F1A),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=800&q=80",
      caption = "Field test of our dynamic torque control loop on uneven shale terrain. Zero telemetry leakage back to base.",
      tags = listOf("#Robotics", "#Hardware", "#Embedded", "#DirectTrust"),
      audioTrackTitle = "Lab Actuators Resonance • Live Audio",
      vouchCount = 819,
      isVouched = false,
      commentCount = 64,
      shareCount = 156,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_3", "Devon Miller", "@devon_aero", "2° Horizon", "Look at that recovery angle. What frequency is your IMU Kalman filter running at?", "3h ago", 21)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_3",
      creatorName = "Dr. Soren Lindqvist",
      creatorHandle = "@soren_neuro",
      creatorDistance = "2° Extended Horizon",
      creatorFacet = "Brain-Computer Interface Systems",
      creatorAvatarColor = Color(0xFF4D96FF),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1507413245164-6160d8298b31?w=800&q=80",
      caption = "Real-time 64-channel EEG spectral decomposition with non-invasive electrode array. Direct neural intent to syntax.",
      tags = listOf("#Neurotech", "#BCI", "#SignalProcessing", "#DeepTech"),
      audioTrackTitle = "Neural Alpha Rhythm Stream #12",
      vouchCount = 512,
      isVouched = false,
      commentCount = 43,
      shareCount = 98,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_4", "Talia Morales", "@talia_spatial", "1° Direct", "Is the latency low enough for AR headset gaze confirmation?", "4h ago", 19)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_4",
      creatorName = "Aria Tanaka",
      creatorHandle = "@aria_synth",
      creatorDistance = "3° Interest Mesh",
      creatorFacet = "Modular Synthesizer & Generative Audio",
      creatorAvatarColor = Color(0xFFA55EEA),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80",
      caption = "Eurorack generative patch: cross-modulating twin analog oscillators through low-pass gates. Synchrony live session.",
      tags = listOf("#ModularSynth", "#GenerativeAudio", "#SoundDesign", "#KickSound"),
      audioTrackTitle = "Generative Patch in F# Minor • Aria Tanaka",
      vouchCount = 1204,
      isVouched = true,
      commentCount = 112,
      shareCount = 340,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_5", "Kevin Vance", "@kevin_audio", "1° Direct", "Those Buchla harmonics are pure silk. Joining your Synchrony audio table!", "5h ago", 33)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_5",
      creatorName = "Devon Miller",
      creatorHandle = "@devon_aero",
      creatorDistance = "2° Extended Horizon",
      creatorFacet = "Orbital Propulsion & Thruster Dynamics",
      creatorAvatarColor = Color(0xFFFF5252),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1517976487502-d210c0d800f0?w=800&q=80",
      caption = "Cold gas thruster test fire in vacuum chamber. 3D-printed Inconel manifold holding pressure at 250 bar.",
      tags = listOf("#Aerospace", "#Propulsion", "#SpaceTech", "#HardwareSyndicate"),
      audioTrackTitle = "Cryogenic Venting Acoustics",
      vouchCount = 684,
      isVouched = false,
      commentCount = 51,
      shareCount = 172,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_6", "Marcus Chen", "@marcus_robotics", "1° Direct", "Clean plume geometry. What's the specific impulse calculation?", "6h ago", 15)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_6",
      creatorName = "Mira Al-Mansoor",
      creatorHandle = "@mira_grid",
      creatorDistance = "2° Extended Horizon",
      creatorFacet = "Distributed Microgrid Decentralization",
      creatorAvatarColor = Color(0xFF20BF6B),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1473341304170-971dccb5ac1e?w=800&q=80",
      caption = "Sub-second load balancing between solar battery storage nodes across 400 residential clusters. Zero curtailment.",
      tags = listOf("#CleanEnergy", "#DistributedGrid", "#ClimateTech", "#Decentralized"),
      audioTrackTitle = "Grid Frequency Humming • Mira Al-Mansoor",
      vouchCount = 429,
      isVouched = false,
      commentCount = 39,
      shareCount = 83,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_7", "Fatima Al-Mansoor", "@fatima_arch", "1° Direct", "Proud of this milestone! Peer-to-peer energy routing actually working.", "7h ago", 26)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_7",
      creatorName = "Kai Vance",
      creatorHandle = "@kai_quantum",
      creatorDistance = "3° Interest Mesh",
      creatorFacet = "Superconducting Qubit Instrumentation",
      creatorAvatarColor = Color(0xFF00C6FF),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackSeeTheWorld.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800&q=80",
      caption = "Dilution refrigerator cooldown cycle reaching 14 millikelvin. Coherence time benchmark surpassing 150 microseconds.",
      tags = listOf("#QuantumComputing", "#Superconductivity", "#Physics", "#DeepTech"),
      audioTrackTitle = "Helium Compressor Cycle Beat",
      vouchCount = 954,
      isVouched = false,
      commentCount = 87,
      shareCount = 245,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_8", "Elena Rostova", "@elena_zk", "2° Horizon", "150 microseconds on transmon architecture is huge for error correction thresholds.", "8h ago", 45)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_8",
      creatorName = "Talia Morales",
      creatorHandle = "@talia_spatial",
      creatorDistance = "1° Direct Circle",
      creatorFacet = "Spatial Computing & Optical Waveguides",
      creatorAvatarColor = Color(0xFFFF7675),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1593508512255-86ab42a8e620?w=800&q=80",
      caption = "Direct retinal projection prototype. Testing diffractive waveguide grating efficiency in outdoor daylight ambient.",
      tags = listOf("#SpatialComputing", "#Optics", "#HardwareDesign", "#DirectTrust"),
      audioTrackTitle = "Photonics Laboratory Ambient #08",
      vouchCount = 742,
      isVouched = true,
      commentCount = 56,
      shareCount = 198,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_9", "Dr. Soren Lindqvist", "@soren_neuro", "2° Horizon", "The eye tracking alignment speed looks instantaneous here.", "9h ago", 12)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_9",
      creatorName = "Zane Thorne",
      creatorHandle = "@zane_kernel",
      creatorDistance = "2° Extended Horizon",
      creatorFacet = "Asynchronous Rust OS Kernel Architecture",
      creatorAvatarColor = Color(0xFFFD79A8),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800&q=80",
      caption = "Lock-free ring buffer IPC demonstration reaching 42M context-switches/sec. Memory isolation guarantees validated.",
      tags = listOf("#RustLang", "#KernelDev", "#SystemsEngineering", "#LowLevel"),
      audioTrackTitle = "Typing on 40% Ortholinear Keyboard",
      vouchCount = 610,
      isVouched = false,
      commentCount = 72,
      shareCount = 165,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_10", "Elena Rostova", "@elena_zk", "2° Horizon", "Are you publishing the cargo crate to Kick artifacts repo?", "10h ago", 17)
      )
    ),
    com.example.model.HorizonVideo(
      id = "vid_10",
      creatorName = "Lydia Brooks",
      creatorHandle = "@lydia_bio",
      creatorDistance = "3° Interest Mesh",
      creatorFacet = "Synthetic Biology & Enzyme Engineering",
      creatorAvatarColor = Color(0xFF6C5CE7),
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
      fallbackThumbnailUrl = "https://images.unsplash.com/photo-1532187863486-abf9dbad1b69?w=800&q=80",
      caption = "High-throughput droplet microfluidics sorting 10,000 enzyme variants per minute for PET plastic depolymerization.",
      tags = listOf("#SyntheticBio", "#Enzymes", "#Bioengineering", "#Microfluidics"),
      audioTrackTitle = "Droplet Sorter Laser Pacing",
      vouchCount = 885,
      isVouched = false,
      commentCount = 81,
      shareCount = 230,
      comments = listOf(
        com.example.model.HorizonVideoComment("c_11", "Devon Miller", "@devon_aero", "2° Horizon", "10k per minute! That completely scales the screening bottleneck.", "12h ago", 29)
      )
    )
  )
}

