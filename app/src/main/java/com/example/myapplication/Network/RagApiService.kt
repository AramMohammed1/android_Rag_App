package com.example.myapplication.Network

import com.example.myapplication.model.ChatResponse
import com.example.myapplication.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query


fun getLoggerInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging

}

val RsponseJson= Json{
    ignoreUnknownKeys = true
}

interface RagApiService{

    @FormUrlEncoded
    @POST("api/query")
    suspend fun postQuestion(
        @Field("question") question: String,
        @Field("chunks") chunks: Int,
        @Field("numofresults") numofresults: Int,
        @Query("Content-Type")
        contentType :String = "application/x-www-form-urlencoded",
    ): ChatResponse

    companion object{
        val retrofit: Retrofit = Retrofit.Builder()
            .client(
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor(getLoggerInterceptor())
                    .build()
            )
            .addConverterFactory(RsponseJson.asConverterFactory("application/json".toMediaType()))
            .baseUrl(Constants.BASE_URL)
            .build()

    }
}