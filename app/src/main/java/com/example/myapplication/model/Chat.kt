package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    @SerialName(value = "_id")
    val id:String,

    @SerialName(value = "title")
    val title:String,

    @SerialName(value = "messages")
    val messages:List<Message> = listOf(),

    @SerialName(value = "chunks")
    val   chunks :Int = 500,

    @SerialName(value = "numofresults")
    val numofresults:Int =1,

    @SerialName(value = "fileName")
    val fileName:List<String> =listOf(),

    @SerialName(value = "modelname")
    val modelName:String="llama",


    )
