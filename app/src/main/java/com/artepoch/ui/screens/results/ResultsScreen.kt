// File: app/src/main/java/com/artepoch/ui/screens/results/ResultsScreen.kt
package com.artepoch.ui.screens.results

import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.artepoch.domain.model.Artwork
import com.artepoch.viewmodel.ArtUiState

@Composable
fun ResultsScreen(
    state: ArtUiState,
    onArtworkClick: (Artwork) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // 🔹 Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Artworks",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        when {
            state.isLoading -> {
                LoadingView()
            }

            state.error != null -> {
                ErrorView(message = state.error)
            }

            state.results.isEmpty() -> {
                EmptyView()
            }

            else -> {
                ArtworkGrid(
                    artworks = state.results,
                    onArtworkClick = onArtworkClick
                )
            }
        }
    }
}

@Composable
private fun ArtworkGrid(
    artworks: List<Artwork>,
    onArtworkClick: (Artwork) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(artworks) { artwork ->
            ArtworkCard(
                artwork = artwork,
                onClick = { onArtworkClick(artwork) }
            )
        }
    }
}

@Composable
private fun ArtworkCard(
    artwork: Artwork,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artwork.imageUrl)
                    .crossfade(true)
                    .setHeader("User-Agent", "ArtEpoch/1.0 (Android)")
                    .build(),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = artwork.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/* ---------- UI States ---------- */

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            androidx.compose.material3.CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading artworks…")
            Text(
                text = "This may take a moment",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ErrorView(message: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Error: ${message ?: "Unknown error"}")
    }
}

@Composable
private fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No artworks found for this selection.")
    }
}
