package com.example.data

/**
 * Models and default mock data for DevOpsHub AI.
 */

data class PipelineRun(
    val id: String,
    val name: String,
    val repo: String,
    val branch: String,
    val status: String, // "SUCCESS", "FAILED", "RUNNING"
    val duration: String,
    val timestamp: String,
    val errorLogs: String
)

data class K8sPod(
    val name: String,
    val namespace: String,
    val status: String, // "Running", "CrashLoopBackOff", "Pending", "Terminating"
    val restarts: Int,
    val cpu: String,
    val memory: String,
    val logs: String
)

data class CloudResource(
    val name: String,
    val provider: String, // "AWS", "Azure", "GCP"
    val type: String,
    val region: String,
    val monthlyCost: Double,
    val status: String
)

data class SystemAlert(
    val id: String,
    val severity: String, // "CRITICAL", "WARNING", "INFO"
    val service: String,
    val message: String,
    val timestamp: String,
    val details: String
)

data class CodeSnippet(
    val title: String,
    val language: String,
    val code: String
)

object DevOpsData {

    // --- SYSTEM PROMPTS FOR GEMINI ---
    const val SYSTEM_PROMPT_LOG_ANALYZER = """
        You are an expert DevOps engineer and Kubernetes/CI-CD log troubleshooting specialist.
        Your task is to analyze failed build logs or container logs, identify the root cause, and provide a clear, concise, and highly actionable recovery guide.
        Format your response clearly using Markdown:
        1. **Root Cause Analysis**: Explain exactly what went wrong in plain english (e.g. missing dependency, port conflict, database down, permission denied).
        2. **Immediate Solution**: Provide a quick, copy-pasteable command or config change to fix it.
        3. **Long-Term Prevention**: Suggest best practices to prevent this from happening again.
        Keep it professional, concise, and focused on developers.
    """

    const val SYSTEM_PROMPT_IAC_GENERATOR = """
        You are an elite Infrastructure as Code (IaC) specialist, focused strictly on generating valid, production-grade, and secure Terraform configurations.
        When a user types their infrastructure requirements, you must generate a complete, valid, syntactically-correct, and modern Terraform (.tf) code snippet.
        Rules:
        - Output *only* standard, clean, and well-commented Terraform code.
        - Ensure variables are used appropriately if needed.
        - Follow security best practices (e.g. no hardcoded credentials, use secure tags, set restricted inbound security rules).
        - Provide a short explanation *after* the code block explaining what resources are being created.
    """

    const val SYSTEM_PROMPT_FINOPS = """
        You are a Cloud FinOps Optimizer and Cost Control Advisor.
        Analyze the cloud resources and spending report provided and recommend exact, high-impact cost-saving actions.
        Format your response with:
        - **Total Potential Savings**: Estimatated savings in dollars.
        - **Immediate Quick Wins**: Turn off idle resources, clean up unused volumes.
        - **Architectural Adjustments**: Right-sizing recommendations, switching to serverless or Spot instances, GP2 to GP3 conversions.
    """

    const val SYSTEM_PROMPT_SECURITY_SCANNER = """
        You are a DevSecOps automated static analysis scanner (SAST).
        Examine the provided code snippet or repository config for security vulnerabilities, secrets leakage, and compliance violations (OWASP Top 10, CIS Benchmarks).
        Provide a structured assessment report in Markdown:
        1. **Security Score**: A percentage (e.g. 75%) or Grade (A-F).
        2. **Vulnerabilities Found**: Listed by severity (HIGH, MEDIUM, LOW) with line reference or context.
        3. **Proposed Safe Implementation**: Copy-pasteable secure alternative code or instructions on remediation.
    """

    // --- MOCK DATA ---

    val pipelineRuns = listOf(
        PipelineRun(
            id = "run-10492",
            name = "Deploy Production API Gateway",
            repo = "github.com/devopshub/api-gateway",
            branch = "main",
            status = "SUCCESS",
            duration = "4m 12s",
            timestamp = "10 minutes ago",
            errorLogs = "Pipeline successfully ran and deployed gateway. Status 200 OK."
        ),
        PipelineRun(
            id = "run-10491",
            name = "Test and Build Web Frontend",
            repo = "github.com/devopshub/web-portal",
            branch = "feature/auth-refresh",
            status = "FAILED",
            duration = "1m 45s",
            timestamp = "25 minutes ago",
            errorLogs = """
                [INFO] Installing dependencies...
                [INFO] npm run build
                > web-portal@1.0.0 build /workspace
                > tsc && vite build
                
                src/components/AuthContainer.tsx:12:31 - error TS2307: Cannot find module 'react-router-dom' or its corresponding type declarations.
                12 import { useNavigate } from 'react-router-dom';
                                               ~~~~~~~~~~~~~~~~~~
                
                Found 1 error in src/components/AuthContainer.tsx:12
                [ERROR] npm run build failed with exit code 2
                [FATAL] Pipeline execution halted: Build Failure.
            """.trimIndent()
        ),
        PipelineRun(
            id = "run-10490",
            name = "Database Migration Engine",
            repo = "github.com/devopshub/db-engine",
            branch = "main",
            status = "SUCCESS",
            duration = "2m 30s",
            timestamp = "1 hour ago",
            errorLogs = "Database migrations applied successfully. 12 pending scripts executed."
        )
    )

    val k8sPods = listOf(
        K8sPod(
            name = "api-gateway-5f7bc9-x8j2",
            namespace = "production",
            status = "Running",
            restarts = 0,
            cpu = "12m",
            memory = "142Mi",
            logs = "Starting server on port 8080...\nRegistering routes...\nReady for traffic on ingress gateway."
        ),
        K8sPod(
            name = "auth-service-7cdbd9-9kp4",
            namespace = "production",
            status = "CrashLoopBackOff",
            restarts = 5,
            cpu = "0m",
            memory = "12Mi",
            logs = """
                FATAL: Database connection failed!
                dial tcp 10.96.112.54:5432: connect: connection refused
                At connection.go line 45 (auth-service/database)
                Process exited with error code 1.
                [RESTARTING] Backoff 40s
            """.trimIndent()
        ),
        K8sPod(
            name = "redis-cache-82ab7c-j2b7",
            namespace = "production",
            status = "Running",
            restarts = 1,
            cpu = "5m",
            memory = "45Mi",
            logs = "Running mode=standalone, port=6379.\nServer initialized.\n12 clients connected, active background saving disabled."
        ),
        K8sPod(
            name = "payment-worker-22da6f-78h2",
            namespace = "billing",
            status = "Running",
            restarts = 0,
            cpu = "45m",
            memory = "256Mi",
            logs = "Subscribing to queue 'billing.payment.process'...\n[INFO] Connected to RabbitMQ cluster successfully."
        )
    )

    val cloudResources = listOf(
        CloudResource("dev-web-asg-ec2", "AWS", "EC2 Auto-Scaling Group", "us-east-1", 450.00, "Active"),
        CloudResource("prod-rds-postgre-multi-az", "AWS", "RDS PostgreSQL Instance (db.r6g.xlarge)", "us-east-1", 580.00, "Active"),
        CloudResource("unused-temp-ebs-volume", "AWS", "EBS Volume (GP2 500GB)", "us-east-1", 60.00, "Disconnected / Idle"),
        CloudResource("azure-cognitive-search", "Azure", "Azure AI Search Standard", "eastus2", 240.00, "Active (0% current queries)"),
        CloudResource("azure-dev-appservice", "Azure", "App Service Plan (Premium v3)", "eastus2", 180.00, "Active (Unused CPU)"),
        CloudResource("gcp-gke-controlplane", "GCP", "Kubernetes Engine Cluster", "us-central1", 120.00, "Active")
    )

    val systemAlerts = listOf(
        SystemAlert(
            id = "alert-4021",
            severity = "CRITICAL",
            service = "auth-service-7cdbd9",
            message = "Container CrashLoopBackOff: Database Unavailable",
            timestamp = "3m ago",
            details = "Service auth-service failed to start 5 times due to database connection timeout on port 5432. Active clients: 0/100."
        ),
        SystemAlert(
            id = "alert-4020",
            severity = "WARNING",
            service = "unused-temp-ebs-volume",
            message = "Unused Storage Resource: Idle for 14 Days",
            timestamp = "1h ago",
            details = "EBS Volume gp2 (500GB) has recorded 0 write IOPS and < 5 read IOPS over the past 336 hours. Accumulating wasteful costs."
        ),
        SystemAlert(
            id = "alert-4019",
            severity = "INFO",
            service = "production-ingress",
            message = "SSL Certificate Auto-Renewal Triggered",
            timestamp = "2h ago",
            details = "Let's Encrypt automation triggered renewal of devopshub.ai domain. Certificate valid for next 90 days."
        )
    )

    val codeSnippets = listOf(
        CodeSnippet(
            title = "Unsecured Python SQL Execution",
            language = "python",
            code = """
                # Unsecured Flask SQL query
                from flask import request, g
                
                @app.route('/user')
                def get_user():
                    user_id = request.args.get('id')
                    # VULNERABLE: Direct string interpolation bypasses sanitization
                    query = f"SELECT * FROM users WHERE id = '{user_id}'"
                    cursor = g.db.execute(query)
                    return cursor.fetchall()
            """.trimIndent()
        ),
        CodeSnippet(
            title = "Vulnerable Root Dockerfile",
            language = "dockerfile",
            code = """
                # Vulnerable Dockerfile pattern
                FROM node:18-alpine
                WORKDIR /app
                COPY package*.json ./
                RUN npm install
                COPY . .
                EXPOSE 3000
                # VULNERABLE: Running as ROOT user in container!
                # Compliance issue: No USER non-root defined.
                CMD ["node", "server.js"]
            """.trimIndent()
        ),
        CodeSnippet(
            title = "Hardcoded Secrets in Go",
            language = "go",
            code = """
                package main
                import "fmt"
                
                // VULNERABLE: Hardcoded credential strings
                const AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE"
                const AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
                
                func main() {
                    fmt.Println("Initializing connection to S3 using: ", AWS_ACCESS_KEY_ID)
                }
            """.trimIndent()
        )
    )

    // --- BLUEPRINT TEXT ASSETS ---
    val BLUEPRINT_ARCHITECTURE_MD = """
## DevOpsHub AI Mobile Architecture Blueprint

### 1. Unified Clean System Architecture
Below is the modern layered Clean Architecture proposed for the DevOpsHub AI application. By separating UI from API Integration and security mechanisms, we ensure testability and robust operation.

```
       [ PRESENTATION LAYER: Flutter UI ]
    Widgets (Views) <----> BLoC/Cubit (State Controllers)
                         |
                         v
       [ DOMAIN LAYER: Entities & Use Cases ]
    DevOps Services, IaC Generator, Cluster Monitors
                         |
                         v
       [ DATA LAYER: Repository & Sources ]
  Repositories <----> Secure Local DB (Encrypted Floor/Hive)
       |
       +---> Remote APIs:
             - Gemini Pro API (REST / Firebase Vertex AI)
             - GitHub Actions, GitLab CI
             - K8s Dashboard API
             - Cloud Providers (AWS, GCP, Azure)
             - Prometheus / Datadog Webhooks
```

---

### 2. The 6 Pillars Automation Layout
Each pillar interacts asynchronously with external cloud architectures and utilizes the Gemini Pro model to parse logs, suggest scripts, or identify cost saving vectors.

```
+------------------------------------------------------------+
|                       DEVOPSHUB AI                         |
+------------------------------------------------------------+
       |
       +---> Pillar 1: CI/CD Pipeline Monitor (Git API + Logs)
       |
       +---> Pillar 2: Mobile IaC Generator (AI Chat + Terraform)
       |
       +---> Pillar 3: Container & K8s Controller (Kube API + Logs)
       |
       +---> Pillar 4: FinOps Cost Optimizations (AWS/Azure Billing)
       |
       +---> Pillar 5: Observability Webhook Alerts (Prometheus Webhook)
       |
       +---> Pillar 6: DevSecOps Vulnerability Scanner (SAST Engine)
```

---

### 3. Detailed Gemini Pro API Integration Flow
The application formats high-context JSON queries injecting local log files, alerts, or requirements alongside dedicated system instructions to guarantee valid syntax and highly specialized trouble-resolution strategies.

#### a) Troubleshooting Pipeline & Log Analyzer Prompt
When a build fails, the device aggregates the error logs, current pipeline configuration, and submits them:
```json
{
  "contents": [{ "parts": [{ "text": "FAILED LOGS:\n[ERROR] npm run build failed...\nHow do I fix this?" }] }],
  "generationConfig": { "temperature": 0.2 },
  "systemInstruction": { "parts": [{ "text": "You are a Kubernetes/CI-CD log troubleshooting specialist..." }] }
}
```

#### b) NL to Terraform Generator Prompt
We enforce a structured scheme for prompt framing so Gemini restricts output to pure, syntax-clean configurations:
```json
{
  "contents": [{ "parts": [{ "text": "Create a 3-tier AWS VPC with EC2 instances..." }] }],
  "generationConfig": { "temperature": 0.1 },
  "systemInstruction": { "parts": [{ "text": "You are an elite Terraform generator. Output ONLY clean, commented TF code..." }] }
}
```
    """.trimIndent()

    val BLUEPRINT_SECURITY_MD = """
## 4. Mobile Security & Secrets Transit

To securely operate cloud commands from a mobile device without exposing critical credentials, we implement a strict **Two-Tier Security & Transit Architecture**:

### Device-Level Data Encryption
Any stored credentials like **AWS Access Keys**, **Git Tokens**, or **Kubeconfig** are never written to plain shared preferences. Instead:
- **Biometric Integration**: Access to the secrets vault is guarded via FaceID/Fingerprint triggers using Local Authentication.
- **Flutter Secure Storage**: Data is saved into platform-native keychain stores (iOS Keychain Services / Android Keystore API) using AES-256 GCM encryption.

### Multi-Cloud Secrets Architecture Diagram
```
  [ Mobile Device Secure Storage ]
           | (Biometric OK)
           v
  AES-256 Local Encrypted Payload
           |
           | TLS 1.3 Pinning / HTTPS
           v
  [ Secure DevOps API Gateway Proxy ] 
           |
           | (Verifies App Check Signature)
           +----> Inject Provider Access token
           +----> Proxy to AWS / GCP / K8s
```

### Transit Security & Direct Execution Prevention
Direct device-to-provider API calls with root credentials are heavily discouraged in production. The app routes critical infrastructure commands through a **Secure DevOps API Gateway Proxy** (e.g. Node.js or Go server protected by Firebase App Check). This gateway:
1. Validates the mobile app's cryptographic integrity signature.
2. Exchanges single-use scoped OAuth tokens.
3. Performs requested actions and streams sanitized logs back, keeping permanent admin tokens completely off the device.
    """.trimIndent()

    val BLUEPRINT_ROADMAP_MD = """
## 5. Development Roadmap: Milestone Plan

### Milestone 1: Foundation & DevOps Dashboard (Weeks 1-3)
- Scaffold Flutter architecture utilizing Riverpod for global state.
- Implement system theming (DevOps terminal-dark UI).
- Setup Flutter Secure Storage and native biometric locks.
- Establish the mock cloud provider interfaces and K8s dashboard feeds.

### Milestone 2: Core AI Engine & IaC Chat (Weeks 4-6)
- Integrate Gemini Pro REST API via direct HTTPS with connection pooling.
- Build the "Mobile IaC Generator" chat interface.
- Implement the "CI/CD Pipeline Monitor" with GitHub webhooks, enabling log analysis.
- Create local offline persistence for historical generated code configurations using Room / SQLite.

### Milestone 3: Observability & Cloud Operations (Weeks 7-9)
- Implement multi-cloud cost analytics utilizing local drawing engines.
- Build "Container & K8s Controller" logs terminal view, allowing interactive pod restarts.
- Write background task listener for webhook push alerts.
- Build the DevSecOps automated code scanning interface.

### Milestone 4: DevSecOps & Production Hardening (Weeks 10-12)
- Implement SSL Pinning on all remote gateway calls.
- Enforce full Firebase App Check and enterprise authentication controls.
- Conduct automated vulnerability audits on code parsing features.
- Package app and release to Apple App Store (TestFlight) & Google Play Console.
    """.trimIndent()
}
