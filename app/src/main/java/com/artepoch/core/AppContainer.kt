// File: app/src/main/java/com/artepoch/core/AppContainer.kt
package com.artepoch.core

import android.content.Context
import com.artepoch.data.local.OfflineArtworkData
import com.artepoch.data.repo.ArtRepository

class AppContainer(context: Context) {

    private val offlineData = OfflineArtworkData(context)

    val artRepository: ArtRepository by lazy {
        ArtRepository(offlineData = offlineData)
    }
}