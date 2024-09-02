package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class NewChatRequest(
    @SerialName(value = "created_at") val createdAt: String,
    @SerialName(value = "title") val title: String

)
