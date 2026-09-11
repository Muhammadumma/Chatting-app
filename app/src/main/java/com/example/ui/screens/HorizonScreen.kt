@file:OptIn(
  androidx.compose.material3.ExperimentalMaterial3Api::class,
  androidx.media3.common.util.UnstableApi::class
)

package com.example.ui.screens

import android.view.ViewGroup
import kotlin.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.model.HorizonVideo
import com.example.model.HorizonVideoComment
import com.example.ui.theme.KickAmber
import com.example.ui.theme.KickBlue
import com.example.ui.theme.KickBorder
import com.example.ui.theme.KickCard
import com.example.ui.theme.KickCyan
import com.example.ui.theme.KickDeep
import com.example.ui.theme.KickObsidian
import com.example.ui.theme.KickTextMuted
import com.example.ui.theme.KickTextPrimary
import com.example.ui.theme.KickTextSecondary
import com.example.ui.theme.KickViolet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class HorizonTabFilter {
  ALL,
  FOLLOWING,
  EXTENDED
}

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HorizonScreen(
  videos: List<HorizonVideo>,
  dailyVouchesRemaining: Int,
  onVouchVideo: (String) -> Unit,
  onAddComment: (videoId: String, comment: String) -> Unit,
  onRequestConduitIntro: (HorizonVideo) -> Unit,
  onOpenSynchrony: () -> Unit,
  onOpenScout: () -> Unit,
  onPublishVideo: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(HorizonTabFilter.ALL) }
  var isGlobalMuted by remember { mutableStateOf(false) }

  val filteredVideos = remember(selectedTab, videos) {
    when (selectedTab) {
      HorizonTabFilter.ALL -> videos
      HorizonTabFilter.FOLLOWING -> videos.filter { it.creatorDistance.contains("1°") }
      HorizonTabFilter.EXTENDED -> videos.filter { !it.creatorDistance.contains("1°") }
    }
  }

  val pagerState = rememberPagerState(pageCount = { filteredVideos.size })
  var activeCommentVideo by remember { mutableStateOf<HorizonVideo?>(null) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black)
      .testTag("horizon_tiktok_screen")
  ) {
    if (filteredVideos.isEmpty()) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No videos found for this orbit filter.", color = KickTextSecondary)
      }
    } else {
      VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        key = { page -> filteredVideos.getOrNull(page)?.id ?: page }
      ) { page ->
        val video = filteredVideos[page]
        val isCurrentPage = pagerState.currentPage == page

        HorizonVideoPageItem(
          video = video,
          isCurrentPage = isCurrentPage,
          isMuted = isGlobalMuted,
          onToggleMute = { isGlobalMuted = !isGlobalMuted },
          onVouch = { onVouchVideo(video.id) },
          onOpenComments = { activeCommentVideo = video },
          onRequestConduit = { onRequestConduitIntro(video) },
          onOpenSynchrony = onOpenSynchrony,
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Top Navigation & Filter Bar Overlay (TikTok style with Kick Trust-Distance tabs)
    HorizonTopBar(
      selectedTab = selectedTab,
      onSelectTab = { selectedTab = it },
      dailyVouches = dailyVouchesRemaining,
      onOpenScout = onOpenScout,
      onPublish = onPublishVideo,
      modifier = Modifier
        .align(Alignment.TopCenter)
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    )

    // Comments Modal Bottom Sheet
    if (activeCommentVideo != null) {
      VideoCommentsBottomSheet(
        video = activeCommentVideo!!,
        onDismiss = { activeCommentVideo = null },
        onPostComment = { text ->
          onAddComment(activeCommentVideo!!.id, text)
          // Update active modal view with local comment
          activeCommentVideo = videos.find { it.id == activeCommentVideo!!.id }
        }
      )
    }
  }
}

@OptIn(UnstableApi::class)
@Composable
fun HorizonVideoPageItem(
  video: HorizonVideo,
  isCurrentPage: Boolean,
  isMuted: Boolean,
  onToggleMute: () -> Unit,
  onVouch: () -> Unit,
  onOpenComments: () -> Unit,
  onRequestConduit: () -> Unit,
  onOpenSynchrony: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isPlaying by remember { mutableStateOf(true) }
  var isFollowed by remember { mutableStateOf(false) }
  var showHeartAnimation by remember { mutableStateOf(false) }
  var showPlayPauseIcon by remember { mutableStateOf(false) }
  var playbackProgress by remember { mutableFloatStateOf(0f) }
  val coroutineScope = rememberCoroutineScope()

  // ExoPlayer instance lifecycle tied to this composable item
  val exoPlayer = remember(context) {
    ExoPlayer.Builder(context).build().apply {
      repeatMode = Player.REPEAT_MODE_ALL
    }
  }

  // Set video source
  LaunchedEffect(video.videoUrl) {
    val mediaItem = MediaItem.fromUri(video.videoUrl)
    exoPlayer.setMediaItem(mediaItem)
    exoPlayer.prepare()
  }

  // Handle play/pause when user scrolls to/from this page
  LaunchedEffect(isCurrentPage, isPlaying) {
    if (isCurrentPage && isPlaying) {
      exoPlayer.play()
    } else {
      exoPlayer.pause()
    }
  }

  // Volume control
  LaunchedEffect(isMuted) {
    exoPlayer.volume = if (isMuted) 0f else 1f
  }

  // Poll playback position for the bottom progress scrubber
  LaunchedEffect(isCurrentPage) {
    if (isCurrentPage) {
      while (true) {
        val duration = exoPlayer.duration
        val position = exoPlayer.currentPosition
        if (duration > 0) {
          playbackProgress = (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
        }
        delay(250)
      }
    }
  }

  DisposableEffect(exoPlayer) {
    onDispose {
      exoPlayer.release()
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(
          onDoubleTap = {
            if (!video.isVouched) {
              onVouch()
            }
            showHeartAnimation = true
            coroutineScope.launch {
              delay(800)
              showHeartAnimation = false
            }
          },
          onTap = {
            isPlaying = !isPlaying
            showPlayPauseIcon = true
            coroutineScope.launch {
              delay(600)
              showPlayPauseIcon = false
            }
          }
        )
      }
  ) {
    // 1. Video Player Surface
    AndroidView(
      factory = { ctx ->
        PlayerView(ctx).apply {
          player = exoPlayer
          useController = false
          layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
          resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }
      },
      modifier = Modifier.fillMaxSize()
    )

    // Fallback Poster thumbnail with smooth fade if buffering
    AsyncImage(
      model = video.fallbackThumbnailUrl,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .then(if (isCurrentPage) Modifier.clip(RoundedCornerShape(0.dp)) else Modifier)
    )

    // Dark gradient overlays for readable text & controls
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.verticalGradient(
            listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)
          )
        )
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .align(Alignment.BottomCenter)
        .background(
          Brush.verticalGradient(
            listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f), Color.Black.copy(alpha = 0.95f))
          )
        )
    )

    // 2. Center Pause / Play indicator flash
    AnimatedVisibility(
      visible = showPlayPauseIcon,
      enter = scaleIn(tween(150)) + fadeIn(),
      exit = scaleOut(tween(250)) + fadeOut(),
      modifier = Modifier.align(Alignment.Center)
    ) {
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.55f))
          .border(1.5.dp, KickCyan.copy(alpha = 0.8f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
          contentDescription = null,
          tint = KickCyan,
          modifier = Modifier.size(40.dp)
        )
      }
    }

    // Double-tap Vouch Burst Animation (Glowing Cyan Shield/Heart)
    AnimatedVisibility(
      visible = showHeartAnimation,
      enter = scaleIn(tween(200, easing = FastOutSlowInEasing)) + fadeIn(),
      exit = scaleOut(tween(300)) + fadeOut(),
      modifier = Modifier.align(Alignment.Center)
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(100.dp)
            .shadow(elevation = 24.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(KickCyan.copy(alpha = 0.35f))
            .border(2.dp, KickCyan, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = "Vouched",
            tint = KickCyan,
            modifier = Modifier.size(60.dp)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "+1 REPUTATION VOUCH",
          fontSize = 12.sp,
          fontWeight = FontWeight.Black,
          color = KickCyan,
          letterSpacing = 1.sp
        )
      }
    }

    // 3. Right-Side Action Controls Stack (TikTok layout)
    RightActionControls(
      video = video,
      isFollowed = isFollowed,
      onToggleFollow = { isFollowed = !isFollowed },
      onVouch = onVouch,
      onRequestConduit = onRequestConduit,
      onOpenComments = onOpenComments,
      onOpenSynchrony = onOpenSynchrony,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .navigationBarsPadding()
        .padding(end = 12.dp, bottom = 64.dp)
    )

    // 4. Bottom-Left Information Overlay (Creator, Distance, Caption, Audio Track)
    BottomInfoOverlay(
      video = video,
      modifier = Modifier
        .align(Alignment.BottomStart)
        .navigationBarsPadding()
        .padding(start = 16.dp, end = 90.dp, bottom = 48.dp)
    )

    // 5. Mute/Unmute quick toggle button (top-right below bar)
    IconButton(
      onClick = onToggleMute,
      modifier = Modifier
        .align(Alignment.TopEnd)
        .statusBarsPadding()
        .padding(top = 56.dp, end = 16.dp)
        .size(38.dp)
        .clip(CircleShape)
        .background(Color.Black.copy(alpha = 0.5f))
        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
    ) {
      Icon(
        imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
        contentDescription = if (isMuted) "Unmute" else "Mute",
        tint = Color.White,
        modifier = Modifier.size(18.dp)
      )
    }

    // 6. Bottom Playback Scrubber Bar
    LinearProgressIndicator(
      progress = { playbackProgress },
      modifier = Modifier
        .fillMaxWidth()
        .height(3.dp)
        .align(Alignment.BottomCenter),
      color = KickCyan,
      trackColor = Color.White.copy(alpha = 0.25f)
    )
  }
}

@Composable
fun HorizonTopBar(
  selectedTab: HorizonTabFilter,
  onSelectTab: (HorizonTabFilter) -> Unit,
  dailyVouches: Int,
  onOpenScout: () -> Unit,
  onPublish: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left: Daily Vouches remaining badge
    Surface(
      modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .border(1.dp, KickCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
      color = Color.Black.copy(alpha = 0.5f)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = null,
          tint = KickCyan,
          modifier = Modifier.size(12.dp)
        )
        Text(
          text = "$dailyVouches Vouches",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = KickCyan
        )
      }
    }

    // Center: Horizon Feed Filter Tabs
    Row(
      modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(Color.Black.copy(alpha = 0.6f))
        .border(1.dp, KickBorder, RoundedCornerShape(20.dp))
        .padding(3.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      HorizonTabFilter.entries.forEach { tab ->
        val isSelected = selectedTab == tab
        val label = when (tab) {
          HorizonTabFilter.ALL -> "Queue (10)"
          HorizonTabFilter.FOLLOWING -> "1° Circle"
          HorizonTabFilter.EXTENDED -> "2°-3° Horizon"
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) KickCyan.copy(alpha = 0.25f) else Color.Transparent)
            .clickable { onSelectTab(tab) }
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = if (isSelected) KickCyan else KickTextSecondary
          )
        }
      }
    }

    // Right: Scout Navigator & Publish buttons
    Row(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Scout Navigator
      IconButton(
        onClick = onOpenScout,
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.5f))
          .border(1.dp, KickBorder, CircleShape)
          .testTag("horizon_scout_btn")
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = "Scout Navigator",
          tint = KickAmber,
          modifier = Modifier.size(16.dp)
        )
      }

      // Publish / Dispatch Video
      IconButton(
        onClick = onPublish,
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(KickCyan)
          .testTag("horizon_publish_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Publish Video",
          tint = Color(0xFF080D12),
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun RightActionControls(
  video: HorizonVideo,
  isFollowed: Boolean,
  onToggleFollow: () -> Unit,
  onVouch: () -> Unit,
  onRequestConduit: () -> Unit,
  onOpenComments: () -> Unit,
  onOpenSynchrony: () -> Unit,
  modifier: Modifier = Modifier
) {
  // Infinite rotation transition for the audio vinyl disc
  val infiniteTransition = rememberInfiniteTransition(label = "disc_rotation")
  val discAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "disc_angle"
  )

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Creator Avatar + Follow (+) Badge
    Box(
      modifier = Modifier.padding(bottom = 6.dp),
      contentAlignment = Alignment.BottomCenter
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(video.creatorAvatarColor)
          .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = video.creatorName.take(1),
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF080D12)
        )
      }

      // Follow toggle button
      Box(
        modifier = Modifier
          .size(20.dp)
          .clip(CircleShape)
          .background(if (isFollowed) KickCyan else Color(0xFFFF5252))
          .border(1.5.dp, Color.Black, CircleShape)
          .clickable { onToggleFollow() },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isFollowed) Icons.Default.Check else Icons.Default.Add,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(12.dp)
        )
      }
    }

    // 2. The Vouch Button (Heart / Shield)
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.clickable { onVouch() }
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(
            if (video.isVouched) KickCyan.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.5f)
          )
          .border(
            1.5.dp,
            if (video.isVouched) KickCyan else Color.White.copy(alpha = 0.3f),
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = "Vouch",
          tint = if (video.isVouched) KickCyan else Color.White,
          modifier = Modifier.size(24.dp)
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "${video.vouchCount}",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = if (video.isVouched) KickCyan else Color.White
      )
    }

    // 3. Double-Blind Conduit Intro Button
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.clickable { onRequestConduit() }
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.5f))
          .border(1.5.dp, KickAmber.copy(alpha = 0.8f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.VpnKey,
          contentDescription = "Request Conduit Intro",
          tint = KickAmber,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Conduit",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = KickAmber
      )
    }

    // 4. Dispatches / Comments Button
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.clickable { onOpenComments() }
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.5f))
          .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Chat,
          contentDescription = "Comments",
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "${video.commentCount}",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
    }

    // 5. Share / Attestation Protocol
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.clickable {
        // Can trigger direct share
      }
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.5f))
          .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Share,
          contentDescription = "Share",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "${video.shareCount}",
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White
      )
    }

    // 6. Spinning Audio Vinyl Disc
    Box(
      modifier = Modifier
        .size(44.dp)
        .rotate(discAngle)
        .clip(CircleShape)
        .background(Color(0xFF121820))
        .border(2.dp, Color(0xFF2A3440), CircleShape)
        .clickable { onOpenSynchrony() },
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .size(16.dp)
          .clip(CircleShape)
          .background(KickCyan)
      )
    }
  }
}

@Composable
fun BottomInfoOverlay(
  video: HorizonVideo,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    // Creator name & Trust distance pill
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = video.creatorName,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )

      Text(
        text = video.creatorHandle,
        fontSize = 13.sp,
        color = KickCyan
      )

      // Orbit Distance badge (1° Direct, 2° Extended, 3° Interest)
      Surface(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .border(
            1.dp,
            if (video.creatorDistance.contains("1°")) KickCyan else KickAmber,
            RoundedCornerShape(8.dp)
          ),
        color = Color.Black.copy(alpha = 0.6f)
      ) {
        Text(
          text = video.creatorDistance,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = if (video.creatorDistance.contains("1°")) KickCyan else KickAmber,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }

    // Caption Description
    Text(
      text = video.caption,
      fontSize = 13.sp,
      color = Color.White.copy(alpha = 0.95f),
      maxLines = 3,
      overflow = TextOverflow.Ellipsis,
      lineHeight = 18.sp
    )

    // Tag pills
    Row(
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      video.tags.take(3).forEach { tag ->
        Text(
          text = tag,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = KickCyan.copy(alpha = 0.9f)
        )
      }
    }

    // Audio Track / Synchrony sound ticker
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Icon(
        imageVector = Icons.Default.MusicNote,
        contentDescription = null,
        tint = Color.White.copy(alpha = 0.8f),
        modifier = Modifier.size(14.dp)
      )
      Text(
        text = video.audioTrackTitle,
        fontSize = 12.sp,
        color = Color.White.copy(alpha = 0.85f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    // Cryptographic attestation seal
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Security,
        contentDescription = null,
        tint = KickCyan.copy(alpha = 0.7f),
        modifier = Modifier.size(12.dp)
      )
      Text(
        text = video.attestationProof,
        fontSize = 10.sp,
        color = KickCyan.copy(alpha = 0.8f)
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCommentsBottomSheet(
  video: HorizonVideo,
  onDismiss: () -> Unit,
  onPostComment: (String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var newCommentText by remember { mutableStateOf("") }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = KickCard,
    contentColor = KickTextPrimary
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .imePadding()
        .padding(horizontal = 20.dp)
        .padding(bottom = 24.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "DISPATCHES & COMMENTS (${video.commentCount})",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = KickCyan
          )
          Text(
            text = "Peer verified nodes • ${video.creatorHandle}",
            fontSize = 11.sp,
            color = KickTextMuted
          )
        }

        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = KickTextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Comments List
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        if (video.comments.isEmpty()) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("No comments yet. Be the first to add a dispatch response!", color = KickTextMuted, fontSize = 12.sp)
            }
          }
        } else {
          items(video.comments) { comment ->
            CommentRowItem(comment = comment)
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Comment input field
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = newCommentText,
          onValueChange = { newCommentText = it },
          modifier = Modifier
            .weight(1f)
            .testTag("video_comment_input"),
          placeholder = { Text("Post a response to ${video.creatorHandle}...", color = KickTextMuted, fontSize = 12.sp) },
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
          keyboardActions = KeyboardActions(onSend = {
            if (newCommentText.isNotBlank()) {
              onPostComment(newCommentText)
              newCommentText = ""
            }
          }),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KickCyan,
            unfocusedBorderColor = KickBorder,
            focusedTextColor = KickTextPrimary,
            unfocusedTextColor = KickTextPrimary,
            focusedContainerColor = KickObsidian,
            unfocusedContainerColor = KickObsidian
          ),
          shape = RoundedCornerShape(14.dp)
        )

        IconButton(
          onClick = {
            if (newCommentText.isNotBlank()) {
              onPostComment(newCommentText)
              newCommentText = ""
            }
          },
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (newCommentText.isNotBlank()) KickCyan else KickBorder),
          enabled = newCommentText.isNotBlank()
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send",
            tint = if (newCommentText.isNotBlank()) Color(0xFF080D12) else KickTextMuted,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
fun CommentRowItem(comment: HorizonVideoComment) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalAlignment = Alignment.Top
  ) {
    // Author initial
    Box(
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .background(KickCyan.copy(alpha = 0.2f))
        .border(1.dp, KickCyan.copy(alpha = 0.4f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = comment.authorName.take(1),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = KickCyan
      )
    }

    Column(modifier = Modifier.weight(1f)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = comment.authorName,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = KickTextPrimary
        )

        Text(
          text = comment.authorDistance,
          fontSize = 9.sp,
          fontWeight = FontWeight.SemiBold,
          color = KickCyan
        )

        Text(
          text = "• ${comment.timeAgo}",
          fontSize = 10.sp,
          color = KickTextMuted
        )
      }

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = comment.text,
        fontSize = 12.sp,
        color = KickTextSecondary,
        lineHeight = 16.sp
      )
    }

    // Comment vouch indicator
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Shield,
        contentDescription = null,
        tint = KickTextMuted,
        modifier = Modifier.size(12.dp)
      )
      Text(
        text = "${comment.vouchCount}",
        fontSize = 10.sp,
        color = KickTextMuted
      )
    }
  }
}
