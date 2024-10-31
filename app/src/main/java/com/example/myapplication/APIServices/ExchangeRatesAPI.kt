package com.example.myapplication.APIServices

import com.example.myapplication.model.ExchangeRatesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Use to call ExchangeRatesAPI to get list rates
interface ExchangeRatesAPI {
    @GET("latest")
    fun getExchangeRates(
        @Query("access_key") apiKey: String
    ): Call<ExchangeRatesResponse>
}