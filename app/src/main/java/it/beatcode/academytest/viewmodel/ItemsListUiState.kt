package it.beatcode.academytest.viewmodel

import it.beatcode.academytest.model.Item
import java.text.Collator

/**
 * Immutable snapshot of everything the UI needs to render. The ViewModel exposes
 * this as a StateFlow; the list and detail screens both read from it.
 *
 * @param items the raw, unsorted items (single source of truth).
 * @param selectedItemId id of the item shown in the detail pane, or null for none.
 */
data class ItemsListUiState(
    val items: List<Item> = emptyList(),
    val selectedItemId: String? = null,
) {
    /** Items sorted for display: by name, then by insertion order. See [itemComparator]. */
    val sortedItems: List<Item>
        get() = items.sortedWith(itemComparator)

    /** The currently selected item, if any. */
    val selectedItem: Item?
        get() = items.firstOrNull { it.id == selectedItemId }

    companion object {
        /**
         * Case-insensitive, locale-aware name ordering with a creation-order tie-break —
         * the Kotlin equivalent of Swift's `localizedCaseInsensitiveCompare` followed by
         * comparing `creationIndex`.
         *
         * [Collator] gives locale-correct ordering; SECONDARY strength makes it
         * case-insensitive while still distinguishing accents (e.g. "Álgebra").
         */
        private val collator: Collator = Collator.getInstance().apply {
            strength = Collator.SECONDARY
        }

        private val itemComparator: Comparator<Item> = Comparator { a, b ->
            val byName = collator.compare(a.name, b.name)
            if (byName != 0) byName else a.creationIndex.compareTo(b.creationIndex)
        }
    }
}
