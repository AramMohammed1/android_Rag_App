package com.example.myapplication

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.model.Chat
import com.example.myapplication.ui.screens.ChannelListScreen
import com.example.myapplication.ui.screens.ChatScreen
import com.example.myapplication.viewModel.ChatListViewModel
import com.example.myapplication.viewModel.FileUploadViewModel
import com.example.myapplication.viewModel.MessageViewModel


enum class RagAppScreen(@StringRes val title:Int){
    HomeScreen(title = R.string.app_name),
    ChatScreen(title = R.string.chatscreen)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ragAppBar(
    currentScreen: RagAppScreen,
    canNavigateBack: Boolean,
    navigateUp: ()->Unit,
    modifier : Modifier = Modifier
){
    TopAppBar(
        title = { Text(text = stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor =  MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        }
    )

}


@Composable
fun RagApp(
    navController: NavHostController = rememberNavController()
    ){
    val messageViewModel: MessageViewModel = viewModel(factory = MessageViewModel.Factory)
    val fileUploadViewModel: FileUploadViewModel = viewModel(factory = FileUploadViewModel.Factory)
    val chatListViewModel: ChatListViewModel = viewModel(factory = ChatListViewModel.Factory)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = RagAppScreen.valueOf(
        backStackEntry?.destination?.route?:RagAppScreen.HomeScreen.name
    )

    Scaffold(
        topBar = {
            ragAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        },
                modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RagAppScreen.HomeScreen.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = RagAppScreen.HomeScreen.name) {
//                Log.e("aram",channelList.toString())
                ChannelListScreen(chatListViewModel = chatListViewModel,onButtonClick = {navController.navigate(RagAppScreen.ChatScreen.name) })
            }
            composable(route = RagAppScreen.ChatScreen.name){
                ChatScreen(messageViewModel = messageViewModel,fileUploadViewModel=fileUploadViewModel,modifier = Modifier.padding(innerPadding))
            }

        }

    }
}



