// File: app/src/main/java/com/artepoch/domain/model/Period.kt
package com.artepoch.domain.model

data class Period(
    val id: String,
    val label: String,
    val startYear: Int,
    val endYear: Int,
    val movementIds: List<String> // Bu dönemde geçerli olan akımlar
)

// Dönemler ve içerdikleri akımlar
val DefaultPeriods = listOf(
    Period(
        id = "renaissance",
        label = "Renaissance (1400–1600)",
        startYear = 1400,
        endYear = 1600,
        movementIds = listOf("renaissance", "mannerism")
    ),
    Period(
        id = "baroque",
        label = "Baroque & Rococo (1600–1750)",
        startYear = 1600,
        endYear = 1750,
        movementIds = listOf("baroque", "rococo")
    ),
    Period(
        id = "neoclassical",
        label = "Neoclassicism & Romanticism (1750–1850)",
        startYear = 1750,
        endYear = 1850,
        movementIds = listOf("neoclassicism", "romanticism")
    ),
    Period(
        id = "modern_19",
        label = "Impressionism & Post-Impressionism (1850–1910)",
        startYear = 1850,
        endYear = 1910,
        movementIds = listOf("realism", "impressionism", "post_impressionism", "art_nouveau")
    ),
    Period(
        id = "modern_20_early",
        label = "Early Modern (1900–1945)",
        startYear = 1900,
        endYear = 1945,
        movementIds = listOf("expressionism", "cubism", "futurism", "dada", "surrealism", "bauhaus")
    ),
    Period(
        id = "modern_20_late",
        label = "Post-War & Contemporary (1945–2000)",
        startYear = 1945,
        endYear = 2000,
        movementIds = listOf("abstract_expressionism", "pop_art", "minimalism", "conceptual_art")
    )
)