package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.example.myapplication.ErrorHandler.ErrorMessages

class CurrencyConverter(private val exchangeRatesByEU: Map<String, Double>) {
    // convert an amount from a currency to another
    fun convert(currencyFrom: String, currencyTo: String, amount: String): String {
        if (amount.isEmpty()) return ""

        val amountFrom = amount.toDouble()
        val rateFrom = this.exchangeRatesByEU[currencyFrom]
        val rateTo = this.exchangeRatesByEU[currencyTo]

        if (rateFrom != null && rateTo != null) {
            val result = amountFrom * (rateTo / rateFrom)
            return String.format("%.2f", result)
        } else {
            return amount
        }
    }


    // Show rate of a currency and another like 1 USD = 25259.30 VND
    @SuppressLint("DefaultLocale")
    fun changeExchangeRate(currencyFrom: String, currencyTo: String): String{
        if(this.exchangeRatesByEU[currencyTo] != null && this.exchangeRatesByEU[currencyFrom] != null){
            val result = "1 ${currencyFrom} = ${String.format("%.2f", this.exchangeRatesByEU[currencyTo]?.div(
                this.exchangeRatesByEU[currencyFrom]!!
            ))} ${currencyTo}"
            return result
        }else{
            return ""
        }


    }
}