package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Channel
import com.example.model.StatusStory
import com.example.ui.components.StoryRing
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenContainer
import com.example.ui.theme.WhatsAppGreenDeep
import com.example.ui.theme.WhatsAppStoryBorder
import com.example.ui.theme.WhatsAppTextPrimary
import com.example.ui.theme.WhatsAppTextSecondary

@Composable
fun UpdatesScreen(
  stories: List<StatusStory>,
  channels: List<Channel>,
  onStoryClick: (StatusStory) -> Unit,
  onCameraClick: () -> Unit,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val followStates = remember {
    mutableStateMapOf<String, Boolean>().apply {
      channels.forEach { put(it.id, it.isFollowing) }
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .testTag("updates_screen")
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top App Bar
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Updates",
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          Spacer(modifier = Modifier.weight(1f))

          IconButton(onClick = onCameraClick) {
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = "Camera",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(23.dp)
            )
          }

          IconButton(onClick = onSearchClick) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }

          IconButton(onClick = { /* Menu */ }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "More",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(24.dp)
            )
          }
        }
      }

      // Scrollable Content
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        // Status Section Header
        item {
          Text(
            text = "Status",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 12.dp)
          )
        }

        // "My status" Row
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = onCameraClick)
              .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier.size(54.dp),
              contentAlignment = Alignment.Center
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF2E3339)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "YOU",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }

              Box(
                modifier = Modifier
                  .size(20.dp)
                  .align(Alignment.BottomEnd)
                  .clip(CircleShape)
                  .background(WhatsAppGreen)
                  .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Add status",
                  tint = Color.White,
                  modifier = Modifier.size(13.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = "My status",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 16.sp
                )
              )
              Text(
                text = "Tap to add status update",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = WhatsAppTextSecondary,
                  fontSize = 13.sp
                )
              )
            }
          }
        }

        // Recent Updates Title
        item {
          Text(
            text = "Recent updates",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = WhatsAppTextSecondary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 6.dp)
          )
        }

        // Contact Stories List
        items(stories.filter { !it.isViewed }, key = { it.id }) { story ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onStoryClick(story) }
              .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier.size(54.dp),
              contentAlignment = Alignment.Center
            ) {
              StoryRing(color = WhatsAppStoryBorder, segments = 2)
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(story.contact.avatarColor),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = story.contact.initials,
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
              }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = story.contact.name,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 16.sp
                )
              )
              Text(
                text = story.timestamp,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = WhatsAppTextSecondary,
                  fontSize = 13.sp
                )
              )
            }
          }
        }

        // Viewed Updates Section
        val viewedStories = stories.filter { it.isViewed }
        if (viewedStories.isNotEmpty()) {
          item {
            Text(
              text = "Viewed updates",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = WhatsAppTextSecondary,
              modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 6.dp)
            )
          }

          items(viewedStories, key = { it.id }) { story ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onStoryClick(story) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
              ) {
                StoryRing(color = Color(0xFFB0BEC5), segments = 1)
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(story.contact.avatarColor),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = story.contact.initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                }
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column {
                Text(
                  text = story.contact.name,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                  )
                )
                Text(
                  text = story.timestamp,
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = WhatsAppTextSecondary,
                    fontSize = 13.sp
                  )
                )
              }
            }
          }
        }

        // Channels Section Divider
        item {
          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(thickness = 0.8.dp, color = Color(0x18000000))
          Spacer(modifier = Modifier.height(16.dp))
        }

        // Channels Header
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Channels",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
              text = "Explore",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              color = WhatsAppGreen
            )
          }
          Text(
            text = "Stay updated on topics you care about. Find channels to follow below.",
            fontSize = 13.sp,
            color = WhatsAppTextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        // Channels Directory List
        items(channels, key = { it.id }) { channel ->
          val isFollowing = followStates[channel.id] ?: false

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { /* Channel details */ }
              .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(channel.avatarColor),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = channel.name.take(2).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = channel.name,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  ),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                if (channel.isVerified) {
                  Spacer(modifier = Modifier.width(4.dp))
                  Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = WhatsAppGreen,
                    modifier = Modifier.size(15.dp)
                  )
                }
              }
              Text(
                text = channel.recentPost,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = WhatsAppTextSecondary,
                  fontSize = 13.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Follow / Following Button
            if (isFollowing) {
              OutlinedButton(
                onClick = { followStates[channel.id] = false },
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = WhatsAppGreen,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Following", fontSize = 12.sp, color = WhatsAppGreen)
              }
            } else {
              Button(
                onClick = { followStates[channel.id] = true },
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenContainer),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Text(
                  text = "Follow",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = WhatsAppGreenDeep
                )
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(90.dp))
        }
      }
    }

    // Floating Action Buttons (Pencil + Camera)
    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(bottom = 90.dp, end = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Small pencil action button for text status
      Box(
        modifier = Modifier
          .size(42.dp)
          .shadow(4.dp, CircleShape)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .clickable(onClick = onCameraClick),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Edit,
          contentDescription = "New text status",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Primary Camera FAB
      Box(
        modifier = Modifier
          .size(56.dp)
          .shadow(8.dp, RoundedCornerShape(18.dp))
          .clip(RoundedCornerShape(18.dp))
          .background(WhatsAppGreen)
          .clickable(onClick = onCameraClick),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.CameraAlt,
          contentDescription = "New media status",
          tint = Color.White,
          modifier = Modifier.size(26.dp)
        )
      }
    }
  }
}
