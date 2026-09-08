plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.kalivex.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kalivex.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"

        // Backend base URL supplied via build config or strings.xml at runtime; do NOT hardcode keys here.
        buildConfigField("String", "BACKEND_BASE_URL", "\"https://your-backend-host.example.com\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.10"
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.compose.ui:ui:1.6.0-alpha01")
    implementation("androidx.compose.material:material:1.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // WebSocket
    implementation("org.java-websocket:Java-WebSocket:1.5.3")

    // Security: EncryptedSharedPreferences
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.31.5-beta")

    // Tests
    testImplementation("junit:junit:4.13.2")
}
