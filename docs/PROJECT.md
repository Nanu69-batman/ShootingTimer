# PROJECT.md

# Shooting Timer

## Vision

Build a reliable, modern, visually appealing Android application designed specifically for shooting practice.

The application should allow users to create custom timer routines, execute them accurately, and manage target photos through a simple, intuitive, and polished user experience.

Reliability, ease of use, and thoughtful design are the highest priorities.

---

# Target Audience

- Sport shooters
- Shooting academy students
- Competitive shooters
- Hobby shooters

Primary design target:
Young female users who enjoy cute, playful, pastel-themed interfaces.

---

# Core Modules

## 1. Routine Module

The Routine Module is responsible for creating, editing, saving, loading, and executing timer routines.

The application always contains one editable **Current Routine**.

Users may optionally save the current routine for future use.

Tasks are reordered by drag-and-drop.
No separate edit mode is required for reordering.

### Current Routine

Users can:

- Create new routine
- Edit routine
- Clear current routine
- Save current routine
- Save current routine as...
- Load saved routine
- Rename saved routine
- Delete saved routine

Saved routines are accessed through a dedicated **Saved Routines** screen or dialog and are not permanently displayed on the Home screen.

---

## 2. Task System

A routine consists of an ordered list of tasks.

Users can:

- Add tasks
- Edit tasks
- Delete tasks
- Reorder tasks

### Supported Task Types

- Wait
- Beep
- Vibrate
- Play Sound

Future task types should be easy to add without modifying the existing architecture.

---

## 3. Task Configuration

Every task is independently configurable.

Each task stores all information required for its own execution.

Examples include:

- Wait duration
- Selected sound
- Playback mode
- Playback duration
- Vibration duration

Changing one task must never affect another task unless explicitly requested by the user.

---

## 4. Playback Modes

The application supports:

### Single Run

Execute the routine once.

### Repeat X Times

Repeat the routine for a user-defined number of cycles.

### Infinite Loop

Continue executing until manually stopped.

---

## 5. Sound & Vibration Behaviour

Every Beep, Play Sound, and Vibrate task manages its own playback behavior.

Each task supports two playback modes.

Users may choose sounds from:

- Built-in sounds
- Theme sounds
- Imported user sounds

Every Play Sound or Beep task stores its own selected sound independently.

### Timed Playback

Execute for a specified duration and stop automatically.

### Manual Stop

Continue until manually stopped by the user.

When Manual Stop mode is active, the application displays a full-screen stop interface with a large button that can be pressed quickly and easily.

Playback behavior is configured individually for every task.



---

## 6. Session Options

Before starting a routine, users can configure options for the current execution.

Examples include:

- Playback mode
- Repeat count
- Keep Screen Awake

These options only affect the current run.

---

## 7. Gallery Module

The Gallery is completely independent from the Routine Module.

Users can:

- Capture photos using the in-app camera
- Import photos
- View photos
- Delete photos
- Share photos
- All captured photos are stored in the application's dedicated folder inside the device's internal storage.

### Camera Workflow

Take Photo

↓

Preview

↓

Choose:

- Keep
- Retake
- Trash

Photos are only saved after selecting **Keep**.

---

## 8. Theme System

The application includes multiple built-in themes.

Examples:

- Sakura Pink
- Bubblegum Pink
- Lavender Dream

Users can switch themes at any time.

Themes should change the application's complete visual personality rather than only changing colors.

A theme package may include:

- Theme metadata
- Color palette
- Typography
- Icons
- Decorative illustrations
- Sounds
- Preview image
- UI animations
- Animated decorative elements

Examples of animated decorative elements:

- Floating hearts
- Sparkles
- Falling petals
- Clouds
- Cats
- Airplanes
- A cat riding an airplane
- Butterflies
- Balloons
- Any other lightweight decorative animations

Animations should always remain decorative and must never interfere with timers, buttons, dialogs, or any user interaction.

Themes are self-contained visual packages.

Colors, icons, illustrations, decorative elements, animations, and sounds always belong to the selected theme and cannot be mixed with assets from another theme.

The only customizable element independent of the selected theme is the font.

Users should be able to:

- Use the theme's default font
- Choose another available font

The application should support importing custom theme packages without modifying the application itself.

## 8.1 Theme Customizer

The application provides a Theme Customizer where users can personalize the currently selected theme without modifying the original theme package.

Users can:

- Change decorative assets
- Add decorative assets
- Remove decorative assets
- Upload their own decorative images
- Assign behaviors to decorations
- Configure behavior properties
- Configure event-triggered decorations
- Adjust decoration frequency
- Adjust decoration count
- Adjust decoration size
- Adjust decoration opacity
- Select which screens display each decoration
(add limits to how much counts it can have, so that doesnt crash the app)
The customizer should allow users to save their customizations as a new custom theme.

Themes should act as starting points, while users may personalize them to create their own unique experience.

---

## 9. Resource Manager

The application maintains a centralized Asset Manager.

Assets include:

- Decorations
- Icons
- Fonts
- Sounds
- Images
- Themes

Assets may be built into the application or imported by the user.

User-uploaded decorative assets should be available for use throughout the Theme Customizer.

Application code should never directly reference asset files.

### User Assets

Users may import their own assets into the application.

Supported user assets include:

- Decorative Images
- Notification Sounds

Imported assets should be available throughout the application wherever applicable.

For example:

- Decorative images may be assigned to behaviors.
- Imported sounds may be selected by Beep and Play Sound tasks.

---

## 10. Settings

Settings contain preferences that users are not expected to change frequently.

### Appearance

- Theme
- Font
- Enable theme animations
- Enable decorative elements

### Sound

- Default sound
- Default playback mode

### Vibration

- Enable vibration
- Default vibration duration

### Gallery

- Image quality
- Compression

### Backup

- Import routines
- Export routines
- Import themes
- Export themes

# User Experience Goals

The application should feel:

- Cute
- Friendly
- Calm
- Modern
- Reliable
- Fast

The interface should minimize the number of taps required to begin a shooting session while keeping advanced functionality easily accessible.