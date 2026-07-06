package it.beatcode.academytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import it.beatcode.academytest.ui.screens.ItemsApp
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
                ItemsApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
