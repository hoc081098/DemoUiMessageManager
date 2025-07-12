package com.hoc081098.demouimessagemanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MainViewModel : ViewModel() {
    private val uiMessageManager = DefaultUiMessageManager<MainEvent>(
        logger = UiMessageManager.Logger.androidLog(TAG)
    )
    val uiMessageFlow = uiMessageManager.uiMessageFlow

    private var count = 0

    fun performLogic() {
        viewModelScope.launch {
            repeat(10) { index ->
                delay(2_000) // Simulate some work

                if (count++ % 2 == 0) {
                    uiMessageManager.emitMessage(
                        UiMessage(
                            message = MainEvent.Success(
                                payload = "count=$count and index=$index"
                            )
                        )
                    )
                } else {
                    uiMessageManager.emitMessage(
                        UiMessage(
                            message = MainEvent.Error(
                                payload = "count=$count and index=$index"
                            )
                        )
                    )
                }
            }
        }
    }

    fun clearMessage(id: Uuid) = uiMessageManager.clearMessage(id)

    companion object {
        private const val TAG = "MainViewModel"
    }
}