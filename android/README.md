# Kalivex Android

This is the Android client for Kalivex AI. It provides:

- Kotlin + Jetpack Compose UI
- Voice assistant UI with microphone button
- Speech-to-text using Android SpeechRecognizer
- Text-to-speech using Android TextToSpeech
- Foreground service stub for voice usage
- Encrypted token storage using EncryptedSharedPreferences
- Retrofit REST client for backend APIs
- WebSocket client for device communication (Java-WebSocket)

Security notes
- Do not store API keys or secrets in the client.
- The backend base URL must be configured via BuildConfig (see android/app/build.gradle.kts) or strings.xml.

How to open in Android Studio
- Open the project at android/ in Android Studio.
- Let Android Studio sync Gradle and download dependencies.

Run notes
- Request microphone permission at runtime before starting voice features.
- The pairing flow contacts the backend to get a device token; store it securely and do not log it.

This is a starting implementation; extend with UI, permission center, paired device listing, confirmation dialogs, and improved error handling.
