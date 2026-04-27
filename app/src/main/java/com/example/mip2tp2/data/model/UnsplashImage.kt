package com.example.mip2tp2.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a single image returned by the Unsplash API.
 *
 * @property id The unique identifier of the photo.
 * @property description An optional description of the photo.
 * @property urls Object containing different size URLs for the image.
 * @property user Object containing the photographer's details.
 */
data class UnsplashImage(
    @SerializedName("id")
    val id: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("urls")
    val urls: ImageUrls,

    @SerializedName("user")
    val user: User
)
