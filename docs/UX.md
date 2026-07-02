# UX.md

# User Experience Specification

This document defines every screen, dialog, bottom sheet, popup, and user interaction within the application.

The goal is to maintain a consistent and intuitive experience throughout the app.

---

# Navigation Overview

Home

├── Routine Editor

│

├── Saved Routines (Bottom Sheet)

│

├── Gallery

│     ├── Camera

│     └── Image Viewer

│

└── Settings

---

# Home Screen

Purpose

Main entry point of the application.

Displays the current routine and allows the user to immediately begin a shooting session.

Components

• Current Routine Card

• Start Button

• Edit Routine Button

• Saved Routines Button

• Gallery Button

• Settings Button

Session Options

• Execution Mode

    ○ Single

    ○ Repeat X Times

    ○ Infinite

• Repeat Count

• Keep Screen Awake

Navigation

→ Routine Editor

→ Gallery

→ Settings

→ Saved Routines Bottom Sheet

---

# Saved Routines Bottom Sheet

Purpose

Quickly load or manage saved routines without leaving the Home screen.

Contents

Saved routines list

Buttons

Load

Rename

Delete

Create New

Close

---

# Routine Editor

Purpose

Create and modify the Current Routine.

Components

Task List

Add Task Button

Save Current Routine

Save As

Clear Routine

Every task is displayed as a rounded card.

Example

⏱ Wait

30 seconds

☰

The drag handle allows changing task order.

Tasks should always be draggable.

No Edit Mode is required for reordering.

---

# Add Task Bottom Sheet

Purpose

Choose the type of task to insert.

Items

Wait

Beep

Vibrate

Play Sound

Selecting an item immediately creates the task.

---

# Edit Task Bottom Sheet

Purpose

Edit a task.

The displayed controls depend on task type.

Examples

Wait

Duration

Play Sound

Sound

Playback Mode

Duration

Vibrate

Playback Mode

Duration

Buttons

Cancel

Save

---

# Gallery

Purpose

Manage target photos.

Components

Photo Grid

Take Photo Button

Import Photo Button

Actions

Open

Share

Delete

---

# Camera

Purpose

Capture a target photo.

Components

Camera Preview

Capture Button

Cancel Button

Navigation

→ Photo Preview

---

# Photo Preview

Purpose

Review the captured image before saving.

Buttons

Keep

Retake

Trash

Images are only saved after selecting Keep.

---

# Image Viewer

Purpose

Display a saved photo.

Features

Zoom

Pan

Swipe between photos

Buttons

Share

Delete

Save to Device

---

# Settings

Purpose

Manage application preferences.

Sections

Appearance

Theme

Font

Enable Theme Animations

Enable Decorative Elements

Sound

Default Sound

Default Playback Mode

Vibration

Enable

Default Duration

Gallery

Image Quality

Compression

Backup

Import Routines

Export Routines

Import Themes

Export Themes

About

Version

Licenses

Contact

---

# Theme Picker Bottom Sheet

Displays all installed themes.

Shows

Preview

Theme Name

Author

Apply Button

---

# Font Picker Bottom Sheet

Displays all installed fonts.

Options

Theme Default

Installed Fonts

---

# Confirmation Dialogs

Used for

Delete Routine

Delete Image

Clear Current Routine

Trash Photo

---

# Manual Stop Screen

Purpose

Displayed when a task uses Manual Stop mode.

Layout

Entire screen becomes a Stop interface.

Large centered Stop button.

Touching Stop immediately ends the current task.

Routine execution then continues normally.

---

# Interaction Rules

Task cards are always draggable.

Buttons should have large touch targets.

Decorative animations must never cover interactive UI.

Bottom sheets should be preferred over full screens for small editing tasks.

Confirmation dialogs should only be used for destructive actions.

Navigation should require the minimum number of taps possible.

# Theme Studio

Purpose

Customize the currently selected theme.

Components

- Decorations
- Events
- Theme Preview
- Save As Theme

Navigation

→ Decoration Editor

→ Event Editor

→ Asset Manager

# Decoration Editor

Purpose

Edit a decorative object.

Properties

- Asset
- Behavior
- Frequency
- Count
- Scale
- Opacity
- Layer
- Screen Visibility

Buttons

Save

Cancel

# Event Editor

Purpose

Assign decorative effects to application events.

Supported Events

- Routine Started
- Routine Finished
- Photo Saved
- Theme Changed

Users can select:

- Asset
- Behavior
- Count
- Frequency

# Asset Manager

Purpose

Manage decorative assets.

Sections

Decorations

Sounds

Fonts

Themes

User Decorations

Buttons

Import Image

Import Sound

Delete

Rename

Preview

# Sound Picker

Purpose

Allows users to choose a sound for a task.

Categories

Built-in Sounds

Theme Sounds

User Sounds

Buttons

Preview

Import Sound

Select

Cancel