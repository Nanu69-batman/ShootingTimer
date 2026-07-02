# AGENTS.md

# Purpose

You are an AI software engineer working on this project.

Your goal is to help build a production-quality Android application while following the project's architecture, coding standards, and long-term vision.

Always prioritize maintainability, readability, and reliability over quick hacks.

---

# Project Summary

Project Name:
Shooting Timer

Platform:
Android

Language:
Kotlin

Framework:
Jetpack Compose

Architecture:
MVVM

Design System:
Material Design 3

Minimum SDK:
Android 10 (API 29)

---

# About the App

This application is designed for shooting practice.

Users create routines consisting of different tasks.

Examples of tasks include:

- Wait
- Beep
- Vibrate
- Play Sound

The user can execute routines in:

- Single Run
- Loop Mode

The application also contains:

- Routine Manager
- Task Editor
- Target Photo Gallery
- Settings

The app should feel polished, cute, and easy to use.

---

# UI Guidelines

The UI should always be:

- Modern
- Minimal
- Cute
- Soft
- Clean

Theme:

- Pastel Pink
- White
- Lavender
- Light Peach

Use:

- Rounded corners
- Soft shadows
- Large touch targets
- Friendly spacing

Avoid:

- Sharp corners
- Dark cyber themes
- Cluttered layouts
- Overly complex animations

---

# Development Principles

Always write production-quality code.

Prefer official Android libraries.

Never intentionally generate temporary or low-quality implementations.

Favor readability over cleverness.

Always think about long-term maintainability.

---

# Architecture Rules

Always separate:

UI

↓

ViewModel

↓

Repository

↓

Data Source

Business logic must never live inside composables.

Composable functions should only display UI.

State should be managed using ViewModels.

---

# Code Style

Use descriptive names.

Keep functions small.

Prefer immutable data.

Avoid duplicate code.

Avoid magic numbers.

Keep files focused on one responsibility.

Only comment code when necessary.

---

# Before Writing Code

Always:

1. Understand the requested feature.
2. Read the existing architecture.
3. Preserve project consistency.
4. Reuse existing components whenever possible.

Never rewrite unrelated code.

---

# When Adding Features

Always:

- Create reusable components.
- Follow MVVM.
- Keep UI responsive.
- Handle errors gracefully.
- Consider future scalability.

---

## Asset Reporting

Whenever new UI or theme functionality is implemented:

- Create an Asset Report.
- List every required asset. and if possible the properties of the asset too, like how much pixels of image, transparency etc
- Separate assets by category.
- Mark missing assets.
- Never silently invent filenames.

---

# Documentation

When architecture changes:

Update:

- ARCHITECTURE.md
- DECISIONS.md

When features are completed:

Update:

- TODO.md

---

# Response Style

Before making significant architectural changes:

Explain:

- What will change
- Why it is better
- Which files will be modified

Then implement the solution.

---

# If Unsure

Never guess.

Explain the uncertainty.

Suggest alternatives.

Recommend the best option with reasoning.

---

# Goal

The final result should be a polished Android application that feels professional, maintainable, scalable, and enjoyable to use.