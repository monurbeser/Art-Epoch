// File: app/src/main/java/com/artepoch/ui/screens/movement/MovementScreen.kt
package com.artepoch.ui.screens.movement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artepoch.domain.model.Movement
import com.artepoch.domain.model.Period
import com.artepoch.domain.model.getMovementsForPeriod

@Composable
fun MovementScreen(
    selectedPeriod: Period?,
    onMovementSelected: (Movement) -> Unit,
    onBack: () -> Unit
) {
    val movements = remember(selectedPeriod) {
        selectedPeriod?.let { getMovementsForPeriod(it) } ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Select Art Movement",
                    style = MaterialTheme.typography.headlineSmall
                )
                selectedPeriod?.let {
                    Text(
                        text = it.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(movements) { movement ->
                MovementRow(
                    movement = movement,
                    onClick = { onMovementSelected(movement) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun MovementRow(
    movement: Movement,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = movement.label,
            style = MaterialTheme.typography.titleMedium
        )
    }
}