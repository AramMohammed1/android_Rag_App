package com.example.myapplication.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontVariation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.RagApplication
import com.example.myapplication.database.BackRepo
import com.example.myapplication.model.Chat
import com.example.myapplication.model.Message
import com.example.myapplication.model.NewChatRequest
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException


sealed interface ChatListUiState{
    data class Success(val chats :List<Chat>) : ChatListUiState
    object Error : ChatListUiState
    object Loading : ChatListUiState
}

sealed interface NewChatCreateState{
    object Success : NewChatCreateState
    object Error : NewChatCreateState
    object Loading : NewChatCreateState

}

sealed interface SelectedChatUiState{
    data class Success(val chat:Chat):SelectedChatUiState
    object Error: SelectedChatUiState
    object Loading: SelectedChatUiState
}

sealed interface MessageState {
    data class Success(val message: Message) : MessageState
    object Error : MessageState
    object Loading : MessageState
}


sealed interface SettingsState{
    object Success :SettingsState
    object Loading :SettingsState
    object Error :SettingsState
}

class ChatListViewModel(private val backRepo: BackRepo): ViewModel() {
    var chatListUiState: ChatListUiState by mutableStateOf(ChatListUiState.Loading)
        internal set
    var newChatCreateState: NewChatCreateState by mutableStateOf(NewChatCreateState.Loading)
        internal set
    var selectedChatUiState:SelectedChatUiState by mutableStateOf(SelectedChatUiState.Loading)
        internal set
    var messageState:MessageState by mutableStateOf(MessageState.Loading)
        internal set
    var settingsState: SettingsState by mutableStateOf(SettingsState.Loading)
        internal set
    var messages:List<Message> by mutableStateOf<List<Message>>(emptyList())
        internal set
    val showNewChatPopUp = mutableStateOf(false)
    init {
        getAllChats("ahmadali")
    }
    fun getAllChats(userName:String){
        viewModelScope.launch {
            chatListUiState = ChatListUiState.Loading
            chatListUiState = try {
                ChatListUiState.Success(backRepo.getAllChats(userName).chatList)
            }
            catch (e: Exception){
                Log.e("aram",e.toString())
                ChatListUiState.Error
            }
        }
    }

    fun setSelectedChat(chatId:String){
        viewModelScope.launch {
            selectedChatUiState = SelectedChatUiState.Loading
            selectedChatUiState = try {
                var chat = backRepo.getSelectedChat(chatId)
                messages=chat.messages
                SelectedChatUiState.Success(chat)
            }
            catch (e: Exception){
                SelectedChatUiState.Error
            }
        }
    }
    fun sendMessage(chatId:String,message:Message){
        viewModelScope.launch {
            messageState=MessageState.Loading
            messageState=
                try{
                    messages=messages+message
                    var response = backRepo.postNewMessage(chatId,message)
                    messages=messages+response
                    MessageState.Success(message)
                }
                catch (e: Exception){
                    MessageState.Error
                }
        }
    }
    fun postSettings(chatId:String,chunks:Int,numofresutls :Int){
        viewModelScope.launch {
            settingsState = SettingsState.Loading
            try {
                backRepo.updateSettings(chatId,chunks,numofresutls)
                SettingsState.Success
            }
            catch (e:Exception){
                SettingsState.Error
            }
        }
    }
    fun createNewChat(participant:List<String>,createdAt:String,title:String) {
        viewModelScope.launch {
            newChatCreateState = NewChatCreateState.Loading
            try {
                val newChat = backRepo.createNewChat(NewChatRequest(participant, createdAt, title))
                newChatCreateState = NewChatCreateState.Success

                // Refresh the chat list after successful chat creation
                getAllChats("ahmadali")
            } catch (e: Exception) {
                newChatCreateState = NewChatCreateState.Error
            }
        }
    }
    companion object{
        val Factory:ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RagApplication)
                val backRepo = application.container.backRepo
                ChatListViewModel(backRepo)
            }
        }
    }

}