package com.example.myapplication.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelMessageResponse(
    @SerialName(value = "model")
    var model:String?= null,
    @SerialName(value = "message")
    var message:String?= null,
    @SerialName(value = "role")
    var role:String? = null
)
