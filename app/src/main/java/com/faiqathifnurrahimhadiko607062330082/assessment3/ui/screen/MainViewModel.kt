package com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.LfcApi
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.LfcApiStatus
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _playerStatus = MutableLiveData<LfcApiStatus>()
    val playerStatus: LiveData<LfcApiStatus> = _playerStatus

    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> = _players

    private val _selectedPlayer = MutableLiveData<Player?>()
    val selectedPlayer: LiveData<Player?> = _selectedPlayer

    // State untuk mengontrol visibilitas dialog tambah pemain
    private val _showAddPlayerDialog = MutableLiveData<Boolean>()
    val showAddPlayerDialog: LiveData<Boolean> = _showAddPlayerDialog

    init {
        fetchLiverpoolPlayers()
        _showAddPlayerDialog.value = false // Defaultnya dialog tidak tampil
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
                _players.value = emptyList() // Atau biarkan null jika Anda menanganinya secara berbeda
                _playerStatus.value = LfcApiStatus.FAILED
                Log.e("MainViewModel", "Error fetching players", e) // <-- INI PENTING UNTUK DILIHAT DI LOGCAT
            }
        }
    }

    fun onPlayerSelected(player: Player) {
        _selectedPlayer.value = player
    }

    fun onDialogDismiss() {
        _selectedPlayer.value = null
    }

    // Fungsi untuk menampilkan dialog tambah pemain
    fun onAddPlayerClicked() {
        _showAddPlayerDialog.value = true
    }

    // Fungsi untuk menyembunyikan dialog tambah pemain
    fun onAddPlayerDialogDismiss() {
        _showAddPlayerDialog.value = false
    }

    // Fungsi untuk menambahkan pemain baru ke daftar
    // Anda mungkin perlu menyesuaikan ini tergantung bagaimana Anda ingin menangani data baru
    // (misalnya, mengirim ke API atau hanya menambahkannya secara lokal)
    fun addPlayer(newPlayer: Player) {
        viewModelScope.launch {
            // Contoh: Menambahkan pemain secara lokal
            val currentPlayers = _players.value?.toMutableList() ?: mutableListOf()
            currentPlayers.add(newPlayer)
            _players.value = currentPlayers
            // Anda mungkin ingin memanggil API untuk menyimpan pemain baru di sini
            // dan kemudian memperbarui daftar dari server jika perlu.
            Log.d("MainViewModel", "Player added: $newPlayer")
            onAddPlayerDialogDismiss() // Tutup dialog setelah pemain ditambahkan
        }
    }
}