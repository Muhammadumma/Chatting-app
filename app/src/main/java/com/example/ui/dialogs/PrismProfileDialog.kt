package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.PrismProfile
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PrismProfileDialog(
  profile: PrismProfile,
  onDismiss: () -> Unit,
  onLogout: (() -> Unit)? = null
) {
  var selectedFacetIndex by remember { mutableStateOf(0) }
  val currentFacet = profile.facets.getOrNull(selectedFacetIndex) ?: profile.facets.first()

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(24.dp))
        .testTag("prism_profile_dialog"),
      color = KickCard
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(KickCyan.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Fingerprint,
                contentDescription = null,
                tint = KickCyan,
                modifier = Modifier.size(18.dp)
              )
            }
            Column {
              Text(
                text = "THE PRISM",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = KickCyan
              )
              Text(
                text = "Context-Adaptive Identity",
                fontSize = 10.sp,
                color = KickTextSecondary
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = KickTextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar & Identity Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(profile.avatarColor),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = profile.name.take(2).uppercase(),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
          }

          Column {
            Text(
              text = profile.name,
              color = KickTextPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = profile.handle,
              color = KickCyan,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sealed Phone Number Badge
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(12.dp)),
          color = KickObsidian
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = KickCyan,
              modifier = Modifier.size(16.dp)
            )
            Column {
              Text(
                text = "Phone Identity: Sealed Cryptographically",
                color = KickTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Hidden from all contacts & networks. Verified by zero-knowledge attestation.",
                color = KickTextSecondary,
                fontSize = 9.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Trust Metrics Panel
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp)),
            color = KickObsidian
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "${profile.totalVouchesReceived}",
                color = KickCyan,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Vouches Received",
                color = KickTextSecondary,
                fontSize = 10.sp
              )
            }
          }

          Surface(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp)),
            color = KickObsidian
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "${profile.dailyVouchesRemaining}/5",
                color = KickAmber,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Daily Vouches Left",
                color = KickTextSecondary,
                fontSize = 10.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prism Facets Switcher
        Text(
          text = "ACTIVE FACETS (CONTEXTUAL PERSONAS)",
          color = KickTextSecondary,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          profile.facets.forEachIndexed { index, facet ->
            val isSelected = index == selectedFacetIndex
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) KickCyan.copy(alpha = 0.2f) else KickObsidian)
                .border(
                  1.dp,
                  if (isSelected) KickCyan else Color(0x20FFFFFF),
                  RoundedCornerShape(10.dp)
                )
                .clickable { selectedFacetIndex = index }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = facet.name.take(12),
                color = if (isSelected) KickCyan else KickTextSecondary,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Facet Details Box
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, KickBorder, RoundedCornerShape(14.dp)),
          color = KickObsidian
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = currentFacet.name,
                color = KickCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(KickBlue.copy(alpha = 0.15f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = currentFacet.visibility,
                  color = KickBlue,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = currentFacet.headline,
              color = KickTextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Skill / Interest Tags
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              currentFacet.tags.forEach { tag ->
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0x25FFFFFF))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(text = "#$tag", color = KickTextSecondary, fontSize = 9.sp)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Current Endeavors
        Text(
          text = "ACTIVE ENDEAVORS",
          color = KickTextSecondary,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          profile.activeEndeavors.forEach { endeavor ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(KickObsidian)
                .padding(8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(KickCyan)
              )
              Text(
                text = endeavor,
                color = KickTextPrimary,
                fontSize = 11.sp
              )
            }
          }
        }

        if (onLogout != null) {
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onLogout,
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("prism_logout_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFFF5252).copy(alpha = 0.15f),
              contentColor = Color(0xFFFF5252)
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.4f))
          ) {
            Text(
              text = "Log Out / Re-test Onboarding Flow",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
