package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Guild
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

@Composable
fun GuildsScreen(
  guilds: List<Guild>,
  onEnterGuild: (Guild) -> Unit,
  onJoinVoiceTable: (Guild) -> Unit,
  modifier: Modifier = Modifier
) {
  var guildList by remember { mutableStateOf(guilds) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(KickDeep)
      .testTag("guilds_screen_root")
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Header
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding(),
        color = KickObsidian.copy(alpha = 0.95f)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "GUILDS",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Black,
                  letterSpacing = 1.5.sp,
                  color = KickCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(KickCyan.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "VOUCH-GATED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = KickCyan
                  )
                }
              }
              Text(
                text = "Structured Commons & Live Audio Roundtables",
                fontSize = 11.sp,
                color = KickTextSecondary
              )
            }
          }
        }
      }

      // Guilds List
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Banner Explainer
        item {
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(16.dp)),
            color = KickCard.copy(alpha = 0.6f)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(KickCyan.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Shield,
                  contentDescription = null,
                  tint = KickCyan,
                  modifier = Modifier.size(20.dp)
                )
              }
              Column {
                Text(
                  text = "Vouch-Bound Membership",
                  color = KickTextPrimary,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Guilds require mutual vouches or 2° network proximity to join, eliminating spam and maintaining high discussion quality.",
                  color = KickTextSecondary,
                  fontSize = 10.sp,
                  lineHeight = 14.sp
                )
              }
            }
          }
        }

        items(guildList, key = { it.id }) { guild ->
          GuildCard(
            guild = guild,
            onEnter = { onEnterGuild(guild) },
            onVoice = { onJoinVoiceTable(guild) },
            onToggleMembership = {
              guildList = guildList.map {
                if (it.id == guild.id) it.copy(isMember = !it.isMember) else it
              }
            }
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GuildCard(
  guild: Guild,
  onEnter: () -> Unit,
  onVoice: () -> Unit,
  onToggleMembership: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, KickBorder, RoundedCornerShape(20.dp))
      .shadow(8.dp, RoundedCornerShape(20.dp)),
    color = KickCard
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(guild.iconColor),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Groups,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Column {
            Text(
              text = guild.name,
              color = KickTextPrimary,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = guild.memberCount,
              color = KickTextSecondary,
              fontSize = 11.sp
            )
          }
        }

        // Trust Requirement Tag
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(KickCyan.copy(alpha = 0.15f))
            .border(0.5.dp, KickCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = guild.trustRequirement,
            color = KickCyan,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = guild.headline,
        color = KickTextPrimary.copy(alpha = 0.9f),
        fontSize = 12.sp,
        lineHeight = 17.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Active Discussion Box
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp)),
        color = KickObsidian
      ) {
        Row(
          modifier = Modifier.padding(10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Forum,
            contentDescription = null,
            tint = KickCyan,
            modifier = Modifier.size(16.dp)
          )
          Column {
            Text(
              text = "CURRENT THREAD",
              color = KickTextSecondary,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = guild.activeDiscussion,
              color = KickTextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // Live Voice Table indicator (if users active)
      if (guild.activeVoiceUsers > 0) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(KickAmber.copy(alpha = 0.15f))
            .clickable(onClick = onVoice)
            .padding(horizontal = 10.dp, vertical = 6.dp),
          color = Color.Transparent
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(KickAmber)
              )
              Text(
                text = "${guild.activeVoiceUsers} people live in Drop-In Voice Table",
                color = KickAmber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "Tap to Listen",
              color = KickAmber,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Sub Channels Pills
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        guild.subChannels.forEach { ch ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0x20FFFFFF))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(text = ch, color = KickTextSecondary, fontSize = 10.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (guild.isMember) {
          Button(
            onClick = onEnter,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
          ) {
            Text(
              text = "Enter Guild Commons",
              color = Color(0xFF080D12),
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        } else {
          OutlinedButton(
            onClick = onToggleMembership,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = "Join with 2° Vouch",
              color = KickCyan,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        }
      }
    }
  }
}
