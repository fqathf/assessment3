package com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import androidx.datastore.core.IOException
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.faiqathifnurrahimhadiko607062330082.assessment3.R
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.User
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.PlayerApi
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.PlayerApiStatus
import com.faiqathifnurrahimhadiko607062330082.assessment3.network.UserDataStore
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.theme.Assessment3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    val viewModel: MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage
//    val itemToEdit by remember { mutableStateOf<Player?>(null) }
//    var bitmapToEdit by remember { mutableStateOf<Bitmap?>(null) }
//    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        // ... di dalam HomeScreen() dan MainScreen() Scaffold
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Liverpool Players")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Menggunakan warna utama
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Warna teks di atas warna utama
                ),
                actions = {
                    IconButton(onClick = { /* ... */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.account_circle_24),
                            contentDescription = stringResource(id = R.string.profil),
                            tint = MaterialTheme.colorScheme.onPrimary // Ikon juga menggunakan warna onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContentHome(
            viewModel = viewModel,
            userId = user.email,
            modifier = Modifier.padding(innerPadding)
        )

        if (showDialog) {
            ProfilDialog(
                user = user,
                onDismissRequest = { showDialog = false }
            ) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun ScreenContentHome(
    viewModel: MainViewModel,
    userId: String,
    modifier: Modifier = Modifier
) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retrieveData()
    }

    when (status) {
        PlayerApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        PlayerApiStatus.SUCCESS -> {
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { player ->
                    ListItemHome(
                        player = player,
                        userId = userId,
                        onDelete = { id -> viewModel.deleteData(userId, id) }
                    )
                }
            }
        }
        PlayerApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrieveData(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemHome(
    player: Player,
    userId: String,
    onDelete: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var showConfirmDelete by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val viewModel: MainViewModel = viewModel()
    val bitmap = rememberBitmapFromUrlHome(PlayerApi.getPlayerPhotoUrl(player.foto))

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Player Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Choose action for \"${player.nama}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showSheet = false
                        showEditDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(Modifier.width(8.dp))
                    Text("Edit Player")
                }

                Button(
                    onClick = {
                        showSheet = false
                        showConfirmDelete = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error, // Gunakan warna error dari tema
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(Modifier.width(8.dp))
                    Text("Delete Player")
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { showSheet = false }) {
                    Text("Close")
                }
            }
        }
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this player?") },
            confirmButton = {
                Button(onClick = {
                    showConfirmDelete = false
                    onDelete(player.id)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmDelete = false }) {
                    Text("No")
                }
            }
        )
    }

    if (showEditDialog) {
        UpdatePlayerDialog(
            bitmap = bitmap,
            currentNama = player.nama,
            currentPosisi = player.posisi,
            onDismissRequest = { showEditDialog = false },
            onConfirmation = { nama, posisi, newBitmap ->
                viewModel.updateData(
                    userId = userId,
                    nama = nama,
                    posisi = posisi,
                    bitmap = newBitmap,
                    id = player.id
                )
                showEditDialog = false
            }
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp), // Sedikit mengurangi radius sudut
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Mengurangi bayangan
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Menggunakan warna surface dari tema
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(PlayerApi.getPlayerPhotoUrl(player.foto))
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.player_image, player.nama),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_img),
                    modifier = Modifier.fillMaxSize()
                )

                if (userId.isNotEmpty() && player.Authorization == userId) {
                    IconButton(
                        onClick = { showSheet = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = player.nama,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge, // Membuat judul lebih besar
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp)) // Spasi antara nama dan posisi
                Text(
                    text = player.posisi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Warna teks sekunder
                )
            }
        }
    }
}

// Helper functions (same as original)
//private suspend fun signIn(context: Context, dataStore: UserDataStore) {
//    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//        .setFilterByAuthorizedAccounts(false)
//        .setServerClientId(BuildConfig.API_KEY)
//        .build()
//
//    val request: GetCredentialRequest = GetCredentialRequest.Builder()
//        .addCredentialOption(googleIdOption)
//        .build()
//
//    try {
//        val credentialManager = CredentialManager.create(context)
//        val result = credentialManager.getCredential(context, request)
//        handleSignIn(result, dataStore)
//    } catch (e: GetCredentialException) {
//        Log.e("SIGN-IN", "AAA: ${e.errorMessage}")
//    }
//}

//private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
//    val credential = result.credential
//    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//        try {
//            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
//            val nama = googleId.displayName ?: ""
//            val email = googleId.id
//            val photoUrl = googleId.profilePictureUri.toString()
//            dataStore.saveData(User(nama, email, photoUrl))
//        } catch (e: GoogleIdTokenParsingException) {
//            Log.e("SIGN-IN", "BBB: ${e.message}")
//        }
//    } else {
//        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
//    }
//}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "CCC: ${e.errorMessage}")
    }
}

//private suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap? {
//    val loader = ImageLoader(context)
//    val request = ImageRequest.Builder(context)
//        .data(url)
//        .allowHardware(false)
//        .build()
//    return try {
//        val result = (loader.execute(request) as SuccessResult).drawable
//        (result as BitmapDrawable).bitmap
//    } catch (e: Exception) {
//        Log.e("LoadBitmap", "Failed to load bitmap from URL: $url", e)
//        null
//    }
//}

@Composable
fun rememberBitmapFromUrlHome(url: String): Bitmap? {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    return bitmap
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HomeScreenPreview() {
    Assessment3Theme {
        HomeScreen()
    }
}