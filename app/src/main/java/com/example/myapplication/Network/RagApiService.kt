package com.example.myapplication.Network

import com.example.myapplication.model.ChatResponse
import com.example.myapplication.model.RagRequestModel
import com.example.myapplication.model.UploadResponse
import com.example.myapplication.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


fun getLoggerInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging

}

val RsponseJson= Json{
    ignoreUnknownKeys = true
}

interface RagApiService{

    @POST("/api/query/")
    @Headers("Content-Type: application/json")
    suspend fun postQuestion(@Body request:RagRequestModel): ChatResponse


    @Multipart
    @POST("/api/uploadfile/")
    suspend fun uploadFile(@Part file: List<MultipartBody.Part>): Response<UploadResponse>

    companion object{
        val retrofit: Retrofit = Retrofit.Builder()
            .client(
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor(getLoggerInterceptor())
                    .build()
            )
            .addConverterFactory(RsponseJson.asConverterFactory("application/json".toMediaType()))
            .baseUrl(Constants.RAG_BASE_URL)
            .build()

    }



}