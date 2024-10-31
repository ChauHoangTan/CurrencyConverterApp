package com.example.myapplication.model


data class CountryInfoCodeAndCurrency(
    val currencies: Map<String, Currency>,
    val cca2: String,
    val flags: Flags,
)

data class Currency(
    val name: String,
    val symbol: String
)

data class Flags(
    val png: String,
    val svg: String
)
