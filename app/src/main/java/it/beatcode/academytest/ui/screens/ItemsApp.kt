package it.beatcode.academytest.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import it.beatcode.academytest.R
import it.beatcode.academytest.ui.components.EmptyState
import it.beatcode.academytest.viewmodel.ItemsListViewModel
import kotlinx.coroutines.launch

/**
 * Top-level screen: an adaptive list/detail layout (the Compose equivalent of
 * SwiftUI's NavigationSplitView). On large widths the list and detail show side by
 * side; on a phone in portrait the detail slides in over the list and Back returns.
 *
 * Both panes read from a single [ItemsListViewModel], so a favorite toggled in the
 * list is reflected in the detail and vice versa — the core requirement of the task.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ItemsApp(
    modifier: Modifier = Modifier,
    viewModel: ItemsListViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // NavigableListDetailPaneScaffold expects a ThreePaneScaffoldNavigator<Any>.
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()
    var showAddSheet by rememberSaveable { mutableStateOf(false) }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        modifier = modifier,
        listPane = {
            AnimatedPane {
                ItemsListScreen(
                    uiState = uiState,
                    onItemClick = { id ->
                        viewModel.select(id)
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                        }
                    },
                    onToggleFavorite = viewModel::toggleFavorite,
                    onDeleteItem = viewModel::delete,
                    onAddItem = { showAddSheet = true },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selected = uiState.selectedItem
                if (selected != null) {
                    ItemDetailScreen(
                        item = selected,
                        showBackButton = navigator.canNavigateBack(),
                        onBack = { scope.launch { navigator.navigateBack() } },
                        onToggleFavorite = { viewModel.toggleFavorite(selected.id) },
                        onDelete = {
                            viewModel.deleteFromDetail(selected.id)
                            // On a phone the detail covers the list; return to it.
                            scope.launch {
                                if (navigator.canNavigateBack()) navigator.navigateBack()
                            }
                        },
                    )
                } else {
                    // No selection: mirrors SwiftUI's "Nessun oggetto" detail placeholder.
                    EmptyState(
                        icon = Icons.Outlined.Inbox,
                        title = stringResource(R.string.empty_title),
                        description = stringResource(R.string.empty_detail_description),
                    )
                }
            }
        },
    )

    if (showAddSheet) {
        AddItemSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name ->
                viewModel.addItem(name)
                showAddSheet = false
            },
        )
    }
}
