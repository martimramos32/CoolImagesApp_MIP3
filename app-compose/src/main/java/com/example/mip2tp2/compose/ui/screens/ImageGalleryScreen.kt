package com.example.mip2tp2.compose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mip2tp2.compose.ui.components.ImageCard
import com.example.mip2tp2.compose.ui.viewmodel.GalleryUiState
import com.example.mip2tp2.compose.ui.viewmodel.GalleryViewModel
import com.example.mip2tp2.core.data.model.UnsplashImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageGalleryScreen(
    viewModel: GalleryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favoritesState.collectAsState()
    val isDarkTheme by viewModel.isDarkMode.collectAsState()
    val isRefreshing = uiState is GalleryUiState.Loading

    var selectedImage by remember { mutableStateOf<UnsplashImage?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            text = "Unsplash Gallery",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold
                        ) 
                    },
                    actions = {
                        IconButton(onClick = { viewModel.toggleDarkMode() }) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.loadImages(isInitial = true) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                
                // Favorites LazyRow
                if (favorites.isNotEmpty()) {
                    Text(
                        text = "Favorites (${favorites.size}/5)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        items(favorites, key = { "fav_${it.id}" }) { image ->
                            ImageCard(
                                image = image,
                                isFavorite = true,
                                onToggleFavorite = { viewModel.toggleFavorite(image) },
                                onClick = { selectedImage = image },
                                modifier = Modifier.width(140.dp)
                            )
                        }
                    }
                }

                // Main Gallery
                when (val state = uiState) {
                    is GalleryUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is GalleryUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    is GalleryUiState.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 120.dp),
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.images, key = { it.id }) { image ->
                                val isFav = favorites.any { it.id == image.id }
                                ImageCard(
                                    image = image,
                                    isFavorite = isFav,
                                    onToggleFavorite = { viewModel.toggleFavorite(image) },
                                    onClick = { selectedImage = image }
                                )
                            }
                        }
                    }
                }
            }
            }
        } // Close Scaffold
        
        // Detail Overlay
        ImageDetailOverlay(
            image = selectedImage,
            onDismiss = { selectedImage = null }
        )
    }
}
