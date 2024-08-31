package com.example.myapplication.services

import com.example.myapplication.network.BackApiService


interface AppContainer{
    val backRepo:IService
}

class DefaultAppContainer():AppContainer{

    private val  backRetrofitService : BackApiService by lazy{
        BackApiService.retrofit.create(BackApiService::class.java)
    }

    override val backRepo: IService by lazy{
        NetworkService(backRetrofitService)
    }
}