package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.MotionPhotosOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenContainer
import com.example.ui.theme.WhatsAppGreenDeep
import com.example.ui.theme.WhatsAppTextPrimary
import com.example.ui.theme.WhatsAppTextSecondary

enum class NavTab {
  CHATS,
  UPDATES,
  COMMUNITIES,
  CALLS
}

data class NavItem(
  val tab: NavTab,
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val badgeCount: Int = 0
)

@Composable
fun BottomNavBar(
  currentTab: NavTab,
  onTabSelected: (NavTab) -> Unit,
  unreadChatsCount: Int = 3,
  modifier: Modifier = Modifier
) {
  val items = listOf(
    NavItem(
      tab = NavTab.CHATS,
      title = "Chats",
      selectedIcon = Icons.AutoMirrored.Filled.Chat,
      unselectedIcon = Icons.Outlined.ChatBubbleOutline,
      badgeCount = unreadChatsCount
    ),
    NavItem(
      tab = NavTab.UPDATES,
      title = "Updates",
      selectedIcon = Icons.Filled.Update,
      unselectedIcon = Icons.Outlined.MotionPhotosOn
    ),
    NavItem(
      tab = NavTab.COMMUNITIES,
      title = "Communities",
      selectedIcon = Icons.Filled.Groups,
      unselectedIcon = Icons.Outlined.Groups
    ),
    NavItem(
      tab = NavTab.CALLS,
      title = "Calls",
      selectedIcon = Icons.Filled.Call,
      unselectedIcon = Icons.Outlined.Call
    )
  )

  // Floating Translucent Liquid Glass Bottom Navigation Bar
  Box(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .testTag("liquid_glass_bottom_nav")
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(
          elevation = 10.dp,
          shape = RoundedCornerShape(28.dp),
          ambientColor = Color(0x18000000),
          spotColor = Color(0x2200A884)
        )
        .border(
          width = 1.dp,
          color = Color(0x2800A884),
          shape = RoundedCornerShape(28.dp)
        ),
      shape = RoundedCornerShape(28.dp),
      color = Color(0xF5FFFFFF), // Liquid Glass frosted white
      tonalElevation = 3.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        items.forEach { item ->
          val isSelected = currentTab == item.tab
          val interactionSource = remember { MutableInteractionSource() }

          val iconTint by animateColorAsState(
            targetValue = if (isSelected) WhatsAppGreenDeep else WhatsAppTextSecondary,
            label = "tab_icon_tint"
          )

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onTabSelected(item.tab) }
              )
              .padding(horizontal = 12.dp, vertical = 4.dp)
              .testTag("nav_tab_${item.tab.name.lowercase()}")
          ) {
            Box(
              contentAlignment = Alignment.Center
            ) {
              // Active Pill Indicator behind Icon
              if (isSelected) {
                Box(
                  modifier = Modifier
                    .size(width = 54.dp, height = 28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(WhatsAppGreenContainer)
                )
              }

              Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
              )

              // Badge Count Pill for Unread Chats or Notifications
              if (item.badgeCount > 0) {
                Box(
                  modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(WhatsAppGreen)
                    .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = item.badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = item.title,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) WhatsAppTextPrimary else WhatsAppTextSecondary
              )
            )
          }
        }
      }
    }
  }
}
