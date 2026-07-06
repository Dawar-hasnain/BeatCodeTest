package it.beatcode.academytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.beatcode.academytest.ui.theme.AcademyTestTheme

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Placeholder(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/** Temporary scaffold placeholder — replaced by the real screens in later feature branches. */
@Composable
fun Placeholder(modifier: Modifier = Modifier) {
    Text(
        text = "AcademyTest — scaffold ready",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

// @Preview renders this composable in the IDE without running the app on a device.
@Preview(showBackground = true)
@Composable
fun PlaceholderPreview() {
    AcademyTestTheme {
        Placeholder()
    }
}
