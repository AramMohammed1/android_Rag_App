package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LLMModel(
    @SerialName(value = "name")
    val name:String,
    @SerialName(value = "description")
    val description:String
)
