package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Message(
    @SerialName(value = "content")
    val text: String,
    @SerialName("message_type")
    val isUser: String,
    @SerialName("message_time")
    val date:String
)
