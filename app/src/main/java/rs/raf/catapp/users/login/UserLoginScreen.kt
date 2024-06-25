package rs.raf.catapp.users.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@ExperimentalMaterial3Api
fun NavGraphBuilder.userLogin(
    route: String,
    navController: NavController
) = composable(route = route){ navBackStackEntry->

    val userViewModel: UserViewModel = hiltViewModel(navBackStackEntry)
    val state by userViewModel.state.collectAsState()

    if(state.loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }else {
        if(state.user == null)
            LoginScreen(eventPublisher = {
                userViewModel.publishEvent(it)
            },)
        else navController.navigate("breedsListScreen")
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    eventPublisher: (UserLoginUiEvent) -> Unit,
) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        val fields = mapOf(
            firstName to { firstNameError = firstName.text.isEmpty() },
            lastName to { lastNameError = lastName.text.isEmpty() },
            username to {
                usernameError = username.text.isEmpty() || username.text.contains(" ")
            },
            email to {
                emailError = email.text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
            }
        )

        var isValid = true
        fields.forEach { (field, validateField) ->
            validateField()
            if (field.text.isEmpty()) isValid = false
        }

        return isValid
    }

    Scaffold(
        topBar = {
            Column(
            ) {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Welcome to Catapp", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = "Sign in",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MyTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Name") },
                        isError = firstNameError,
                        errorMessage = "Enter your name"
                    )
                    MyTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        isError = lastNameError,
                        errorMessage = "Enter your last name"
                    )
                    MyTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        isError = usernameError,
                        errorMessage = "Enter your username (without spaces)"
                    )
                    MyTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        isError = emailError,
                        errorMessage = "Enter your email"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (validate()) {
                                eventPublisher(UserLoginUiEvent.CreateUser(
                                    firstName = firstName.text,
                                    lastName = lastName.text,
                                    mail = email.text,
                                    username = username.text
                                ))
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(width = 200.dp, height = 50.dp)
                    ) {
                        Text(text = "Sign in")
                    }
//                    Button(
//                        onClick = {
//                            if (validate()) {
//                                eventPublisher(UserLoginUiEvent.CreateUser(
//                                    firstName = name.text,
//                                    lastName = lastName.text,
//                                    mail = email.text,
//                                    username = username.text
//                                ))
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 16.dp)
//                            .height(50.dp),
//                        shape = RoundedCornerShape(25)
//                    ) {
//                        Text(text = "Sign In")
//                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            ,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            isError = isError
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 13.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen()
//}