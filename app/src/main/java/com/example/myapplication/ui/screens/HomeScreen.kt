package com.example.myapplication.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Chat
import com.example.myapplication.viewModel.ChatListUiState
import com.example.myapplication.viewModel.ChatListViewModel
import com.example.myapplication.viewModel.MessageState
import com.example.myapplication.viewModel.MessageViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val loading =false
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChannelListScreen(
    chatListViewModel: ChatListViewModel,
    onButtonClick: ()->Unit = {},
    modifier: Modifier=Modifier
    ) {
    var chatListUiState = chatListViewModel.chatListUiState


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(Modifier.fillMaxSize()) { // 5
            when (chatListUiState) {
                is ChatListUiState.Success -> {
                    items(chatListUiState.chats) { channel ->
                        ChannelListItem(channel = channel, onButtonClick = onButtonClick)
                        Divider()
                    }
                }

                is ChatListUiState.Error -> {
                    item {
                        Text(text = "Error!!!")
                    }
                }

                is ChatListUiState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }

            }
        }
        var localTime :String =""
        LaunchedEffect(chatListUiState){
            localTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString()
        }

        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)){
            CreateNewChat({chatListViewModel.createNewChat(listOf("ahmadali"),localTime,"new chat from mobile2")})
        }
    }


}


@Composable
fun ChannelListItem(
    channel: Chat,
    onButtonClick: ()->Unit = {},
    modifier: Modifier=Modifier)
    {

    Card(
        onClick = onButtonClick
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
//channel.messages.lastOrNull() ?:
                val lastMessageText =  "..."
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

