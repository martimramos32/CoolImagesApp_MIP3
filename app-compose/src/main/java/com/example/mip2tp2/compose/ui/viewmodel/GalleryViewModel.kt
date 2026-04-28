package com.example.mip2tp2.compose.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mip2tp2.core.data.model.UnsplashImage
import com.example.mip2tp2.core.data.repository.FavoritesManager
import com.example.mip2tp2.core.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context

sealed class GalleryUiState {
    object Loading : GalleryUiState()
    data class Success(val images: List<UnsplashImage>) : GalleryUiState()
    data class Error(val message: String) : GalleryUiState()
}

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val favoritesManager = FavoritesManager(application)
    private val repository = ImageRepository(favoritesManager = favoritesManager)

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    private val _favoritesState = MutableStateFlow<List<UnsplashImage>>(emptyList())
    val favoritesState: StateFlow<List<UnsplashImage>> = _favoritesState.asStateFlow()

    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("is_dark_mode", false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        loadImages(isInitial = true)
        loadFavorites()
    }

    fun loadImages(isInitial: Boolean = false) {
        viewModelScope.launch {
            if (isInitial) {
                _uiState.value = GalleryUiState.Loading
            }
            try {
                val images = repository.getRandomImages(count = 30, isInitial = isInitial)
                _uiState.value = GalleryUiState.Success(images)
            } catch (e: Exception) {
                _uiState.value = GalleryUiState.Error(e.message ?: "Failed to load images")
            }
        }
    }

    private fun loadFavorites() {
        _favoritesState.value = favoritesManager.getFavorites()
    }

    fun toggleFavorite(image: UnsplashImage) {
        val result = favoritesManager.toggleFavorite(image)
        loadFavorites() // Refresh the list
    }

    fun toggleDarkMode() {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        prefs.edit().putBoolean("is_dark_mode", newValue).apply()
    }
}
