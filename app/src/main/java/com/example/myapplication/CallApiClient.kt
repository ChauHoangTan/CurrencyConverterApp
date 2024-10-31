package com.example.myapplication

import com.example.myapplication.APIServices.CurrencyFlagsAPI
import com.example.myapplication.APIServices.ExchangeRatesAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Use to config api client
object CallExchangeRatesApiClient {
    private const val BASE_URL = "https://api.exchangeratesapi.io/v1/"

    val apiService: ExchangeRatesAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRatesAPI::class.java)
    }
}

object CallCurrencyFlagsApiClient {
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    val apiService: CurrencyFlagsAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyFlagsAPI::class.java)
    }
}