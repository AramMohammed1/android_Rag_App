package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    @SerialName(value = "id")
    val id:String,

    @SerialName(value = "title")
    val title:String

)
