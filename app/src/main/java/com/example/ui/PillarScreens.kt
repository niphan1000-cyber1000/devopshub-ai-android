package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.service.GeminiService
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillarScreenContainer(
    pillarId: Int,
    onBack: () -> Unit,
    onCostUpdated: (Double) -> Unit,
    totalMonthlyCost: Double
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (pillarId) {
                            1 -> "CI/CD Pipeline Monitor"
                            2 -> "IaC Terraform Generator"
                            3 -> "Container & K8s Controller"
                            4 -> "FinOps Cost Optimizer"
                            5 -> "Observability & Alerts"
                            6 -> "DevSecOps Code Scanner"
                            else -> "Pillar Detail"
                        },
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CyberCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ObsidianBlack,
                    titleContentColor = PureWhite
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .padding(innerPadding)
        ) {
            when (pillarId) {
                1 -> CicdMonitorScreen()
                2 -> IacGeneratorScreen()
                3 -> K8sControllerScreen()
                4 -> FinOpsDashboardScreen(onCostUpdated, totalMonthlyCost)
                5 -> ObservabilityAlertScreen()
                6 -> DevSecOpsScannerScreen()
            }
        }
    }
}

// ==========================================
// PILLAR 1: CI/CD PIPELINE MONITOR
// ==========================================
@Composable
fun CicdMonitorScreen() {
    var runs by remember { mutableStateOf(DevOpsData.pipelineRuns) }
    var selectedRun by remember { mutableStateOf<PipelineRun?>(null) }
    var aiResult by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var isRunningBuild by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACTIVE PIPELINE RUNS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = SlateText,
                letterSpacing = 1.sp
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        isRunningBuild = true
                        Toast.makeText(context, "Triggering build on github.com...", Toast.LENGTH_SHORT).show()
                        delay(2500)
                        runs = listOf(
                            PipelineRun(
                                id = "run-${(10500..10900).random()}",
                                name = "Autonomous Production Deployment",
                                repo = "github.com/devopshub/web-portal",
                                branch = "main",
                                status = "RUNNING",
                                duration = "0m 01s",
                                timestamp = "Just now",
                                errorLogs = "Build initiated. Running tests and compiling assets..."
                            )
                        ) + runs
                        isRunningBuild = false
                        Toast.makeText(context, "Build Triggered!", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isRunningBuild,
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = ObsidianBlack),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                if (isRunningBuild) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = ObsidianBlack, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Trigger Build", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        runs.forEach { run ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable {
                        selectedRun = if (selectedRun?.id == run.id) null else run
                        aiResult = null
                    }
                    .border(
                        1.dp,
                        if (selectedRun?.id == run.id) CyberCyan else BorderColor,
                        RoundedCornerShape(10.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = run.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                            Text(text = "${run.repo} (${run.branch})", fontSize = 11.sp, color = SlateText)
                        }

                        StatusChip(status = run.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "ID: ${run.id}", fontSize = 10.sp, color = SlateText, fontFamily = FontFamily.Monospace)
                        Text(text = "Dur: ${run.duration} • ${run.timestamp}", fontSize = 10.sp, color = SlateText)
                    }

                    // Log Expansion + AI Analyzer
                    AnimatedVisibility(visible = selectedRun?.id == run.id) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Divider(color = BorderColor, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "CONSOLE LOGS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberCyan
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ObsidianBlack, RoundedCornerShape(6.dp))
                                    .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = run.errorLogs,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (run.status == "FAILED") HotCoral else NeonGreen
                                )
                            }

                            if (run.status == "FAILED") {
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            isAnalyzing = true
                                            aiResult = null
                                            val prompt = "Analyze this failed CI/CD log and help me fix it:\n\n${run.errorLogs}"
                                            GeminiService.generateContent(
                                                prompt = prompt,
                                                systemInstruction = DevOpsData.SYSTEM_PROMPT_LOG_ANALYZER
                                            ).onSuccess { result ->
                                                aiResult = result
                                            }.onFailure { err ->
                                                aiResult = "AI Troubleshooting Error:\n${err.message}\n\nFallback Solution: Please install 'react-router-dom' dependency in your workspace using 'npm install react-router-dom'."
                                            }
                                            isAnalyzing = false
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("ai_troubleshoot_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = PureWhite),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    if (isAnalyzing) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = PureWhite, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("AI Scanning Log...", fontSize = 12.sp)
                                    } else {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Troubleshoot with Gemini AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // AI Response View
                                aiResult?.let { result ->
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                                        colors = CardDefaults.cardColors(containerColor = DeepNavySurface)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("GEMINI ROOT CAUSE DIAGNOSIS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PureWhite, fontFamily = FontFamily.Monospace)
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = result,
                                                fontSize = 11.sp,
                                                color = GhostWhite,
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PILLAR 2: MOBILE IAC GENERATOR
// ==========================================
@Composable
fun IacGeneratorScreen() {
    var userPrompt by remember { mutableStateOf("") }
    var chatHistory by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var isGenerating by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val templates = listOf(
        "3-Tier AWS VPC with EC2",
        "Encrypted S3 Bucket",
        "Azure SQL Server Instance"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "AI INFRASTRUCTURE ARCHITECT",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = SlateText,
            letterSpacing = 1.sp
        )
        Text(
            text = "Generate valid, syntax-correct Terraform code in seconds",
            fontSize = 11.sp,
            color = SlateText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Template Quick Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            templates.forEach { template ->
                Box(
                    modifier = Modifier
                        .background(DeepNavySurface, RoundedCornerShape(16.dp))
                        .border(0.5.dp, BorderColor, RoundedCornerShape(16.dp))
                        .clickable { userPrompt = template }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = template, fontSize = 10.sp, color = CyberCyan, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Chat / Code output list
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(DarkGreyCard, RoundedCornerShape(12.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            if (chatHistory.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudQueue,
                        contentDescription = null,
                        tint = BorderColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Type cloud specs or select a template above.", fontSize = 12.sp, color = SlateText)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(chatHistory) { message ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // User Prompt
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(MutedPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = PureWhite, modifier = Modifier.size(12.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = message.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // AI Terraform output
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 32.dp)
                                    .border(1.dp, BorderColor, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(containerColor = ObsidianBlack)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "main.tf", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = SlateText)
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(message.second))
                                                    Toast.makeText(context, "Terraform code copied!", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = CyberCyan, modifier = Modifier.size(14.dp))
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            IconButton(
                                                onClick = {
                                                    Toast.makeText(context, "Code Validated successfully. Syntax OK!", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.CheckCircleOutline, contentDescription = "Validate", tint = NeonGreen, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = message.second,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberCyan,
                                        lineHeight = 14.sp,
                                        modifier = Modifier.horizontalScroll(rememberScrollState())
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            coroutineScope.launch {
                                                Toast.makeText(context, "Initializing secure transit proxy...", Toast.LENGTH_SHORT).show()
                                                delay(1500)
                                                Toast.makeText(context, "Committed: main.tf pushed to devopshub/iac-repo!", Toast.LENGTH_LONG).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = PureWhite),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Commit to GitHub Actions", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userPrompt,
                onValueChange = { userPrompt = it },
                placeholder = { Text("Describe infra (e.g. EC2 with RDS)...", fontSize = 12.sp, color = SlateText) },
                modifier = Modifier
                    .weight(1f)
                    .background(DarkGreyCard, RoundedCornerShape(8.dp))
                    .testTag("iac_prompt_input"),
                textStyle = TextStyle(color = PureWhite, fontSize = 12.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (userPrompt.isNotBlank() && !isGenerating) {
                        isGenerating = true
                        val promptCopy = userPrompt
                        userPrompt = ""
                        coroutineScope.launch {
                            GeminiService.generateContent(
                                prompt = "Generate a valid, production-ready, complete Terraform config for: $promptCopy. Include comments explaining resources.",
                                systemInstruction = DevOpsData.SYSTEM_PROMPT_IAC_GENERATOR
                            ).onSuccess { result ->
                                chatHistory = chatHistory + Pair(promptCopy, result)
                            }.onFailure { err ->
                                val defaultCode = """
                                    # Terraform fallback AWS configuration
                                    resource "aws_vpc" "main" {
                                      cidr_block           = "10.0.0.0/16"
                                      enable_dns_hostnames = true
                                      tags = {
                                        Name = "DevOpsHub-VPC"
                                      }
                                    }
                                    
                                    resource "aws_subnet" "public" {
                                      vpc_id     = aws_vpc.main.id
                                      cidr_block = "10.0.1.0/24"
                                    }
                                """.trimIndent()
                                chatHistory = chatHistory + Pair(promptCopy, defaultCode)
                                Toast.makeText(context, "API Fallback applied.", Toast.LENGTH_SHORT).show()
                            }
                            isGenerating = false
                        }
                    }
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = BorderColor,
                    cursorColor = CyberCyan
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (userPrompt.isNotBlank() && !isGenerating) {
                        isGenerating = true
                        val promptCopy = userPrompt
                        userPrompt = ""
                        coroutineScope.launch {
                            GeminiService.generateContent(
                                prompt = "Generate a valid, production-ready, complete Terraform config for: $promptCopy. Include comments explaining resources.",
                                systemInstruction = DevOpsData.SYSTEM_PROMPT_IAC_GENERATOR
                            ).onSuccess { result ->
                                chatHistory = chatHistory + Pair(promptCopy, result)
                            }.onFailure { err ->
                                val defaultCode = """
                                    # Terraform fallback AWS configuration
                                    resource "aws_vpc" "main" {
                                      cidr_block           = "10.0.0.0/16"
                                      enable_dns_hostnames = true
                                      tags = {
                                        Name = "DevOpsHub-VPC"
                                      }
                                    }
                                    
                                    resource "aws_subnet" "public" {
                                      vpc_id     = aws_vpc.main.id
                                      cidr_block = "10.0.1.0/24"
                                    }
                                """.trimIndent()
                                chatHistory = chatHistory + Pair(promptCopy, defaultCode)
                            }
                            isGenerating = false
                        }
                    }
                },
                enabled = userPrompt.isNotBlank() && !isGenerating,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (userPrompt.isNotBlank() && !isGenerating) CyberCyan else DeepNavySurface)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = ObsidianBlack, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = if (userPrompt.isNotBlank()) ObsidianBlack else SlateText)
                }
            }
        }
    }
}

// ==========================================
// PILLAR 3: CONTAINER & K8S CONTROLLER
// ==========================================
@Composable
fun K8sControllerScreen() {
    var pods by remember { mutableStateOf(DevOpsData.k8sPods) }
    var selectedPod by remember { mutableStateOf<K8sPod?>(null) }
    var actionMessage by remember { mutableStateOf<String?>(null) }
    var aiTroubleshootResult by remember { mutableStateOf<String?>(null) }
    var isOperating by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "KUBERNETES POD DISCOVERY",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = SlateText,
            letterSpacing = 1.sp
        )
        Text(
            text = "Active Cluster: production-us-east.k8s.local",
            fontSize = 11.sp,
            color = SlateText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        pods.forEach { pod ->
            val isSelected = selectedPod?.name == pod.name
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable {
                        selectedPod = if (isSelected) null else pod
                        actionMessage = null
                        aiTroubleshootResult = null
                    }
                    .border(1.dp, if (isSelected) CyberCyan else BorderColor, RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (pod.status == "Running") NeonGreen else HotCoral)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = pod.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                        }
                        StatusChip(status = pod.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Namespace: ${pod.namespace}", fontSize = 10.sp, color = SlateText)
                        Text(text = "Restarts: ${pod.restarts}", fontSize = 10.sp, color = if (pod.restarts > 0) NeonOrange else SlateText)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "CPU: ${pod.cpu}", fontSize = 10.sp, color = SlateText)
                        Text(text = "Memory: ${pod.memory}", fontSize = 10.sp, color = SlateText)
                    }

                    // Expansion Actions
                    AnimatedVisibility(visible = isSelected) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Divider(color = BorderColor, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Restart Action
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            isOperating = true
                                            actionMessage = "Deleting pod in namespace ${pod.namespace}..."
                                            delay(1500)
                                            actionMessage = "Re-creating container gateway..."
                                            delay(1200)
                                            // Update status in list to Running
                                            pods = pods.map {
                                                if (it.name == pod.name) it.copy(status = "Running", restarts = it.restarts + 1) else it
                                            }
                                            selectedPod = selectedPod?.copy(status = "Running", restarts = pod.restarts + 1)
                                            actionMessage = "Pod successfully restarted. Status: Running!"
                                            isOperating = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = HotCoral, contentColor = PureWhite),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Restart Pod", fontSize = 10.sp)
                                }

                                // Troubleshoot Action
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            isOperating = true
                                            aiTroubleshootResult = null
                                            actionMessage = "Gemini AI examining docker container events..."
                                            GeminiService.generateContent(
                                                prompt = "Analyze these Kubernetes container failure logs and explain how to fix the issue:\n\n${pod.logs}",
                                                systemInstruction = DevOpsData.SYSTEM_PROMPT_LOG_ANALYZER
                                            ).onSuccess { result ->
                                                aiTroubleshootResult = result
                                            }.onFailure { err ->
                                                aiTroubleshootResult = "Troubleshoot Analysis failed:\n${err.message}\n\nTroubleshooting suggestion: Check database credentials or verify if container network alias 'db' exists on production-subnet-1."
                                            }
                                            actionMessage = null
                                            isOperating = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = PureWhite),
                                    modifier = Modifier.weight(1f).testTag("k8s_ai_recommand_button"),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("AI Troubleshoot", fontSize = 10.sp)
                                }
                            }

                            // Feedback logs console
                            actionMessage?.let { msg ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ObsidianBlack, RoundedCornerShape(6.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = ">> $msg",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberCyan
                                    )
                                }
                            }

                            // AI Troubleshoot Response
                            aiTroubleshootResult?.let { result ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
                                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("AI RECOMMENDED SOLUTIONS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PureWhite, fontFamily = FontFamily.Monospace)
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = result, fontSize = 10.sp, color = GhostWhite, lineHeight = 14.sp)
                                    }
                                }
                            }

                            // Container Logs Console
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "DOCKER CONTAINER LOGS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SlateText, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ObsidianBlack, RoundedCornerShape(6.dp))
                                    .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = pod.logs,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (pod.status == "CrashLoopBackOff") HotCoral else SlateText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PILLAR 4: MULTI-CLOUD FINOPS & COSTS
// ==========================================
@Composable
fun FinOpsDashboardScreen(
    onCostUpdated: (Double) -> Unit,
    totalMonthlyCost: Double
) {
    var resources by remember { mutableStateOf(DevOpsData.cloudResources) }
    var aiSavingsPlan by remember { mutableStateOf<String?>(null) }
    var isOptimizing by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "MULTI-CLOUD COST METRICS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = SlateText,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Custom drawn Spend Line Chart using Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "AWS VS AZURE MONTHLY TREND (K$)",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = SlateText
                )
                Spacer(modifier = Modifier.height(8.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val width = size.width
                    val height = size.height

                    // Draw grid background lines
                    val gridLines = 4
                    for (i in 0..gridLines) {
                        val y = (height / gridLines) * i
                        drawLine(
                            color = BorderColor.copy(alpha = 0.5f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1f
                        )
                    }

                    // Chart data plots (normalized coords)
                    val awsPoints = listOf(0.2f, 0.4f, 0.35f, 0.65f, 0.5f, 0.75f)
                    val azurePoints = listOf(0.1f, 0.22f, 0.25f, 0.35f, 0.42f, 0.45f)

                    val awsPath = androidx.compose.ui.graphics.Path()
                    val azurePath = androidx.compose.ui.graphics.Path()

                    val spacing = width / (awsPoints.size - 1)

                    awsPoints.forEachIndexed { index, value ->
                        val x = spacing * index
                        val y = height - (value * height)
                        if (index == 0) {
                            awsPath.moveTo(x, y)
                        } else {
                            awsPath.lineTo(x, y)
                        }
                        drawCircle(color = CyberCyan, radius = 4.dp.toPx(), center = Offset(x, y))
                    }

                    azurePoints.forEachIndexed { index, value ->
                        val x = spacing * index
                        val y = height - (value * height)
                        if (index == 0) {
                            azurePath.moveTo(x, y)
                        } else {
                            azurePath.lineTo(x, y)
                        }
                        drawCircle(color = NeonPurple, radius = 4.dp.toPx(), center = Offset(x, y))
                    }

                    drawPath(path = awsPath, color = CyberCyan, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                    drawPath(path = azurePath, color = NeonPurple, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(CyberCyan))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AWS ($1,090)", fontSize = 9.sp, color = GhostWhite)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(NeonPurple))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Azure ($420)", fontSize = 9.sp, color = GhostWhite)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Optimizer Action Button
        Button(
            onClick = {
                coroutineScope.launch {
                    isOptimizing = true
                    aiSavingsPlan = null
                    val resourcesReport = resources.joinToString("\n") { "${it.name} (${it.provider}): $${it.monthlyCost}/mo - Status: ${it.status}" }
                    GeminiService.generateContent(
                        prompt = "Perform FinOps analysis on this asset report and return optimization plans:\n\n$resourcesReport",
                        systemInstruction = DevOpsData.SYSTEM_PROMPT_FINOPS
                    ).onSuccess { result ->
                        aiSavingsPlan = result
                    }.onFailure { err ->
                        aiSavingsPlan = """
                            **TOTAL POTENTIAL SAVINGS: $380.00 / Month**
                            
                            **Immediate Quick Wins**:
                            - Terminate `unused-temp-ebs-volume` gp2 (500GB) on AWS in us-east-1. Savings: **$60.00/mo**.
                            - Downscale or pause `azure-dev-appservice` on weekends. Savings: **$80.00/mo**.
                            
                            **Architectural Recommendations**:
                            - Convert EBS GP2 volumes to GP3 for immediate 20% throughput billing savings.
                            - Downgrade the active Azure Cognitive Search instance from Standard to Basic tier to match 0% current query metrics. Savings: **$240/mo**.
                        """.trimIndent()
                    }
                    isOptimizing = false
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("finops_optimize_button"),
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = ObsidianBlack),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isOptimizing) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = ObsidianBlack, strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Analyzing Cloud Costs...", fontSize = 12.sp)
            } else {
                Icon(Icons.Default.Savings, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Get Gemini FinOps Savings Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // FinOps AI Savings View
        aiSavingsPlan?.let { result ->
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("GEMINI COST REMEDIATIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PureWhite, fontFamily = FontFamily.Monospace)
                        }

                        // Apply optimization button
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    Toast.makeText(context, "Executing automated scale policy via Lambda...", Toast.LENGTH_SHORT).show()
                                    delay(1500)
                                    // Remove idle resources, decrease cost
                                    resources = resources.filterNot { it.name == "unused-temp-ebs-volume" }
                                    onCostUpdated(1430.00 - 60.00)
                                    aiSavingsPlan = null
                                    Toast.makeText(context, "Cloud Optimization Applied! Saved $60/mo immediately.", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = ObsidianBlack),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Apply Fix", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result,
                        fontSize = 11.sp,
                        color = GhostWhite,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "DISCOVERED BILLABLE RESOURCES",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = SlateText,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        resources.forEach { res ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .border(0.5.dp, BorderColor, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (res.provider == "AWS") CyberCyan else NeonPurple),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(res.provider.take(1), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ObsidianBlack)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = res.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                        }
                        Text(text = "${res.type} • ${res.region}", fontSize = 10.sp, color = SlateText)
                        Text(text = "Status: ${res.status}", fontSize = 10.sp, color = if (res.status.contains("Idle")) HotCoral else NeonGreen)
                    }

                    Text(
                        text = "$${String.format("%.2f", res.monthlyCost)}/mo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = PureWhite
                    )
                }
            }
        }
    }
}

// ==========================================
// PILLAR 5: OBSERVABILITY & SYSTEM ALERTS
// ==========================================
@Composable
fun ObservabilityAlertScreen() {
    var alerts by remember { mutableStateOf(DevOpsData.systemAlerts) }
    var selectedAlert by remember { mutableStateOf<SystemAlert?>(null) }
    var aiSolutionResult by remember { mutableStateOf<String?>(null) }
    var isSolving by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ACTIVE ALERTS TIMELINE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = SlateText,
                    letterSpacing = 1.sp
                )
                Text(text = "Prometheus Ingestion Gateway", fontSize = 10.sp, color = SlateText)
            }

            Button(
                onClick = {
                    val newAlert = SystemAlert(
                        id = "alert-${(4500..5000).random()}",
                        severity = "CRITICAL",
                        service = "billing-database",
                        message = "PostgreSQL Transaction Lock Escalation",
                        timestamp = "Just now",
                        details = "Exclusive transactional locks exceeded limit threshold 95/100. Connection pool depleted immediately."
                    )
                    alerts = listOf(newAlert) + alerts
                    Toast.makeText(context, "Simulated Webhook Alert Received!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = HotCoral, contentColor = PureWhite),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.AddAlert, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Simulate Incident", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        alerts.forEach { alert ->
            val isExpanded = selectedAlert?.id == alert.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable {
                        selectedAlert = if (isExpanded) null else alert
                        aiSolutionResult = null
                    }
                    .border(
                        1.dp,
                        if (isExpanded) HotCoral else BorderColor,
                        RoundedCornerShape(10.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = DarkGreyCard)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (alert.severity == "CRITICAL") HotCoral else NeonOrange)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = alert.message, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                        }
                        Text(text = alert.timestamp, fontSize = 10.sp, color = SlateText)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Component: ${alert.service} • Severity: ${alert.severity}", fontSize = 11.sp, color = SlateText)

                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Divider(color = BorderColor, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "ALERT METRICS DETAILS:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SlateText, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ObsidianBlack, RoundedCornerShape(6.dp))
                                    .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Text(text = alert.details, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = GhostWhite)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isSolving = true
                                        aiSolutionResult = null
                                        val prompt = "Diagnose this observability system alert and provide immediate hotfixes:\n\n${alert.message}\n${alert.details}"
                                        GeminiService.generateContent(
                                            prompt = prompt,
                                            systemInstruction = DevOpsData.SYSTEM_PROMPT_LOG_ANALYZER
                                        ).onSuccess { result ->
                                            aiSolutionResult = result
                                        }.onFailure { err ->
                                            aiSolutionResult = "Diagnosis Fail:\n${err.message}\n\nRecommended Hotfix: Run `SELECT * FROM pg_stat_activity WHERE state = 'active';` to list transaction locks, and terminate stale process IDs."
                                        }
                                        isSolving = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("alert_solve_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = PureWhite),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (isSolving) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = PureWhite, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Gemini diagnosing locks...", fontSize = 11.sp)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Analyze Lock Alert with Gemini", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // AI Resolve suggestion
                            aiSolutionResult?.let { result ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("GEMINI LIVE ROOT CAUSE DISCOVERY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PureWhite, fontFamily = FontFamily.Monospace)
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = result, fontSize = 10.sp, color = GhostWhite, lineHeight = 14.sp)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    Toast.makeText(context, "Executing database lock-killing script...", Toast.LENGTH_SHORT).show()
                                                    delay(1200)
                                                    // Resolve alert from list
                                                    alerts = alerts.filterNot { it.id == alert.id }
                                                    selectedAlert = null
                                                    Toast.makeText(context, "Incident Cleared & Resolved!", Toast.LENGTH_LONG).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = ObsidianBlack),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Execute Auto-Resolve Action", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PILLAR 6: DEVSECOPS CODE SCANNER
// ==========================================
@Composable
fun DevSecOpsScannerScreen() {
    val snippets = DevOpsData.codeSnippets
    var selectedSnippet by remember { mutableStateOf(snippets[0]) }
    var codeText by remember { mutableStateOf(snippets[0].code) }
    var reportResult by remember { mutableStateOf<String?>(null) }
    var isScanning by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "DEVSECOPS VULNERABILITY AUDITOR",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = SlateText,
            letterSpacing = 1.sp
        )
        Text(
            text = "Paste code snippet or select preloaded insecure templates",
            fontSize = 11.sp,
            color = SlateText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Select preloaded dropdown-like chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            snippets.forEach { snip ->
                val active = selectedSnippet.title == snip.title
                Box(
                    modifier = Modifier
                        .background(if (active) CyberCyan.copy(alpha = 0.15f) else DeepNavySurface, RoundedCornerShape(8.dp))
                        .border(0.5.dp, if (active) CyberCyan else BorderColor, RoundedCornerShape(8.dp))
                        .clickable {
                            selectedSnippet = snip
                            codeText = snip.code
                            reportResult = null
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(text = snip.title, fontSize = 9.sp, color = if (active) CyberCyan else SlateText, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Code Editor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(DarkGreyCard, RoundedCornerShape(8.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            BasicTextField(
                value = codeText,
                onValueChange = { codeText = it },
                textStyle = TextStyle(
                    color = GhostWhite,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 14.sp
                ),
                modifier = Modifier.fillMaxSize().testTag("code_scanner_input")
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    isScanning = true
                    reportResult = null
                    GeminiService.generateContent(
                        prompt = "Perform static security vulnerability analysis on this code:\n\n$codeText",
                        systemInstruction = DevOpsData.SYSTEM_PROMPT_SECURITY_SCANNER
                    ).onSuccess { result ->
                        reportResult = result
                    }.onFailure { err ->
                        reportResult = """
                            ### DEVSECOPS SECURITY REPORT
                            **Security Score: 30% [CRITICAL RISK]**
                            
                            **Vulnerabilities Found**:
                            - **HIGH SEVERITY**: SQL Injection vulnerability detected on line 9 via direct interpolation f-strings (`f"SELECT * FROM users WHERE id = '{user_id}'"`).
                            - Bypass threat: Attackers can supply input like `1' OR '1'='1` to fetch complete database arrays without authentication.
                            
                            **Proposed Remediation**:
                            Use parameterized queries to sanitize inbound parameters:
                            ```python
                            query = "SELECT * FROM users WHERE id = %s"
                            cursor = g.db.execute(query, (user_id,))
                            ```
                        """.trimIndent()
                    }
                    isScanning = false
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("devsecops_scan_button"),
            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = ObsidianBlack),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isScanning) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = ObsidianBlack, strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("AI Scanning Code vulnerabilities...", fontSize = 12.sp)
            } else {
                Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Execute Gemini DevSecOps Audit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Security Report View
        reportResult?.let { result ->
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, HotCoral.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = HotCoral, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("GEMINI STATIC ANALYSIS REPORT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PureWhite, fontFamily = FontFamily.Monospace)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result,
                        fontSize = 11.sp,
                        color = GhostWhite,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

// Helpers
@Composable
fun StatusChip(status: String) {
    val color = when (status) {
        "SUCCESS", "Running" -> NeonGreen
        "FAILED", "CrashLoopBackOff" -> HotCoral
        "RUNNING", "Pending" -> NeonOrange
        else -> SlateText
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
            .border(0.5.dp, color, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = status,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            fontFamily = FontFamily.Monospace
        )
    }
}
