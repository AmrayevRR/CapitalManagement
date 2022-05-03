package com.example.capitalmanagement

import android.app.Application
import com.example.capitalmanagement.api.ExchangeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyApplication: Application() {

    private var exchangeApiService: ExchangeApiService? = null

    override fun onCreate() {
        super.onCreate()

        instance = this

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.exchangeratesapi.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        exchangeApiService = retrofit.create(ExchangeApiService::class.java)
    }

    fun getExchangeApiService(): ExchangeApiService? {
        return exchangeApiService
    }

    companion object {
        lateinit var instance: MyApplication
    }
}