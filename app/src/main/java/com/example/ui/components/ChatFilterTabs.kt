package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatFilter
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenContainer
import com.example.ui.theme.WhatsAppGreenDeep

@Composable
fun ChatFilterTabs(
  selectedFilter: ChatFilter,
  onFilterSelected: (ChatFilter) -> Unit,
  unreadCount: Int,
  modifier: Modifier = Modifier
) {
  val filters = listOf(
    ChatFilter.ALL to "All",
    ChatFilter.UNREAD to "Unread",
    ChatFilter.PERSONAL to "Personal",
    ChatFilter.BUSINESS to "Business",
    ChatFilter.GROUPS to "Groups"
  )

  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .testTag("chat_filter_tabs"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    items(filters) { (filter, label) ->
      val isSelected = filter == selectedFilter

      val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) WhatsAppGreenContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
        label = "filter_bg"
      )
      val textColor by animateColorAsState(
        targetValue = if (isSelected) WhatsAppGreenDeep else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "filter_text"
      )
      val borderColor = if (isSelected) WhatsAppGreen.copy(alpha = 0.35f) else Color.Transparent

      Box(
        modifier = Modifier
          .clip(CircleShape)
          .background(backgroundColor)
          .border(1.dp, borderColor, CircleShape)
          .clickable { onFilterSelected(filter) }
          .padding(horizontal = 14.dp, vertical = 7.dp)
          .testTag("filter_tab_${filter.name.lowercase()}"),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor
          )

          // Show unread badge count for UNREAD filter tab if count > 0
          if (filter == ChatFilter.UNREAD && unreadCount > 0) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(WhatsAppGreen)
                .padding(horizontal = 5.dp, vertical = 1.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = unreadCount.toString(),
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}
