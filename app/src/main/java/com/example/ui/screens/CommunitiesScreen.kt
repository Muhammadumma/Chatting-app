package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Community
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenContainer
import com.example.ui.theme.WhatsAppTextSecondary

@Composable
fun CommunitiesScreen(
  communities: List<Community>,
  onNewCommunityClick: () -> Unit,
  onCommunityClick: (Community) -> Unit,
  onCameraClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .testTag("communities_screen")
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
            text = "Communities",
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

      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        // "New Community" Action Card
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = onNewCommunityClick)
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(WhatsAppGreenContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                tint = WhatsAppGreen,
                modifier = Modifier.size(28.dp)
              )
              Box(
                modifier = Modifier
                  .size(18.dp)
                  .align(Alignment.BottomEnd)
                  .clip(CircleShape)
                  .background(WhatsAppGreen),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "New",
                  tint = Color.White,
                  modifier = Modifier.size(12.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
              text = "New community",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
              )
            )
          }

          HorizontalDivider(thickness = 8.dp, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        }

        // Active Communities List
        items(communities, key = { it.id }) { community ->
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onCommunityClick(community) }
              .padding(vertical = 12.dp)
          ) {
            // Community Header
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .background(community.iconColor),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Groups,
                  contentDescription = community.name,
                  tint = Color.White,
                  modifier = Modifier.size(26.dp)
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = community.name,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                  )
                )
                Text(
                  text = community.memberCount,
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = WhatsAppTextSecondary,
                    fontSize = 12.sp
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Announcements Sub-channel Item
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(WhatsAppGreenContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Campaign,
                  contentDescription = "Announcements",
                  tint = WhatsAppGreen,
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Announcements",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                  )
                  Spacer(modifier = Modifier.weight(1f))
                  Text(
                    text = community.announcementTime,
                    fontSize = 11.sp,
                    color = WhatsAppTextSecondary
                  )
                }
                Text(
                  text = community.announcementSnippet,
                  fontSize = 12.sp,
                  color = WhatsAppTextSecondary,
                  maxLines = 1
                )
              }
            }

            // Sub-groups preview chips
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(start = 70.dp, end = 16.dp, top = 4.dp)
            ) {
              Text(
                text = "${community.subGroups.size} groups in community",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = WhatsAppGreen
              )
            }
          }

          HorizontalDivider(thickness = 8.dp, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        }

        item {
          Spacer(modifier = Modifier.height(90.dp))
        }
      }
    }
  }
}
