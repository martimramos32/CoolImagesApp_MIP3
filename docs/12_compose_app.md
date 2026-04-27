# Modern UI Plan: Jetpack Compose (`:app-compose`)

## Feature Overview
This document outlines the implementation of the new modern UI application. It will consume the shared `:core` module and use **Jetpack Compose** for a fully declarative UI. To satisfy the assignment requirements, this module will include a **Compose-Exclusive Feature**: A Dynamic Theming and Smooth Animation system.

## Implementation Plan (Compose App)

**Step 6: Compose Module Setup**
Create the `:app-compose` Android application module. Ensure the `build.gradle.kts` is properly configured to enable Jetpack Compose, add the necessary Compose dependencies (Material 3, UI Tooling, ViewModel Compose), and add the `:core` module as a dependency.

**Step 7: Compose UI Architecture & State**
In `MainActivity.kt`, set up the Compose `setContent` block. Create a modern Compose ViewModel (if a separate presentation logic is needed) that observes the `Repository` from the `:core` module and exposes a clean `StateFlow` to the UI representing the list of Unsplash images and the loading status.

**Step 8: Declarative UI Construction**
Create the UI components using Compose:
* `ImageGalleryScreen`: The main screen containing a `LazyVerticalGrid` to display the photos.
* `ImageCard`: A composable for individual grid items, using Coil or Glide for Compose to load the image URL.
* `PullRefresh`: Integrate the Compose `pullRefresh` modifier to allow users to fetch new data.

**Step 9: The Exclusive Feature (Animations & Dynamic Theme)**
Implement the Compose-exclusive feature:
* **Detail Animation:** When a user taps an `ImageCard`, use Compose Animations (e.g., `AnimatedVisibility` or `animateContentSize`) to smoothly transition the image to a full-screen detailed view, rather than launching a new Activity.
* **Dynamic Palette (Optional/Bonus):** Extract the dominant color from the selected image and dynamically animate the application's background color to match the image's vibe using Material 3 `ColorScheme` overrides.