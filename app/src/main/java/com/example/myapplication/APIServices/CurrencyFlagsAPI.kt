package com.example.myapplication.APIServices

import com.example.myapplication.model.CountryInfoCodeAndCurrency
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Use to call RestCountries API to get list flags
interface CurrencyFlagsAPI {

    @GET("all")
    fun getCurrencyAndFlag(
        @Query("fields") query: String,
    ): Call<List<CountryInfoCodeAndCurrency>>
}