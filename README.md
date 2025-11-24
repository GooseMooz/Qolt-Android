 # Qolt

## Features

- NFC-based app blocking/unblocking
- Integration with Android screen time APIs
- Usage statistics and analytics
- Customizable widgets for productivity metrics
- Session tracking and history

## Tech Stack

### Core
- **Language**: Kotlin
- **Min SDK**: 29 (Android 10)
- **Target SDK**: 36
- **UI Framework**: Jetpack Compose with Material 3

### Architecture
- **Pattern**: Clean Architecture (Data/Domain/UI layers)
- **DI**: Hilt (Dagger)
- **Async**: Kotlin Coroutines + Flow
- **Navigation**: Navigation Compose

### Data & Persistence
- **Local Database**: Room
- **Preferences**: DataStore (Preferences)
- **Background Work**: WorkManager

### Key Libraries
- **Lifecycle**: ViewModel, LiveData, Lifecycle-aware components
- **Permissions**: Accompanist Permissions
- **Logging**: Timber
- **Code Quality**: Detekt

### Testing
- **Unit Tests**: JUnit, MockK, Truth, Turbine
- **Integration Tests**: Room Testing
- **UI Tests**: Compose UI Testing, Espresso

## Project Structure

```
app/src/main/java/ca/qolt/
├── data/
│   ├── local/
│   │   ├── dao/              # Room DAOs
│   │   ├── entity/           # Room entities
│   │   └── QoltDatabase.kt   # Room database
│   └── repository/           # Repository implementations
├── domain/
│   ├── model/                # Domain models
│   ├── repository/           # Repository interfaces
│   └── usecase/              # Business logic use cases
├── ui/
│   └── theme/                # Compose theme
├── di/                       # Hilt modules
├── util/                     # Utilities & extensions
├── MainActivity.kt
└── QoltApplication.kt        # Application class with Hilt
```

## Getting Started

### Prerequisites
- Android Studio Koala or later
- JDK 11 or later
- Android SDK 36
- Physical Android device with NFC capability (for testing NFC features)

### Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Qolt
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the Qolt directory

3. **Sync Gradle**
   - Android Studio should automatically sync Gradle
   - If not, click "Sync Project with Gradle Files" in the toolbar

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

### Build Variants

The project includes two build variants:

- **debug**: For development with debugging enabled
  - Application ID: `ca.qolt.debug`
  - Debuggable: `true`

- **release**: For production builds
  - Application ID: `ca.qolt`
  - Minification: Enabled
  - ProGuard: Enabled

Switch between variants: `Build > Select Build Variant`

## Development Workflow

### Code Quality

Run Detekt for code analysis:
```bash
./gradlew detekt
```

### Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

Run all tests:
```bash
./gradlew testDebugUnitTest connectedDebugAndroidTest
```

### Database Schema

Room database schemas are exported to `app/schemas/`. These should be committed to version control to track database migrations.

## Key Permissions

The app requires the following permissions:

- `NFC`: For scanning NFC tags
- `PACKAGE_USAGE_STATS`: For accessing app usage statistics
- `POST_NOTIFICATIONS`: For sending notifications (Android 13+)
- `INTERNET`: For potential cloud sync features

### Usage Stats Permission

Users must manually grant `PACKAGE_USAGE_STATS` permission:
1. Settings > Apps > Special app access > Usage access
2. Find Qolt and toggle it on

## Architecture Details

### Data Layer
- **Room Database**: Stores blocked apps, usage sessions, and statistics
- **DataStore**: Stores user preferences and settings
- **DAOs**: Provide reactive data access with Flow

### Domain Layer
- **Use Cases**: Encapsulate business logic
- **Repository Interfaces**: Define data access contracts
- **Models**: Domain-specific data models

### UI Layer
- **Jetpack Compose**: Modern declarative UI
- **ViewModel**: Manages UI state and business logic
- **Navigation**: Type-safe navigation with Navigation Compose

### Dependency Injection

Hilt modules are organized by responsibility:
- `DatabaseModule`: Provides Room database and DAOs
- `DataStoreModule`: Provides DataStore instance
- `DispatchersModule`: Provides coroutine dispatchers

## Gradle Configuration

### Version Catalog

All dependencies are managed in `gradle/libs.versions.toml` for centralized version management. This ensures consistent dependency versions across modules.

### Build Configuration

Key build features:
- **KSP**: Used for Room and Hilt annotation processing
- **Compose Compiler**: Kotlin 2.0+ with Compose plugin
- **BuildConfig**: Enabled for feature flags
- **Vector Drawables**: Support library enabled

## Contributing

1. Create a feature branch from `main`
2. Make your changes
3. Run code quality checks: `./gradlew detekt`
4. Run tests: `./gradlew test`
5. Create a pull request

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Keep functions small and focused
- Write tests for new features
- Document complex logic

## License

[Add your license here]

## Team

[Add team members here]

## Acknowledgments

- Built with Android Jetpack libraries
- Uses Material Design 3 components
