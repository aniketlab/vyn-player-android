package com.vyn.player.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyn.player.data.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: SongRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    private var isReloading = false

    fun loadSongsIfNeeded() {
        Log.d("HOME_FLOW", "loadSongsIfNeeded called")
        if (_state.value.songs.isNotEmpty()) {
            Log.d("HOME_FLOW", "Songs already present: ${_state.value.songs.size}")
            return
        }

        loadSongs()
    }

    fun loadSongs() {
        Log.d("HOME_FLOW", "loadSongs() CALLED")

        viewModelScope.launch(Dispatchers.IO) {
            val songs = repository.getSongs()
            Log.d("HOME_FLOW", "Loaded songs: ${songs.size}")
            Log.d("CHECK_FLOW", "Songs in ViewModel: ${songs.size}")
            _state.value = _state.value.copy(songs = songs)
        }
    }

    fun reloadSongs() {
        if (isReloading) return
        isReloading = true

        Log.d("HOME_FLOW", "Manual reload triggered")
        repository.clearCache()
        loadSongs()
        isReloading = false
    }
}
