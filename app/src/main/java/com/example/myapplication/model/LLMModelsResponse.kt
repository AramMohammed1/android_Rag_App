package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LLMModelsResponse(
    @SerialName("models")
    val models:List<LLMModel>

)
