package com.crawford.ciq.dev.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {
    private const val BASE_URL = "https://ciqmobilecmsapiqa.crawco.com/api/"

    private fun getRetrofit(): Retrofit {
     val client:OkHttpClient = OkHttpClient()
        client.connectTimeoutMillis()
        client.readTimeoutMillis()
        return Retrofit.Builder()
            .baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }


    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}