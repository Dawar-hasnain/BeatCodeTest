package it.beatcode.academytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import it.beatcode.academytest.ui.screens.ItemsListScreen
import it.beatcode.academytest.ui.theme.AcademyTestTheme
import it.beatcode.academytest.viewmodel.ItemsListViewModel

/**
 * The single Activity that hosts all Compose UI.
 * (Compose apps are typically single-Activity; screens are composables, not Activities.)
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // draw behind the system bars for a modern edge-to-edge look
        setContent {
            AcademyTestTheme {
                AcademyApp()
            }
        }
    }
}

@Composable
private fun AcademyApp(
    viewModel: ItemsListViewModel = viewModel(),
) {
    // Observe the ViewModel's single source of truth, lifecycle-aware.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ItemsListScreen(
        uiState = uiState,
        onItemClick = viewModel::select, // selection only for now; detail pane arrives next branch
        onToggleFavorite = viewModel::toggleFavorite,
        onAddItem = { /* TODO(feat/add-item-sheet): present the add-item sheet */ },
        modifier = Modifier.fillMaxSize(),
    )
}
