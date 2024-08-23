package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody

data class UploadRequest(
    @SerialName("files")
    val files:List<MultipartBody.Part> = listOf()

)
