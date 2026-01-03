// File: app/src/main/java/com/artepoch/ui/screens/detail/DetailScreen.kt
package com.artepoch.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.artepoch.domain.model.Artwork

@Composable
fun DetailScreen(
    artwork: Artwork?,
    onBack: () -> Unit
) {
    if (artwork == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Artwork not found")
        }
        return
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // 🔹 Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = artwork.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
        }

        // 🔹 Artwork image
        AsyncImage(
            model = artwork.imageUrl,
            contentDescription = artwork.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Metadata
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            InfoRow("Artist", artwork.artist)
            InfoRow("Date", artwork.displayDate ?: "Unknown")
            InfoRow("Medium", artwork.medium ?: "Unknown")
            InfoRow("Type", artwork.classification ?: "Unknown")

            Spacer(modifier = Modifier.height(16.dp))

            if (!artwork.museumUrl.isNullOrBlank()) {
                Text(
                    text = "View on The Met website",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(artwork.museumUrl)
                            )
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
