package com.marchcode.evaluateuitest.data.model

/**
 * Sealed class representing different UI states for better testability
 * and deterministic behavior.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}