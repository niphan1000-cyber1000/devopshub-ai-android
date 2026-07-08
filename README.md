<div align="center">
<img width="1200" height="475" alt="DevOpsHub AI Banner" src="assets/devops_hub_banner.jpg" />
</div>

# DevOpsHub AI

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-M3-purple.svg)](https://developer.android.com/jetpack/compose)
[![API Level](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com/studio/releases/platforms)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](#license)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](#)
[![Tests Status](https://img.shields.io/badge/Tests-Passing-brightgreen.svg)](#)

An Android DevOps assistant built with **Jetpack Compose (Material 3)**, powered by the **Gemini API**. DevOpsHub AI brings six core "pillars" of cloud infrastructure, security, and operations into a single cohesive mobile dashboard.

This application is designed with a lightweight unidirectional data flow, using native Material 3 styling, Edge-to-Edge capabilities, local mock database state machines, and direct integration with Gemini REST services for intelligent root-cause analysis, IaC generation, cluster troubleshooting, and security remediation.

---

## 💡 Why DevOpsHub AI?

Modern SREs and DevOps engineers constantly juggle multiple disconnected systems: cloud dashboards, CI/CD tools, Kubernetes consoles, terminal monitors, and general AI assistants.

**DevOpsHub AI** unifies these critical workflows into a single mobile interface. It empowers on-call engineers to:
- Instantly diagnose failed CI/CD pipelines on the go.
- Troubleshoot crashed Kubernetes pods with real-time AI log diagnostics.
- Keep cloud spending optimized with automated FinOps suggestions.
- Write robust Terraform configurations directly from their phone.

---

## 📱 Screenshots

Here is a visual overview of the DevOpsHub AI experience:

| Dashboard Overview | Terraform Generator | Kubernetes Controller |
| :---: | :---: | :---: |
| <img src="assets/dashboard.jpg" width="280" alt="Dashboard" /> | <img src="assets/terraform.jpg" width="280" alt="Terraform" /> | <img src="assets/k8s.jpg" width="280" alt="Kubernetes" /> |

---

## 🎬 Interaction Flow (Walkthrough)

Here is a conceptual breakdown of a typical interactive session with the assistant:

```
┌─────────────────┐       ┌────────────────────┐       ┌────────────────────┐
│ 1. Open App     ├──────►│ 2. Select Pillar   ├──────►│ 3. Execute Actions │
│ View main grid  │       │ (e.g. Kubernetes)  │       │ (e.g. Restart Pod) │
│ & cloud metrics │       │ Inspect failing UI │       │ State updates live │
└─────────────────┘       └─────────┬──────────┘       └─────────┬──────────┘
                                    │                            │
                                    ▼                            ▼
                          ┌────────────────────┐       ┌────────────────────┐
                          │ 4. Request AI Help ├──────►│ 5. Review & Apply  │
                          │ Gemini-3.5-Flash   │       │ Generated Terraform│
                          │ analyzes system state│     │ or security patch  │
                          └────────────────────┘       └────────────────────┘
```

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

## 🛠️ Tech Stack & Real Architecture

To keep developers informed of the exact engineering setup of this prototype, here is the honest layout of our tech stack:

### Mermaid Architecture Diagram

```mermaid
graph TD
    UI[Jetpack Compose UI] -->|Direct Local State| State[Unidirectional State management]
    State -->|Triggers Rest API| GeminiService[Gemini API REST Client]
    State -->|Updates/Reads| LocalData[Local Cache & DevOpsData Repository]
    GeminiService -->|OkHttpClient & JSON| GeminiAPI[Google Gemini API v1beta]
    
    style UI fill:#3DDC84,stroke:#073042,stroke-width:2px,color:#073042
    style State fill:#4285F4,stroke:#0D47A1,stroke-width:2px,color:#FFF
    style GeminiService fill:#9C27B0,stroke:#4A148C,stroke-width:2px,color:#FFF
    style LocalData fill:#FF9800,stroke:#E65100,stroke-width:2px,color:#000
    style GeminiAPI fill:#E91E63,stroke:#880E4F,stroke-width:2px,color:#FFF
```

- **Architecture Pattern**: Native single-activity structure coordinating views using Compose-based State Management (`remember { mutableStateOf() }` with state-hoisting callback lambdas). It does *not* utilize a separate `ViewModel` class or standard navigation libraries, choosing direct Compose flow controls instead for high-performance navigation and tab-switches.
- **Networking & API**: Powered by a lightweight native REST Client built directly around **OkHttp (`OkHttpClient`)** and manual `org.json` processing. This avoids any high-overhead abstraction libraries like Retrofit for basic AI endpoints.
- **AI Model**: Native queries are routed to the cutting-edge **`gemini-3.5-flash`** model optimized for high-speed DevOps query compilation.
- **Aesthetics & Theme**: Built on a dark, cybersecurity-focused visual theme using Material 3 containers. The system explicitly disables Dynamic System Theme Colors (`dynamicColor = false` in `Theme.kt`) to lock in and preserve the distinctive branding accents (Cyber Cyan, Neon Orange, Neon Green, Neon Purple, and Hot Coral).
- **Typography & Layout**: Standard body components leverage default system sans-serif curves, while code snippet text blocks and terminal emulation boxes use Compose's native `FontFamily.Monospace` font definitions to provide optimal code readability.
- **Testing Suite**: Includes local JVM test targets powered by **Robolectric** and screenshot-assertion tests using **Roborazzi**.

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
│   └── GeminiService.kt     # Lightweight REST API client for Google Gemini (gemini-3.5-flash)
├── ui/
│   ├── theme/
│   │   ├── Color.kt         # Custom brand colors (Neon Orange, Cyber Cyan, Hot Coral, Neon Green)
│   │   ├── Theme.kt         # Centralized Material 3 Design System (enforces branding over Dynamic Colors)
│   │   └── Type.kt          # Default Material 3 typography fallback properties
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

---

## 📦 Download & Release

You can download compiled artifacts for quick demonstration:
- **Latest Dev APK**: Available via the AI Studio preview menu.
- **Production-ready builds**: Can be signed and exported directly using the Android Studio `Build > Build Bundle(s) / APK(s) > Build APK(s)` tool.

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

## 📈 Release Status & Versions

| Version | Release Date | Status | Description |
| :---: | :---: | :---: | :--- |
| **v1.0.0** | July 2026 | `In-Development` | Prototype preview showing 6 interactive DevOps pillars integrated with Gemini AI. |

---

## ⚠ Known Limitations

- **Simulated Infrastructure Data**: Cloud servers, Kubernetes clusters, and pipelines are modeled as realistic local state machines inside `DevOpsData.kt` rather than live production instances.
- **No Direct Cloud Writes**: Generated Terraform code and DevSecOps recommendations must be manually verified and executed, protecting your live environment from accidental mutations.
- **Client Key Storage Warning**: The Gemini API key is loaded locally from `.env` and bundled into the debug binary, making it insecure for public play store distribution without an intermediate proxy backend.

---

## 💬 FAQ

### Does this app connect to my live AWS/GCP/Kubernetes?
**No**. All network activities are simulated using secure local state controllers. Only the diagnostic and generator fields send prompts to the secure Google Gemini API.

### Is a Gemini API key strictly required?
**Yes**, to use the AI capabilities (Terraform generator, CI/CD analysis, K8s troubleshooting, DevSecOps code scanner). However, the dashboard, charts, metric controls, and simulation flows remain fully functional offline even without a key.

---

## 🤝 Contributing

Contributions to improve DevOpsHub AI are highly welcome!
1. **Fork** the repository.
2. **Create a branch** for your feature: `git checkout -b feature/amazing-feature`.
3. **Commit** your changes: `git commit -m "Add some amazing feature"`.
4. **Push** to your fork: `git push origin feature/amazing-feature`.
5. Open a **Pull Request**.

---

## 🎖️ Acknowledgements

- **Google Gemini API**: Providing robust language model reasoning directly to mobile devices.
- **Android Jetpack Compose & M3**: For the highly customizable, edge-to-edge component framework.
- **Robolectric & Roborazzi**: Ensuring solid, automated visual regression coverage.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
