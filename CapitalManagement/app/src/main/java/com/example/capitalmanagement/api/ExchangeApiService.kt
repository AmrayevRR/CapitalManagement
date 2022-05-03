package com.example.capitalmanagement.api

import com.example.capitalmanagement.model.ExchangePost
import retrofit2.Call
import retrofit2.http.GET

interface ExchangeApiService {
    @GET("latest?access_key=a6dda8dafde9e4a8477dfdfc69b9a388&symbols=USD,KZT,RUB,KGS,GBP,CNY&format=1")
    fun getExchangePost(): Call<ExchangePost>
}