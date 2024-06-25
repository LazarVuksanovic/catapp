package rs.raf.catapp.leaderboard

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapp.leaderboard.model.QuizResultUiModel

@ExperimentalMaterial3Api
fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {navBackStackEntry->

    val leaderboardViewModel: LeaderboardViewModel = hiltViewModel(navBackStackEntry)
    val state by leaderboardViewModel.state.collectAsState()

    LeaderboardListScreen(
        navController = navController,
        state = state,
    )
}

@ExperimentalMaterial3Api
@Composable
fun LeaderboardListScreen(
    navController: NavController,
    state: LeaderboardState,
) {

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Leaderboard list", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
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
                    Text(text = "Error. Failed to load.")
                }
            }else{
                LeaderboardList(it, state)
            }

        }
    )

}



@Composable
private fun LeaderboardList(
    paddingValues: PaddingValues,
    state: LeaderboardState
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 16.dp),
        state = scrollState
    ) {
        itemsIndexed(
            items = state.leaderboard,
            key = { index, _ -> index }
        ) { index, item ->
            LeaderboardListItem(index = index+1, data = item)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardListItem(
    index: Int,
    data: QuizResultUiModel,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    fontSize = TextUnit(24f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold,
                    text = data.nickname,
                )
                Text(
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    text = data.result.toString()
                )
            }

            Text(
                fontSize = TextUnit(30f, TextUnitType.Sp),
                text = "#$index"
            )
        }
    }
}