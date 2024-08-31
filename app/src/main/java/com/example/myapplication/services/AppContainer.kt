package com.example.myapplication.services

import com.example.myapplication.network.BackApiService
import com.example.myapplication.network.RagApiService
import com.example.myapplication.utils.TokenManager


interface AppContainer{
    val ragResponseRepo:RagResponseRepo
    val backRepo:IService
}

class DefaultAppContainer():AppContainer{
    private val ragRetrofitService: RagApiService by lazy{
        RagApiService.retrofit.create(RagApiService::class.java)
    }
    private val  backRetrofitService : BackApiService by lazy{
        BackApiService.retrofit.create(BackApiService::class.java)
    }

    override val ragResponseRepo: RagResponseRepo by lazy {
        NetworkRagRepository(ragRetrofitService)
    }

    override val backRepo: IService by lazy{
        NetworkService(backRetrofitService)
    }
}