package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.viewModel.ChatListViewModel
import com.example.myapplication.viewModel.UserLoginState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    chatListViewModel: ChatListViewModel,
    onRegisterButton:()-> Unit={},
    onLoginButton: () -> Unit={}
) {
    val context = LocalContext.current

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val myColor = Color(0xFF0084FF)
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var userLoginState : UserLoginState = chatListViewModel.userLoginState
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
            Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

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
                    focusedIndicatorColor =myColor,
                    unfocusedIndicatorColor = myColor.copy(0.5f)),
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                placeholder = { Text(text = "Confirm Password") },
                singleLine = true,
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        R.drawable.baseline_visibility_24
                    else
                        R.drawable.baseline_visibility_off_24

                    val description = if (confirmPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {confirmPasswordVisible = !confirmPasswordVisible}){
                        Icon(painter  = painterResource(id = image), description)
                    }
                }

            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if(password == confirmPassword){
                        chatListViewModel.signup(email,password)
                    }
                    else
                    {
                        Toast.makeText(context, "passwords does not match!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor= myColor),
                ) {
                Text(text = "Register")
            }
            LaunchedEffect(userLoginState) {
                when (userLoginState) {

                    is UserLoginState.Error -> {

                        Toast.makeText(context, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                    is UserLoginState.Success -> {
                        chatListViewModel.getAllChats(userLoginState.token)
                        onRegisterButton()
                    }
                    else -> {
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onLoginButton) {
                Text(text = "Already have an account? Login",color = myColor)
            }
            Spacer(modifier = Modifier.height(5.dp))

            }
    }
}
