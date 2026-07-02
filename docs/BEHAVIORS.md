# BEHAVIORS.md

# Behavior Engine Specification

The Behavior Engine is responsible for animating decorative assets.

Behaviors define *how* an asset moves.

Assets define *what* is displayed.

Behaviors must remain generic and reusable.

The same behavior should work with any decorative asset.

---

# Behavior Parameters

A behavior may use one or more of the following parameters.

- Speed
- Scale
- Opacity
- Rotation
- Spawn Count
- Spawn Interval
- Frequency
- Lifetime
- Layer
- Screen Visibility
- Randomness

---

# Supported Behaviors

## idle

does nothing and stays at its place and runs idle animation

---

## float

Moves smoothly up and down.

---

## drift

Moves slowly in random directions.

---

## fly_left

Moves horizontally from right to left.

---

## fly_right

Moves horizontally from left to right.

---

## walk

Moves across the bottom of the screen.

---

## crawl

Slower version of Walk.

---

## bounce

Continuously bounces.

---

## rotate

Slow rotation.

---

## pulse

Repeatedly grows and shrinks.

---

## wiggle

Small rotational movement.

---

## fade

Repeatedly fades in and out.

---

## blink

Repeatedly appears and disappears.

---

## pop

Quick scale animation when spawned.

---

## rain

Randomly spawns objects from the top of the screen.

Objects fall downward.

---

## throw

Spawns objects from the bottom.

Objects travel upward before falling naturally.

---

# Future Behaviors

Future versions may introduce additional behaviors.

New behaviors should never require modifications to existing themes.

---

# Behavior Rules

Behaviors must never:

- Block buttons
- Block dialogs
- Cover timers
- Reduce usability

Decorations are visual enhancements only.

---

# Event Support

Behaviors may be triggered by application events.

Examples:

- Routine Started
- Routine Finished
- Theme Changed
- Photo Saved

Multiple behaviors may be triggered simultaneously.