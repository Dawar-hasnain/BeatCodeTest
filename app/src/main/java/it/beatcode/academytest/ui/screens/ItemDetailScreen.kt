package it.beatcode.academytest.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.beatcode.academytest.R
import it.beatcode.academytest.model.Item
import it.beatcode.academytest.ui.components.FavoriteButton
import it.beatcode.academytest.ui.theme.AcademyTestTheme

/**
 * Detail view for a single item: its name and a favorite toggle, under a top bar
 * whose title is the item name. Stateless — it renders [item] and reports the
 * favorite toggle upward, so the change lands in the shared ViewModel state and is
 * reflected in the list at the same time.
 *
 * @param showBackButton true in single-pane mode (phone portrait); false when the
 *   list is already visible next to the detail (tablet / landscape two-pane).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    item: Item,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.delete_item),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.detail_section),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
            )
            ElevatedCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.name_label),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.favorite),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.weight(1f))
                    FavoriteButton(isFavorite = item.isFavorite, onToggle = onToggleFavorite)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemDetailScreenFavoritePreview() {
    AcademyTestTheme {
        ItemDetailScreen(
            item = Item(creationIndex = 0, name = "Lupo 🐺", isFavorite = true),
            onToggleFavorite = {},
            onDelete = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemDetailScreenNotFavoritePreview() {
    AcademyTestTheme {
        ItemDetailScreen(
            item = Item(creationIndex = 0, name = "Álgebra", isFavorite = false),
            onToggleFavorite = {},
            onDelete = {},
            onBack = {},
        )
    }
}
