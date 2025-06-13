package com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.PlayerApi
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.PlayerApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Player>())
        private set

    var status = MutableStateFlow(PlayerApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String = "all") {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = PlayerApiStatus.LOADING
            try {
                val result = PlayerApi.service.getPlayers(userId)
                data.value = result
                status.value = PlayerApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = PlayerApiStatus.FAILED
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun saveData(userId: String, nama: String, posisi: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = PlayerApi.service.addPlayer(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    posisi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultiPartBody("foto")
                )
                if (result.status == "success") {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateData(userId: String, nama: String, posisi: String, bitmap: Bitmap, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = PlayerApi.service.updatePlayer(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    posisi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultiPartBody("foto"),
                    id
                )
                if (result.status == "success") {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(userId: String, playerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = PlayerApi.service.deletePlayer(
                    userId,
                    playerId
                )
                if (result.status == "success") {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Error delete: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultiPartBody(partName: String): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            partName, "image.jpg", requestBody)
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}