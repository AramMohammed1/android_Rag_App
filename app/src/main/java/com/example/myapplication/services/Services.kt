package com.example.myapplication.services

import com.example.myapplication.network.BackApiService
import com.example.myapplication.model.Chat
import com.example.myapplication.model.GetAllChatsResponse
import com.example.myapplication.model.Message
import com.example.myapplication.model.NewChatRequest
import com.example.myapplication.model.SettingRequest
import com.example.myapplication.model.Sign
import com.example.myapplication.model.UploadResponse
import com.example.myapplication.model.VoidResponse
import okhttp3.MultipartBody
import retrofit2.Response


interface IService {
    suspend fun getAllChats(token:String ): GetAllChatsResponse
    suspend fun createNewChat(token:String,chat:NewChatRequest): VoidResponse
    suspend fun getSelectedChat(token:String,chatId:String): Chat
    suspend fun postNewMessage(token:String,chatId:String,message: Message):Message
    suspend fun updateSettings(token:String,chatId: String,chunks:Int,numOfResults:Int):VoidResponse
    suspend fun login(email:String,password:String): String
    suspend fun signup(email:String,password:String): String
    suspend fun uploadFiles(token:String,chatId: String,files: List<MultipartBody.Part>): Response<UploadResponse>
}

class NetworkService(private val apiService: BackApiService):IService{
    override suspend fun getAllChats(token:String): GetAllChatsResponse{
        return apiService.getAllChats("bearer $token")
    }

    override suspend fun createNewChat(token:String,chat: NewChatRequest): VoidResponse {
        return apiService.postNewChat("bearer $token",chat)
    }

    override suspend fun getSelectedChat(token: String,chatId: String): Chat {
        return apiService.getSelectedChat("bearer $token",chatId)
    }

    override suspend fun postNewMessage(token:String,chatId: String,message:Message): Message {
        return apiService.postNewMessage("bearer $token",chatId,message)
    }

    override suspend fun updateSettings(token:String,chatId: String, chunks: Int, numOfResults: Int):VoidResponse {
        return apiService.updateSettings("bearer $token",chatId, SettingRequest(chunks,numOfResults))
    }
    override suspend fun uploadFiles(token: String,chatId:String,files: List<MultipartBody.Part>):Response<UploadResponse>
    {
        return apiService.uploadFile("bearer $token",chatId,files)
    }

    override suspend fun login(email:String,password:String): String {
        return apiService.login(Sign(email,password))
    }
    override suspend fun signup(email:String,password:String):String {
        return apiService.signup(Sign(email,password))
    }

}