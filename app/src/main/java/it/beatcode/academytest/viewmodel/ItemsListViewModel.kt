package it.beatcode.academytest.viewmodel

import androidx.lifecycle.ViewModel
import it.beatcode.academytest.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Owns the list of items and exposes it as a [StateFlow]. This is the single
 * source of truth: the list screen and the detail screen both observe [uiState],
 * and every mutation flows back through the functions here, so a change made in
 * one place is reflected everywhere.
 *
 * @param initialItems seed data (defaults to [defaultItems]); injectable for tests.
 */
class ItemsListViewModel(
    initialItems: List<Item> = defaultItems,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemsListUiState(items = initialItems))
    val uiState: StateFlow<ItemsListUiState> = _uiState.asStateFlow()

    /** Next value for a new item's creationIndex; keeps insertion order monotonic. */
    private var nextCreationIndex: Int = (initialItems.maxOfOrNull { it.creationIndex } ?: -1) + 1

    /** Adds a new (non-favorite) item, trimming surrounding whitespace. Returns it. */
    fun addItem(name: String): Item {
        val newItem = Item(
            creationIndex = nextCreationIndex++,
            name = name.trim(),
            isFavorite = false,
        )
        _uiState.update { it.copy(items = it.items + newItem) }
        return newItem
    }

    /** Flips the favorite flag of the item with [id]; leaves all others untouched. */
    fun toggleFavorite(id: String) {
        _uiState.update { state ->
            state.copy(
                items = state.items.map { item ->
                    if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
                },
            )
        }
    }

    /** Sets (or clears, with null) which item the detail pane shows. */
    fun select(id: String?) {
        _uiState.update { it.copy(selectedItemId = id) }
    }

    /**
     * Deletes from the list (swipe-to-delete). If the removed item was selected,
     * selection falls back to the first remaining item in display order — matching
     * the SwiftUI behavior.
     */
    fun delete(id: String) {
        _uiState.update { state ->
            val remaining = state.items.filterNot { it.id == id }
            val newSelectedId = if (state.selectedItemId == id) {
                ItemsListUiState(items = remaining).sortedItems.firstOrNull()?.id
            } else {
                state.selectedItemId
            }
            state.copy(items = remaining, selectedItemId = newSelectedId)
        }
    }

    /** Deletes from the detail screen: removes the item and clears the selection. */
    fun deleteFromDetail(id: String) {
        _uiState.update { state ->
            state.copy(
                items = state.items.filterNot { it.id == id },
                selectedItemId = null,
            )
        }
    }

    companion object {
        /** Seed data mirroring the SwiftUI sample (Italian names + emoji). */
        val defaultItems: List<Item> = listOf(
            Item(creationIndex = 0, name = "Lupo 🐺", isFavorite = true),
            Item(creationIndex = 1, name = "Giraffa 🦒", isFavorite = false),
            Item(creationIndex = 2, name = "Leone 🦁", isFavorite = false),
        )
    }
}
