package it.beatcode.academytest.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import it.beatcode.academytest.R
import it.beatcode.academytest.ui.theme.AcademyTestTheme
import it.beatcode.academytest.ui.theme.FavoriteYellow

/**
 * A star button that toggles favorite state. Stateless: it renders [isFavorite]
 * and reports taps via [onToggle]; the owner (ViewModel-backed state) decides what
 * changes. This "state hoisting" is why the same toggle works identically in the
 * list row and the detail screen.
 */
@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onToggle, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = stringResource(
                if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites,
            ),
            tint = if (isFavorite) FavoriteYellow else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteButtonFavoritePreview() {
    AcademyTestTheme {
        FavoriteButton(isFavorite = true, onToggle = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteButtonNotFavoritePreview() {
    AcademyTestTheme {
        FavoriteButton(isFavorite = false, onToggle = {})
    }
}
