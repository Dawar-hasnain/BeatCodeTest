// Top-level build file. Plugins are declared here with `apply false` so their
// versions are fixed once for the whole project; each module then applies the
// ones it needs (see app/build.gradle.kts).
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
