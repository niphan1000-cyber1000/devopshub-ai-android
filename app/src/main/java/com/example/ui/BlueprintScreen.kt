package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DevOpsData
import com.example.ui.theme.*

@Composable
fun BlueprintScreen() {
    var expandedSection by remember { mutableStateOf<Int?>(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(NeonPurple.copy(alpha = 0.1f), Color.Transparent)))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ARCHITECTURE & DESIGN SPEC",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "DevOpsHub AI Blueprint",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Production implementation blueprint mapping local state models, security credential transit protocols, Gemini API system prompt frameworks, and multi-cloud operations.",
                        fontSize = 12.sp,
                        color = SlateText,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "SPECIFICATION SECTIONS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SlateText,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Expandable specifications
        BlueprintSectionItem(
            id = 1,
            title = "1. Core Architecture & Tech Stack",
            description = "Layered Clean Architecture pattern, state container choices, and Gemini Pro orchestration.",
            icon = Icons.Default.Layers,
            expanded = expandedSection == 1,
            onClick = { expandedSection = if (expandedSection == 1) null else 1 },
            content = DevOpsData.BLUEPRINT_ARCHITECTURE_MD
        )

        BlueprintSectionItem(
            id = 2,
            title = "2. Mobile Security & Secrets Transit",
            description = "Biometrics local authentication, Flutter Secure Storage credentials vault, and direct command security transit proxy.",
            icon = Icons.Default.Lock,
            expanded = expandedSection == 2,
            onClick = { expandedSection = if (expandedSection == 2) null else 2 },
            content = DevOpsData.BLUEPRINT_SECURITY_MD
        )

        BlueprintSectionItem(
            id = 3,
            title = "3. Step-by-Step Milestone Roadmap",
            description = "4 development milestones covering MVP foundation, core AI engines, Observability telemetry, and DevSecOps production hardening.",
            icon = Icons.Default.Route,
            expanded = expandedSection == 3,
            onClick = { expandedSection = if (expandedSection == 3) null else 3 },
            content = DevOpsData.BLUEPRINT_ROADMAP_MD
        )
    }
}

@Composable
fun BlueprintSectionItem(
    id: Int,
    title: String,
    description: String,
    icon: ImageVector,
    expanded: Boolean,
    onClick: () -> Unit,
    content: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() }
            .border(
                1.dp,
                if (expanded) CyberCyan else BorderColor,
                RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(CyberCyan.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                        if (!expanded) {
                            Text(
                                text = description,
                                fontSize = 11.sp,
                                color = SlateText,
                                maxLines = 1,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = SlateText
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider(color = BorderColor, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = content,
                        fontSize = 11.sp,
                        color = GhostWhite,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
    }
}
