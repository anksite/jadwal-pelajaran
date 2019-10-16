package com.anksite.jadwalpelajaran

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    fun getClient(baseURL: String): Retrofit{
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout((60*5).toLong(), TimeUnit.SECONDS)
            .readTimeout(   (60*5).toLong(), TimeUnit.SECONDS)
            .writeTimeout(  (60*5).toLong(), TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}