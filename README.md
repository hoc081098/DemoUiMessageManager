# Demo UI Message Manager

This project demonstrates a robust and reusable UI message management system for Android apps using Jetpack Compose and Kotlin. It provides a clean architecture for handling transient UI messages (such as success and error notifications) in a lifecycle-aware, testable, and extensible way.

## Features
- **UiMessageManager**: Interface and default implementation for managing UI messages in a type-safe, observable, and decoupled manner.
- **Logger Support**: Pluggable logger for debugging and analytics.
- **Lifecycle Awareness**: Message collection is lifecycle-aware, preventing memory leaks and unnecessary work.
- **Composable Integration**: Easily collect and react to UI messages in your Compose UI using `CollectUiMessageFlowEffect`.
- **Sample ViewModel**: Demonstrates how to emit and clear messages from business logic.
- **Demo UI**: Simple Compose UI to trigger and display messages.

## How It Works
- The `UiMessageManager` manages a flow of `UiMessage<T>` objects, each with a unique ID and payload.
- The `DefaultUiMessageManager` provides a concrete implementation, supporting logging and message queueing.
- The `CollectUiMessageFlowEffect` composable collects messages from the flow and allows you to react to them in a lifecycle-safe way.
- The sample `MainViewModel` emits success and error messages, which are displayed in the UI.

## Usage
1. **Integrate UiMessageManager** in your ViewModel or business logic layer.
2. **Emit messages** using `emitMessage` and clear them with `clearMessage`.
3. **Collect messages** in your Compose UI using `CollectUiMessageFlowEffect`.

## Example
```kotlin
// In your ViewModel
val uiMessageManager = DefaultUiMessageManager<MainEvent>()
val uiMessageFlow = uiMessageManager.uiMessageFlow

// In your Composable
CollectUiMessageFlowEffect(uiMessageFlow = viewModel.uiMessageFlow) { message ->
    // Handle message (e.g., show Snackbar, update UI)
    viewModel.clearMessage(id = message.id)
}
```

## Reference & Original Source
This project is inspired by and adapted from the excellent work in the Tivi project by Chris Banes:
- https://github.com/chrisbanes/tivi/blob/main/common/ui/compose/src/commonMain/kotlin/app/tivi/common/compose/UiMessage.kt

## Requirements
- Kotlin
- Jetpack Compose
- AndroidX Lifecycle

## License
MIT License
