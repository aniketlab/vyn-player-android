package com.vyn.player.ui.onboarding

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel(
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _state = MutableStateFlow(
        OnboardingState(
            isCompleted = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false),
        ),
    )
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.NextClicked -> onNextClicked()
            OnboardingEvent.PermissionGranted -> onPermissionGranted()
            OnboardingEvent.Finish -> onFinish()
        }
    }

    private fun onNextClicked() {
        val currentState = _state.value
        if (currentState.currentPage >= LAST_PAGE_INDEX) return

        _state.value = currentState.copy(currentPage = currentState.currentPage + 1)
        Log.d("ONBOARDING_FLOW", "Next clicked -> page=${_state.value.currentPage}")
    }

    private fun onPermissionGranted() {
        _state.value = _state.value.copy(isPermissionGranted = true)
        Log.d("ONBOARDING_FLOW", "Permission granted")
    }

    private fun onFinish() {
        sharedPreferences.edit()
            .putBoolean(KEY_ONBOARDING_COMPLETED, true)
            .apply()

        _state.value = _state.value.copy(isCompleted = true)
        Log.d("ONBOARDING_FLOW", "Onboarding finished")
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
        private const val LAST_PAGE_INDEX = 2
    }
}
