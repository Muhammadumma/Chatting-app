package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BlurCircular
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LatticeNode
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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LatticeScreen(
  nodes: List<LatticeNode>,
  onNodeDirectMessage: (LatticeNode) -> Unit,
  onNodeRequestConduit: (LatticeNode) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedOrbitFilter by remember { mutableStateOf<Int?>(null) } // null = All
  var selectedNode by remember { mutableStateOf<LatticeNode?>(nodes.firstOrNull { it.orbitDistance == 2 }) }

  val filteredNodes = remember(selectedOrbitFilter, nodes) {
    if (selectedOrbitFilter == null) nodes
    else nodes.filter { it.orbitDistance == selectedOrbitFilter }
  }

  // Animation pulse for center node
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
  val pulseRadius by infiniteTransition.animateFloat(
    initialValue = 28f,
    targetValue = 42f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_radius"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(KickDeep)
      .testTag("lattice_screen_root")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 100.dp)
    ) {
      // Top Header
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
                  text = "THE LATTICE",
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
                    text = "RADAR",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = KickCyan
                  )
                }
              }
              Text(
                text = "Concentric Trust Orbits • Interactive Topology",
                fontSize = 11.sp,
                color = KickTextSecondary
              )
            }

            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, KickBorder, RoundedCornerShape(12.dp)),
              color = KickCard
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.FilterCenterFocus,
                  contentDescription = null,
                  tint = KickCyan,
                  modifier = Modifier.size(12.dp)
                )
                Text(
                  text = "${nodes.size} Nodes",
                  color = KickTextPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Orbit Filter Tabs
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OrbitFilterTab(
              title = "All Orbits",
              isSelected = selectedOrbitFilter == null,
              onClick = { selectedOrbitFilter = null },
              accentColor = KickCyan
            )
            OrbitFilterTab(
              title = "1° Direct",
              isSelected = selectedOrbitFilter == 1,
              onClick = { selectedOrbitFilter = 1 },
              accentColor = KickBlue
            )
            OrbitFilterTab(
              title = "2° Horizon",
              isSelected = selectedOrbitFilter == 2,
              onClick = { selectedOrbitFilter = 2 },
              accentColor = KickCyan
            )
            OrbitFilterTab(
              title = "3° Nebula",
              isSelected = selectedOrbitFilter == 3,
              onClick = { selectedOrbitFilter = 3 },
              accentColor = KickViolet
            )
          }
        }
      }

      // Visual Radar Canvas
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(340.dp)
          .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(KickObsidian)
            .border(1.dp, KickBorder, RoundedCornerShape(24.dp))
            .pointerInput(nodes) {
              detectTapGestures { offset ->
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val maxR = size.width.coerceAtMost(size.height) * 0.44f

                // Find closest node to tap
                var closestNode: LatticeNode? = null
                var minDistance = 50f // tap tolerance in pixels

                nodes.forEach { node ->
                  val angleRad = Math.toRadians(node.radialAngleDeg.toDouble())
                  val r = node.normalizedRadius * maxR
                  val nodeX = (centerX + cos(angleRad) * r).toFloat()
                  val nodeY = (centerY + sin(angleRad) * r).toFloat()

                  val dist = Math.hypot((offset.x - nodeX).toDouble(), (offset.y - nodeY).toDouble()).toFloat()
                  if (dist < minDistance) {
                    minDistance = dist
                    closestNode = node
                  }
                }

                if (closestNode != null) {
                  selectedNode = closestNode
                }
              }
            }
        ) {
          val centerX = size.width / 2f
          val centerY = size.height / 2f
          val maxR = size.width.coerceAtMost(size.height) * 0.44f

          // Draw Concentric Orbit Rings
          // Orbit 1: Direct (0.35 maxR)
          drawCircle(
            color = KickBlue.copy(alpha = 0.25f),
            radius = maxR * 0.35f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
          )

          // Orbit 2: The Horizon (0.68 maxR)
          drawCircle(
            color = KickCyan.copy(alpha = 0.4f),
            radius = maxR * 0.68f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2f)
          )

          // Orbit 3: Interest Mesh (0.95 maxR)
          drawCircle(
            color = KickViolet.copy(alpha = 0.25f),
            radius = maxR * 0.95f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f)))
          )

          // Draw Center Pulse and "You" Node
          drawCircle(
            color = KickCyan.copy(alpha = 0.15f),
            radius = pulseRadius,
            center = Offset(centerX, centerY)
          )
          drawCircle(
            color = KickCyan,
            radius = 16f,
            center = Offset(centerX, centerY)
          )
          drawCircle(
            color = Color(0xFF080D12),
            radius = 8f,
            center = Offset(centerX, centerY)
          )

          // Draw Nodes and Connecting Radii
          nodes.forEach { node ->
            val angleRad = Math.toRadians(node.radialAngleDeg.toDouble())
            val r = node.normalizedRadius * maxR
            val nodeX = (centerX + cos(angleRad) * r).toFloat()
            val nodeY = (centerY + sin(angleRad) * r).toFloat()

            val isSelected = selectedNode?.id == node.id

            // Connecting faint line to center
            drawLine(
              color = node.avatarColor.copy(alpha = if (isSelected) 0.5f else 0.15f),
              start = Offset(centerX, centerY),
              end = Offset(nodeX, nodeY),
              strokeWidth = if (isSelected) 2f else 1f
            )

            // Outer Selection Glow
            if (isSelected) {
              drawCircle(
                color = KickCyan.copy(alpha = 0.35f),
                radius = 22f,
                center = Offset(nodeX, nodeY)
              )
            }

            // Node Dot
            drawCircle(
              color = node.avatarColor,
              radius = if (isSelected) 14f else 10f,
              center = Offset(nodeX, nodeY)
            )

            // Inner Contrast Core
            drawCircle(
              color = Color.White,
              radius = 4f,
              center = Offset(nodeX, nodeY)
            )
          }
        }

        // Tap hint badge
        Box(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(KickCard.copy(alpha = 0.8f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "Tap any node to inspect trust path",
            color = KickTextSecondary,
            fontSize = 10.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Node Inspector Card (Selected Node)
      selectedNode?.let { node ->
        NodeInspectorCard(
          node = node,
          onDirectMessage = { onNodeDirectMessage(node) },
          onRequestConduit = { onNodeRequestConduit(node) },
          onDismiss = { selectedNode = null }
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Quick Cluster Browser Row
      Text(
        text = "ACTIVE TOPICAL NEBULAE",
        color = KickTextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(8.dp))

      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val clusters = listOf(
          "Systems Engineering (42)" to KickCyan,
          "Robotics & Hardware (18)" to KickAmber,
          "Sound Design & Media (35)" to KickViolet,
          "Clean Energy Syndicate (51)" to Color(0xFF20BF6B),
          "Zero-Knowledge Proofs (74)" to Color(0xFF00C6FF)
        )
        items(clusters) { (name, color) ->
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
            color = KickCard
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(color)
              )
              Text(
                text = name,
                color = KickTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun OrbitFilterTab(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  accentColor: Color
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (isSelected) accentColor.copy(alpha = 0.18f) else KickCard)
      .border(
        width = 1.dp,
        color = if (isSelected) accentColor else KickBorder,
        shape = RoundedCornerShape(12.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Text(
      text = title,
      color = if (isSelected) accentColor else KickTextSecondary,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )
  }
}

@Composable
fun NodeInspectorCard(
  node: LatticeNode,
  onDirectMessage: () -> Unit,
  onRequestConduit: () -> Unit,
  onDismiss: () -> Unit
) {
  val orbitName = when (node.orbitDistance) {
    1 -> "1° Direct Contact"
    2 -> "2° Horizon (Extended Network)"
    else -> "3° Interest Constellation"
  }
  val orbitColor = when (node.orbitDistance) {
    1 -> KickBlue
    2 -> KickCyan
    else -> KickViolet
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, KickBorder, RoundedCornerShape(20.dp))
      .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = orbitColor),
    color = KickCard
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header with close
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
              .size(44.dp)
              .clip(CircleShape)
              .background(node.avatarColor),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = node.name.take(2).uppercase(),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }

          Column {
            Text(
              text = node.name,
              color = KickTextPrimary,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${node.handle} • ${node.facet}",
              color = KickTextSecondary,
              fontSize = 11.sp
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(orbitColor.copy(alpha = 0.16f))
            .border(0.5.dp, orbitColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = orbitName,
            color = orbitColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Stats and Recent Topic
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp)),
        color = KickObsidian
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "${node.vouchesCount}",
              color = KickCyan,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(text = "Reputation Vouches", color = KickTextSecondary, fontSize = 10.sp)
          }

          Box(
            modifier = Modifier
              .width(1.dp)
              .height(30.dp)
              .background(Color(0x20FFFFFF))
          )

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "${node.activeDispatchesCount}",
              color = KickTextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(text = "Active Dispatches", color = KickTextSecondary, fontSize = 10.sp)
          }

          Box(
            modifier = Modifier
              .width(1.dp)
              .height(30.dp)
              .background(Color(0x20FFFFFF))
          )

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = node.clusterCategory.take(10),
              color = orbitColor,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Text(text = "Nebula", color = KickTextSecondary, fontSize = 10.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Zero-Knowledge Privacy Seal
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0x1800E5B8))
          .padding(horizontal = 10.dp, vertical = 7.dp),
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
          text = "Zero-Knowledge Sealed: Connecting mutual path is cryptographically concealed.",
          color = KickCyan,
          fontSize = 10.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Buttons
      if (node.isConnectedDirectly) {
        Button(
          onClick = onDirectMessage,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = KickCyan)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = null,
            tint = Color(0xFF080D12),
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Open Direct Dialog",
            color = Color(0xFF080D12),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      } else {
        Button(
          onClick = onRequestConduit,
          modifier = Modifier.fillMaxWidth(),
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
            text = "Request Conduit Intro (Double-Blind)",
            color = Color(0xFF080D12),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}
