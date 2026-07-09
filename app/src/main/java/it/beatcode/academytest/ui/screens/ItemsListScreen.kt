package it.beatcode.academytest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.beatcode.academytest.R
import it.beatcode.academytest.model.Item
import it.beatcode.academytest.ui.components.EmptyState
import it.beatcode.academytest.ui.components.ItemRow
import it.beatcode.academytest.ui.theme.AcademyTestTheme
import it.beatcode.academytest.viewmodel.ItemsListUiState

/**
 * The list screen: title bar with an add action, and either the empty state or a
 * scrollable list of rows in display order. Stateless — it renders [uiState] and
 * forwards user intents through the callbacks, so it's trivial to preview and test.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsListScreen(
    uiState: ItemsListUiState,
    onItemClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDeleteItem: (String) -> Unit,
    onAddItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.items_title)) },
                actions = {
                    IconButton(onClick = onAddItem) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_item),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (uiState.items.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.Inbox,
                title = stringResource(R.string.empty_title),
                description = stringResource(R.string.empty_list_description),
                modifier = Modifier.padding(innerPadding),
                action = {
                    Button(onClick = onAddItem) {
                        Text(stringResource(R.string.add_item))
                    }
                },
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                // key = item.id lets Compose track rows across reordering (sorting).
                items(uiState.sortedItems, key = { it.id }) { item ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                onDeleteItem(item.id)
                                true
                            } else {
                                false
                            }
                        },
                    )
                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false, // only swipe left-to-delete
                        backgroundContent = { SwipeToDeleteBackground() },
                    ) {
                        // Opaque surface so the red delete background is only revealed while swiping.
                        Surface {
                            ItemRow(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onToggleFavorite = { onToggleFavorite(item.id) },
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

/** Red background with a trash icon, revealed as a row is swiped left to delete. */
@Composable
private fun SwipeToDeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}

private val previewItems = listOf(
    Item(creationIndex = 0, name = "Lupo 🐺", isFavorite = true),
    Item(creationIndex = 1, name = "Giraffa 🦒", isFavorite = false),
    Item(creationIndex = 2, name = "Leone 🦁", isFavorite = false),
)

@Preview(showBackground = true)
@Composable
private fun ItemsListScreenPreview() {
    AcademyTestTheme {
        ItemsListScreen(
            uiState = ItemsListUiState(items = previewItems),
            onItemClick = {},
            onToggleFavorite = {},
            onDeleteItem = {},
            onAddItem = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemsListScreenEmptyPreview() {
    AcademyTestTheme {
        ItemsListScreen(
            uiState = ItemsListUiState(items = emptyList()),
            onItemClick = {},
            onToggleFavorite = {},
            onDeleteItem = {},
            onAddItem = {},
        )
    }
}
