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

    fun loadSongsIfNeeded() {
        Log.d("HOME_FLOW", "loadSongsIfNeeded called")
        Log.d("HOME_FLOW", "Songs size: ${_state.value.songs.size}")
        if (_state.value.songs.isNotEmpty()) return
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val songs = repository.getSongs()
            Log.d("HOME_FLOW", "Loaded songs: ${songs.size}")
            Log.d("CHECK_FLOW", "Songs in ViewModel: ${songs.size}")
            _state.value = _state.value.copy(songs = songs)
        }
    }

    fun reloadSongs() {
        repository.clearCache()
        loadSongs()
    }
}
