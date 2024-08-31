package com.example.myapplication.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.example.myapplication.model.Message
import com.example.myapplication.viewModel.ChatListViewModel
import com.example.myapplication.viewModel.FileUploadViewModel
import com.example.myapplication.viewModel.MessageState
import com.example.myapplication.viewModel.SelectedChatUiState
import com.example.myapplication.viewModel.UserLoginState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



@Composable
fun UserMessageItem(message: Message,modifier:Modifier =Modifier){
    val backgroundColor = Color(0xFF0084FF)
    var showDate by remember { mutableStateOf(false) }
    Column (
    ){

        if(showDate) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(message.date, color = Color.Gray)
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { showDate = !showDate }
            ,
            horizontalArrangement = Arrangement.End
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

}

@Preview (showBackground = true)
@Composable
fun ModelMessageItem(message: Message=Message("google","request",""),modifier :Modifier = Modifier) {
    val backgroundColor =  Color(0xFFF0F0F0 )
    var showDate by remember { mutableStateOf(false) }
    Column(modifier = Modifier.imePadding() ){

        if(showDate) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(message.date, color = Color.Gray)
            }
        }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDate = !showDate }
            .padding(vertical = 4.dp)
            .imePadding(),

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
                .imePadding()

        ) {
            Text(text = message.text, fontSize = 16.sp, color = Color.Black)

        }
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
            if(message.isUser == "request"){
                UserMessageItem(message)

            }
            else{
                ModelMessageItem(message)
            }
        }
    }
}


@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput(
    modifier:Modifier = Modifier,
    fileUploadViewModel:FileUploadViewModel,
    textState: TextFieldValue =  TextFieldValue("") ,
    onTextChange: (TextFieldValue) -> Unit ={},
    onSendClick: () -> Unit={},
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val myColor = Color(0xFF0084FF)

        OutlinedTextField(
            value = textState,
            onValueChange = onTextChange,
            modifier = modifier
                .weight(1f)
                .padding(end = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedLabelColor = myColor,
                cursorColor = myColor,
                focusedLabelColor = myColor,
                containerColor = myColor.copy(.2f),
                focusedIndicatorColor =myColor,
                unfocusedIndicatorColor = myColor.copy(
                    0.5f)
            ),
            shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
            placeholder = { Text(text = "Type a message") },
            singleLine = true,

        )
        Button(
            onClick = onSendClick,
            colors = ButtonDefaults.buttonColors(containerColor= Color(0xFF0084FF)),
            modifier = modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = "send")
        }
    }
}





@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatListViewModel: ChatListViewModel,
    fileUploadViewModel: FileUploadViewModel,
    modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var chunksTextState by remember { mutableStateOf(TextFieldValue("")) }
    var numOfResultsTextState by remember { mutableStateOf(TextFieldValue("")) }
    var fileNames by fileUploadViewModel.fileNames
    var uploadStatus by fileUploadViewModel.uploadStatus
    var selectedChatUiState = chatListViewModel.selectedChatUiState
    var messages = chatListViewModel.messages
    var showPopup by fileUploadViewModel.showPopup
    var userLoginState:UserLoginState = chatListViewModel.userLoginState
    var settingsState = chatListViewModel.settingsState
    if (showPopup) {
        Dialog(onDismissRequest = { showPopup = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Chat settings", style = MaterialTheme.typography.bodySmall)

                    TextField(
                        value = chunksTextState,
                        onValueChange = {  chunksTextState = it },
                        label = { Text("chunks") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = numOfResultsTextState,
                        onValueChange = { numOfResultsTextState = it },
                        label = { Text("Number of results") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(onClick = { showPopup = false }, colors = ButtonDefaults.buttonColors(containerColor= Color(0xFF0084FF)),
                        ) {
                            Text("Close")
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Button(onClick = {

                            var chunks = try {
                                chunksTextState.text.toInt()
                            }catch (e:Exception){500}
                            var numberOfResults = try {
                                numOfResultsTextState.text.toInt()
                            }catch (e:Exception){5}
                            when(selectedChatUiState){
                                is SelectedChatUiState.Success->{
                                    when (userLoginState){
                                        is UserLoginState.Success->{
                                            chatListViewModel.postSettings(token = userLoginState.token, chatId = selectedChatUiState.chat.id,chunks = chunks, numofresutls = numberOfResults)

                                        }
                                        else ->{

                                        }
                                    }
                                    showPopup = false
                                }
                                else ->{}
                            }
                        }, colors = ButtonDefaults.buttonColors(containerColor= Color(0xFF0084FF)),
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }

    Column(
            modifier = Modifier.fillMaxSize()
        )
        {
            when(selectedChatUiState) {
                is SelectedChatUiState.Success -> {
                val context = LocalContext.current
                var messageState = chatListViewModel.messageState

                MessagesList(messages = messages, modifier = Modifier.weight(1f))

                UserInput(
                    textState = textState,
                    onTextChange = { textState = it },
                    onSendClick = {
                        val localTime =
                            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString()
                        if (textState.text.isNotBlank()) {
                            if(uploadStatus == "Upload Successful"){
                                when(userLoginState){
                                    is UserLoginState.Success->{
                                        chatListViewModel.sendMessage(token = userLoginState.token, chatId = selectedChatUiState.chat.id,message=Message(textState.text,"request",localTime))

                                    }
                                    else ->{}
                                }
                            textState = TextFieldValue("")
                            }
                            else{
                                Toast.makeText(context, "Please upload you files first!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show()
                        }
                    },
                    fileUploadViewModel = fileUploadViewModel,
                    modifier = Modifier
                )
                LaunchedEffect(messageState) {
                    when (messageState) {
                        is MessageState.Success -> {
                            messages = messages + messageState.message
                        }
                        is MessageState.Loading -> {
                        }
                        is MessageState.Error -> {
                            val localTime =
                                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                    .toString()
                            messages = messages + Message(
                                "Something went wrong, please ask your question again",
                                "response",
                                localTime
                            )
                        }
                    }
                }
            }
           is SelectedChatUiState.Loading -> {
                LoadingScreen()
            }
            is SelectedChatUiState.Error ->{
                ErrorScreen {}
            }
        }
    }
}


@Composable
fun PopupWithTextField() {
    // State to control dialog visibility
    val showDialog = remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }

    // Button to show the dialog
    Button(onClick = { showDialog.value = true }) {
        Text("Show Popup")
    }

    // Dialog implementation
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Enter your text", style = MaterialTheme.typography.bodySmall)

                    // TextField for user input
                    TextField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        label = { Text("Text Field") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(onClick = { showDialog.value = false }) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}

