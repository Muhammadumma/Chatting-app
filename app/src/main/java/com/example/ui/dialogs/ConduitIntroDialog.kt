package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.KickAmber
import com.example.ui.theme.KickBorder
import com.example.ui.theme.KickCard
import com.example.ui.theme.KickCyan
import com.example.ui.theme.KickDeep
import com.example.ui.theme.KickObsidian
import com.example.ui.theme.KickTextMuted
import com.example.ui.theme.KickTextPrimary
import com.example.ui.theme.KickTextSecondary

@Composable
fun ConduitIntroDialog(
  targetName: String,
  targetHandle: String,
  targetFacet: String,
  targetAvatarColor: Color,
  onSendRequest: (category: String, intentStatement: String) -> Unit,
  onDismiss: () -> Unit
) {
  val categories = listOf(
    "Technical Collaboration",
    "Advisory & IP",
    "Talent & Hiring",
    "Syndicate Investment"
  )
  var selectedCategory by remember { mutableStateOf(categories.first()) }
  var intentText by remember { mutableStateOf("") }
  var isSent by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(24.dp))
        .testTag("conduit_intro_dialog"),
      color = KickCard
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
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
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = KickCyan,
                modifier = Modifier.size(18.dp)
              )
            }
            Column {
              Text(
                text = "CONDUIT PROTOCOL",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = KickCyan
              )
              Text(
                text = "Double-Blind Warm Introduction",
                fontSize = 10.sp,
                color = KickTextSecondary
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = KickTextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isSent) {
          // Success confirmation screen
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(KickCyan.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = KickCyan,
                modifier = Modifier.size(32.dp)
              )
            }
            Text(
              text = "Conduit Request Dispatched",
              color = KickTextPrimary,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Your request was delivered to mutual contacts in zero-knowledge mode. You will be alerted when the intro is facilitated.",
              color = KickTextSecondary,
              fontSize = 12.sp,
              lineHeight = 16.sp,
              modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = onDismiss,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
            ) {
              Text(
                text = "Done",
                color = Color(0xFF080D12),
                fontWeight = FontWeight.Bold
              )
            }
          }
        } else {
          // Target Candidate Card
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp)),
            color = KickObsidian
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(targetAvatarColor),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = targetName.take(2).uppercase(),
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
              }
              Column {
                Text(
                  text = targetName,
                  color = KickTextPrimary,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "$targetHandle • $targetFacet",
                  color = KickTextSecondary,
                  fontSize = 11.sp
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Objective Category Selector
          Text(
            text = "CATEGORY OF INTENT",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = KickTextSecondary,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            categories.take(2).forEach { cat ->
              val isSelected = selectedCategory == cat
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
                  .clickable { selectedCategory = cat }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = cat,
                  color = if (isSelected) KickCyan else KickTextSecondary,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            categories.drop(2).forEach { cat ->
              val isSelected = selectedCategory == cat
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
                  .clickable { selectedCategory = cat }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = cat,
                  color = if (isSelected) KickCyan else KickTextSecondary,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Intent Statement Input
          Text(
            text = "STATEMENT OF INTENT (MAX 140 CHARS)",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = KickTextSecondary,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = intentText,
            onValueChange = { if (it.length <= 140) intentText = it },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("conduit_intent_input"),
            placeholder = {
              Text(
                text = "Explain your background and why you'd like to collaborate...",
                color = KickTextMuted,
                fontSize = 12.sp
              )
            },
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = KickCyan,
              unfocusedBorderColor = Color(0x25FFFFFF),
              focusedTextColor = KickTextPrimary,
              unfocusedTextColor = KickTextPrimary,
              focusedContainerColor = KickObsidian,
              unfocusedContainerColor = KickObsidian
            ),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Protocol Privacy Note
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0x1800E5B8))
              .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = KickCyan,
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = "An anonymous bridge prompt is sent to mutual contacts. Mutual consent is strictly required before dialog is opened.",
              color = KickCyan,
              fontSize = 10.sp,
              lineHeight = 13.sp
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Submit Button
          Button(
            onClick = {
              isSent = true
              onSendRequest(selectedCategory, intentText)
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("send_conduit_request_btn"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = null,
              tint = Color(0xFF080D12),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Discreetly Send Conduit Request",
              color = Color(0xFF080D12),
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }
      }
    }
  }
}
