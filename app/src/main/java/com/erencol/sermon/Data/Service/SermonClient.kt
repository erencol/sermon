package com.erencol.sermon.data.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object SermonClient {
    fun createSermonClient(): ISermons {
        val client = OkHttpClient.Builder()
            .cache(null)
            .addInterceptor(RetryOn404Interceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Host.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            
        return retrofit.create(ISermons::class.java)
    }

    fun createReligiousClient(): IReligious {
        val client = OkHttpClient.Builder()
            .cache(null)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Host.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            
        return retrofit.create(IReligious::class.java)
    }
}
