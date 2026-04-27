package com.example.mip2tp2.data.repository

import com.example.mip2tp2.data.api.RetrofitClient
import com.example.mip2tp2.data.api.UnsplashApiService
import com.example.mip2tp2.data.model.UnsplashImage

/**
 * Repository class that acts as the single source of truth for image data.
 * It abstracts the network call to the Unsplash API and manages memory caches securely.
 */
class ImageRepository(
    private val apiService: UnsplashApiService = RetrofitClient.apiService,
    private val favoritesManager: FavoritesManager? = null
) {
    private val cachedImages = mutableListOf<UnsplashImage>()

    /**
     * Fetches a list of random images from the API constraintly executed on a coroutine.
     * Manages a strict 50-limit memory cache evicting non-favorite items natively.
     */
    suspend fun getRandomImages(count: Int = 10, isInitial: Boolean = false): List<UnsplashImage> {
        val newImages = apiService.getRandomImages(count = count)
        
        if (isInitial) {
            cachedImages.clear()
        }
        
        cachedImages.addAll(newImages)
        
        // Advanced Caching Extension 3: Enforce exactly 50 items (except favorites)
        if (cachedImages.size > 50) {
            val itemsToRemove = cachedImages.size - 50
            var removedCount = 0
            val iterator = cachedImages.iterator()
            
            while (iterator.hasNext() && removedCount < itemsToRemove) {
                val image = iterator.next()
                val isFav = favoritesManager?.isFavorite(image.id) == true
                if (!isFav) { // Never evict favorites
                    iterator.remove()
                    removedCount++
                }
            }
        }
        return cachedImages.toList()
    }
}
