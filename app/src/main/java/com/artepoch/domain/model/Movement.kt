// File: app/src/main/java/com/artepoch/domain/model/Movement.kt
package com.artepoch.domain.model

data class Movement(
    val id: String,
    val label: String,
    val wikiCategory: String // Wikimedia Commons kategori adı
)

// Tüm akımlar - Wikimedia Commons kategorileriyle
val AllMovements = listOf(
    // Renaissance & Mannerism
    Movement(
        id = "renaissance",
        label = "Renaissance",
        wikiCategory = "Renaissance paintings"
    ),
    Movement(
        id = "mannerism",
        label = "Mannerism",
        wikiCategory = "Mannerist paintings"
    ),

    // Baroque & Rococo
    Movement(
        id = "baroque",
        label = "Baroque",
        wikiCategory = "Baroque paintings"
    ),
    Movement(
        id = "rococo",
        label = "Rococo",
        wikiCategory = "Rococo paintings"
    ),

    // Neoclassicism & Romanticism
    Movement(
        id = "neoclassicism",
        label = "Neoclassicism",
        wikiCategory = "Neoclassical paintings"
    ),
    Movement(
        id = "romanticism",
        label = "Romanticism",
        wikiCategory = "Romantic paintings"
    ),

    // 19th Century
    Movement(
        id = "realism",
        label = "Realism",
        wikiCategory = "Realist paintings"
    ),
    Movement(
        id = "impressionism",
        label = "Impressionism",
        wikiCategory = "Impressionist paintings"
    ),
    Movement(
        id = "post_impressionism",
        label = "Post-Impressionism",
        wikiCategory = "Post-Impressionist paintings"
    ),
    Movement(
        id = "art_nouveau",
        label = "Art Nouveau",
        wikiCategory = "Art Nouveau paintings"
    ),

    // Early 20th Century
    Movement(
        id = "expressionism",
        label = "Expressionism",
        wikiCategory = "Expressionist paintings"
    ),
    Movement(
        id = "cubism",
        label = "Cubism",
        wikiCategory = "Cubist paintings"
    ),
    Movement(
        id = "futurism",
        label = "Futurism",
        wikiCategory = "Futurist paintings"
    ),
    Movement(
        id = "dada",
        label = "Dadaism",
        wikiCategory = "Dada paintings"
    ),
    Movement(
        id = "surrealism",
        label = "Surrealism",
        wikiCategory = "Surrealist paintings"
    ),
    Movement(
        id = "bauhaus",
        label = "Bauhaus",
        wikiCategory = "Bauhaus paintings"
    ),

    // Post-War & Contemporary
    Movement(
        id = "abstract_expressionism",
        label = "Abstract Expressionism",
        wikiCategory = "Abstract expressionist paintings"
    ),
    Movement(
        id = "pop_art",
        label = "Pop Art",
        wikiCategory = "Pop art paintings"
    ),
    Movement(
        id = "minimalism",
        label = "Minimalism",
        wikiCategory = "Minimalist paintings"
    ),
    Movement(
        id = "conceptual_art",
        label = "Conceptual Art",
        wikiCategory = "Conceptual art"
    )
)

// Belirli bir dönem için uygun akımları getir
fun getMovementsForPeriod(period: Period): List<Movement> {
    return AllMovements.filter { it.id in period.movementIds }
}