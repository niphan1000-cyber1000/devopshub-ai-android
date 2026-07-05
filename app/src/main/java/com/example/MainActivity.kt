package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BlueprintScreen
import com.example.ui.DevOpsDashboard
import com.example.ui.PillarScreenContainer
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    // Top-level Navigation & Interactive App State
    var currentTab by remember { mutableStateOf(0) } // 0 = Dashboard, 1 = Spec Blueprint
    var selectedPillarId by remember { mutableStateOf<Int?>(null) }
    var showWarningBanner by remember { mutableStateOf(true) }

    // Reactive State shared across screens
    var activeBuildsCount by remember { mutableStateOf(1) }
    var unhealthyPodsCount by remember { mutableStateOf(1) }
    var totalMonthlyCost by remember { mutableStateOf(1430.00) }
    var activeAlertsCount by remember { mutableStateOf(1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (selectedPillarId == null) {
                NavigationBar(
                    containerColor = ObsidianBlack,
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(0.5.dp, BorderColor, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                        label = { Text("Core Hub", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = CyberCyan,
                            indicatorColor = CyberCyan,
                            unselectedIconColor = SlateText,
                            unselectedTextColor = SlateText
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        icon = { Icon(Icons.Default.Layers, contentDescription = "Blueprint Specs") },
                        label = { Text("Blueprint Spec", fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = CyberCyan,
                            indicatorColor = CyberCyan,
                            unselectedIconColor = SlateText,
                            unselectedTextColor = SlateText
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .padding(innerPadding)
        ) {
            if (selectedPillarId != null) {
                // Individual Pillar Screen Detail Container
                PillarScreenContainer(
                    pillarId = selectedPillarId!!,
                    onBack = {
                        // Check if states need to update based on simulator activity
                        if (selectedPillarId == 3) {
                            // If K8s controller was opened, assume user might have restarted auth pod
                            unhealthyPodsCount = 0
                        }
                        if (selectedPillarId == 5) {
                            // If Alerts was opened, assume user might have auto-resolved
                            activeAlertsCount = 0
                        }
                        selectedPillarId = null
                    },
                    onCostUpdated = { newCost ->
                        totalMonthlyCost = newCost
                    },
                    totalMonthlyCost = totalMonthlyCost
                )
            } else {
                // Dashboard or Blueprint Specs Tab view
                Column(modifier = Modifier.fillMaxSize()) {
                    // Critical Secret Management Warning Banner (Mandated)
                    if (showWarningBanner) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .border(1.dp, HotCoral.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(containerColor = HotCoral.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning",
                                    tint = HotCoral,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "SECURITY WARNING",
                                        color = HotCoral,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Security Warning: I have included your API keys in the generated APK file for this prototype. Please be aware that Android APKs can be easily decompiled, and these keys can be extracted by anyone who has access to the file. Do not share this APK file publicly or with unauthorized individuals to prevent potential misuse.",
                                        color = GhostWhite,
                                        fontSize = 10.sp,
                                        lineHeight = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "I UNDERSTAND",
                                        color = CyberCyan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier
                                            .clickable { showWarningBanner = false }
                                            .testTag("dismiss_warning_button")
                                    )
                                }
                            }
                        }
                    }

                    if (currentTab == 0) {
                        DevOpsDashboard(
                            onPillarSelected = { id -> selectedPillarId = id },
                            activeBuildsCount = activeBuildsCount,
                            unhealthyPodsCount = unhealthyPodsCount,
                            totalMonthlyCost = totalMonthlyCost,
                            activeAlertsCount = activeAlertsCount
                        )
                    } else {
                        BlueprintScreen()
                    }
                }
            }
        }
    }
}
