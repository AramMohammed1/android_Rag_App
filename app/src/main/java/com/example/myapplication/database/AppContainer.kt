package com.example.myapplication.database

import com.example.myapplication.Network.RagApiService


interface AppContainer{
    val ragResponseRepo:RagResponseRepo
}

class DefaultAppContainer():AppContainer{
    private val retrofitService: RagApiService by lazy{
        RagApiService.retrofit.create(RagApiService::class.java)
    }

    override val ragResponseRepo: RagResponseRepo by lazy {
        NetworkRagRepository(retrofitService)
    }
}