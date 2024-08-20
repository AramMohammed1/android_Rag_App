package com.example.myapplication

import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatScreen(modifier =Modifier.padding(innerPadding))
                }
            }
        }
    }
}


data class Message(val text: String, val isUser: Boolean)




@Composable
fun MessageItem(message: String, isUser: Boolean) {
    val alignment =     if(isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (isUser) Color.Blue else Color.Red

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if(!isUser) {

            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .background(backgroundColor)
                    .align(Alignment.CenterVertically)
                    .size(50.dp)

            )

            Spacer(modifier = Modifier.padding(2.dp))
        }
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(backgroundColor)
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        ) {
            Text(text = message, fontSize = 16.sp,color = Color.White)
        }
        if(isUser){
            Spacer(modifier = Modifier.padding(2.dp))
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .background(backgroundColor)
                    .align(Alignment.CenterVertically)
                    .size(50.dp)

            )
        }
    }
}



var f = false

@Composable
fun MessagesList(messages: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        reverseLayout = true
    ) {
        items(messages.reversed()) { message ->
            MessageItem(message,f)
            f = !f
        }
    }
}

@Composable
fun UserInput(
    textState: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = textState,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text(text = "Type a message") }
        )
        Button(
            onClick = onSendClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = "Send")
        }
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
       val l:List<String> = listOf("hi","hello how can i help you today?","tell me about ahmad ali","ahmad ali kalb.")
        var textState = remember { TextFieldValue() }
//        MessagesList(l)

//        UserInput(textState = textState, onTextChange = {} ) {}
    //                MessageItem("hello world!",false)
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreen(modifier:Modifier =Modifier) {
    var messages by remember { mutableStateOf(listOf<String>("hi","hello how can i help you today?","tell me about ahmad ali","ahmad ali kalb.")
    ) }
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize()) {
        MessagesList(messages = messages, modifier = Modifier.weight(1f))
        UserInput(
            textState = textState,
            onTextChange = { textState = it },
            onSendClick = {
                if (textState.text.isNotBlank()) {
                    messages = messages + textState.text
                    // Here you can also add a chatbot response to messages
                    messages = messages + "Bot Response to: ${textState.text}"
                    textState = TextFieldValue("")
                }

            }
        )
    }
}

