# Architecture & Modularization (MIP-3)

## 1. Overview
The application has transitioned from a monolithic structure to a multi-module architecture. [cite_start]This approach enforces a strict Separation of Concerns (SoC), allowing the exact same business logic and data layer to power two completely different User Interfaces (UIs).

## 2. Architectural Pattern: MVVM
[cite_start]Both UI applications follow the **Model-View-ViewModel (MVVM)** design pattern with a Unidirectional Data Flow (UDF):
* [cite_start]**Model (Data Layer):** Retrieves and structures the weather data.
* [cite_start]**ViewModel:** Holds the UI state, handles user events, and requests data from the Model.
* [cite_start]**View (UI Layer):** Observes the ViewModel's state and updates automatically whenever the state changes.

## 3. Module Structure
[cite_start]The project is strictly divided into three isolated modules:

### `:core` (Shared Module)
[cite_start]A Kotlin/Android Library module that acts as the "brain" of the application. It is completely independent of any UI framework.
* [cite_start]**Responsibilities:** Contains Data Models (`WeatherData`), the API Client (using Ktor as `WeatherApiClient`), and the Repository layer for data access.

### `:app-xml` (Legacy UI Module)
* [cite_start]**Description:** The refactored version of the original application.
* [cite_start]**Technology:** Uses traditional XML layouts, Activities, and an imperative UI approach.
* [cite_start]**Dependency:** Consumes the shared `:core` module for all data and logic.

### `:app-compose` (Modern UI Module)
* [cite_start]**Description:** A completely new application module built from scratch.
* [cite_start]**Technology:** Uses Jetpack Compose (Declarative UI) to draw the screens.
* [cite_start]**Dependency:** Consumes the exact same `:core` module as the XML app.
* [cite_start]**Exclusive Features:** Includes modern UI features (e.g., dynamic theming, animations, adaptive layouts) not present in the XML version to demonstrate Compose's capabilities.