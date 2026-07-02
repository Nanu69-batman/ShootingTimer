# RULES.md

# General Rules

- Always write production-quality code.
- Prefer readability over cleverness.
- Prioritize maintainability.
- Do not over-engineer solutions.
- Never implement features that were not requested.
- If requirements are ambiguous, ask instead of assuming.
- Preserve existing functionality unless explicitly instructed otherwise.

---

# Architecture Rules

- Follow MVVM architecture.
- Separate UI, business logic, and data layers.
- Business logic must never exist inside Composable functions.
- UI should only display state and forward user actions.
- Use ViewModels to manage UI state.
- Keep components modular and reusable.

---

# Code Style

- Use Kotlin best practices.
- Use descriptive names.
- Keep functions short and focused.
- Keep classes focused on a single responsibility.
- Avoid duplicate code.
- Avoid magic numbers.
- Prefer immutable data where possible.
- Use official Android libraries whenever possible.
- Avoid unnecessary third-party dependencies.

---

# Jetpack Compose Rules

- Use Material Design 3.
- Create reusable composables.
- Avoid deeply nested composables.
- Hoist state whenever appropriate.
- Keep composables small and focused.
- Separate UI from state management.

---

# UI Rules

- Keep interfaces clean and uncluttered.
- Prioritize usability over decoration.
- Maintain consistent spacing.
- Use rounded corners throughout the application.
- Keep decorative animations lightweight.
- Decorations must never block interaction.
- Large touch targets are preferred.
- Accessibility should always be considered.

---

# Performance Rules

- Avoid unnecessary recompositions.
- Do not block the main thread.
- Use asynchronous operations when appropriate.
- Optimize memory usage.
- Avoid unnecessary object creation.

---

# Resource Rules

- Never hardcode colors.
- Never hardcode strings.
- Never hardcode dimensions.
- Store reusable resources centrally.
- Use the Resource Manager whenever appropriate.

---

# Documentation Rules

Whenever architecture changes:

- Update ARCHITECTURE.md

Whenever product decisions change:

- Update PROJECT.md

Whenever implementation decisions change:

- Update DECISIONS.md

Whenever a task is completed:

- Update TODO.md

---

# Git Rules

Implement one logical feature at a time.

Keep commits focused.

Do not mix unrelated changes in the same commit.

---

# AI Behavior

Before implementing a feature:

- Read the relevant project documentation.
- Explain the implementation approach.
- Mention which files will be modified.

After implementation:

- Summarize what was completed.
- List all modified files.
- Mention any assumptions that were made.

---

# Error Handling

- Handle errors gracefully.
- Never ignore exceptions silently.
- Display user-friendly error messages.
- Log useful debugging information when appropriate.

---

# Testing

- Consider edge cases.
- Avoid breaking existing features.
- Ensure changes are consistent with the current architecture.

---

# Theme & Asset Rules

- Never hardcode decorative assets.
- Never hardcode theme assets.
- Always load assets through the Asset Manager.
- Behaviors must remain generic.
- Never create code specifically for one asset.
- Decorations should be data-driven.
- Themes should define appearance, not application logic.
- Imported sounds should always be loaded through the Asset Manager.
- Never directly access sound files.