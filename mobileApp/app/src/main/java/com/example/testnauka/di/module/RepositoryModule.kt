package com.example.testnauka.di.module


import com.example.testnauka.api.ClientForApi
import com.example.testnauka.repository.ApiRepository
import toothpick.config.Module
import toothpick.ktp.binding.bind


class RepositoryModule: Module() {
    private val client = ClientForApi()

    init {
        bind<ApiRepository>().toInstance {
            ApiRepository(client.getRestApi())
        }
    }
}