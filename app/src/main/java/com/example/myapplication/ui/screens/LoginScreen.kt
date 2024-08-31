package com.example.myapplication.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Visibility
import com.example.myapplication.R
import com.example.myapplication.viewModel.ChatListViewModel
import com.example.myapplication.viewModel.UserLoginState
import kotlinx.coroutines.awaitAll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    chatListViewModel: ChatListViewModel,
    onLoginButton: () -> Unit ={},
    onRegisterButton: () -> Unit={}
) {
    val context = LocalContext.current
    val userLoginState = chatListViewModel.userLoginState
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val myColor = Color(0xFF0084FF)

    Box (modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.axolotl_home_screen),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            modifier = Modifier.fillMaxSize())


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedLabelColor = myColor,
                    cursorColor = myColor,
                    focusedLabelColor = myColor,
                    containerColor = myColor.copy(.2f),
                    focusedIndicatorColor = myColor,
                    unfocusedIndicatorColor = myColor.copy(0.5f)
                ),
                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                placeholder = { Text(text = "Email") },
                singleLine = true,

                )

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedLabelColor = myColor,
                    cursorColor = myColor,
                    focusedLabelColor = myColor,
                    containerColor = myColor.copy(.2f),
                    focusedIndicatorColor = myColor,
                    unfocusedIndicatorColor = myColor.copy(0.5f)
                ),
                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                placeholder = { Text(text = "Password") },
                singleLine = true,
                trailingIcon = {
                    val image = if (passwordVisible)
                        R.drawable.baseline_visibility_24
                    else
                        R.drawable.baseline_visibility_off_24

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(painter  = painterResource(id = image), description)
                    }
                }

            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    chatListViewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = myColor),
            ) {
                Text(text = "Login")
            }
            LaunchedEffect(userLoginState) {
                when (userLoginState) {

                    is UserLoginState.Error -> {

                        Toast.makeText(context, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                    is UserLoginState.Success -> {
                        chatListViewModel.getAllChats(userLoginState.token)
                        onLoginButton()
                    }
                    else -> {
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = {onRegisterButton()} ) {
                Text(text = "Don't have an account? Register", color = myColor)
            }
            Spacer(modifier = Modifier.height(60.dp))


        }
    }
}

