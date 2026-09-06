package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenLight

@Composable
fun WhatsAppLogo(
  modifier: Modifier = Modifier,
  size: Dp = 34.dp,
  showText: Boolean = false
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Canvas(modifier = Modifier.size(size)) {
      val w = this.size.width
      val h = this.size.height
      val radius = w / 2f
      val center = Offset(w / 2f, h / 2f)

      // WhatsApp green circle speech bubble base
      drawCircle(
        brush = Brush.linearGradient(
          colors = listOf(WhatsAppGreenLight, WhatsAppGreen),
          start = Offset(0f, 0f),
          end = Offset(w, h)
        ),
        radius = radius,
        center = center
      )

      // Speech bubble tail
      val tailPath = Path().apply {
        moveTo(w * 0.28f, h * 0.72f)
        lineTo(w * 0.12f, h * 0.88f)
        lineTo(w * 0.38f, h * 0.82f)
        close()
      }
      drawPath(
        path = tailPath,
        brush = Brush.linearGradient(
          colors = listOf(WhatsAppGreen, WhatsAppGreenLight)
        )
      )

      // Inner phone handset icon (white)
      drawPhoneHandset(w, h)
    }

    if (showText) {
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "WhatsApp",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp,
          color = WhatsAppGreen
        )
      )
    }
  }
}

private fun DrawScope.drawPhoneHandset(w: Float, h: Float) {
  // Handset drawn with precise, smooth vector stroke
  val strokeWidth = w * 0.11f
  val phonePath = Path().apply {
    // Earpiece
    moveTo(w * 0.35f, h * 0.36f)
    cubicTo(
      w * 0.35f, h * 0.32f,
      w * 0.42f, h * 0.30f,
      w * 0.46f, h * 0.35f
    )
    // Cord body curvature
    cubicTo(
      w * 0.52f, h * 0.43f,
      w * 0.58f, h * 0.50f,
      w * 0.64f, h * 0.56f
    )
    // Mouthpiece
    cubicTo(
      w * 0.70f, h * 0.62f,
      w * 0.68f, h * 0.68f,
      w * 0.64f, h * 0.69f
    )
  }

  // Draw phone handset silhouette with rounded caps
  drawPath(
    path = phonePath,
    color = Color.White,
    style = Stroke(
      width = strokeWidth,
      cap = StrokeCap.Round,
      join = StrokeJoin.Round
    )
  )

  // Handset end bulbs (earpiece and mouthpiece)
  drawCircle(
    color = Color.White,
    radius = strokeWidth * 0.7f,
    center = Offset(w * 0.38f, h * 0.35f)
  )
  drawCircle(
    color = Color.White,
    radius = strokeWidth * 0.7f,
    center = Offset(w * 0.63f, h * 0.66f)
  )
}

@Composable
fun MetaAIRingIcon(
  modifier: Modifier = Modifier,
  size: Dp = 22.dp
) {
  // Meta AI signature gradient circle ring
  Box(
    modifier = modifier
      .size(size)
      .background(
        brush = Brush.sweepGradient(
          colors = listOf(
            Color(0xFF00C6FF),
            Color(0xFF0072FF),
            Color(0xFF6C5CE7),
            Color(0xFFFF007F),
            Color(0xFF00C6FF)
          )
        ),
        shape = CircleShape
      )
      .padding(2.dp)
      .background(Color.White, CircleShape)
  )
}
