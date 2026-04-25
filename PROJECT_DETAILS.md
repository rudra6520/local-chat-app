# Local Chat v1.4.0 \u2014 UI/UX & Frontend Specification

This document outlines the design system, layout constraints, and technical architecture of the **Local Chat v1.4.0** application. It is intended for Frontend Developers and UI/UX Designers tasked with understanding, extending, or porting the application (e.g., to an Electron app or Web Application).

---

## 1. Design System & Tokens

The application features a modern, high-contrast, dual-theme (Light/Dark mode) aesthetic. The current implementation utilizes raw Java2D, but these tokens map directly to standard CSS/Figma variables.

### Color Palette (Hex)

| Token Name | Dark Mode | Light Mode | Usage / Notes |
| :--- | :--- | :--- | :--- |
| **Background Deep** | `#0A0F1C` | `#F1F5F9` | Main application window background. |
| **Background Header** | `#0F172A` | `#FFFFFF` | Gradient pair for Background Deep. |
| **Background Panel** | `#0F172A` | `#FFFFFF` | Cards, sidebars, and chat message containers. |
| **Background Elevated**| `#1E293B` | `#F8FAFC` | Modals, dropdowns, and input fields. |
| **Border / Divider** | `#334155` | `#E2E8F0` | Panel borders and list dividers. |
| **Accent Primary** | `#6366F1` | `#6366F1` | Primary brand color (Indigo). Calls to action. |
| **Accent Hover** | `#818CF8` | `#4F46E5` | Active state for Accent Primary. |
| **Accent Dim** | `rgba(99,102,241,0.2)`| `rgba(99,102,241,0.1)`| Subtle highlights and selected list items. |
| **Text Foreground** | `#F8FAFC` | `#0F172A` | Primary text and headings. |
| **Text Muted** | `#94A3B8` | `#64748B` | Secondary text, timestamps, and placeholders. |
| **Danger Hover** | `#EF4444` | `#EF4444` | Destructive actions (e.g., Logout, Delete). |

### Typography & Spacing
- **Font Family**: `SansSerif` (System default: Inter, Roboto, or Segoe UI).
- **Scales**:
  - `Title`: 24px, Bold.
  - `Header`: 17px, Semi-Bold.
  - `Body`: 14px, Regular.
  - `Small`: 12px, Regular.
- **Corner Radii**: 
  - Standard Panels/Cards: `10px`
  - Input Fields/Pills: `20px`

---

## 2. Core Layouts & Screens

### 2.1 The Login Screen
A centered, focused authentication flow layered over a dynamic, animated background.
- **Background**: A smooth gradient from `Background Deep` to `Background Header`. Features 6 softly glowing, floating ambient "blobs" (using Indigo, Purple, and Cyan at 15-20% opacity) that bounce off the edges of the screen.
- **Center Card**: A frosted glass container containing the Login/Signup form.
- **Top Right Header**: Contains a Ghost Button for the **Theme Toggle** (Sun/Moon icon).

### 2.2 The Main Dashboard
A fluid, three-column layout designed to maximize screen real estate on desktop monitors (Minimum size: `1280x900`).

1. **Left Column (Network & Discovery - ~330px fixed)**
   - **Discovery Radar**: A 250px tall panel visualizing active network scans.
   - **LAN Peers**: A searchable, scrolling list of online users.
2. **Center Column (Chat Arena - Fluid Width)**
   - **Chat Transcript**: The primary reading zone. Self-messages align right, peer messages align left.
   - **Composer**: Fixed to the bottom. Contains Mode selector (Private/Group), Room name input, Attachment button, Emoji button, and a large Accent Send button.
3. **Right Column (Tools - ~250px fixed)**
   - A vertical stack of utility buttons: Search, Export Base64, Edit Message, Dashboard, and Data Deletion.

---

## 3. UI Components & Interactions

### Ghost Buttons (Header Actions)
Header utilities (like `Theme Toggle` and `Logout`) utilize a minimalist "Ghost" style.
- **Default State**: No borders, no background, `Text Muted` color.
- **Hover State**: Text and custom vector icon smoothly transition to `Accent Hover` (or `Danger Hover` for Logout). Cursor changes to pointer.

### The Discovery Radar (Animated Component)
A highly visual representation of the UDP discovery ping system.
- **Visuals**: A deep radial gradient background (`Background Panel` fading into `Background Deep`).
- **Animations**: 
  - *Concentric Rings*: Rings expand outward from the center, fading in opacity the further they travel.
  - *Scanner Sweep*: A translucent indigo cone `rgba(99, 102, 241, 0.4)` rotates continuously around the center point, creating a radar sweep effect.

### Glassmorphism Overlays
When modal dialogs (like the Account Settings or Dashboard) are active, a `GlassPanel` overlay covers the entire frame. This is a solid black fill with `40% opacity` (`rgba(0,0,0,0.4)`), drawing focus to the modal while blurring/darkening the background context.

---

## 4. Technical Constraints for Frontend Developers

If porting this UI to a modern web framework (e.g., React/Vue via Electron or Tauri), consider the following architectural rules underlying the UI:

1. **Strictly Offline Network Stack**:
   - The app has no central server.
   - **Discovery** relies on UDP Broadcasts (Port `8081`). The UI must dynamically add/remove users from the Left Column as UDP pings arrive or timeout (3 missed pings = offline).
   - **Messaging** relies on direct TCP Sockets (Port `8080`). Frontend apps will require raw socket access (e.g., Node.js `net` and `dgram` modules). Standard WebSockets `ws://` will not work for P2P LAN discovery.
2. **Local File Persistence**:
   - Chat transcripts are written locally in real-time.
   - All local storage is encrypted using **AES-128**. The UI should not attempt to read/write plaintext chat histories directly to the filesystem.
3. **Typing Indicators**:
   - Typing events are fired via UDP every few hundred milliseconds while the user is typing in the composer. The UI must debounce these events and show/hide the "User is typing..." indicator dynamically above the composer.
