# Local Chat App (LAN Messenger)

Local Chat App is a desktop messenger for chatting with people on the same Wi-Fi/LAN network.

This folder includes:
- `LocalChatApp.exe` (Windows launcher with bundled Java runtime)
- `app/LocalChatApp.jar` (cross-platform Java app)
- `runtime/` (bundled Java runtime used by the Windows launcher)

## Quick Start (All Platforms)
1. Extract the project ZIP/folder to any location.
2. Make sure devices are on the same local network.
3. Launch the app (steps for each OS are below).
4. Allow firewall/network permissions when prompted.

## Run on Windows
### Option A (Recommended)
1. Open this folder.
2. Double-click `LocalChatApp.exe`.

### Option B (Java command)
1. Open PowerShell or Command Prompt in this folder.
2. Run:
   ```powershell
   java -jar app\LocalChatApp.jar
   ```

Windows notes:
- If SmartScreen appears, click **More info -> Run anyway** (only if you trust the source).
- Allow the app through Windows Firewall so LAN discovery and chat work.

## Run on macOS
Prerequisite:
- Java 17+ installed (`java -version` should work in Terminal).

Steps:
1. Open Terminal.
2. Navigate to the extracted folder.
3. Run:
   ```bash
   java -jar app/LocalChatApp.jar
   ```

macOS notes:
- The first launch may trigger a firewall prompt. Click **Allow**.
- If Java is missing, install OpenJDK 17+ (for example via Homebrew).

## Run on Linux (Ubuntu/Fedora/Arch and others)
Prerequisite:
- Java 17+ installed (`java -version`).

Steps:
1. Open a terminal.
2. Navigate to the extracted folder.
3. Run:
   ```bash
   java -jar app/LocalChatApp.jar
   ```

Linux notes:
- Allow local network traffic in your firewall if discovery does not work.
- On headless/minimal installs, ensure desktop GUI support is available for Java Swing.

## Build and Run from Source (Optional)
If you want to run from `source/LocalChatApp.java`:

1. Install JDK 17+.
2. From the project root:
   - Compile:
     ```bash
     javac -d out source/LocalChatApp.java
     ```
   - Run:
     ```bash
     java -cp out LocalChatApp
     ```

## Troubleshooting
- **App does not discover other users:** Verify all users are on the same network/subnet and firewall allows the app.
- **`java` command not found:** Install Java 17+ and restart terminal.
- **Double-click does nothing on Windows:** Try launching `LocalChatApp.exe` from PowerShell to view errors.

## Version
1.2.0
