package com.faiqathifnurrahimhadiko607062330082.assessment3.ui.common // Atau package lain yang sesuai

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.faiqathifnurrahimhadiko607062330082.assessment3.R // Impor R dari aplikasi Anda
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player // Impor model Player

@Composable
fun PlayerDetailDialog(
    player: Player, // Menggunakan model Player Anda
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = player.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(player.photoUrl) // Menggunakan photoUrl dari model Player
                        .crossfade(true)
                        .build(),
                    contentDescription = player.name,
                    contentScale = ContentScale.Crop,
                    // Pastikan drawable ini ada di res/drawable
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_img),
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )
                // Pastikan string resources ini ada di res/values/strings.xml
                PlayerDetailRow(label = stringResource(R.string.player_number), value = player.number.toString())
                PlayerDetailRow(label = stringResource(R.string.player_position), value = player.position)
                PlayerDetailRow(label = stringResource(R.string.player_nationality), value = player.nationality)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.close)) // Pastikan string resource ini ada
            }
        }
    )
}

@Composable
fun PlayerDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}