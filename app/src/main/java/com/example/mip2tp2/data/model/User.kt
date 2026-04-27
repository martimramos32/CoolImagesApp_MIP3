package com.example.mip2tp2.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the photographer who uploaded the image.
 *
 * @property name The display name of the photographer.
 */
data class User(
    @SerializedName("name")
    val name: String
)
