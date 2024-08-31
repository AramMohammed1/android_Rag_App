package com.example.myapplication.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.RagApplication
import com.example.myapplication.services.IService
import com.example.myapplication.model.Chat
import com.example.myapplication.model.Message
import com.example.myapplication.model.NewChatRequest
import com.example.myapplication.utils.TokenManager
import kotlinx.coroutines.launch

sealed interface UserLoginState{
    data class Success(val token:String):UserLoginState
    object Error:UserLoginState
    object Loading:UserLoginState
}


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

class ChatListViewModel(private val service: IService,private val tokenManager: TokenManager): ViewModel() {
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
    var userLoginState :UserLoginState by mutableStateOf(UserLoginState.Loading)
        internal set
    val showNewChatPopUp = mutableStateOf(false)
    init {
//        getAllChats("ahmadali")
    }
    fun getAllChats(token:String){
        viewModelScope.launch {
            chatListUiState = ChatListUiState.Loading
            chatListUiState = try {
                ChatListUiState.Success(service.getAllChats(token).chatList)
            }
            catch (e: Exception){
                ChatListUiState.Error
            }
        }
    }
    fun login(email:String,password:String){
        viewModelScope.launch {
            userLoginState= UserLoginState.Loading
            userLoginState=try{
                val token=service.login(email,password)
                tokenManager.saveToken(token)
                UserLoginState.Success(token)
            }
            catch (e:Exception){
                UserLoginState.Error
            }
        }
    }
    fun logout() {
        tokenManager.clearToken()
        userLoginState = UserLoginState.Error
    }

    fun signup(email:String,password:String){
        viewModelScope.launch {
            userLoginState= UserLoginState.Loading
            userLoginState=try{
                UserLoginState.Success(service.signup(email, password))
            }
            catch (e:Exception){
                UserLoginState.Error
            }
        }
    }

    fun setSelectedChat(token:String,chatId:String){
        viewModelScope.launch {
            selectedChatUiState = SelectedChatUiState.Loading
            selectedChatUiState = try {
                var chat = service.getSelectedChat(token,chatId)
                messages=chat.messages
                SelectedChatUiState.Success(chat)
            }
            catch (e: Exception){
                SelectedChatUiState.Error
            }
        }
    }
    fun sendMessage(token:String,chatId:String,message:Message){
        viewModelScope.launch {
            messageState=MessageState.Loading
            messageState=
                try{
                    messages=messages+message
                    var response = service.postNewMessage(token,chatId,message)
                    messages=messages+response
                    MessageState.Success(message)
                }
                catch (e: Exception){
                    MessageState.Error
                }
        }
    }
    fun postSettings(token:String,chatId:String,chunks:Int,numofresutls :Int){
        viewModelScope.launch {
            settingsState = SettingsState.Loading
            try {
                service.updateSettings(token,chatId,chunks,numofresutls)
                SettingsState.Success
            }
            catch (e:Exception){
                SettingsState.Error
            }
        }
    }
    fun createNewChat(token:String,createdAt:String,title:String) {
        viewModelScope.launch {
            newChatCreateState = NewChatCreateState.Loading
            try {
                val newChat = service.createNewChat(token,NewChatRequest(createdAt, title))
                newChatCreateState = NewChatCreateState.Success
                getAllChats(token)
            } catch (e: Exception) {
                newChatCreateState = NewChatCreateState.Error
            }
        }
    }
    companion object{
        val Factory:ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RagApplication)
                val service = application.container.backRepo
                val tokenManager = TokenManager(application.applicationContext)
                ChatListViewModel(service,tokenManager)
            }
        }
    }

}