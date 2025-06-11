package com.example.myapplication2

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import coil3.compose.AsyncImage
import com.example.myapplication2.db.CharacterDatabase
import com.example.myapplication2.Disney.Character
import com.example.myapplication2.repository.CharacterRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkMonitor {
    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> get() = _isConnected

    fun register(context: Context) {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isConnected.postValue(true)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                val hasInternet =
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                _isConnected.postValue(hasInternet)
            }

            override fun onLost(network: Network) {
                _isConnected.postValue(false)
            }
        }

        connectivityManager.registerNetworkCallback(request, callback)
    }
}

object RetrofitInstance {
    val api: RickAndMortyApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApiService::class.java)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NetworkMonitor.register(applicationContext)

        val db = Room
            .databaseBuilder(
                applicationContext,
                CharacterDatabase::class.java,
                "character_db"
            )
            .fallbackToDestructiveMigration(false)
            .build()

        val repository = CharacterRepository(RetrofitInstance.api, db.characterDao())
        val viewModel = CharacterViewModel(repository)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "list") {
                composable("list") {
                    CharacterListScreen(viewModel, navController)
                }
                composable("details/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                    if (id != null) {
                        CharacterDetailScreen(id,viewModel)
                    } else {
                        Text("Invalid character ID")
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkStatusBanner() {
    val isConnected = NetworkMonitor.isConnected.observeAsState(false).value

    if (!isConnected) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No internet connection",
                color = MaterialTheme.colorScheme.onError,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.error)
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun CharacterListScreen(viewModel: CharacterViewModel, navController: NavController) {
    val characters = viewModel.characters
    val isLoading = viewModel.isLoading


    LaunchedEffect(Unit) {
        if (characters.isEmpty()) {
            viewModel.loadNextPage()
        }
    }

    Scaffold(
        topBar = {
            NetworkStatusBanner()
        },
        bottomBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = { viewModel.loadPrevPage() },
                    enabled = viewModel.currentPage > 1 && !viewModel.isLoading
                ) {
                    Text("Назад")
                }


                Text(
                    text = "Страница ${viewModel.currentPage}",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )


                Button(
                    onClick = { viewModel.loadNextPage() },
                    enabled = !viewModel.isLoading && !viewModel.isEnd
                ) {
                    Text("Вперед")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(characters) { character ->
                CharacterCard(character) {
                    navController.navigate("details/${character.id}")
                }
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = "Avatar of ${character.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,

            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${character.status} - ${character.species}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(id:Int, viewModel: CharacterViewModel) {
    val character = viewModel.characters.find { it.id == id }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(character!!.name) })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ){
            item {
                AsyncImage(
                    model = character?.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)

                )
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Status: ${character!!.status}", style = MaterialTheme.typography.bodyLarge)
                    Text("Species: ${character.species}", style = MaterialTheme.typography.bodyLarge)
                    Text("Gender: ${character.gender}", style = MaterialTheme.typography.bodyLarge)
                    if (character.type.isNotEmpty()) {
                        Text("Type: ${character.type}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }


            item {
                Text("Appears in:", style = MaterialTheme.typography.titleMedium)
            }

            character!!.getEpisodesGroupedBySeason().forEach { (season, episodes) ->
                item {
                    Text("Season $season",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp))
                }
                items(episodes) { episode ->
                    Text("S${season.toString().padStart(2, '0')}$episode",
                        modifier = Modifier.padding(vertical = 2.dp))
                }
            }



        }
    }
}

