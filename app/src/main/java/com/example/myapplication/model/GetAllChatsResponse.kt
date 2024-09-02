package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetAllChatsResponse(
    @SerialName(value = "chats")
    val chatList:List<Chat> = emptyList()
)
