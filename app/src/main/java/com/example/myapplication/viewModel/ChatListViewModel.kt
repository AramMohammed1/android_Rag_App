package com.example.myapplication.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.RagApplication
import com.example.myapplication.database.BackRepo
import com.example.myapplication.model.Chat
import com.example.myapplication.model.NewChatRequest
import kotlinx.coroutines.launch


sealed interface ChatListUiState{
    data class Success(val chats :List<Chat>) : ChatListUiState
    object Error : ChatListUiState
    object Loading : ChatListUiState
}

class ChatListViewModel(private val backRepo: BackRepo): ViewModel() {
    var chatListUiState: ChatListUiState by mutableStateOf(ChatListUiState.Loading)
        internal set
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
                ChatListUiState.Error
            }
        }
    }

    fun createNewChat(participant:List<String>,createdAt:String,title:String) {
        viewModelScope.launch {
            backRepo.createNewChat(NewChatRequest(participant, createdAt, title))
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