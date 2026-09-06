package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StatusStory
import com.example.model.User
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppStoryBorder

@Composable
fun StatusRow(
  stories: List<StatusStory>,
  onStoryClick: (StatusStory) -> Unit,
  onMyStatusClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .testTag("status_updates_row"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // "My status" with add badge
    item {
      MyStatusItem(onClick = onMyStatusClick)
    }

    // Contact Status items
    items(stories, key = { it.id }) { story ->
      ContactStatusItem(
        story = story,
        onClick = { onStoryClick(story) }
      )
    }
  }
}

@Composable
private fun MyStatusItem(onClick: () -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(68.dp)
      .clip(CircleShape)
      .clickable(onClick = onClick)
      .testTag("my_status_item")
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.size(60.dp)
    ) {
      // User Avatar Circle
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(CircleShape)
          .background(Color(0xFF2E3339)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "YOU",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      }

      // Plus badge
      Box(
        modifier = Modifier
          .size(22.dp)
          .align(Alignment.BottomEnd)
          .offset(x = (-2).dp, y = (-2).dp)
          .clip(CircleShape)
          .background(WhatsAppGreen)
          .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add status",
          tint = Color.White,
          modifier = Modifier.size(14.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "My status",
      style = MaterialTheme.typography.bodySmall.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      ),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.Center
    )
  }
}

@Composable
private fun ContactStatusItem(
  story: StatusStory,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(68.dp)
      .clip(CircleShape)
      .clickable(onClick = onClick)
      .testTag("status_item_${story.id}")
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.size(60.dp)
    ) {
      // Segmented story ring
      val ringColor = if (story.isViewed) Color(0xFFB0BEC5) else WhatsAppStoryBorder
      StoryRing(
        color = ringColor,
        segments = if (story.id == "story_alex") 3 else 1
      )

      // Inner profile avatar
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(story.contact.avatarColor),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = story.contact.initials,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = story.contact.name.split(" ").firstOrNull() ?: story.contact.name,
      style = MaterialTheme.typography.bodySmall.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
      ),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.Center
    )
  }
}

@Composable
fun StoryRing(
  color: Color,
  segments: Int = 1,
  modifier: Modifier = Modifier
) {
  Canvas(modifier = modifier.size(58.dp)) {
    val strokeWidth = 2.4.dp.toPx()
    val padding = strokeWidth / 2f
    val arcSize = Size(size.width - padding * 2, size.height - padding * 2)
    val topLeft = Offset(padding, padding)

    if (segments <= 1) {
      drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
      )
    } else {
      val gapAngle = 8f
      val sweepPerSegment = (360f - (segments * gapAngle)) / segments
      for (i in 0 until segments) {
        val startAngle = -90f + i * (sweepPerSegment + gapAngle)
        drawArc(
          color = color,
          startAngle = startAngle,
          sweepAngle = sweepPerSegment,
          useCenter = false,
          topLeft = topLeft,
          size = arcSize,
          style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
      }
    }
  }
}
