package com.example.testnauka.api

import com.example.testnauka.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class ClientForApi {
    private val restApi: RestApi

    init {
        val restApiClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor())
            .build()

        /*
            api interface for retrofit requests
         */
        restApi = Retrofit.Builder()
            .client(restApiClient)
            .baseUrl(Constants.BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(RestApi::class.java)

    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun getRestApi() = restApi

}