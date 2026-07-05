# ARCHITECTURE.md

# Application Architecture

Architecture Pattern

MVVM (Model - View - ViewModel)

The application should maintain a clear separation between UI, business logic, and data.

---

# High Level Structure

UI

↓

Navigation

↓

ViewModels

↓

Repositories

↓

Managers

↓

Data Sources

---

# Modules

## UI

Responsible only for displaying information.

Contains:

- Screens
- Components
- Theme
- Animations

Never contains business logic.

---

## Navigation

Responsible for moving between screens.

Examples:

- Home
- Routine Editor
- Saved Routines
- Gallery
- Settings

---

## ViewModels

Responsible for:

- Managing UI state
- Receiving UI events
- Calling repositories
- Exposing observable state

Never directly access storage.

---

## Repositories

Repositories provide data to ViewModels.

Examples:

- Routine Repository
- Gallery Repository
- Theme Repository

Repositories hide implementation details.

---

## Managers

Managers handle specialized functionality.

Examples:

- Routine Execution Manager
- Audio Manager
- Vibration Manager
- Camera Manager
- Resource Manager
- Theme Manager

Managers should remain independent.

---

## Data Sources

Responsible for persistence.

Examples:

- Local Database
- Local Storage
- Theme Packages

---

# Screen Structure

Each screen should have:

Screen

↓

ViewModel

↓

Repository

↓

Manager (if needed)

---

# Current Routine Flow

User edits Current Routine

↓

ViewModel updates state

↓

Repository saves changes

↓

Execution Manager executes routine

---

# Gallery Flow

Camera

↓

Preview

↓

Keep / Retake / Trash

↓

Gallery Repository

↓

Gallery Screen

---

Image Storage

All captured images should be stored in a dedicated "Shooting Timer" folder within a folder in device's internal storage.

The application should maintain references to these images rather than storing the image data directly in a database.

If an image is deleted through the application, both the stored file and its metadata should be removed.

# Theme Flow

Theme Manager

↓

Asset Manager

↓

Behavior Engine

↓

Decoration Renderer

↓

Load Theme

↓

Load Assets

↓

Apply Theme

↓

Refresh UI

---

# Resource Flow

Every shared resource is managed centrally.

Examples:

- Sounds
- Images
- Fonts
- Themes

Other modules should reference resources through the Resource Manager instead of directly accessing files.

---

# Task System

Every task is an independent object.

Each task owns its own configuration.

Examples:

Wait Task

- Duration

Beep Task

- Playback Mode
- Duration

Play Sound Task

- Selected Sound
- Playback Mode
- Duration

Vibrate Task

- Playback Mode
- Duration

Future task types should be addable without changing the execution engine.

---

# Execution Engine

The execution engine reads tasks sequentially.

Task

↓

Execute

↓

Next Task

↓

Repeat until routine finishes

Depending on execution mode:

- Stop
- Repeat X Times
- Infinite Loop

---

# Theme Packages

Themes should be self-contained.

They may contain:

- Metadata
- Colors
- Fonts
- Icons
- Illustrations
- Sounds
- Decorative assets
- Animations

The application should load themes through the Theme Manager.

---

## Behavior Engine

The Behavior Engine is responsible for animating decorative assets.

It does not know what an asset represents.

It only knows how to perform supported behaviors.

Examples:

- Float
- Rain
- Throw
- Walk
- Bounce
- Drift
- Fly

The same behavior should work with any decorative asset.

---

## Asset Manager

The Asset Manager (incorporating the Resource Manager) is responsible for:
- Loading built-in assets from APK assets
- Loading theme assets from theme ZIPs in private storage
- Loading user assets from the `User/` directory in private storage
- loading Sounds
- Validating imported assets
- Providing assets to the renderer via a unified AssetProvider interface

Application code should never directly access asset files.

---

##  After implementing a feature:

- Update TODO.md.
- Update ASSET_REQUIREMENTS.md only if new assets are introduced.
- Update PROJECT.md or ARCHITECTURE.md only if the implementation changes the specification.
- Do not generate unnecessary summaries or reports.

---

# Design Principles

- Single Responsibility Principle
- Reusable Components
- Modular Design
- Consistent Naming
- Separation of Concerns
- Scalable Architecture

---

# Dependency Direction

UI depends on ViewModels.

ViewModels depend on Repositories.

Repositories depend on Managers and Data Sources.

Managers should not depend on UI.

The data layer should never know about the UI layer.

---

# Goal

The architecture should remain modular, maintainable, and easy to extend without requiring major refactoring when new features are added.