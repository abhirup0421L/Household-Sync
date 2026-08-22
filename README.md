<div align="center">

  <!-- Dynamic Animated Header -->
  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=10,20,30,40,50&height=250&section=header&text=HOUSEHOLD%20SYNC&fontSize=65&fontAlignY=35&animation=twinkling&fontColor=FFFFFF" width="100%" alt="Household Sync Header" />

  <!-- Animated Typing Telemetry -->
  <a href="https://git.io/typing-svg">
    <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=20&duration=3000&pause=1000&color=00F0FF&center=true&vCenter=true&multiline=false&width=800&height=50&lines=%E2%96%B6+Real-Time+Collaborative+Living+Space+Manager;%E2%96%B6+Zero-Latency+Firebase+Synchronization;%E2%96%B6+Modern+Bento-Box+Material+Design+Architecture" alt="Typing SVG" />
  </a>

  <!-- Tech Stack Badges -->
  <p align="center">
    <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
    <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
    <img src="https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose" />
    <img src="https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase" />
  </p>

  <p align="center">
    <strong>A centralized, multiplayer hub for roommates, families, and couples to manage shared expenses, chores, and groceries with absolute zero latency.</strong>
  </p>

</div>

---

## 🪐 System Architecture & Features

Household Sync is engineered to eliminate the friction of shared living. Powered entirely by a **real-time Firebase backend**, any state change—from checking off a grocery item to logging a utility bill—is instantly synchronized across all authenticated devices in the network.

### 🏠 Multi-Household Matrix
Designed for dynamic living situations, users are never restricted to a single environment. 
*   **Secure Room Codes:** Generate or join households using a unique 6-character encrypted access code.
*   **Context Switching:** Seamlessly toggle between multiple active households (e.g., Family Home, College Dorm, Vacation Group) directly from the centralized House Selector dashboard.
*   **Granular Access Control:** strict Admin vs. Member roles. Admins retain full control to manage users or terminate the house, while members can easily invite others or detach themselves from the group.

### ✅ Task & Grocery Telemetry
A unified, intelligent ledger for tracking shared responsibilities and physical inventory.
*   **Attribution Engine:** Every task and grocery item logs its creator and allows for specific user assignment, ensuring complete transparency.
*   **Smart State Sorting:** Active tasks remain prioritized at the top of the UI.
*   **Fluid Resolution:** Upon completion, tasks trigger a smooth Compose animation, dynamically migrating to a segregated "Completed" zone to maintain an uncluttered active dashboard.

### 💰 Fiscal Vault & Expense Splitter
A robust financial tracker designed to handle complex shared economics without requiring third-party calculators.
*   **Custom Ledger:** Track rent, utilities, and shared receipts.
*   **Asymmetric Splitting:** Granular control over expense distribution. Specify exactly who paid the initial cost and select which specific subset of house members are responsible for the split (e.g., splitting a grocery bill among 3 out of 4 roommates).
*   **In-App Computation:** Features a natively integrated calculator accessible via a fluid Bottom Sheet, allowing users to crunch receipt numbers instantly without context-switching to other apps.

### 🎨 Bento-Box UI & Localization
Built entirely with Jetpack Compose, the application adheres to a highly polished, modern "Bento Box" design philosophy.
*   **Adaptive Theming:** Comprehensive Light and Dark mode support featuring custom, high-contrast color palettes optimized for day and night viewing.
*   **Global Localization:** Dynamic customization options allow users to toggle preferred UI languages and local currency symbols ($, €, £, ₹) directly from the configuration settings.

---

## 🛠️ Technical Implementation

*   **Architecture:** MVVM (Model-View-ViewModel) enforcing clean state management and separation of concerns.
*   **Concurrency:** Kotlin Coroutines and StateFlows utilized for non-blocking UI rendering and asynchronous database listening.
*   **Database:** Firebase Firestore enabling real-time WebSockets for instantaneous cross-device UI updates.
*   **UI Framework:** 100% Declarative UI via Jetpack Compose.

---
