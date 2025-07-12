package com.hoc081098.demouimessagemanager

import android.util.Log
import androidx.annotation.AnyThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UiMessage<out T> @OptIn(ExperimentalUuidApi::class) constructor(
    val message: T,
    val id: Uuid = Uuid.random()
)

@AnyThread
@OptIn(ExperimentalUuidApi::class)
interface UiMessageManager<T> {
    /**
     * A flow emitting the current message to display.
     */
    val uiMessageFlow: Flow<UiMessage<T>?>

    /**
     * Emit a new message to display.
     */
    fun emitMessage(message: UiMessage<T>)

    /**
     * Clear a message by its ID.
     */
    fun clearMessage(id: Uuid)

    interface Logger {
        fun log(message: String)

        companion object {
            val NOOP: Logger = object : Logger {
                override fun log(message: String) = Unit
            }

            val STDOUT: Logger = object : Logger {
                override fun log(message: String) = println(message)
            }

            fun androidLog(tag: String): Logger = AndroidLogger(tag)

            private class AndroidLogger(private val tag: String) : Logger {
                override fun log(message: String) = Log.d(tag, message).let { }
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
class DefaultUiMessageManager<T>(
    private val logger: UiMessageManager.Logger = UiMessageManager.Logger.NOOP,
) : UiMessageManager<T> {
    private val _messagesStateFlow = MutableStateFlow(emptyList<UiMessage<T>>())

    /**
     * A flow emitting the current message to display.
     */
    override val uiMessageFlow: Flow<UiMessage<T>?> =
        _messagesStateFlow
            .map { it.firstOrNull() }
            .distinctUntilChanged()

    override fun emitMessage(message: UiMessage<T>) =
        _messagesStateFlow.update { it + message }
            .also { logger.log("emitMessage: $message") }

    override fun clearMessage(id: Uuid) =
        _messagesStateFlow.update { messages -> messages.filter { it.id != id } }
            .also { logger.log("clearMessage: $id") }

}

@Composable
fun <T> CollectUiMessageFlowEffect(
    uiMessageFlow: Flow<UiMessage<T>?>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onMessage: suspend (uiMessage: UiMessage<T>) -> Unit,
) {
    LaunchedEffect(uiMessageFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiMessageFlow.collect { message ->
                message?.let { onMessage(it) }
            }
        }
    }
}
