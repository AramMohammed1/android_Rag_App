package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.model.Chat
import com.example.myapplication.model.GetAllChatsResponse
import com.example.myapplication.model.Message
import com.example.myapplication.model.NewChatRequest
import com.example.myapplication.model.SettingRequest
import com.example.myapplication.model.Sign
import com.example.myapplication.model.UploadResponse
import com.example.myapplication.model.VoidResponse
import com.example.myapplication.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


val BackRsponseJson= Json{
    ignoreUnknownKeys = true
}

interface BackApiService{
    @POST("users/login")
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body sign:Sign
    ):String

    @POST("users/signup")
    @Headers("Content-Type: application/json")
    suspend fun signup(
        @Body sign:Sign
    ):String

    @GET("user/chats")
    suspend fun getAllChats(
        @Header("Authorization") token: String
    ): GetAllChatsResponse

    @GET("chat/{chat_id}")
    suspend fun getSelectedChat(
        @Header("Authorization") token: String,
        @Path("chat_id") chatId:String
    ):Chat

    @POST("chat/add")
    @Headers("Content-Type: application/json")
    suspend fun postNewChat(
        @Header("Authorization") token: String,
        @Body chat:NewChatRequest
    ): VoidResponse

    @POST("chat/{chat_id}/add_message")
    @Headers("Content-Type: application/json")
    suspend fun postNewMessage(
        @Header("Authorization") token: String,
        @Path("chat_id") chatId:String,
        @Body message: Message
    ):Message


    @POST("chat/{chat_id}/update")
    @Headers("Content-Type: application/json")
    suspend fun updateSettings(
        @Header("Authorization") token: String,
        @Path("chat_id") chatId:String,
        @Body settings: SettingRequest
    ):VoidResponse

    @Multipart
    @POST("chat/{chat_id}/updatefile")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Path("chat_id") chatId:String,
        @Part file: List<MultipartBody.Part>
    ): Response<UploadResponse>

    companion object{
        val retrofit: Retrofit = Retrofit.Builder()
            .client(
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor(getLoggerInterceptor())
                    .build()
            )
            .addConverterFactory(BackRsponseJson.asConverterFactory("application/json".toMediaType()))
            .baseUrl(Constants.WEB_BACK_URL)
            .build()

    }



}