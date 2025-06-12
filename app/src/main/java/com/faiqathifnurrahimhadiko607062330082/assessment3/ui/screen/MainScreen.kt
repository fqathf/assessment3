package com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.faiqathifnurrahimhadiko607062330082.assessment3.R
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.LfcApiStatus
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.theme.Assessment3Theme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val players by viewModel.players.observeAsState(emptyList())
    val status by viewModel.playerStatus.observeAsState(LfcApiStatus.LOADING)
    val selectedPlayer by viewModel.selectedPlayer.observeAsState()
    val showAddPlayerDialog by viewModel.showAddPlayerDialog.observeAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.liverpool_players)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddPlayerClicked() }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.tambah_pemain))
            }
        }
    ) { paddingValues ->
        ScreenContent(
            status = status,
            players = players,
            onPlayerSelected = { player -> viewModel.onPlayerSelected(player) },
            modifier = Modifier.padding(paddingValues)
        )

        selectedPlayer?.let { player ->
            PlayerDetailDialog(
                player = player,
                onDismissRequest = { viewModel.onDialogDismiss() }
            )
        }

        if (showAddPlayerDialog) {
            AddPlayerDialog(
                onDismissRequest = { viewModel.onAddPlayerDialogDismiss() },
                onPlayerAdded = { newPlayer ->
                    viewModel.addPlayer(newPlayer)
                }
            )
        }
    }
}

@Composable
fun ScreenContent(
    status: LfcApiStatus,
    players: List<Player>,
    onPlayerSelected: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    when (status) {
        LfcApiStatus.LOADING -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        LfcApiStatus.SUCCESS -> {
            if (players.isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.data_kosong))
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(players) { player ->
                        PlayerCard(player = player, onClick = { onPlayerSelected(player) })
                    }
                }
            }
        }
        LfcApiStatus.FAILED -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.gagal_muat))
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(player.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.broken_img),
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = player.position,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PlayerDetailDialog(player: Player, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(player.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_img),
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Position: ${player.position}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Nationality: ${player.nationality}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Button(onClick = onDismissRequest) {
                    Text(text = stringResource(R.string.tutup))
                }
            }
        }
    }
}

@Composable
fun AddPlayerDialog(
    onDismissRequest: () -> Unit,
    onPlayerAdded: (Player) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var goalsString by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var numberString by remember { mutableStateOf("") } // Tambahkan state untuk nomor punggung

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.tambah_pemain_baru),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.nama_pemain)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField( // Tambahkan input untuk Nomor Punggung
                    value = numberString,
                    onValueChange = { numberString = it },
                    label = { Text(stringResource(R.string.nomor_punggung)) }, // Tambahkan string resource ini
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text(stringResource(R.string.posisi)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nationality,
                    onValueChange = { nationality = it },
                    label = { Text(stringResource(R.string.kebangsaan)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    label = { Text(stringResource(R.string.url_gambar_pemain)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(R.string.batal))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val numberInt = numberString.toIntOrNull() // Bisa jadi null jika tidak diisi atau tidak valid
                        val newPlayer = Player(
                            id = UUID.randomUUID().toString(), // Generate ID unik
                            name = name,
                            number = numberInt ?: 0, // Sediakan nilai default jika null, atau handle error
                            position = position,
                            nationality = nationality,
                            image = image
                        )
                        onPlayerAdded(newPlayer)
                    }) {
                        Text(stringResource(R.string.tambah))
                    }
                }
            }
        }
    }
}

// Preview untuk MainScreen (jika belum ada atau ingin diperbarui)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DefaultPreview() {
    Assessment3Theme {
        MainScreen()
    }
}