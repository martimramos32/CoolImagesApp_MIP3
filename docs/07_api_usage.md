# API Usage

## API Endpoint
To fetch random images, the application will use the following Unsplash API endpoint:
`https://api.unsplash.com/photos/random?count=10`

**Important Authorization Note:** Every request MUST include an Authorization header with the Access Key, like this:
`Authorization: Client-ID YOUR_UNSPLASH_ACCESS_KEY`

## Response Format
The API returns a JSON Array containing objects with image details.

## Example JSON Response (Simplified)
[
  {
    "id": "Dwu85P9SOIk",
    "width": 4000,
    "height": 6000,
    "description": "A man drinking a coffee.",
    "alt_description": "Man drinking coffee",
    "urls": {
      "raw": "https://images.unsplash.com/photo-1543326...",
      "regular": "https://images.unsplash.com/photo-1543326...",
      "small": "https://images.unsplash.com/photo-1543326...",
      "thumb": "https://images.unsplash.com/photo-1543326..."
    },
    "user": {
      "id": "qpVcM153nEw",
      "username": "example_user",
      "name": "John Doe"
    }
  }
]