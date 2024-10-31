package com.example.myapplication

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyConverterTest {
    private lateinit var converter: CurrencyConverter

    @Before
    fun setUp(){
        val exchangeRates = mapOf(
            "EUR" to 1.0,
            "USD" to 1.2,
            "VND" to 27000.0,
            "GBP" to 0.834765
        )
        converter = CurrencyConverter(exchangeRates)
    }

    @Test
    fun testConvertEUR_To_USD(){
        val result = converter.convert("EUR", "USD", "10");
        assertEquals("12.00", result)
    }

    @Test
    fun testConvertUSD_To_VND(){
        val result = converter.convert("USD", "VND", "10");
        assertEquals("225000.00", result)
    }

    @Test
    fun testConvert_InvalidCurrency() {
        val result = converter.convert("ABC", "USD", "10")
        assertEquals("10", result)
    }

    @Test
    fun testConvert_EmptyAmount() {
        val result = converter.convert("EUR", "USD", "")
        assertEquals("", result)
    }
    
    @Test
    fun testExchangeRateEUR_To_USD(){
        val result = converter.changeExchangeRate("EUR", "USD")
        assertEquals("1 EUR = 1.20 USD", result)
    }

    @Test
    fun testExchangeRateEUR_To_EUR(){
        val result = converter.changeExchangeRate("EUR", "EUR")
        assertEquals("1 EUR = 1.00 EUR", result)
    }

    @Test
    fun testExchangeRateUSD_To_VND(){
        val result = converter.changeExchangeRate("USD", "VND")
        assertEquals("1 USD = 22500.00 VND", result)
    }
}