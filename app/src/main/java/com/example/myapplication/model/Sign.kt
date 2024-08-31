package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sign(
    @SerialName(value = "email")
    val email:String,
    @SerialName(value = "password")
    val password:String
)
