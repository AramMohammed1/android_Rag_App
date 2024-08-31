package com.example.myapplication

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.ChatListScreen
import com.example.myapplication.ui.screens.ChatScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.RegisterScreen
import com.example.myapplication.utils.TokenManager
import com.example.myapplication.viewModel.ChatListViewModel
import com.example.myapplication.viewModel.FileUploadViewModel
import com.example.myapplication.viewModel.SelectedChatUiState
import com.example.myapplication.viewModel.UserLoginState


enum class RagAppScreen(var title:String){
    HomeScreen(title = "Axolotl"),
    LoginScreen(title = "Login"),
    RegisterScreen(title = "Register"),
    ChatListScreen(title = "Chat List"),
    ChatScreen(title = "New Chat"),
    TestScreen(title = "test screen")
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RagAppBar(
    fileUploadViewModel:FileUploadViewModel,
    chatListViewModel: ChatListViewModel,
    currentScreen: RagAppScreen,
    navController: NavHostController,
    canNavigateBack: Boolean,
    navigateUp: ()->Unit,
    modifier : Modifier = Modifier
){
    var userLoginState:UserLoginState = chatListViewModel.userLoginState

    val selectedChatUiState= chatListViewModel.selectedChatUiState
        val resultLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
                    when(selectedChatUiState){
                        is SelectedChatUiState.Success-> {
                            when(userLoginState){
                                is UserLoginState.Success->{
                                    fileUploadViewModel.updateSelectedFileUris(userLoginState.token,selectedChatUiState.chat, uris)

                                }
                                else->{

                                }
                            }
                        }
                        is SelectedChatUiState.Loading->{}
                        is SelectedChatUiState.Error->{}
                    }

    }
    var showPopup by fileUploadViewModel.showPopup
    val uploadStatus by fileUploadViewModel.uploadStatus
    val context = LocalContext.current
    TopAppBar(
        title = { Text(text =  currentScreen.title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor =  MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        },
    actions = {

        if(currentScreen.title ==RagAppScreen.ChatScreen.title ) {
            IconButton(onClick = {
                resultLauncher.launch(arrayOf("*/*"))
            }) {
                Icon(
                    painter =
                    when (uploadStatus) {
                        "Upload Failed" -> painterResource(id = R.drawable.baseline_error_outline_24)
                        else -> painterResource(id = R.drawable.baseline_attach_file_24)
                    },
                    contentDescription = "Upload file",
                    tint= when (uploadStatus) {
                        "Upload Failed" -> Color.Red
                        else -> Color.DarkGray
                    },
                    modifier = Modifier.padding(4.dp)
                )
            }

            IconButton(onClick = {
                showPopup = !showPopup
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "chat settings",
                    modifier = Modifier.padding(4.dp)
                )

            }

            LaunchedEffect(uploadStatus) {

                when (uploadStatus) {
                    "Upload Successful" -> Toast.makeText(context, "Files Uploaded successfully", Toast.LENGTH_SHORT).show()
                    "Upload Failed" -> Toast.makeText(context, "Error occurred Uploading files", Toast.LENGTH_SHORT).show()
                    null -> {}
                }
            }
        }
        IconButton(onClick = {
            chatListViewModel.logout()
            navController.navigate(RagAppScreen.HomeScreen.name)

        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription ="logout" )
        }
    })

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RagApp(
    navController: NavHostController = rememberNavController(),
    tokenManager: TokenManager
    ) {
    val fileUploadViewModel: FileUploadViewModel = viewModel(factory = FileUploadViewModel.Factory)
    val chatListViewModel: ChatListViewModel = viewModel(factory = ChatListViewModel.Factory)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = RagAppScreen.valueOf(
        backStackEntry?.destination?.route?:RagAppScreen.HomeScreen.name
    )
    val token = tokenManager.getToken()
    Scaffold(
        topBar = {
            RagAppBar(
                fileUploadViewModel =fileUploadViewModel,
                chatListViewModel=chatListViewModel,
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navController =navController,
            )
        },
                modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination =
            if (token.isNullOrEmpty()) {RagAppScreen.HomeScreen.name } else {
                chatListViewModel.userLoginState = UserLoginState.Success(token)
                try{chatListViewModel.getAllChats(token)
                    RagAppScreen.ChatListScreen.name
                }
                catch (e:Exception){
                    chatListViewModel.logout()
                    RagAppScreen.HomeScreen.name
                }}
            ,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = RagAppScreen.HomeScreen.name) {
                HomeScreen(onLoginButton = {navController.navigate(RagAppScreen.LoginScreen.name)}, onRegisterScreen = {navController.navigate(RagAppScreen.RegisterScreen.name)})
            }
            composable(route = RagAppScreen.ChatListScreen.name) {
                ChatListScreen(chatListViewModel = chatListViewModel,onButtonClick = {navController.navigate(RagAppScreen.ChatScreen.name) })
            }
            composable(route = RagAppScreen.LoginScreen.name) {
                LoginScreen(chatListViewModel=chatListViewModel,onLoginButton = { navController.navigate(RagAppScreen.ChatListScreen.name) }, onRegisterButton = {navController.navigate(RagAppScreen.RegisterScreen.name)})
            }
            composable(route = RagAppScreen.RegisterScreen.name) {
                RegisterScreen(chatListViewModel= chatListViewModel,onRegisterButton = {navController.navigate(RagAppScreen.ChatListScreen.name)}, onLoginButton = {navController.navigate(RagAppScreen.LoginScreen.name)})
            }
            composable(route = RagAppScreen.ChatScreen.name){
                ChatScreen(chatListViewModel = chatListViewModel,fileUploadViewModel=fileUploadViewModel,modifier = Modifier.padding(innerPadding))
            }
        }

    }
}




