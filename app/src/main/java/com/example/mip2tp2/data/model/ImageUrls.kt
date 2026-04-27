package com.example.mip2tp2.data.model

import com.google.gson.annotations.SerializedName

/**
 * Contains the different size URLs for an Unsplash image.
 *
 * @property regular The URL for a regular-sized image (suitable for display).
 */
data class ImageUrls(
    @SerializedName("regular")
    val regular: String
)
