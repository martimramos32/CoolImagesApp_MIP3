# Screens

## Main Screen (MainActivity)
**Components:**
* A Toolbar at the top with the app title.
* A loading indicator (ProgressBar) centered on the screen, visible when fetching data from the internet.
* A scrollable list/grid (RecyclerView) to display the fetched Unsplash images.
* A refresh action (SwipeRefreshLayout or a Refresh Button) to request new random images.
* Each item in the list will show the image itself and a small text with the author's name.