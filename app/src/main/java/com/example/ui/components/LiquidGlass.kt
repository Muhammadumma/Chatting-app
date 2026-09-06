package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Liquid Glass styling modifier that provides a translucent frosted glass look
 * with soft light rim, depth shadow, and semi-transparent surface.
 */
fun Modifier.liquidGlassEffect(
  shape: Shape = RoundedCornerShape(24.dp),
  backgroundColor: Color = Color(0xF2FFFFFF), // 95% opacity frosted white
  borderStrokeColor: Color = Color(0x3300A884), // subtle WhatsApp green/cyan sheen
  elevation: Dp = 8.dp
): Modifier = this
  .shadow(
    elevation = elevation,
    shape = shape,
    ambientColor = Color(0x1A000000),
    spotColor = Color(0x2400A884)
  )
  .clip(shape)
  .background(
    brush = Brush.verticalGradient(
      colors = listOf(
        backgroundColor,
        backgroundColor.copy(alpha = backgroundColor.alpha * 0.92f)
      )
    ),
    shape = shape
  )
  .border(
    width = 1.dp,
    brush = Brush.verticalGradient(
      colors = listOf(
        Color(0x80FFFFFF), // top highlight reflection
        borderStrokeColor
      )
    ),
    shape = shape
  )

@Composable
fun LiquidGlassSurface(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(24.dp),
  backgroundColor: Color = Color(0xF2FFFFFF),
  borderStrokeColor: Color = Color(0x2600A884),
  elevation: Dp = 8.dp,
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    modifier = modifier.liquidGlassEffect(
      shape = shape,
      backgroundColor = backgroundColor,
      borderStrokeColor = borderStrokeColor,
      elevation = elevation
    ),
    content = content
  )
}
