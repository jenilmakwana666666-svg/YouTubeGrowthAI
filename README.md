# YouTube Growth AI

YouTube Growth AI is a lightweight Android application for creating YouTube Shorts content ideas offline.

## Package

com.itsdark.youtubegrowthai

## Features

- 10 Shorts title ideas
- SEO-friendly description
- Opening hook
- Hashtags
- Search keywords
- Thumbnail text
- CTA
- Growth tips
- Alternative title styles
- Copy individual sections
- Copy all results
- Offline-first architecture
- GGUF model path management
- No API keys
- No Firebase
- No analytics or tracking

## Target Niche

- Anime
- Dragon Ball
- Goku
- Vegeta
- Broly
- Frieza
- Naruto
- Bleach
- Anime edits
- YouTube Shorts

## Android Requirements

- Android 6.0 / API 23 or newer
- Recommended: 3 GB RAM or more
- Keep at least 1 GB free storage for the application and model.

## Open in Android Studio

1. Download or clone this repository.
2. Open the `YouTubeGrowthAI` project folder in Android Studio.
3. Allow Android Studio to sync Gradle.
4. Connect an Android phone or use an emulator.
5. Press Run.

## GitHub Actions

The repository contains:

`.github/workflows/build-apk.yml`

The workflow:

1. Checks out the repository.
2. Installs Java 17.
3. Sets up Android SDK.
4. Installs Android API 35 and build tools.
5. Installs Gradle 8.7.
6. Builds the debug APK.
7. Uploads the APK as a GitHub Actions artifact.

To run manually:

GitHub → Actions → Build Android APK → Run workflow.

## Download APK

After a successful workflow:

Actions → Build Android APK → successful run → Artifacts → YouTubeGrowthAI-debug.

## Offline operation

The application does not require a web API for its core architecture.

Topics are not sent to external servers.

No API key is required.

## GGUF model

The application reserves a private local model directory:

`/data/data/com.itsdark.youtubegrowthai/files/models/`

The configured default model filename is:

`model.gguf`

Only one model should be used on a low-storage phone.

Recommended model range for a 3 GB RAM phone:

- Approximately 0.5 GB to 1.5 GB
- Prefer a small quantized model
- Avoid large 7B/8B models unless the device can handle them

## Important GGUF note

A GGUF file by itself cannot be executed by Android/Kotlin.

A native inference engine such as llama.cpp must be connected through Android JNI/NDK before the GGUF model can actually generate text.

The application keeps model management separate from the generation controller so that native inference can be added without changing the UI architecture.

## Security

- No API keys
- No passwords
- No analytics
- No tracking
- No Firebase
- No external AI API
- No topic uploads

## Performance

For low-end phones:

- Use one small quantized model.
- Keep generation token limits modest.
- Avoid loading multiple models.
- Close other memory-heavy applications before local inference.
- Keep at least 1 GB free storage.

## Troubleshooting

### GitHub Actions fails

Open:

GitHub → Actions → Build Android APK → failed job

Read the first error in the Gradle build step.

### Model not found

Check that the configured GGUF model exists at the path reported by the model manager.

### Generation is slow

Local LLM inference depends heavily on the phone CPU, available RAM, model size and quantization.

## License

For personal and educational use. Add an appropriate license before public distribution.
