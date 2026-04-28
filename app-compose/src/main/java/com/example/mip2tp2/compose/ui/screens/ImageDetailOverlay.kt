package com.example.mip2tp2.compose.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mip2tp2.core.data.model.UnsplashImage

@Composable
fun ImageDetailOverlay(
    image: UnsplashImage?,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = image != null,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f, animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f, animationSpec = tween(300))
    ) {
        if (image == null) return@AnimatedVisibility

        BackHandler { onDismiss() }

        var dominantColor by remember { mutableStateOf(Color(0xFF121212)) }
        val animatedBackgroundColor by animateColorAsState(
            targetValue = dominantColor, 
            animationSpec = tween(500),
            label = "bg_color"
        )

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.urls.regular)
                .crossfade(true)
                .allowHardware(false) // Needed for software bitmap extraction
                .build()
        )

        // Extract Palette when image is successfully loaded
        LaunchedEffect(painter.state) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Success) {
                val bitmap = state.result.drawable.toBitmap()
                Palette.from(bitmap).generate { palette ->
                    palette?.dominantSwatch?.rgb?.let { colorInt ->
                        dominantColor = Color(colorInt)
                    } ?: palette?.mutedSwatch?.rgb?.let { colorInt ->
                        dominantColor = Color(colorInt)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedBackgroundColor)
                .clickable(enabled = false) {} // Prevent clicks from passing through to the grid
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(top = 48.dp, end = 16.dp, bottom = 16.dp)
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painter,
                        contentDescription = image.description,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 72.dp)
                ) {
                    Text(
                        text = image.user.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = image.description ?: "No description provided.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
