package com.example.myapplication.viewModel

import android.util.Log
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
import com.example.myapplication.database.RagResponseRepo
import com.example.myapplication.model.Chat
import com.example.myapplication.model.Message
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException

sealed interface MessageState {
    data class Success(val message: Message) : MessageState
    object Error : MessageState
    object Loading : MessageState
}

class MessageViewModel(private val ragResponseRepo: RagResponseRepo,private val backRepo:BackRepo): ViewModel(){
    var messageState:MessageState by mutableStateOf(MessageState.Loading)
        internal set
    init {
    }


    fun sendMessage(fileNames:List<String>,question:String, chunks:Int, numofresults:Int){
        viewModelScope.launch {

            messageState=try{
                 var x=ragResponseRepo.postQuestion(fileNames,question,chunks,numofresults).message
                 if(x==null){
                     x=""
                 }

                MessageState.Success(Message(x,false,""))
             }
             catch (e: IOException){
                 MessageState.Error
             }
             catch (e: retrofit2.HttpException){
                 MessageState.Error
             }
            catch(e : ConnectException){
                MessageState.Error
            }
        }
    }
    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RagApplication)
                val ragResponseRepo = application.container.ragResponseRepo
                val backRepo = application.container.backRepo
                MessageViewModel(ragResponseRepo,backRepo)
            }
        }
    }

}

