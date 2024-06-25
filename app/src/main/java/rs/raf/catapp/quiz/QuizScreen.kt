package rs.raf.catapp.quiz

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapp.quiz.db.PlayedQuiz

fun NavGraphBuilder.quizHome(
    route: String,
    navController: NavController,
) = composable(
    route = route
) { navBackStackEntry ->
    val quizViewModel: QuizViewModel = hiltViewModel(navBackStackEntry)

    val state by quizViewModel.state.collectAsState()

    QuizHomeScreen(state = state, navController = navController)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHomeScreen(
    state: QuizUiState,
    navController: NavController
) {
    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Quiz", color = Color.White) },
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
        },
        content = {paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    fontSize = 24.sp,
                    text = "Test your knowledge about cats!"
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        navController.navigate("quizQuestion")
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(width = 200.dp, height = 50.dp)
                ) {
                    Text(text = "Play")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(top = 36.dp),
                    fontSize = 24.sp,
                    text = "Previous results"
                )

                if (state.previous.isNotEmpty()){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp),
                        state = scrollState
                    ) {
                        itemsIndexed(
                            items = state.previous.reversed(),
                            key = { index, _ -> index }
                        ) { index, item ->
                            QuizListItem(index = index+1, data = item)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                else {
                    Text(text = "Not played yet")
                }
            }
        }
    )
}

@Composable
public fun QuizListItem(
    index: Int,
    data: PlayedQuiz,
) {
    Card(
        modifier = Modifier
//            .padding(horizontal = 16.dp)
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
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    text = "${data.score} points"
                )
            }

            Text(
                fontSize = TextUnit(20f, TextUnitType.Sp),
                text = "${data.rightAnswers}/20"
            )
        }
    }
}