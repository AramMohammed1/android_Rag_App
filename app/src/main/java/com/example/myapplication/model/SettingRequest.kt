package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SettingRequest(
    @SerialName(value = "chunks")
    val chunks:Int=500,
    @SerialName(value = "numofresults")
    val numofresults:Int=3,
    @SerialName(value = "modelname")
    val modelname:String,

)
