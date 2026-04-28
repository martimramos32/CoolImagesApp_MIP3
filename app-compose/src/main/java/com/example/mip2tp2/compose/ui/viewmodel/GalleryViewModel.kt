package com.example.mip2tp2.compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mip2tp2.core.data.model.UnsplashImage
import com.example.mip2tp2.core.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GalleryUiState {
    object Loading : GalleryUiState()
    data class Success(val images: List<UnsplashImage>) : GalleryUiState()
    data class Error(val message: String) : GalleryUiState()
}

class GalleryViewModel(
    private val repository: ImageRepository = ImageRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        loadImages(isInitial = true)
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
}
