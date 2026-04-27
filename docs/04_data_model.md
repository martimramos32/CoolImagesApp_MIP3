# Data Model

## Models
**UnsplashImage**
* `id`: String (The unique identifier of the photo)
* `urls`: ImageUrls (Object containing different sizes of the image)
* `user`: User (Object containing the author's details)

**ImageUrls**
* `regular`: String (The actual web link to the image that we will display)

**User**
* `name`: String (The name of the photographer)