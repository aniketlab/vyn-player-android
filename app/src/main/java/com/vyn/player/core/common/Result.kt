package com.vyn.player.core.common

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val throwable: Throwable? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}