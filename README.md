<div align="center">
<img width="1200" height="475" alt="DevOpsHub AI Banner" src="assets/devops_hub_banner.jpg" />
</div>

# DevOpsHub AI

[![Build Status](https://img.shields.io/badge/Build-Succeeded-brightgreen.svg)](#)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-M3-purple.svg)](https://developer.android.com/jetpack/compose)
[![API Level](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com/studio/releases/platforms)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

An Android DevOps assistant built with **Jetpack Compose (Material 3)**, powered by the **Gemini API**. DevOpsHub AI brings six core "pillars" of cloud infrastructure, security, and operations into a single cohesive mobile dashboard.

This application is designed with **MVVM architecture**, using native Material 3 styling, Edge-to-Edge capabilities, local mock database state machines, and direct integration with Gemini REST services for intelligent root-cause analysis, IaC generation, cluster troubleshooting, and security remediation.

---

## 🚀 Key Features (The 6 Pillars)

DevOpsHub AI aggregates the daily tasks of modern SREs and DevOps Engineers into interactive modules:

1. **CI/CD Pipeline Monitor** — View pipeline runs, trigger simulated builds, and get automated, highly accurate root-cause analysis on failed builds.
2. **IaC Terraform Generator** — Describe infrastructure in plain English and instantly generate production-ready, syntax-correct, and secure Terraform configurations.
3. **Container & K8s Controller** — Inspect pod lists, check runtime status, fetch log streams, and use Gemini to diagnose and troubleshoot crashes like `CrashLoopBackOff`.
4. **FinOps Cost Optimizer** — View cloud resources, analyze multi-cloud cost matrices, and execute automated cleanup strategies (such as purging orphaned EBS volumes) to save budget.
5. **Observability & Alerts** — Review active system alerts and incidents with AI-driven triage and suggested manual or automated hotfixes.
6. **DevSecOps Code Scanner** — Paste raw code snippets to scan for vulnerabilities (e.g., hardcoded credentials, injection risks) and generate secure compliance remediations.

---

## 🛠️ Tech Stack & Architecture

DevOpsHub AI is built with modern Android guidelines, ensuring speed, stability, and ease of maintainability.

```
       ┌────────────────────────────────────────────────────────┐
       │                  Jetpack Compose UI                    │
       │   (Dashboard, Detail Screens, Spec Blueprint Viewer)   │
       └───────────────────────────┬────────────────────────────┘
                                   │
                                   ▼
       ┌────────────────────────────────────────────────────────┐
       │                State Management & VM                   │
       │    (Kotlin Flow, StateFlow, Coroutines, State Share)    │
       └───────────────────────────┬────────────────────────────┘
                                   │
                     ┌─────────────┴─────────────┐
                     ▼                           ▼
       ┌───────────────────────────┐   ┌───────────────────────────┐
       │       Gemini Service      │   │     Local Cache / Data    │
       │  (REST API Client, OkHttp)│   │  (DevOpsData Data Store)  │
       └─────────────┬─────────────┘   └───────────────────────────┘
                     │
                     ▼
       ┌───────────────────────────┐
       │     Google Gemini API     │
       │    (gemini-1.5-flash)     │
       └───────────────────────────┘
```

- **Language**: Kotlin (100% Type-safe)
- **UI Framework**: Jetpack Compose (Material 3, Dynamic Colors, Edge-to-Edge via `enableEdgeToEdge()`)
- **Concurrecy**: Kotlin Coroutines & Flow (structured concurrency for network and file tasks)
- **Networking**: Retrofit / OkHttp (making direct, authenticated REST API queries to Google's Gemini endpoint)
- **Data Model**: Clean local repositories and models representing real Cloud configurations, Kubernetes Pods, system alerts, and pipeline runs.
- **Testing Suite**: 
  - **Robolectric**: Local JVM unit and integration testing without requiring a running emulator.
  - **Roborazzi**: Screenshot regression tests for validating the UI design consistently.

---

## 🔑 Setup & API Key Configuration

To protect your credentials, this app uses the **Secrets Gradle Plugin**. API keys are injected at build time from environment variables or a local `.env` file and accessed in Kotlin via `BuildConfig`.

### Step 1: Create local `.env` file

In the root directory of this project, create a file named `.env` (you can copy `.env.example` as a starting template):

```bash
# Workspace Root: /.env
GEMINI_API_KEY=AIzaSyYourActualAPIKeyHere
```

> ⚠️ **IMPORTANT SECURITY NOTE**: Never commit `.env` or hardcode your API key into source control. Anyone who obtains the compiled APK can decompile it and retrieve your raw API key. See [Production Security Recommendations](#-security--production-notes) below.

---

## 📦 Project Structure

```
app/src/main/java/com/example/
├── data/
│   └── DevOpsData.kt        # Local mock database, schemas, and custom specialized System Prompts
├── service/
│   └── GeminiService.kt  # Rest API client for Google Gemini
├── ui/
│   ├── theme/
│   │   ├── Color.kt         # Custom high-contrast brand colors (Neon Orange, Cyber Cyan, Hot Coral)
│   │   ├── Theme.kt         # Centralized Material 3 Design System setup
│   │   └── Type.kt          # Space Grotesk and JetBrains Mono typography declarations
│   ├── DevOpsDashboard.kt   # Parent screen representing the main hub grid & statistics
│   ├── PillarScreens.kt     # Detail UI and action handlers for the 6 core pillars
│   └── BlueprintScreen.kt   # System Architecture blueprint viewer built directly into the app
└── MainActivity.kt          # Single-Activity entry point coordinating Tab controllers and back navigation
```

---

## ⚙️ Build & Run Locally

### Requirements

- **Android Studio** (latest stable release)
- **Android SDK**: `minSdk 24`, `compileSdk 36`
- **Gradle Wrapper**: Automated build via bundled Gradle scripts

### Running the App

1. Clone or import this project into Android Studio.
2. Let Gradle sync and download any missing dependencies.
3. Configure your `.env` file in the project root with a valid Gemini API key.
4. Select the **debug** variant in the **Build Variants** panel.
5. Run on a connected emulator or physical device.

### Building Release APK

To package a production-signed, optimized build:

```bash
# Configure signing configs via environment variables (optional)
export KEYSTORE_PATH="/path/to/upload-key.jks"
export STORE_PASSWORD="keystore-pass"
export KEY_PASSWORD="key-pass"

# Build release bundle or APK
./gradlew assembleRelease
```

---

## 🧪 Testing Suite

Automated tests are critical to avoid UI regression. We use local JVM testing with **Robolectric** and screenshot comparisons with **Roborazzi**:

### Execute Local Unit & Robolectric Tests

```bash
./gradlew testDebugUnitTest
```

### Run and Verify Screenshot Layouts

To verify if your current changes break existing visual layouts compared to baseline images (saved in `app/src/test/screenshots/`):

```bash
./gradlew verifyRoborazziDebug
```

### Record New Reference Screenshots

If you made intentional UI adjustments and want to update the official baselines:

```bash
./gradlew recordRoborazziDebug
```

---

## 🗺️ Development Roadmap

- [x] **Phase 1: Foundation (Completed)**
  - Core 6-pillar UI dashboard implementation in Material 3.
  - Standardized local state tracking (unhealthy pod restart triggers, EBS cost optimization).
  - Secure `.env` file integration and local test builds.
- [ ] **Phase 2: Secure Hybrid Architecture (Current)**
  - Integrate **Firebase App Check** & **AppCheck reCAPTCHA Enterprise** to authenticate genuine client instances.
  - Shift direct API calls away from the client device toward a lightweight **Cloud Function API Proxy** to prevent key harvesting.
- [ ] **Phase 3: Deep Integrations (Future)**
  - Connect real-time telemetry from live Kubernetes clusters via read-only K8s REST APIs.
  - Implement real Webhook endpoints for instant Slack or Microsoft Teams alert dispatch.

---

## 🔒 Security & Production Notes

In a real production environment, calling raw endpoints directly with a bundled API key poses severe security risks:

- **Key Harvesting**: Malicious users can easily extract the `GEMINI_API_KEY` from compiled bytecode.
- **Client Shielding**: We recommend using our direct-in-app **Blueprint Screen** to understand how to design and build a secure backend proxy:
  1. The client requests data.
  2. The request is validated by **Firebase App Check** to ensure it's an untampered app instance.
  3. The request is routed to a secure, private Node.js/Python backend.
  4. The backend appends the API key from a secure secret manager (like GCP Secret Manager) and executes the call.
