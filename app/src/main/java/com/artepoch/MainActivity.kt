// File: app/src/main/java/com/artepoch/MainActivity.kt
package com.artepoch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artepoch.core.AppContainer
import com.artepoch.ui.nav.NavGraph
import com.artepoch.ui.theme.ArtEpochTheme
import com.artepoch.viewmodel.ArtViewModel
import com.artepoch.viewmodel.ArtViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArtEpochTheme {
                // Basit DI: tek container
                val container = remember { AppContainer() }

                // ViewModel Factory ile repo enjekte ediyoruz (Hilt yok)
                val vm: ArtViewModel = viewModel(
                    factory = ArtViewModelFactory(container.artRepository)
                )

                NavGraph(vm = vm)
            }
        }
    }
}
