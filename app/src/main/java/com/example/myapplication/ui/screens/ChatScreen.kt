package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.Message
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewModel.MessageState
import com.example.myapplication.viewModel.RagAppViewModel


@Composable
fun UserMessageItem(message: Message,modifier:Modifier =Modifier){
    val alignment = Alignment.End
    val backgroundColor = Color(0xFF0084FF)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement =  Arrangement.End
    ) {

        Box(
            modifier = modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(backgroundColor)

                .align(Alignment.CenterVertically)
                .padding(8.dp)
        ) {
            Text(text = message.text, fontSize = 16.sp, color = Color.White)

        }

        Spacer(modifier = modifier.padding(2.dp))
        Image(
            painter = painterResource(id = R.drawable.molham1),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .clip(shape = CircleShape)
                .background(backgroundColor)
                .align(Alignment.CenterVertically)
                .size(50.dp)

        )

    }

}
@Composable
fun ModelMessageItem(message: Message,modifier :Modifier = Modifier) {
    val alignment = Alignment.Start
    val backgroundColor =  Color(0xFFF0F0F0 )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement= Arrangement.Start
    ) {

        Image(
            painter = painterResource(id = R.drawable.molham2),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .clip(shape = CircleShape)
                .background(backgroundColor)
                .align(Alignment.CenterVertically)
                .size(50.dp)

        )

        Spacer(modifier = modifier.padding(2.dp))

        Box(
            modifier = modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(backgroundColor)
                .align(Alignment.CenterVertically)
                .padding(8.dp)

        ) {
            Text(text = message.text, fontSize = 16.sp,color = Color.Black)

        }

    }
}




@Composable
fun MessagesList(messages: List<Message>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        reverseLayout = true
    ) {
        items(messages.reversed()) { message ->
            if(message.isUser){
                UserMessageItem(message)

            }
            else{
                ModelMessageItem(message)
            }
        }
    }
}
var send="Send"
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput(
    textState: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onSendClick: () -> Unit,
    modifier:Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val myColor = Color(0xFF0084FF)

        TextField(
            value = textState,
            onValueChange = onTextChange,
            modifier = modifier
                .weight(1f)
                .padding(end = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedLabelColor = myColor,
                cursorColor = myColor,
                focusedLabelColor = myColor,
//                textColor = myColor,
                containerColor = myColor.copy(.2f),
                focusedIndicatorColor =myColor,
                unfocusedIndicatorColor = myColor.copy(
                    0.5f)
            ),
            shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
            placeholder = { Text(text = "Type a message") }
        )
        Button(
            onClick = onSendClick,
            colors = ButtonDefaults.buttonColors(containerColor= Color(0xFF0084FF)),
            modifier = modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = send)
        }
    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    ragAppViewModel: RagAppViewModel,
    modifier: Modifier = Modifier) {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()))
    {
        val messageState = ragAppViewModel.messageState

        MessagesList(messages = messages, modifier = modifier.weight(1f))
        UserInput(
            textState = textState,

            onTextChange = { textState = it },

            onSendClick = {
                if (textState.text.isNotBlank()) {
                    messages = messages + Message(textState.text,true)
                    messages= messages + Message("Loading ... ",false)
                    send = "Dont Send"
                    ragAppViewModel.sendMessage(textState.text,500,2)
                    textState = TextFieldValue("")

                }

            },
            modifier = modifier
        )
        LaunchedEffect(messageState) {
            when (messageState) {
                is MessageState.Success -> {
                    send = "Send"
                    messages = messages.dropLast(1)
                    messages = messages + messageState.message
                }
                is MessageState.Loading -> {
                }
                is MessageState.Error -> {
                    send = "Send"
                    messages = messages.dropLast(1)
                    messages = messages + Message("Something went wrong, please ask your question again", false)
                }
            }
        }
    }
}

