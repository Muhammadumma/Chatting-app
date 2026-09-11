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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TextFields
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
import com.example.model.Dispatch
import com.example.model.DispatchType
import com.example.ui.theme.KickAmber
import com.example.ui.theme.KickBorder
import com.example.ui.theme.KickCard
import com.example.ui.theme.KickCyan
import com.example.ui.theme.KickObsidian
import com.example.ui.theme.KickTextMuted
import com.example.ui.theme.KickTextPrimary
import com.example.ui.theme.KickTextSecondary

@Composable
fun PublishDispatchDialog(
  onPublish: (title: String, body: String, type: DispatchType) -> Unit,
  onDismiss: () -> Unit
) {
  var title by remember { mutableStateOf("") }
  var body by remember { mutableStateOf("") }
  var selectedType by remember { mutableStateOf(DispatchType.BRIEF) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(24.dp))
        .testTag("publish_dispatch_dialog"),
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
                imageVector = Icons.Default.PostAdd,
                contentDescription = null,
                tint = KickCyan,
                modifier = Modifier.size(18.dp)
              )
            }
            Column {
              Text(
                text = "NEW DISPATCH",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = KickCyan
              )
              Text(
                text = "Broadcast to Extended Trust Mesh",
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

        Spacer(modifier = Modifier.height(14.dp))

        // Type Selector Tabs
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val types = listOf(
            Triple(DispatchType.BRIEF, "Brief", Icons.Default.TextFields),
            Triple(DispatchType.AUDIO_MEMO, "Audio", Icons.Default.Mic),
            Triple(DispatchType.ARTIFACT, "Artifact", Icons.Default.Code),
            Triple(DispatchType.INQUIRY, "Inquiry", Icons.Default.MonetizationOn)
          )

          types.forEach { (type, label, icon) ->
            val isSelected = selectedType == type
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
                .clickable { selectedType = type }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  tint = if (isSelected) KickCyan else KickTextSecondary,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = label,
                  color = if (isSelected) KickCyan else KickTextSecondary,
                  fontSize = 10.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          modifier = Modifier.fillMaxWidth(),
          placeholder = { Text(text = "Headline / Title (Optional)", color = KickTextMuted, fontSize = 12.sp) },
          singleLine = true,
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

        Spacer(modifier = Modifier.height(10.dp))

        // Body
        OutlinedTextField(
          value = body,
          onValueChange = { body = it },
          modifier = Modifier.fillMaxWidth(),
          placeholder = {
            Text(
              text = when (selectedType) {
                DispatchType.INQUIRY -> "Describe what or who you are looking for, and what intro bounty you offer..."
                DispatchType.ARTIFACT -> "Describe the code repository, paper, or research artifact..."
                DispatchType.AUDIO_MEMO -> "Enter audio memo transcript or field recording notes..."
                else -> "Share high-signal insight with your 2° network..."
              },
              color = KickTextMuted,
              fontSize = 12.sp
            )
          },
          maxLines = 4,
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

        // Privacy note
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x1800E5B8))
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = KickCyan,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Dispatches travel via cryptographic trust distances. No global algorithmic feed or tracking cookies.",
            color = KickCyan,
            fontSize = 10.sp
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (body.isNotBlank() || title.isNotBlank()) {
              onPublish(title, body, selectedType)
              onDismiss()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("confirm_publish_dispatch"),
          shape = RoundedCornerShape(12.dp),
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
            text = "Propagate to Horizon",
            color = Color(0xFF080D12),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}
