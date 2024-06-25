package rs.raf.catapp.quiz

import android.os.CountDownTimer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import rs.raf.catapp.quiz.db.PlayedQuiz
import java.util.concurrent.TimeUnit

fun NavGraphBuilder.quizQuestion(
    route: String,
    navController: NavController,
) = composable(
    route = route
) { navBackStackEntry ->
    val questionViewModel: QuestionViewModel = hiltViewModel(navBackStackEntry)

    val state by questionViewModel.state.collectAsState()

    QuizScreen(
        state = state,
        navController = navController,
        eventPublisher = { questionViewModel.publishEvent(it) }
    )
}

@Composable
fun QuizScreen(
    state: QuestionUiState,
    navController: NavController,
    eventPublisher: (QuestionUiEvent) -> Unit
){

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
            }
            else if (state.finished){
                LaunchedEffect(state.finished) {
                    eventPublisher(
                        QuestionUiEvent.PostQuiz(
                            PlayedQuiz(
                                score = state.result,
                                userId = state.user?.id!!,
                                rightAnswers = state.rightAnswers
                            )
                        )
                    )
                }

                EndScreen(
                    state = state,
                    navController = navController,
                    eventPublisher = eventPublisher,
                )
            }
            else {
                QuestionScreen(
                    state.questions,
                    state.currentQuestion,
                    navController,
                    eventPublisher = eventPublisher,
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun QuestionScreen(
    questions: List<Question>,
    currentQuestion: Int,
    navController: NavController,
    eventPublisher: (QuestionUiEvent) -> Unit,
) {
    var selected by remember { mutableIntStateOf(-1) }
    var showDialog by remember { mutableStateOf(false) }
    var time by remember { mutableLongStateOf(300) }

    BackHandler {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "End Quiz") },
            text = { Text(text = "Your progress will not be saved.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.popBackStack()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    Column {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = {showDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            title = {
                Text(
                    text = "Quiz",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Question ${currentQuestion + 1}/20", fontSize = 24.sp)
                Timer(
                    durationSeconds = 300,
                    onTimeChange = {newTime -> time = newTime},
                    eventPublisher = eventPublisher
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = questions[currentQuestion].text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                ){
                    SubcomposeAsyncImage(
                        modifier = if ((selected == 0)) {
                            Modifier.border(
                                width = 5.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            Modifier
                        }
                            .clickable {
                                selected = 0
                            }
                            .height(220.dp),
                        model = questions[currentQuestion].firstBreed.image?.url,
                        contentDescription = "Cat Image",
                        contentScale = ContentScale.FillHeight,
                        loading = { CircularProgressIndicator() },
                        )
                    Text(
                        modifier = Modifier
                            .background(color = Color.Black.copy(alpha = 0.5f))
                            .padding(all = 8.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        text = questions[currentQuestion].firstBreed.name
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    contentAlignment = Alignment.BottomCenter,
                ){
                    SubcomposeAsyncImage(
                        modifier = if ((selected == 1)) {
                            Modifier.border(
                                width = 5.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            Modifier
                        }
                            .clickable {
                                selected = 1
                            }
                            .height(220.dp),
                        model = questions[currentQuestion].secondBreed.image?.url,
                        contentDescription = "Cat Image",
                        contentScale = ContentScale.FillHeight,
                        loading = { CircularProgressIndicator() },
                    )
                    Text(
                        modifier = Modifier
                            .background(color = Color.Black.copy(alpha = 0.5f))
                            .padding(all = 8.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        text = questions[currentQuestion].secondBreed.name
                    )
                }
            }

            Button(
                onClick = {
                    if(selected != -1){
                        eventPublisher(QuestionUiEvent.NextQuestion(selected, time.toInt()))
                        selected = -1
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(width = 200.dp, height = 50.dp)
            ) {
                Text(text = "Next")
            }
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun EndScreen(
    state: QuestionUiState,
    navController: NavController,
    eventPublisher: (QuestionUiEvent) -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            title = {
                Text(
                    text = "Quiz finished",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Correct answers",
                fontSize = 24.sp
            )

            Text(
                text = "${state.rightAnswers}/20",
                fontSize = 36.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Score",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${state.result}",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .size(width = 200.dp, height = 50.dp)
            ) {
                Text(text = "Go to quiz home")
            }

            Button(
                onClick = {
                    eventPublisher(QuestionUiEvent.Publish(
                        username = state.user?.username!!,
                        score = state.result,
                        category = 3
                    ))
                },
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 0.dp)
                    .size(width = 200.dp, height = 50.dp)
            ) {
                Text(text = "Publish score")
            }
        }
    }
}


@Composable
fun Timer(
    durationSeconds: Long,
    onTimeChange: (Long) -> Unit,
    eventPublisher: (QuestionUiEvent) -> Unit
) {
    var timeLeft by remember { mutableLongStateOf(durationSeconds) }

    LaunchedEffect(durationSeconds) {
        object : CountDownTimer(durationSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTimeChange(timeLeft)
                timeLeft = millisUntilFinished / 1000
            }

            override fun onFinish() {
                timeLeft = 0
            }
        }.start()
    }

    if (timeLeft.toInt() == 0){
        eventPublisher(QuestionUiEvent.NextQuestion(-1, timeLeft.toInt()))
    }

    val minutes = TimeUnit.SECONDS.toMinutes(timeLeft)
    val seconds = timeLeft % 60

    val timeString = String.format("%02d:%02d", minutes, seconds)

    Text(
        text = timeString,
        fontSize = 24.sp,
        color = Color.Black,
    )
}