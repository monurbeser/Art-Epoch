// File: app/src/main/java/com/artepoch/ui/screens/splash/SplashPeriodScreen.kt
package com.artepoch.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.artepoch.domain.model.DefaultPeriods
import com.artepoch.domain.model.Period

@Composable
fun SplashPeriodScreen(
    onPeriodSelected: (Period) -> Unit
) {
    // Bej renkli gradyen background
    val beigeGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F5DC), // Açık bej
            Color(0xFFE8D5C4), // Orta bej
            Color(0xFFD4C5B0)  // Koyu bej
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(beigeGradient)
    ) {
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
                    color = Color(0xFF2C2416)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Explore art through time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2C2416).copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Select a Time Period",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF2C2416)
                )
                Text(
                    text = "Choose an era to explore its artistic movements",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2C2416).copy(alpha = 0.6f)
                )
            }

            // 🔹 Period selector
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                itemsIndexed(DefaultPeriods) { index, period ->
                    var visible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(index * 100L)
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(400)) +
                               slideInVertically(initialOffsetY = { it / 3 })
                    ) {
                        PeriodCard(
                            period = period,
                            onClick = { onPeriodSelected(period) }
                        )
                    }
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "periodScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = period.label,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C2416)
        )
    }
}
