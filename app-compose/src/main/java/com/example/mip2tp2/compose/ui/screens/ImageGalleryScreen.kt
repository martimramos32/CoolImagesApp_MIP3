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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mip2tp2.compose.ui.components.ImageCard
import com.example.mip2tp2.compose.ui.viewmodel.GalleryUiState
import com.example.mip2tp2.compose.ui.viewmodel.GalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageGalleryScreen(
    viewModel: GalleryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favoritesState.collectAsState()
    val isRefreshing = uiState is GalleryUiState.Loading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unsplash Gallery") },
                colors = TopAppBarDefaults.topAppBarColors(
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
                                onClick = { /* TODO: Detail Animation */ },
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
                                    onClick = { /* TODO: Detail Animation */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
