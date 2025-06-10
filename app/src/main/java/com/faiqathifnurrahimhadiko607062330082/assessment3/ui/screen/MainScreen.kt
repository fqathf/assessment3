package com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen // Sesuaikan package jika perlu

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faiqathifnurrahimhadiko607062330082.assessment3.R // Impor R
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player // Impor model Player
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.LfcApiStatus // Impor enum Status
// Impor PlayerDetailDialog. Sesuaikan path jika berbeda.
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.common.PlayerDetailDialog // Pastikan path ini benar
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.theme.Assessment3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel() // Menggunakan MainViewModel
) {
    val players by mainViewModel.players.observeAsState(emptyList())
    val apiStatus by mainViewModel.playerStatus.observeAsState()
    val selectedPlayer by mainViewModel.selectedPlayer.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.liverpool_players)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (apiStatus) {
                LfcApiStatus.LOADING -> {
                    LoadingIndicator()
                }
                LfcApiStatus.SUCCESS -> {
                    if (players.isEmpty() && selectedPlayer == null) {
                        EmptyState(message = stringResource(R.string.no_players_found))
                    } else {
                        PlayerList(
                            players = players,
                            onPlayerClick = { player ->
                                mainViewModel.onPlayerSelected(player)
                            }
                        )
                    }
                }
                LfcApiStatus.FAILED -> {
                    ErrorState(
                        message = stringResource(R.string.error_loading_players),
                        onRetry = { mainViewModel.fetchLiverpoolPlayers() }
                    )
                }
                null -> { // Handle initial state or unknown state
                    LoadingIndicator()
                }
            }

            // Tampilkan dialog jika ada pemain yang dipilih
            selectedPlayer?.let { player ->
                PlayerDetailDialog(
                    player = player,
                    onDismissRequest = { mainViewModel.onDialogDismiss() }
                )
            }
        }
    }
}

@Composable
fun PlayerList(
    players: List<Player>, // Menggunakan model Player Anda
    onPlayerClick: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(players, key = { player -> player.id }) { player ->
            PlayerItem(player = player, onClick = { onPlayerClick(player) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerItem(
    player: Player, // Menggunakan model Player Anda
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = player.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "#${player.number}", // Menampilkan nomor pemain
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, fontSize = 18.sp, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Assessment3Theme {
        MainScreen()
    }
}