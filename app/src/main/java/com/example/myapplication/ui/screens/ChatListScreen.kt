package com.example.myapplication.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.RagAppScreen
import com.example.myapplication.model.Chat
import com.example.myapplication.viewModel.ChatListUiState
import com.example.myapplication.viewModel.ChatListViewModel
import com.example.myapplication.viewModel.UserLoginState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(
    chatListViewModel: ChatListViewModel,
    onButtonClick: ()->Unit = {},
    modifier: Modifier=Modifier
    ) {
    var chatListUiState = chatListViewModel.chatListUiState
    var newChatCreateState = chatListViewModel.newChatCreateState
    var showNewChatPopUp = chatListViewModel.showNewChatPopUp
    var userLoginState = chatListViewModel.userLoginState
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
            when (chatListUiState) {
                is ChatListUiState.Success -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(chatListUiState.chats) { channel ->
                            ChatListItem(channel = channel, chatListViewModel=chatListViewModel,onButtonClick = onButtonClick)
                            Divider()
                        }
                    }
                    Box(modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp)){
                        CreateNewChat {
                            showNewChatPopUp.value=true
                        }
                    }
                }

                is ChatListUiState.Error -> {
                        ErrorScreen(onRetryButton = {
                            if(userLoginState is UserLoginState.Success)
                            chatListViewModel.getAllChats(token = userLoginState.token)
                        })

                }

                is ChatListUiState.Loading -> {
                        LoadingScreen()

                }

            }
        }
        var localTime :String =LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        if(showNewChatPopUp.value){
            createNewChatPopup(chatListViewModel,{showNewChatPopUp.value=false})
        }



}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun createNewChatPopup(
    chatListViewModel: ChatListViewModel,
    onDismissRequest:()->Unit={}){
    var chatTitleTextState by remember { mutableStateOf(TextFieldValue("")) }
    var userLoginState = chatListViewModel.userLoginState
    Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Create New Chat", style = MaterialTheme.typography.bodySmall)

                    TextField(
                        value = chatTitleTextState,
                        onValueChange = {  chatTitleTextState = it },
                        label = { Text("Chat title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(onClick = {
                            var localTime :String =LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                            if(chatTitleTextState.text!=""){
                                when(userLoginState){
                                    is UserLoginState.Success ->{
                                        chatListViewModel.createNewChat(userLoginState.token,localTime,chatTitleTextState.text)
                                    }
                                    else -> { }
                                }
                                onDismissRequest()
                            } },
                            colors = ButtonDefaults.buttonColors(containerColor= Color(0xFF0084FF)),
                        ) {
                            Text("create")
                        }
                    }
            }
        }
}
}

@Composable
fun ChatListItem(
    channel: Chat,
    chatListViewModel: ChatListViewModel,
    onButtonClick: ()->Unit = {},
    modifier: Modifier=Modifier)
    {
        var userLoginState:UserLoginState = chatListViewModel.userLoginState

        Card(
        onClick = {
            when(userLoginState){
                is UserLoginState.Success ->{
                    chatListViewModel.setSelectedChat(userLoginState.token,channel.id)
                }
                else ->{

                }
            }
            RagAppScreen.ChatScreen.title= channel.title
            onButtonClick()
        }
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.padding(start = 8.dp)) { // 3
                Text(
                    text = channel.title,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp,
                )
                val lastMessageText =  channel.messages.lastOrNull()?.text ?: "..."
                Text(
                    text = lastMessageText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


@Composable
fun CreateNewChat(onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        Icon(Icons.Filled.Add, "Small floating action button.")
    }
}

