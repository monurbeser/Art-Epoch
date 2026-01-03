// File: app/src/main/java/com/artepoch/core/AppContainer.kt
package com.artepoch.core

import com.artepoch.data.wiki.WikiNetwork
import com.artepoch.data.repo.ArtRepository

class AppContainer {

    private val wikiApi = WikiNetwork.createApi()

    val artRepository: ArtRepository by lazy {
        ArtRepository(wikiApi = wikiApi)
    }
}