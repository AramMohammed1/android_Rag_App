package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RagRequestModel(
    @SerialName(value = "filepaths")
    val filename:List<String> = listOf(),
    @SerialName(value = "question")
    val message:String? = null,
    @SerialName(value = "chunks")
    val chunks:Int? = null,
    @SerialName(value = "numofresults")
    val numofresults:Int? = null,

)
