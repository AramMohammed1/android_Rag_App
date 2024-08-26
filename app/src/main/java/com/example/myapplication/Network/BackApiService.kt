package com.example.myapplication.Network

import com.example.myapplication.model.GetAllChatsResponse
import com.example.myapplication.model.NewChatRequest
import com.example.myapplication.model.VoidResponse
import com.example.myapplication.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime
import java.util.Date


val BackRsponseJson= Json{
    ignoreUnknownKeys = true
}

interface BackApiService{

    @GET("user/{user_email}/chats")
    suspend fun getAllChats(
        @Path("user_email")  userEmail: String
    ): GetAllChatsResponse


    @POST("chat/add")
    @Headers("Content-Type: application/json")
    suspend fun postNewChat(
        @Body chat:NewChatRequest
    ): VoidResponse


//    @GET("chat/{chat_id}")
//    suspend fun getChatMessages(
//
//    ):


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