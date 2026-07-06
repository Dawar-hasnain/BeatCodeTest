plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    // Unique identifier / Kotlin package root for generated code.
    namespace = "it.beatcode.academytest"
    // SDK version we compile against (uses the latest APIs).
    compileSdk = 35
    // Pin to an already-installed build-tools version so Gradle doesn't try to
    // auto-download one (this machine's cmdline-tools layout breaks auto-install).
    buildToolsVersion = "35.0.1"

    defaultConfig {
        applicationId = "it.beatcode.academytest"
        minSdk = 26      // oldest Android version the app runs on (8.0 Oreo)
        targetSdk = 35   // version the app is tested/optimized for
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true   // turn on Jetpack Compose for this module
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    // platform(...) applies the BOM so the Compose libs below get matched versions.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.adaptive.navigation)
    // Local JVM unit tests (sort/add/delete logic).
    testImplementation(libs.junit)
    // Tooling only bundled into debug builds (drives the @Preview renderer).
    debugImplementation(libs.androidx.ui.tooling)
}
