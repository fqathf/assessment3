package com.faiqathifnurrahimhadiko607062330082.assessment3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
// Impor MainScreen. Sesuaikan path jika berbeda.
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen.MainScreen
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.theme.Assessment3Theme // Ganti dengan nama tema Anda

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assessment3Theme { // Ganti dengan nama tema aplikasi Anda
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen() // Memanggil MainScreen
                }
            }
        }
    }
}