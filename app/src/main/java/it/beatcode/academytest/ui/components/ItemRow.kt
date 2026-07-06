package it.beatcode.academytest.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.beatcode.academytest.R
import it.beatcode.academytest.model.Item
import it.beatcode.academytest.ui.theme.AcademyTestTheme

/**
 * A single row in the items list: the name, a "favorite / not favorite" caption,
 * and the star toggle on the trailing edge. Tapping the row body opens the detail;
 * tapping the star toggles favorite.
 */
@Composable
fun ItemRow(
    item: Item,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(
                    if (item.isFavorite) R.string.favorite else R.string.not_favorite,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        FavoriteButton(isFavorite = item.isFavorite, onToggle = onToggleFavorite)
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemRowPreview() {
    AcademyTestTheme {
        Column {
            ItemRow(
                item = Item(creationIndex = 0, name = "Caffè", isFavorite = true),
                onClick = {},
                onToggleFavorite = {},
            )
            ItemRow(
                item = Item(creationIndex = 1, name = "Zaino", isFavorite = false),
                onClick = {},
                onToggleFavorite = {},
            )
        }
    }
}
