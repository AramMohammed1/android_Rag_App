package com.example.myapplication.database

import android.app.backup.BackupHelper
import com.example.myapplication.Network.BackApiService
import com.example.myapplication.Network.RagApiService


interface AppContainer{
    val ragResponseRepo:RagResponseRepo
    val backRepo:BackRepo
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

    override val backRepo: BackRepo by lazy{
        NetworkBackRepository(backRetrofitService)
    }
}