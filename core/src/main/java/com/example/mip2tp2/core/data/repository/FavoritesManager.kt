package com.example.mip2tp2.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.mip2tp2.core.data.model.UnsplashImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Handles the secure persistent local storage of up to 5 favorite Unsplash imagery.
 */
class FavoritesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getFavorites(): List<UnsplashImage> {
        val json = prefs.getString("favorites_list", null) ?: return emptyList()
        val type = object : TypeToken<List<UnsplashImage>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Toggles an image's favorite state. Returns a Pair representing (wasAdded, isSuccess).
     * isSuccess will be false if trying to add more than 5 elements.
     */
    fun toggleFavorite(image: UnsplashImage): Pair<Boolean, Boolean> {
        val currentFavorites = getFavorites().toMutableList()
        val existingIndex = currentFavorites.indexOfFirst { it.id == image.id }

        if (existingIndex != -1) {
            // Already a favorite, so we remove it
            currentFavorites.removeAt(existingIndex)
            saveFavorites(currentFavorites)
            return Pair(false, true)
        } else {
            // Not a favorite, attempt to add
            if (currentFavorites.size >= 5) {
                return Pair(false, false) // Failed, capacity reached
            }
            currentFavorites.add(image)
            saveFavorites(currentFavorites)
            return Pair(true, true)
        }
    }

    fun isFavorite(imageId: String): Boolean {
        return getFavorites().any { it.id == imageId }
    }

    private fun saveFavorites(favorites: List<UnsplashImage>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString("favorites_list", json).apply()
    }
}
