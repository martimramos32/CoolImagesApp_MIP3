# Implementation Plan

**Step 1: Project Setup**
Create the base Android project structure using Kotlin and XML Views. Do NOT use Jetpack Compose. Add necessary dependencies for network calls (like Retrofit/Gson) and image loading (like Glide or Coil).

**Step 2: Data Model**
Create the Kotlin data classes (e.g., `UnsplashImage`, `User`) to parse the JSON response from the Unsplash API.

**Step 3: API Service Setup**
Implement the Retrofit interface to fetch random images from `https://api.unsplash.com/photos/random`. Ensure the API call includes the necessary Authorization header with the Access Key.

**Step 4: Repository and ViewModel**
Create a Repository class to handle the API calls. Create a ViewModel using `LiveData` to expose the list of images and the loading state to the UI.

**Step 5: UI Layout Design**
Design the `activity_main.xml` layout. It must include a `RecyclerView` for the images, a `ProgressBar` for the loading state, and a way to refresh the data (e.g., a `Button` or `SwipeRefreshLayout`). Design a separate XML layout for the individual items in the RecyclerView.

**Step 6: RecyclerView Adapter**
Create the `RecyclerView.Adapter` to bind the image data (loading the image URL into an `ImageView` and setting the author name in a `TextView`).

**Step 7: Connect UI to Logic**
In `MainActivity.kt`, initialize the RecyclerView, observe the ViewModel's `LiveData`, and set up the refresh action to fetch new images.