package it.beatcode.academytest.model

import java.util.UUID

/**
 * A single list item.
 *
 * Immutable by design: unlike the SwiftUI original (a mutable `@Observable` class),
 * the Compose approach keeps items as read-only values. To "change" an item we
 * produce a modified copy via [copy] and hand it back to the ViewModel, which owns
 * the single source of truth. Both the list and the detail screen render from that
 * one source, so any change is reflected everywhere automatically.
 *
 * @param id stable identity (used for selection & updates); equality/selection key.
 * @param creationIndex insertion order; used as the tie-breaker when names are equal.
 */
data class Item(
    val id: String = UUID.randomUUID().toString(),
    val creationIndex: Int,
    val name: String,
    val isFavorite: Boolean = false,
)
