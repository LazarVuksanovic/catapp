package rs.raf.catapp.users.details

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapp.quiz.QuizListItem

@ExperimentalMaterial3Api
fun NavGraphBuilder.userDetails(
    route: String,
    navController: NavController
) = composable(route = route){navBackStackEntry ->

    val userViewModel: UserDetailsViewModel = hiltViewModel(navBackStackEntry)
    val state = userViewModel.state.collectAsState()

    if(state.value.loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }else {
        if(state.value.user != null) {
            UserDetails(
                state,
                navController,
                ranking = state.value.user!!.ranking,
                name = state.value.user!!.firstName,
                surname = state.value.user!!.lastName,
                username = state.value.user!!.username,
                email = state.value.user!!.mail,
            )

        }
        else navController.navigate("home") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserDetails(
    state: State<UserDetailsUiState>,
    navController: NavController,
    ranking: Int,
    name: String,
    surname: String,
    username: String,
    email: String,
    ) {
    val scrollState = rememberLazyListState()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                title = { Text(text = "User Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(vertical = 36.dp),
                    text = "Profile info",
                    fontSize = 20.sp,
                )

                UserDetailsItem(
                    label = "Ranking",
                    value =  if (ranking == -1) "Not ranked" else ranking.toString()
                )
                UserDetailsItem(
                    label = "Name",
                    value = name
                )
                UserDetailsItem(
                    label = "Surname",
                    value = surname
                )
                UserDetailsItem(
                    label = "Email",
                    value = email
                )
                UserDetailsItem(
                    label = "Username",
                    value = username
                )
                UserDetailsItem(
                    label = "Best Score",
                    value = ((state.value.quizzes.maxByOrNull { it.score })?.score ?: 0).toString()
                )

                Button(
                    onClick = {
                        navController.navigate("userEdit")
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(width = 200.dp, height = 50.dp)
                ) {
                    Text(text = "Edit profile")
                }

                Text(text = "Quiz history",
                     fontSize = 20.sp,
                     modifier = Modifier
                         .padding(top = 17.dp)
                )

                if (state.value.quizzes.isNotEmpty()){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                        state = scrollState
                    ) {
                        itemsIndexed(
                            items = state.value.quizzes.reversed(),
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
fun UserDetailsItem(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
            Text(text = label, fontWeight = FontWeight.Bold)
            Text(text = value)
    }
}