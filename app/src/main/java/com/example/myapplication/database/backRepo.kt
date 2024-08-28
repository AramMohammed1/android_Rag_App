package com.example.myapplication.database

import android.util.Log
import com.example.myapplication.Network.BackApiService
import com.example.myapplication.model.Chat
import com.example.myapplication.model.GetAllChatsResponse
import com.example.myapplication.model.Message
import com.example.myapplication.model.NewChatRequest
import com.example.myapplication.model.SettingRequest
import com.example.myapplication.model.UploadResponse
import com.example.myapplication.model.VoidResponse
import okhttp3.MultipartBody
import retrofit2.Response


interface BackRepo {
    suspend fun getAllChats(userEmail:String ): GetAllChatsResponse
    suspend fun createNewChat(chat:NewChatRequest): VoidResponse
    suspend fun getSelectedChat(chatId:String): Chat
    suspend fun postNewMessage(chatId:String,message: Message):Message
    suspend fun updateSettings(chatId: String,chunks:Int,numOfResults:Int):VoidResponse
    suspend fun uploadFiles(chatId: String,files: List<MultipartBody.Part>): Response<UploadResponse>
}

class NetworkBackRepository(private val apiService: BackApiService):BackRepo{
    override suspend fun getAllChats(userEmail:String): GetAllChatsResponse{
        return apiService.getAllChats(userEmail)
    }

    override suspend fun createNewChat(chat: NewChatRequest): VoidResponse {
        return apiService.postNewChat(chat)
    }

    override suspend fun getSelectedChat(chatId: String): Chat {
        return apiService.getSelectedChat(chatId)
    }

    override suspend fun postNewMessage(chatId: String,message:Message): Message {
        return apiService.postNewMessage(chatId,message)
    }

    override suspend fun updateSettings(chatId: String, chunks: Int, numOfResults: Int):VoidResponse {
        return apiService.updateSettings(chatId, SettingRequest(chunks,numOfResults))
    }
    override suspend fun uploadFiles(chatId:String,files: List<MultipartBody.Part>):Response<UploadResponse>
    {
        return apiService.uploadFile(chatId,files)
    }


}