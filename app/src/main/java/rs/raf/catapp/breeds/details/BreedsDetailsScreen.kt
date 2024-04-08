package rs.raf.catapp.breeds.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import rs.raf.catapp.breeds.domain.BreedData
import rs.raf.catapp.breeds.list.BreedListState
import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.breeds.repository.BreedRepository
import java.util.UUID
import java.util.logging.Filter

fun NavGraphBuilder.breedDetails(
    route: String,
    navController: NavController,
) = composable(
    route = route
) { navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("dataId")
    ?: throw IllegalArgumentException("id is required.")

    val breedDetailsViewModel = viewModel<BreedDetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if(modelClass.isAssignableFrom((BreedDetailsViewModel::class.java))){
                    return BreedDetailsViewModel(breedId = dataId) as T
                } else {
                    throw IllegalArgumentException()
                }
            }
        }
    )
    val state by breedDetailsViewModel.state.collectAsState()

    BreedData(
        id = UUID.randomUUID().toString(),
        name = "",
        origin = "",
        wikipediaUrl = "",
        description = ""
    )

    BreedsDetailsScreen(state = state, navController)
}

@Composable
fun BreedsDetailsScreen(
    state: BreedDetailsState,
    navController: NavController
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
            if(state.loading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    CircularProgressIndicator()
                }
            }else if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    val errorMessage = when (state.error) {
                        is BreedDetailsState.DetailsError.LoadingFailed ->
                            "Failed to load. Error message: ${state.error.cause?.message}"
                    }
                    Text(text = errorMessage)
                }
            }else if(state.data != null){
                BreedsDataColumn(state.data, navController)
            } else {
                NoDataContent(id = state.breedId)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BreedsDataColumn(
    data: BreedUiModel,
    navController: NavController
) {
    Column {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            title = {
                Text(
                    text = data.name,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFa81fde)
            ),
        )

        data.image?.let { image ->
            SubcomposeAsyncImage(
                model = image.url,
                contentDescription = "Cat Image",
                loading = { CircularProgressIndicator() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Origin",
                )
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    text = data.origin,
                )
            }

            var rareText = "Not Rare"
            if (data.rare == 1)
                rareText = "Rare!"

            FilterChip(
                modifier = Modifier.padding(end = 16.dp),
                selected = false,
                onClick = {},
                label = { Text(text = rareText) },
            )

        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.description,
        )

        Spacer(modifier = Modifier.height(16.dp))

        val temperamentParts = data.temperament.split(",")
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(temperamentParts) { temperament ->
                FilterChip(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 0.dp, bottom = 16.dp),
                    onClick = {},
                    label = { Text(temperament.trim()) },
                    selected = false,
                )
            }
        }

         Text(
             modifier = Modifier.padding(horizontal = 16.dp),
             style = MaterialTheme.typography.bodyLarge,
             text = "Life span: " + data.lifeSpan + " years"
         )

         Text(
             modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
             style = MaterialTheme.typography.bodyLarge,
             text = "Weight: " + data.weight.metric.toString() + "kg"
         )

        Spacer(modifier = Modifier.height(16.dp))

        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Affection level"
            )
            Row(
                Modifier.padding(end = 16.dp)
            )  {
                for(i in 0..4){
                    var c = Color.LightGray
                    if (i < data.affectionLevel)
                        c = Color(0xFFd6d311)
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Origin",
                        tint = c
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Child friendly"
            )
            Row(
                Modifier.padding(end = 16.dp)
            )  {
                for(i in 0..4){
                    var c = Color.LightGray
                    if (i < data.childFriendly)
                        c = Color(0xFFd6d311)
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Origin",
                        tint = c
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Energy level"
            )
            Row(
                Modifier.padding(end = 16.dp)
            )  {
                for(i in 0..4){
                    var c = Color.LightGray
                    if (i < data.energyLevel)
                        c = Color(0xFFd6d311)
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Origin",
                        tint = c
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Dog friendly"
            )
            Row(
                Modifier.padding(end = 16.dp)
            ) {
                for(i in 0..4){
                    var c = Color.LightGray
                    if (i < data.dogFriendly)
                        c = Color(0xFFd6d311)
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Origin",
                        tint = c
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Shedding level"
            )
            Row{
                for(i in 0..4){
                    var c = Color.LightGray
                    if (i < data.sheddingLevel)
                        c = Color(0xFFd6d311)
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Origin",
                        tint = c
                    )
                }
            }
        }

        val ctx = LocalContext.current
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.wikipediaUrl))
                    ctx.startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(width = 200.dp, height = 50.dp) // Adjust size as needed
            ) {
                Text(text = "Wiki page")
            }
        }
    }
}

@Composable
private fun NoDataContent(
    id: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "There is no data for id '$id'.",
            fontSize = 18.sp,
        )
    }
}