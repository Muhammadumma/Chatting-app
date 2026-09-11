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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ScoutQuery
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
fun ScoutAssistantDialog(
  initialQueries: List<ScoutQuery>,
  onExecuteAction: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var queryHistory by remember { mutableStateOf(initialQueries) }
  var inputPrompt by remember { mutableStateOf("") }

  val sampleSuggestions = listOf(
    "Find 2° engineers in distributed DBs",
    "Summarize trending topics in my network",
    "Active sound synthesis communities"
  )

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .border(1.dp, KickBorder, RoundedCornerShape(24.dp))
        .testTag("scout_assistant_dialog"),
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
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(listOf(KickCyan, Color(0xFF00B894)))
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFF080D12),
                modifier = Modifier.size(20.dp)
              )
            }

            Column {
              Text(
                text = "SCOUT AI",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = KickCyan
              )
              Text(
                text = "Private Network Navigator",
                fontSize = 11.sp,
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

        Spacer(modifier = Modifier.height(12.dp))

        // Privacy note
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x1800E5B8))
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = KickCyan,
            modifier = Modifier.size(12.dp)
          )
          Text(
            text = "Scout operates locally with zero-knowledge path confidentiality.",
            color = KickCyan,
            fontSize = 10.sp
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Suggestion Chips
        Text(
          text = "SUGGESTED NAVIGATIONS",
          color = KickTextSecondary,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          sampleSuggestions.forEach { suggestion ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(10.dp))
                .clickable {
                  inputPrompt = suggestion
                },
              color = KickObsidian
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Search,
                  contentDescription = null,
                  tint = KickCyan,
                  modifier = Modifier.size(12.dp)
                )
                Text(
                  text = suggestion,
                  color = KickTextPrimary,
                  fontSize = 11.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Query Stream History
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = false)
            .height(180.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(queryHistory, key = { it.id }) { q ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, KickBorder, RoundedCornerShape(12.dp)),
              color = KickObsidian
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "“${q.prompt}”",
                    color = KickCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(KickCyan.copy(alpha = 0.15f))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "${q.matchedCount} Matched",
                      color = KickCyan,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = q.responseSummary,
                  color = KickTextPrimary.copy(alpha = 0.9f),
                  fontSize = 11.sp,
                  lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                  onClick = { onExecuteAction(q.suggestedAction) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
                ) {
                  Text(
                    text = q.suggestedAction,
                    color = Color(0xFF080D12),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = inputPrompt,
            onValueChange = { inputPrompt = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = "Ask Scout about your network...", color = KickTextMuted, fontSize = 12.sp) },
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

          IconButton(
            onClick = {
              if (inputPrompt.isNotBlank()) {
                val newQuery = ScoutQuery(
                  id = "sq_${System.currentTimeMillis()}",
                  prompt = inputPrompt,
                  responseSummary = "Queried 412 nodes in your 2° horizon. Found relevant connections matching \"$inputPrompt\" with cryptographic path seclusion.",
                  matchedCount = 4,
                  suggestedAction = "View Dispatches & Request Conduit"
                )
                queryHistory = listOf(newQuery) + queryHistory
                inputPrompt = ""
              }
            },
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(KickCyan)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              tint = Color(0xFF080D12),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
