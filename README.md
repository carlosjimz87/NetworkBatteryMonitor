# 🔋📶 Network & Battery Monitor App 🤖📱

**Network & Battery Monitor** is an Android application that provides real-time tracking of **network connectivity** and **battery status**. With a modern, responsive UI built using **Jetpack Compose**, the app leverages **Kotlin Coroutines**, **StateFlow**, and **Koin** to deliver a clean, maintainable, and testable architecture. Users receive instant updates and notifications about network changes and low battery levels.

---

## ✅ Features

- **📶 Network Monitoring**
    - Detects network availability and type (Wi-Fi, Mobile, Ethernet).
    - Real-time UI updates with smooth animated transitions.
    - Displays a Snackbar alert on network connection or disconnection.

- **🔋 Battery Monitoring**
    - Tracks battery level in real-time.
    - Sends an in-app notification when the battery falls below 20%.

- **🧼 Clean Architecture**
    - Implements separation of concerns with ViewModel, Repository, and Data Source layers.
    - Utilizes **Koin** for lightweight dependency injection.
    - Designed for full testability across components.

- **⚡ Jetpack Compose UI**
    - Fully reactive, lifecycle-aware interface.
    - Features animations and adaptive layouts for both small and large screens.

---

## 📦 Project Structure

┣ 📂common \
┃ ┣ 📜Constants.kt \
┃ ┗ 📜Extensions.kt \
┣ 📂data \
┃ ┣ 📂battery \
┃ ┃ ┗ 📜BatteryMonitor.kt \
┃ ┣ 📂connectivity \
┃ ┃ ┗ 📜NetworkMonitor.kt \
┃ ┗ 📂models \
┃ ┃ ┣ 📜BatteryStatus.kt \
┃ ┃ ┣ 📜MonitoringState.kt \
┃ ┃ ┗ 📜NetworkStatus.kt \
┣ 📂di \
┃ ┗ 📜appModules.kt \
┣ 📂domain \
┃ ┗ 📂repository \
┃ ┃ ┣ 📜StatusRepository.kt \
┃ ┃ ┗ 📜StatusRepositoryImpl.kt \
┣ 📂ui \
┃ ┣ 📂events \
┃ ┃ ┗ 📜UiEvent.kt \
┃ ┣ 📂main \
┃ ┃ ┣ 📜MainScreen.kt \
┃ ┃ ┗ 📜MainViewModel.kt \
┃ ┣ 📂theme \
┃ ┃ ┣ 📜Color.kt \
┃ ┃ ┣ 📜Theme.kt \
┃ ┃ ┗ 📜Type.kt \
┣ 📂utils \
┃ ┗ 📜NotificationHelper.kt \
┣ 📜MainActivity.kt \
┗ 📜MonitorApp.kt

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```
git clone https://github.com/your-username/network-battery-monitor.git
cd network-battery-monitor
```

### 2. Open in Android Studio
- **Minimum SDK**: API 24 (Android 7.0)
- **Compile SDK**: API 34
- **Kotlin**: 1.9.x
- **Gradle**: 8.x
- **Jetpack Compose**: 1.5+

Open the project in Android Studio (latest stable version recommended).

### 3. Build the Project
Ensure you have the latest Android Gradle Plugin with Compose support installed. Build the project using:
```
./gradlew build
```
Alternatively, click the "Build" (hammer) button in Android Studio.

### 4. Run the App
Connect an emulator or physical device (API 24 or higher), then launch the app by clicking the "Run" (play) button in Android Studio.

---

## 🧪 Running Tests

The project includes a robust test suite leveraging:
- **kotlinx.coroutines.test** for coroutine testing.
- **app.cash.turbine** for testing Flows.
- **Koin test modules** for injecting mock dependencies.

To execute all unit tests:
1. Right-click the `(test)` folder in Android Studio and select "Run Tests," or
2. Use the command:
   ```
   ./gradlew testDebugUnitTest
   ```

---

## 🧪 Manual Testing via ADB

To simulate different battery and network scenarios without needing physical changes, you can use the following ADB commands:

### 🔋 Battery Simulation

Set battery level:
```
adb shell dumpsys battery set level [percentage value]
```

Reset to real battery values:
```
adb shell dumpsys battery reset
```

### 📶 Network Simulation

Disable/Enable Wi-Fi:
```
adb shell svc wifi disable
adb shell svc wifi enable
```

Disable/Enable Mobile Data:
```
adb shell svc data disable
adb shell svc data enable
```

---

## ✨ Contributions

This app was developed as an educational project and welcomes contributions! Feel free to fork the repository, submit pull requests, or report issues via the GitHub issue tracker.

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.