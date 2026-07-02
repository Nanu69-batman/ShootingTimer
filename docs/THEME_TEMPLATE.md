# THEME_TEMPLATE.md

# Theme Package Specification

Every theme is distributed as a ZIP archive.

Themes define the visual appearance and personality of the application.

Themes never contain executable code.

All behavior logic is provided by the application.

---

# Folder Structure

ThemeName.zip

├── theme.json

├── preview.png

├── icons/

├── fonts/

├── decorations/

├── illustrations/

├── sounds/

└── screenshots/

---

# Required Files

theme.json

preview.png

---

# theme.json

Contains:

- Theme Name
- Author
- Version
- Description
- Minimum Supported App Version
- Default Font
- Theme Colors
- Decorations
- Event Bindings

---

# Colors

The theme should define the application's color palette.

Examples

- Primary
- Secondary
- Background
- Surface
- Card
- Accent
- Text
- Success
- Error

---

# Fonts

The fonts folder may contain one or more fonts.

Supported formats

- TTF
- OTF

The theme specifies which font should be the default.

Users may override the font.

---

# Icons

Icons replace the application's default icons.

Icons must preserve the required filenames.

Icons should follow the application's size guidelines.

---

# Decorations

The decorations folder contains decorative images.

Supported formats

- PNG
- WEBP

Decorations are passive assets.

They do not define behavior.

Behavior is assigned inside theme.json.

Example decorations

- Cat
- Flower
- Butterfly
- Cloud
- Heart
- Balloon
- Airplane

---

# Sounds

sounds/

Contains default sounds provided by the theme.

These sounds may be used by:

- Default task sounds
- Decorative effects
- Event effects

Users may override these sounds with imported sounds.

Optional notification sounds.

Supported formats

- MP3
- WAV
- OGG

---

# Preview

preview.png

Used inside Theme Studio.

---

# Event Bindings

Themes may define decorative reactions to application events.

Example

Routine Finished

↓

Heart Rain

Photo Saved

↓

Star Pop

---

# Theme Inheritance

A custom theme may inherit from another theme.

Only modified values need to be stored.

The application loads the parent theme first and then applies the user's customizations.

---

# Asset Rules

Themes may replace:

- Colors
- Icons
- Fonts
- Decorations
- Sounds

Themes must never replace:

- Application logic
- Behaviors
- Navigation
- Screens
- Business logic

---

# Compatibility

Unknown files should be ignored.

Missing optional assets should fall back to the application's default assets.

Invalid themes should not be imported.

---

# Goal

Themes should be portable, easy to create, easy to share, and compatible across future versions of the application.