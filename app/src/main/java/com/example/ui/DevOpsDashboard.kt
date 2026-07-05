package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevOpsDashboard(
    onPillarSelected: (Int) -> Unit,
    activeBuildsCount: Int,
    unhealthyPodsCount: Int,
    totalMonthlyCost: Double,
    activeAlertsCount: Int
) {
    val pillars = listOf(
        PillarItem(1, "CI/CD Monitor", "View build pipelines, run triggers, analyze failures", Icons.Default.DirectionsRun, NeonOrange),
        PillarItem(2, "IaC Generator", "Design infrastructure and generate Terraform", Icons.Default.Terminal, CyberCyan),
        PillarItem(3, "K8s Controller", "Check pods, restart nodes, troubleshoot logs", Icons.Default.Dns, NeonGreen),
        PillarItem(4, "FinOps Dashboard", "Multi-cloud metrics, scale recommendations", Icons.Default.Savings, NeonPurple),
        PillarItem(5, "Instant Alerts", "System incidents, automatic summary, fixes", Icons.Default.Warning, HotCoral),
        PillarItem(6, "Code Scanner", "DevSecOps vulnerability & compliance scanner", Icons.Default.Security, CyberCyan)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(16.dp)
    ) {
        // App Header
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(listOf(CyberCyan, NeonPurple)))
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(6.dp))
                                .background(ObsidianBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = "Logo",
                                tint = CyberCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "DEVOPSHUB AI",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = PureWhite,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "6-Pillars Autonomous Assistant",
                            fontSize = 11.sp,
                            color = SlateText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Profile Circle avatar from Elegant Dark template
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DeepNavySurface)
                        .border(1.dp, BorderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JS",
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Main AI Command Surface Card
        item(span = { GridItemSpan(2) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, BorderColor, RoundedCornerShape(28.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Running pulse indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NeonGreen)
                            .align(Alignment.TopEnd)
                    )

                    Column {
                        Text(
                            text = "GEMINI PRO ASSISTANT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateText,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "How can I help you automate your infrastructure today?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = PureWhite,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { onPillarSelected(2) }, // IaC Generator
                                colors = ButtonDefaults.buttonColors(containerColor = DarkGreyCard),
                                border = BorderStroke(1.dp, BorderColor),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Build VPC...", color = PureWhite, fontSize = 11.sp)
                            }

                            Button(
                                onClick = { onPillarSelected(1) }, // CI/CD Monitor
                                colors = ButtonDefaults.buttonColors(containerColor = DarkGreyCard),
                                border = BorderStroke(1.dp, BorderColor),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Debug Logs", color = PureWhite, fontSize = 11.sp)
                            }

                            Button(
                                onClick = { onPillarSelected(2) }, // IaC Generator prompt focusing
                                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Ask AI", color = ObsidianBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Connection Webhooks / Live Status Indicators
        item(span = { GridItemSpan(2) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "DEVOPS AGENT SIGNALS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateText,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusIndicator(label = "GitHub Actions", connected = true)
                        StatusIndicator(label = "AWS Multi-AZ", connected = true)
                        StatusIndicator(label = "K8s Cluster", connected = unhealthyPodsCount == 0)
                        StatusIndicator(label = "Prometheus", connected = true)
                    }
                }
            }
        }

        // Metric Card - Active Pipelines
        item(span = { GridItemSpan(1) }) {
            MetricCard(
                title = "Active Pipelines",
                value = "$activeBuildsCount Active",
                subText = "1 running, 1 failed",
                color = if (activeBuildsCount > 0) NeonOrange else NeonGreen,
                icon = Icons.Default.Cached
            )
        }

        // Metric Card - K8s Service Health
        item(span = { GridItemSpan(1) }) {
            MetricCard(
                title = "K8s Service Health",
                value = if (unhealthyPodsCount > 0) "Degraded" else "Optimal",
                subText = if (unhealthyPodsCount > 0) "$unhealthyPodsCount Pod Crashed" else "4/4 Pods Online",
                color = if (unhealthyPodsCount > 0) HotCoral else NeonGreen,
                icon = Icons.Default.Grid3x3
            )
        }

        // Metric Card - Multi-Cloud Costs
        item(span = { GridItemSpan(1) }) {
            MetricCard(
                title = "Multi-Cloud Costs",
                value = "$${String.format("%.2f", totalMonthlyCost)}",
                subText = "-$320.00 Opt. Available",
                color = CyberCyan,
                icon = Icons.Default.MonetizationOn
            )
        }

        // Metric Card - Critical Incidents
        item(span = { GridItemSpan(1) }) {
            MetricCard(
                title = "Critical Incidents",
                value = "$activeAlertsCount Triggered",
                subText = "AI Proposals ready",
                color = if (activeAlertsCount > 0) HotCoral else NeonGreen,
                icon = Icons.Default.NotificationsActive
            )
        }

        // 6 Pillars Section Title
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "DEV-OPS CORE 6 PILLARS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SlateText,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        // The 6 pillars cards list
        items(pillars) { pillar ->
            PillarCard(pillar = pillar, onClick = { onPillarSelected(pillar.id) })
        }
    }
}

data class PillarItem(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun StatusIndicator(label: String, connected: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (connected) NeonGreen else HotCoral)
                .padding(if (connected) (2.dp * scale) else 0.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = GhostWhite,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subText: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
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
                    text = title,
                    fontSize = 11.sp,
                    color = SlateText,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subText,
                fontSize = 10.sp,
                color = SlateText
            )
        }
    }
}

@Composable
fun PillarCard(
    pillar: PillarItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .clickable { onClick() }
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .testTag("pillar_card_${pillar.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(pillar.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = pillar.icon,
                        contentDescription = null,
                        tint = pillar.color,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "P${pillar.id}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = pillar.color
                )
            }

            Column {
                Text(
                    text = pillar.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = pillar.description,
                    fontSize = 9.sp,
                    color = SlateText,
                    lineHeight = 11.sp,
                    maxLines = 2
                )
            }
        }
    }
}
