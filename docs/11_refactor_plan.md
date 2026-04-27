# Refactoring Plan: Core Extraction

## Feature Overview
The goal of this phase is to extract the existing business logic, data models, and network communication from the original monolithic application and move them into a dedicated `:core` module. The legacy application will be renamed to `:app-xml` and configured to consume this new core module.

## Implementation Plan (Refactoring)

**Step 1: Create the Core Module**
Create a new Android Library module named `core`. Update the project's `settings.gradle.kts` to include both the new `:core` module and the renamed `:app-xml` module.

**Step 2: Migrate the Data Layer**
Move all Data Classes (Models) and the API Service interface (Retrofit/Ktor client) from the original app into the `:core` module. Ensure that the logic to read the API key from `local.properties` is also securely migrated to the `core` build configuration.

**Step 3: Migrate the Repository and Caching**
Move the `Repository` class and the entire local caching logic (Favorites and Offline 50-item cache) to the `:core` module. The `core` module must now handle all data fetching and persistence autonomously.

**Step 4: Update the XML Application (`:app-xml`)**
Add the `:core` module as a dependency in the `:app-xml` module's `build.gradle`. Clean up the `:app-xml` module by fixing all the broken imports in the `ViewModel`, `MainActivity`, and `Adapters` so they correctly point to the new packages inside the `:core` module. 

**Step 5: Verify the Build**
Compile the project to ensure there are no missing dependencies and that the `:app-xml` application runs exactly as it did before, but now powered by the `:core` module.