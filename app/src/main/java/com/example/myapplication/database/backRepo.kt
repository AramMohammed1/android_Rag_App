package com.example.myapplication.database

import android.util.Log
import com.example.myapplication.Network.BackApiService
import com.example.myapplication.model.GetAllChatsResponse
import com.example.myapplication.model.NewChatRequest
import com.example.myapplication.model.VoidResponse
import okhttp3.MultipartBody
import retrofit2.Response


interface BackRepo {
    suspend fun getAllChats(userEmail:String ): GetAllChatsResponse
    suspend fun createNewChat(chat:NewChatRequest): VoidResponse
}

class NetworkBackRepository(private val apiService: BackApiService):BackRepo{
    override suspend fun getAllChats(userEmail:String): GetAllChatsResponse{
        return apiService.getAllChats(userEmail)
    }

    override suspend fun createNewChat(chat: NewChatRequest): VoidResponse {
        return apiService.postNewChat(chat)
    }
}