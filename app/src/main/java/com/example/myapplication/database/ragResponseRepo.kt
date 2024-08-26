package com.example.myapplication.database

import com.example.myapplication.Network.RagApiService
import com.example.myapplication.model.ModelMessageResponse
import com.example.myapplication.model.RagRequestModel
import com.example.myapplication.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface RagResponseRepo {
    suspend fun postQuestion(fileNames:List<String>,question:String,chunks:Int,numofresults:Int ):ModelMessageResponse
    suspend fun uploadFiles(files: List<MultipartBody.Part>): Response<UploadResponse>


}

class NetworkRagRepository(private val apiService:RagApiService):RagResponseRepo{

    override suspend fun postQuestion(fileNames:List<String>,question:String,chunks:Int,numofresults:Int): ModelMessageResponse {
        return apiService.postQuestion(RagRequestModel(fileNames,question,chunks,numofresults) )
    }

    override suspend fun uploadFiles(files: List<MultipartBody.Part>):Response<UploadResponse>
    {
        return apiService.uploadFile(files)
    }



}