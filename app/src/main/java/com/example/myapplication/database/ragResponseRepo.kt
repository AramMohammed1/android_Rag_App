package com.example.myapplication.database

import com.example.myapplication.Network.RagApiService
import com.example.myapplication.model.ChatResponse
import com.example.myapplication.model.RequestModel
import com.example.myapplication.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import java.io.File

interface RagResponseRepo {
    suspend fun postQuestion(question:String,chunks:Int,numofresults:Int ):ChatResponse
    suspend fun uploadFiles(files: List<MultipartBody.Part>): Response<UploadResponse>


}

class NetworkRagRepository(private val apiService:RagApiService):RagResponseRepo{

    override suspend fun postQuestion(question:String,chunks:Int,numofresults:Int): ChatResponse {
        return apiService.postQuestion(RequestModel(listOf("aram_mohammed.pdf"),question,chunks,numofresults) )
    }

    override suspend fun uploadFiles(files: List<MultipartBody.Part>):Response<UploadResponse>
    {
        return apiService.uploadFile(files)
    }



}