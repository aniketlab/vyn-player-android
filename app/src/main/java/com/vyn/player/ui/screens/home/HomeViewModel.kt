package com.vyn.player.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyn.player.domain.usecase.GetSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getSongsUseCase: GetSongsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun loadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DEBUG", "HomeViewModel.loadSongs() called")
            val songs = getSongsUseCase()
            Log.d("CHECK_FLOW", "Songs in ViewModel: ${songs.size}")
            Log.d("DEBUG", "HomeViewModel.loadSongs() result count=${songs.size}")
            _state.value = HomeState(
                songs = songs,
                hasPermission = true,
            )
        }
    }
}
