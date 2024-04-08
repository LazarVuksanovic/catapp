package rs.raf.catapp.breeds.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import rs.raf.catapp.breeds.list.model.BreedUiModel

@ExperimentalMaterial3Api
fun NavGraphBuilder.breedsListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val breedListViewModel = viewModel<BreedsListViewModel>()
    val state by breedListViewModel.state.collectAsState()

    BreedsListScreen(
        state = state,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onItemClick = {
            navController.navigate(route = "breeds/${it.id}")
            },
        )
    }

@ExperimentalMaterial3Api
@Composable
fun BreedsListScreen(
    state: BreedListState,
    eventPublisher: (BreedsListUiEvent) -> Unit,
    onItemClick: (BreedUiModel) -> Unit,
) {
    Scaffold(
        topBar = {
                Column(
//                    modifier = Modifier.background(Color(0xFFa81fde))
                ) {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Breeds List", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFa81fde)
                    ),
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    value = state.query,
                    onValueChange = { new -> eventPublisher(BreedsListUiEvent.SearchQueryChanged(new)) },
                    placeholder = { Text(text = "Breed name") },
                    label = {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Origin",
                            )
                            Text(text = "Search breeds...")
                        }
                    },
                )
            }
        },
        content = {
            Divider(Modifier.padding(vertical = 16.dp))
            if(state.loading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    CircularProgressIndicator()
                }
            }else if (state.error != null){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    val errorMessage = when (state.error) {
                        is BreedListState.ListError.LoadingFailed ->
                            "Failed to load. Error message: ${state.error.cause?.message}"
                    }
                    Text(text = errorMessage)
                }
            }else{
                BreedList(it, onItemClick, state)
            }

        }
    )
}

@Composable
private fun BreedList(
    paddingValues: PaddingValues,
    onItemClick: (BreedUiModel) -> Unit,
    state: BreedListState
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        state = scrollState
    ) {
        items(
            items = state.filteredBreeds,
            key = { breed -> breed.id }
        ) { breed ->
            BreedListItem(
                data = breed,
                onClick = {
                    onItemClick(breed)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreedListItem(
    data: BreedUiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            fontSize = TextUnit(24f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            text = data.name,
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
            text = data.altNames.ifBlank {
                "No alternative names"
            }
        )

        data.image?.let { image ->
            SubcomposeAsyncImage(
                model = image.url,
                contentDescription = "Cat Image",
                loading = { CircularProgressIndicator() },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
            text = data.description.take(250) + "..."
        )

        val temperamentParts = data.temperament.split(",")
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(temperamentParts.take(3)) { temperament ->
                FilterChip(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 0.dp, bottom = 16.dp),
                    onClick = {},
                    label = { Text(temperament.trim()) },
                    selected = false,
                )
            }
        }
    }
}