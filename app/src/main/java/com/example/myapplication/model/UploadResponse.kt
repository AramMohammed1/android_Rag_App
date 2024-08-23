package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UploadResponse(
    @SerialName(value ="message")
    val message: String
)
