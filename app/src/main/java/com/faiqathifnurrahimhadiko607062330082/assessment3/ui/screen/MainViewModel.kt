package com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen // Sesuaikan package jika perlu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player // Impor model Player
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.LfcApi // Impor objek Api
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.LfcApiStatus // Impor enum Status
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _playerStatus = MutableLiveData<LfcApiStatus>()
    val playerStatus: LiveData<LfcApiStatus> = _playerStatus

    private val _players = MutableLiveData<List<Player>>() // Menggunakan model Player Anda
    val players: LiveData<List<Player>> = _players

    private val _selectedPlayer = MutableLiveData<Player?>() // Menggunakan model Player Anda
    val selectedPlayer: LiveData<Player?> = _selectedPlayer

    init {
        fetchLiverpoolPlayers()
    }

    fun fetchLiverpoolPlayers() {
        viewModelScope.launch {
            _playerStatus.value = LfcApiStatus.LOADING
            try {
                val playerList = LfcApi.service.getPlayers()
                _players.value = playerList
                _playerStatus.value = LfcApiStatus.SUCCESS
                Log.d("MainViewModel", "Players fetched: ${playerList.size}")
            } catch (e: Exception) {
                _players.value = emptyList()
                _playerStatus.value = LfcApiStatus.FAILED
                Log.e("MainViewModel", "Error fetching players", e)
            }
        }
    }

    fun onPlayerSelected(player: Player) { // Menggunakan model Player Anda
        _selectedPlayer.value = player
    }

    fun onDialogDismiss() {
        _selectedPlayer.value = null
    }
}