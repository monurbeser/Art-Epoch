// File: app/src/main/java/com/artepoch/ui/screens/splash/SplashPeriodScreen.kt
package com.artepoch.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.artepoch.R
import com.artepoch.domain.model.DefaultPeriods
import com.artepoch.domain.model.Period

@Composable
fun SplashPeriodScreen(
    onPeriodSelected: (Period) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Splash background image
        Image(
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🔹 Dark overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 🔹 App title
            Column {
                Text(
                    text = "ArtEpoch",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Explore art through time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // 🔹 Period selector
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(DefaultPeriods) { period ->
                    PeriodCard(
                        period = period,
                        onClick = { onPeriodSelected(period) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PeriodCard(
    period: Period,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.88f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = period.label,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}
