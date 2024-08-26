package com.example.myapplication.ui.screens

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.Message
import com.example.myapplication.viewModel.FileUploadViewModel
import com.example.myapplication.viewModel.MessageState
import com.example.myapplication.viewModel.MessageViewModel
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

                Text("14-11-2002", color = Color.Gray)
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
fun ModelMessageItem(message: Message=Message("google",true,""),modifier :Modifier = Modifier) {
    val backgroundColor =  Color(0xFFF0F0F0 )
    var showDate by remember { mutableStateOf(false) }
    Column (
    ){

        if(showDate) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text("14-11-2002", color = Color.Gray)
            }
        }

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
            if(message.isUser){
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
            trailingIcon = {
                uploadbutton(fileUploadViewModel =fileUploadViewModel )
            }
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
    messageViewModel: MessageViewModel,
    fileUploadViewModel: FileUploadViewModel,
    modifier: Modifier = Modifier) {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var fileNames by fileUploadViewModel.fileNames
    var uploadStatus by fileUploadViewModel.uploadStatus
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()))
    {
        val messageState = messageViewModel.messageState
        val context = LocalContext.current
        MessagesList(messages = messages, modifier = modifier.weight(1f))

        UserInput(
            textState = textState,

            onTextChange = { textState = it },

            onSendClick = {
                val localTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString()
                if (textState.text.isNotBlank() && uploadStatus == "Upload Successful") {
                    messages = messages + Message(textState.text,true,localTime)
                    messages= messages + Message("Loading ... ",false,"")
                    messageViewModel.sendMessage(fileNames,textState.text,500,2)
                    textState = TextFieldValue("")
                }
                else{
                    Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT)
                }

            },
            fileUploadViewModel=fileUploadViewModel,
            modifier = modifier
        )
        LaunchedEffect(messageState) {
            when (messageState) {
                is MessageState.Success -> {
                    messages = messages.dropLast(1)
                    messages = messages + messageState.message
                }
                is MessageState.Loading -> {
                }
                is MessageState.Error -> {
                    val localTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString()
                    messages = messages.dropLast(1)
                    messages = messages + Message("Something went wrong, please ask your question again", false,localTime)
                }
            }
        }
    }
}



@Composable
fun uploadbutton(fileUploadViewModel:FileUploadViewModel){
    var selectedFileUris by fileUploadViewModel.selectedFileUris
    var uploadStatus by fileUploadViewModel.uploadStatus
    val fileNames by fileUploadViewModel.fileNames
    val resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
        fileUploadViewModel.updateSelectedFileUris(uris)
    }
    val context = LocalContext.current

    Column {
        Button(
            onClick = {
                resultLauncher.launch(arrayOf("*/*"))

            },
            colors = ButtonDefaults.buttonColors(
                Color.Transparent
            ),
        ) {
            Icon(
                painter =
                when (uploadStatus) {
                    "Upload Failed" -> painterResource(id = R.drawable.baseline_error_outline_24)
                    else -> painterResource(id = R.drawable.baseline_attach_file_24)
                },
                contentDescription = "Attach File",
                tint =
                when (uploadStatus) {
                    "Upload Failed" -> Color.Red
                    else -> Color.DarkGray
                },
                modifier = Modifier.background(Color.Transparent)
            )
        }


    }
    LaunchedEffect(uploadStatus) {

        when (uploadStatus) {
            "Upload Successful" -> Toast.makeText(context, "Files Uploaded successfully", Toast.LENGTH_SHORT).show()
            "Upload Failed" -> Toast.makeText(context, "Error occurred Uploading files", Toast.LENGTH_SHORT).show()
            null -> {}
        }
    }
}

