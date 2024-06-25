package com.raf.catalist.users.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapp.users.details.UserDetailsUiEvent
import rs.raf.catapp.users.details.UserDetailsUiState
import rs.raf.catapp.users.details.UserDetailsViewModel
import java.util.regex.Pattern

@ExperimentalMaterial3Api
fun NavGraphBuilder.userEdit(
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
            UserEdit(
                state = state,
                eventPublisher = { userViewModel.publishEvent(it) },
                navController = navController,
                ranking = state.value.user!!.ranking,
                name = state.value.user!!.firstName,
                surname = state.value.user!!.lastName,
                username = state.value.user!!.username,
                email = state.value.user!!.mail
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
fun UserEdit(
    ranking: Int,
    name: String,
    surname: String,
    username: String,
    email: String,
    state: State<UserDetailsUiState>,
    eventPublisher: (UserDetailsUiEvent) -> Unit,
    navController: NavController,
) {

    val scrollState = rememberLazyListState()
    val (firstName, setFirstName) = remember { mutableStateOf(name) }
    val (lastName, setLastName) = remember { mutableStateOf(surname) }
    val (newEmail, setEmail) = remember { mutableStateOf(email) }
    val (newUsername, setUsername) = remember { mutableStateOf(username) }

    val (firstNameError, setFirstNameError) = remember { mutableStateOf("") }
    val (lastNameError, setLastNameError) = remember { mutableStateOf("") }
    val (emailError, setEmailError) = remember { mutableStateOf("") }
    val (usernameError, setUsernameError) = remember { mutableStateOf("") }

    fun validateInputs(): Boolean {
        var isValid = true

        firstName.let {
            val error = when {
                it.isBlank() -> {
                    isValid = false
                    "First name is mandatory"
                }

                else -> ""
            }
            setFirstNameError(error)
        }

        lastName.let {
            val error = when {
                it.isBlank() -> {
                    isValid = false
                    "Last name is mandatory"
                }
                else -> ""
            }
            setLastNameError(error)
        }

        newEmail.let {
            val error = when {
                it.isBlank() -> {
                    isValid = false
                    "Email is mandatory"
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() -> {
                    isValid = false
                    "Email format is invalid"
                }
                else -> ""
            }
            setEmailError(error)
        }

        newUsername.let {
            val error = when {
                it.isBlank() -> {
                    isValid = false
                    "Username is mandatory"
                }
                !Pattern.matches("^[a-zA-Z0-9_]*$", it) -> {
                    isValid = false
                    "Username can contain only letters, numbers, and underscores"
                }
                else -> ""
            }
            setUsernameError(error)
        }

        return isValid
    }

    Scaffold(
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
                    modifier = Modifier
                        .padding(vertical = 36.dp),
                    text = "Edit your profile",
                    fontSize = 20.sp,
                )

                UserDetailsItemEditable(
                    label = "Name",
                    value = firstName,
                    onValueChange = setFirstName,
                    error = firstNameError
                )
                UserDetailsItemEditable(
                    label = "Surname",
                    value = lastName,
                    onValueChange = setLastName,
                    error = lastNameError
                )
                UserDetailsItemEditable(
                    label = "Email",
                    value = newEmail,
                    onValueChange = setEmail,
                    error = emailError
                )
                UserDetailsItemEditable(
                    label = "Username",
                    value = newUsername,
                    onValueChange = setUsername,
                    error = usernameError
                )

                Button(
                    onClick = {
                        if (validateInputs()) {
                            eventPublisher(
                                UserDetailsUiEvent.UpdateUser(
                                    id = state.value.user?.id,
                                    firstName = "" + firstName,
                                    lastName =  "" + lastName,
                                    mail =  "" + newEmail,
                                    username =  "" + newUsername,
                                    ranking = state.value.user?.ranking
                                ))
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(width = 200.dp, height = 50.dp)
                ) {
                    Text(text = "Save")
                }
                // Edit button
//                Button(
//                    onClick = {
//                        if (validateInputs()) {
//                            eventPublisher(
//                                UserDetailsUiEvent.UpdateUser(
//                                id = state.value.user?.id,
//                                firstName = "" + firstName,
//                                lastName =  "" + lastName,
//                                mail =  "" + newEmail,
//                                username =  "" + newUsername,
//                                ranking = state.value.user?.ranking
//                            ))
//                            navController.popBackStack()
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 7.dp)
//                        .padding(horizontal = 11.dp),
//                    contentPadding = PaddingValues(16.dp)
//                ) {
//                    Text(
//                        text = "Submit",
//                        color = Color.White
//                    )
//                }
            }
        }
    )
}

@Composable
fun UserDetailsItemEditable(label: String, value: String, onValueChange: (String) -> Unit, error: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}