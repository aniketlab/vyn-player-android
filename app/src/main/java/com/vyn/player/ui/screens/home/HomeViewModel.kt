package com.vyn.player.ui.screens.home

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
            val songs = getSongsUseCase()
            _state.value = HomeState(
                songs = songs,
                hasPermission = true,
            )
        }
    }
}
