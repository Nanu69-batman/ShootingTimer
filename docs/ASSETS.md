# ASSETS.md

# Asset Specification

This document defines how application assets should be organized, named, stored, and loaded.

All assets should follow a consistent structure to make future expansion and theme importing straightforward.

---

# Asset Categories

The application supports the following asset types:

- Themes
- Sounds
- Fonts
- Images
- Decorative Elements
- Animations
- Icons

Users may import custom sound files.

Supported formats:

- MP3
- WAV
- OGG

Imported sounds become available throughout the application.

---



---

# Application Storage

# Sound Storage

Application Private Storage
Assets/
    Sounds/
        BuiltIn/
        Theme/
        User/

The application should maintain metadata for imported sounds while preserving the original audio file.

## Gallery Images

Captured images should be stored in:

Pictures/

└── Shooting Timer/

The application should never save a captured image until the user presses **Keep** on the preview screen.

The application stores references to image files rather than storing image data inside the database.

---

# Built-in Assets

Built-in assets are packaged with the application.

Examples:

- Default sounds

- Built-in themes

- Built-in fonts

These assets cannot be deleted.

---

# Imported Assets

Users may import:

- Themes

Imported themes should be stored inside the application's private storage.

Imported assets should never overwrite built-in assets.

---

# Theme Package

Supported format:

ZIP Archive

Example:

ThemeName.zip

Contents:

theme.json

preview.png

colors.json

fonts/

icons/

illustrations/

animations/

sounds/

---

# Theme Metadata

Every theme should include:

- Theme Name

- Author

- Version

- Description

- Preview Image

---

# Theme Assets

A theme may contain:

## Colors

Application color palette.

## Fonts

One or more font files.

## Icons

Icons matching the theme style.

## Illustrations

Decorative artwork.

## Sounds

Optional notification sounds.

## Animations

Decorative animations.

## Decorative Elements

Animated objects.

Examples:

- Hearts

- Clouds

- Butterflies

- Cats

- Airplanes

- Flowers

- Stars

Themes may include any decorative elements provided they do not interfere with usability.

---

# Font Override

The selected theme provides its own default font.

Users may override only the font.

Users cannot mix:

- Icons

- Illustrations

- Sounds

- Animations

- Decorative Elements

between different themes.

---

# Sound Assets

Supported formats:

- WAV

- MP3

- OGG

Every sound should include:

- Name

- Duration

- Category

Examples:

Bell

Whistle

Buzzer

Countdown

---

# Animation Assets

Animations are decorative.

They should:

- Run smoothly

- Consume minimal battery

- Never block interaction

- Never cover important controls

- Be easy to disable

---

# Icon Guidelines

Icons should:

- Match the active theme

- Be rounded

- Follow Material Design sizing

- Remain readable

---

# Image Guidelines

Target images should:

- Preserve quality

- Support sharing

- Support exporting

- Be removable from both the application and storage

---

# Naming Conventions

Themes

theme_name.zip

Images

IMG_YYYYMMDD_HHMMSS.jpg

Animations

animation_name.json

Sounds

sound_name.mp3

Fonts

font_name.ttf

---

# Resource Loading

Resources should always be loaded through the Resource Manager.

Application code should never directly reference asset files.

Every asset should have a unique identifier.

The Resource Manager is responsible for locating, loading, caching, and providing assets to the rest of the application.

---

# User Decorations

Users may upload PNG images to use as decorative assets.

Uploaded images are stored separately from gallery images.

These assets may be assigned behaviors inside the Theme Customizer.

---

# Decorative Asset Storage

Application Private Storage
Decorations/
Built-in/
User/

---

# Asset Validation

Before importing a theme, verify:

- Required files exist.

- Metadata is valid.

- Asset formats are supported.

- Theme version is compatible.

Invalid themes should never be installed.

---

# Future Compatibility

The asset system should remain extensible.

Future asset types should be addable without requiring major architectural changes.