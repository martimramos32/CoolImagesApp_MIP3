package com.example.mip2tp2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mip2tp2.core.data.model.UnsplashImage
import com.example.mip2tp2.core.data.repository.FavoritesManager
import com.example.mip2tp2.core.data.repository.ImageRepository
import kotlinx.coroutines.launch

/**
 * AndroidViewModel to manage UI-related data passing Context efficiently down repository structures.
 * Exposes images and complex pagination/loading states using LiveData natively.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val favoritesManager = FavoritesManager(application)
    private val repository = ImageRepository(favoritesManager = favoritesManager)

    // LiveData for the list of cached images
    private val _images = MutableLiveData<List<UnsplashImage>>()
    val images: LiveData<List<UnsplashImage>> get() = _images

    // LiveData for initial full-clear loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    
    // LiveData specifically tailored for relative pagination indication states
    private val _isPaginating = MutableLiveData<Boolean>()
    val isPaginating: LiveData<Boolean> get() = _isPaginating

    init {
        // Automatically execute initial 20 size logic efficiently
        fetchRandomImages(isInitial = true)
    }

    /**
     * Launches a dynamic fetch request intelligently splitting between base resets (20) and relative paginations (10).
     */
    fun fetchRandomImages(isInitial: Boolean = true) {
        // Thread Lock blocking overlapping asynchronous queries dynamically
        if (_isLoading.value == true || _isPaginating.value == true) return
        
        viewModelScope.launch {
            if (isInitial) _isLoading.value = true else _isPaginating.value = true
            try {
                val count = if (isInitial) 20 else 10 
                val result = repository.getRandomImages(count, isInitial)
                _images.value = result
            } catch (e: Exception) {
                // Ignore silent constraints explicitly on standard tests
                e.printStackTrace()
            } finally {
                if (isInitial) _isLoading.value = false else _isPaginating.value = false
            }
        }
    }
}
