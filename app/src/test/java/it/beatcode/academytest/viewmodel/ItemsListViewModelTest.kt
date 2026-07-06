package it.beatcode.academytest.viewmodel

import it.beatcode.academytest.model.Item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemsListViewModelTest {

    private fun vmWith(vararg names: String) = ItemsListViewModel(
        initialItems = names.mapIndexed { index, name -> Item(creationIndex = index, name = name) },
    )

    private val ItemsListViewModel.state get() = uiState.value

    // --- Sorting -----------------------------------------------------------

    @Test
    fun `default items are sorted alphabetically by name`() {
        val vm = ItemsListViewModel()
        assertEquals(
            listOf("Giraffa 🦒", "Leone 🦁", "Lupo 🐺"),
            vm.state.sortedItems.map { it.name },
        )
    }

    @Test
    fun `sorting ignores case`() {
        val vm = vmWith("banana", "Apple", "cherry")
        assertEquals(
            listOf("Apple", "banana", "cherry"),
            vm.state.sortedItems.map { it.name },
        )
    }

    @Test
    fun `equal names are ordered by creation index`() {
        val vm = ItemsListViewModel(
            initialItems = listOf(
                Item(creationIndex = 5, name = "Alpha"),
                Item(creationIndex = 2, name = "alpha"),
            ),
        )
        assertEquals(listOf(2, 5), vm.state.sortedItems.map { it.creationIndex })
    }

    // --- Adding ------------------------------------------------------------

    @Test
    fun `addItem trims whitespace and is not favorite`() {
        val vm = vmWith()
        val added = vm.addItem("  Zebra  ")

        assertEquals("Zebra", added.name)
        assertFalse(added.isFavorite)
        assertEquals(1, vm.state.items.size)
    }

    @Test
    fun `addItem keeps creation indices increasing`() {
        val vm = ItemsListViewModel() // seed max creationIndex is 2
        val first = vm.addItem("A")
        val second = vm.addItem("B")

        assertEquals(3, first.creationIndex)
        assertEquals(4, second.creationIndex)
    }

    // --- Favorites (the "reflected everywhere" guarantee) ------------------

    @Test
    fun `toggleFavorite flips only the targeted item`() {
        val vm = ItemsListViewModel()
        val giraffa = vm.state.items.first { it.name.startsWith("Giraffa") }

        vm.toggleFavorite(giraffa.id)

        assertTrue(vm.state.items.first { it.id == giraffa.id }.isFavorite)
        // Only Lupo (seeded favorite) plus Giraffa are now favorites.
        assertEquals(2, vm.state.items.count { it.isFavorite })
    }

    @Test
    fun `toggleFavorite twice returns to original`() {
        val vm = ItemsListViewModel()
        val leone = vm.state.items.first { it.name.startsWith("Leone") }

        vm.toggleFavorite(leone.id)
        vm.toggleFavorite(leone.id)

        assertFalse(vm.state.items.first { it.id == leone.id }.isFavorite)
    }

    // --- Selection & deletion ---------------------------------------------

    @Test
    fun `deleting the selected item from the list selects the first remaining`() {
        val vm = ItemsListViewModel()
        val lupo = vm.state.items.first { it.name.startsWith("Lupo") }
        vm.select(lupo.id)

        vm.delete(lupo.id)

        // Remaining sorted -> Giraffa, Leone; first is Giraffa.
        assertEquals("Giraffa 🦒", vm.state.selectedItem?.name)
    }

    @Test
    fun `deleting a non-selected item keeps the current selection`() {
        val vm = ItemsListViewModel()
        val giraffa = vm.state.items.first { it.name.startsWith("Giraffa") }
        val leone = vm.state.items.first { it.name.startsWith("Leone") }
        vm.select(giraffa.id)

        vm.delete(leone.id)

        assertEquals(giraffa.id, vm.state.selectedItemId)
    }

    @Test
    fun `deleting the only item clears the selection`() {
        val vm = vmWith("Solo")
        val solo = vm.state.items.first()
        vm.select(solo.id)

        vm.delete(solo.id)

        assertNull(vm.state.selectedItemId)
        assertTrue(vm.state.items.isEmpty())
    }

    @Test
    fun `deleteFromDetail removes the item and clears the selection`() {
        val vm = ItemsListViewModel()
        val lupo = vm.state.items.first { it.name.startsWith("Lupo") }
        vm.select(lupo.id)

        vm.deleteFromDetail(lupo.id)

        assertNull(vm.state.selectedItemId)
        assertEquals(2, vm.state.items.size)
    }
}
