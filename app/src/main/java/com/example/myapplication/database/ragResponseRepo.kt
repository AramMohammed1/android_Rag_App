package com.example.myapplication.database

import com.example.myapplication.Network.RagApiService
import com.example.myapplication.model.ChatResponse

interface RagResponseRepo {
    suspend fun postQuestion(question:String,chunks:Int,numofresults:Int ):ChatResponse
}

class NetworkRagRepository(private val apiService:RagApiService):RagResponseRepo{

    override suspend fun postQuestion(question:String,chunks:Int,numofresults:Int): ChatResponse {
        return apiService.postQuestion(question,chunks,numofresults)
    }
}