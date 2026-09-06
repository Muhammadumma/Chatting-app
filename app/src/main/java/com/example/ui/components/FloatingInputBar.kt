package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppTextPrimary
import com.example.ui.theme.WhatsAppTextSecondary

@Composable
fun FloatingInputBar(
  text: String,
  onTextChanged: (String) -> Unit,
  onSend: (String) -> Unit,
  onCameraClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showAttachments by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    // Attachment sheet popover
    AnimatedVisibility(visible = showAttachments) {
      AttachmentOptionsGrid(
        onOptionSelected = {
          showAttachments = false
          if (it == "Camera") onCameraClick()
        }
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Frosted Glass Floating Input Container
      Surface(
        modifier = Modifier
          .weight(1f)
          .shadow(
            elevation = 6.dp,
            shape = RoundedCornerShape(26.dp),
            ambientColor = Color(0x18000000),
            spotColor = Color(0x1F00A884)
          )
          .border(
            width = 1.dp,
            color = Color(0x3300A884),
            shape = RoundedCornerShape(26.dp)
          ),
        shape = RoundedCornerShape(26.dp),
        color = Color(0xF5FFFFFF), // Liquid frosted translucent glass
        tonalElevation = 2.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Emoji Icon
          IconButton(
            onClick = { /* Toggle emoji keyboard */ },
            modifier = Modifier
              .size(42.dp)
              .testTag("input_emoji_button")
          ) {
            Icon(
              imageVector = Icons.Default.SentimentSatisfied,
              contentDescription = "Emojis",
              tint = WhatsAppTextSecondary,
              modifier = Modifier.size(24.dp)
            )
          }

          // Text Field
          Box(
            modifier = Modifier
              .weight(1f)
              .padding(vertical = 10.dp)
          ) {
            if (text.isEmpty()) {
              Text(
                text = "Message",
                style = TextStyle(
                  fontSize = 16.sp,
                  color = WhatsAppTextSecondary
                )
              )
            }
            BasicTextField(
              value = text,
              onValueChange = onTextChanged,
              textStyle = TextStyle(
                fontSize = 16.sp,
                color = WhatsAppTextPrimary
              ),
              cursorBrush = SolidColor(WhatsAppGreen),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("chat_text_input")
            )
          }

          // Attachment Paperclip
          IconButton(
            onClick = { showAttachments = !showAttachments },
            modifier = Modifier
              .size(40.dp)
              .testTag("input_attach_button")
          ) {
            Icon(
              imageVector = Icons.Default.AttachFile,
              contentDescription = "Attach file",
              tint = WhatsAppTextSecondary,
              modifier = Modifier.size(22.dp)
            )
          }

          // Camera Icon (only visible when text is empty)
          if (text.isEmpty()) {
            IconButton(
              onClick = onCameraClick,
              modifier = Modifier
                .size(40.dp)
                .testTag("input_camera_button")
            ) {
              Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Camera",
                tint = WhatsAppTextSecondary,
                modifier = Modifier.size(22.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.width(6.dp))

      // Action Button: Voice Mic or Send Button
      val isSend = text.isNotBlank()
      val buttonBg by animateColorAsState(
        targetValue = WhatsAppGreen,
        label = "btn_bg"
      )

      Box(
        modifier = Modifier
          .size(48.dp)
          .shadow(
            elevation = 6.dp,
            shape = CircleShape,
            ambientColor = Color(0x2000A884),
            spotColor = Color(0x3300A884)
          )
          .clip(CircleShape)
          .background(buttonBg)
          .clickable {
            if (isSend) {
              onSend(text)
            }
          }
          .testTag(if (isSend) "send_message_button" else "voice_note_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isSend) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
          contentDescription = if (isSend) "Send" else "Voice record",
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
      }
    }
  }
}

@Composable
fun AttachmentOptionsGrid(onOptionSelected: (String) -> Unit) {
  val options = listOf(
    AttachmentOption("Document", Icons.Default.Description, Color(0xFF7F66FF)),
    AttachmentOption("Camera", Icons.Default.CameraAlt, Color(0xFFFF2E93)),
    AttachmentOption("Gallery", Icons.Default.Image, Color(0xFFAC44CF)),
    AttachmentOption("Audio", Icons.Default.Headphones, Color(0xFFFF8A00)),
    AttachmentOption("Location", Icons.Default.LocationOn, Color(0xFF1EBB67)),
    AttachmentOption("Contact", Icons.Default.Person, Color(0xFF009DE2)),
    AttachmentOption("Poll", Icons.Default.BarChart, Color(0xFF00BFA5))
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 10.dp)
      .shadow(8.dp, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    color = Color(0xF8FFFFFF)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        options.take(4).forEach { option ->
          AttachmentItem(option = option, onClick = { onOptionSelected(option.title) })
        }
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        options.drop(4).forEach { option ->
          AttachmentItem(option = option, onClick = { onOptionSelected(option.title) })
        }
      }
    }
  }
}

data class AttachmentOption(
  val title: String,
  val icon: ImageVector,
  val color: Color
)

@Composable
private fun AttachmentItem(
  option: AttachmentOption,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(52.dp)
        .clip(CircleShape)
        .background(option.color),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = option.icon,
        contentDescription = option.title,
        tint = Color.White,
        modifier = Modifier.size(24.dp)
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = option.title,
      fontSize = 12.sp,
      color = WhatsAppTextSecondary
    )
  }
}
