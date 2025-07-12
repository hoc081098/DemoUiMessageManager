package com.hoc081098.demouimessagemanager

import androidx.compose.runtime.Immutable

@Immutable
sealed interface MainEvent {
    data class Success(val payload: String) : MainEvent
    data class Error(val payload: String) : MainEvent
}