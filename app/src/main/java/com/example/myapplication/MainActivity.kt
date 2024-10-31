package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.Adapter.SpinnerItemAdapter
import com.example.myapplication.model.CountryInfoCodeAndCurrency
import com.example.myapplication.model.ExchangeRatesResponse
import com.example.myapplication.model.Flags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class MainActivity : AppCompatActivity() {
    // store map of rates base on EU like { "EUR": "1", "VND": "27000" }
    var exchangeRatesByEU = mutableMapOf<String, Double>();

    // store map of currency and its flag { "USD": "https://flagcdn.com/w320/us.png", "VND": "https://flagcdn.com/w320/vn.png" }
    var flagMap = mutableMapOf<String, String>();

    lateinit var currencyConverter: CurrencyConverter;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerFrom: Spinner = findViewById<Spinner>(R.id.spinnerFrom);
        val spinnerTo: Spinner = findViewById<Spinner>(R.id.spinnerTo);
        val inputAmountFrom: EditText = findViewById<EditText>(R.id.amountFrom)
        val inputAmountTo: EditText = findViewById<EditText>(R.id.amountTo)
        val textExchangeRate: TextView = findViewById<TextView>(R.id.exchangeRate)
        val imageChangeCurrenciesButton: ImageButton = findViewById<ImageButton>(R.id.changeCurrenciesButton)

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("DefaultLocale")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // when change currency of a spinner, text view of two currencies will be changed
                textExchangeRate.text = currencyConverter.changeExchangeRate(spinnerFrom.adapter.getItem(position).toString(),
                    spinnerTo.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // action when do not have any item selected
            }

        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // when change currency of a spinner, text view of two currencies will be changed
                textExchangeRate.text = currencyConverter.changeExchangeRate(spinnerFrom.selectedItem.toString(),
                    spinnerTo.adapter.getItem(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // action when do not have any item selected
            }

        }

        inputAmountFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do action before changing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do action when text changes
                // When input amount change, calculate the amount converted and show it
                inputAmountTo.setText(currencyConverter.convert(spinnerFrom.selectedItem.toString(), spinnerTo.selectedItem.toString(), inputAmountFrom.text.toString()))
            }

            override fun afterTextChanged(s: Editable?) {
                // Do action after changed

            }
        })

        // set onClick of change button
        // swap currency from and to
        imageChangeCurrenciesButton.setOnClickListener{
            val tempCurrencyPosition = spinnerFrom.selectedItemId
            spinnerFrom.setSelection(spinnerTo.selectedItemId.toInt())
            spinnerTo.setSelection(tempCurrencyPosition.toInt())
        }

        // get rates base on EU by call ExchangeRatesAPI
        fetchExchangeRates(spinnerFrom, spinnerTo)

    }

    private fun fetchExchangeRates(spinnerFrom: Spinner, spinnerTo: Spinner){
        val call = CallExchangeRatesApiClient.apiService.getExchangeRates(getString(R.string.API_Key))

        call.enqueue(object : Callback<ExchangeRatesResponse> {
            override fun onResponse(
                call: Call<ExchangeRatesResponse>,
                response: Response<ExchangeRatesResponse>
            ) {
                if (response.isSuccessful) {
                    val exchangeRates = response.body()

                    exchangeRatesByEU = exchangeRates?.rates as MutableMap<String, Double>;

                    // Asynchronous control
                    CoroutineScope(Dispatchers.Main).launch {
                        // get the map of currency and its flag and store into flagMap
                        val fetchCurrencyJob = async { fetchCurrencyAndFlag() }

                        fetchCurrencyJob.await();

                        for (currency in exchangeRatesByEU.keys) {
                            // check if a currency in exchangeRatesByEU not exist in flagMap
                            if (!flagMap.containsKey(currency)) {
                                // add currency in to flagMap with temp flag
                                flagMap[currency] = "https://media.istockphoto.com/id/1409329028/vector/no-picture-available-placeholder-thumbnail-icon-illustration-design.jpg?s=612x612&w=0&k=20&c=_zOuJu755g2eEUioiOUdz_mHKJQJn-tDgIAhQzyeKUQ="
                            }
                        }

                        // construct currencyConverter
                        currencyConverter = CurrencyConverter(exchangeRatesByEU)

                        updateSpinner(spinnerFrom, spinnerTo) // update adapter spinner from and spinner to
                    }

                } else {
                    Log.e("ExchangeRates", "Error: ${response.errorBody()?.string()}")
                    showError("Call ExchangeRatesAPI error!!")
                }
            }

            override fun onFailure(call: Call<ExchangeRatesResponse>, t: Throwable) {
                Log.e("ExchangeRates", "API call failed: ${t.message}")
                showError("Network Error!!")
            }
        })
    }

    // get cca2 of a country and its currency
    private suspend fun fetchCurrencyAndFlag(){

        val call = CallCurrencyFlagsApiClient.apiService.getCurrencyAndFlag("cca2,currencies,flags")
        val response = call.awaitResponse()
        if(response.isSuccessful && response.body() != null){
            val countryInfoList = response.body()!!
            if(countryInfoList.isNotEmpty()){
                for(country in countryInfoList){
                    val countryCode = country.cca2
                    val currency = country.currencies.keys.firstOrNull()
                    val flag = country.flags.png
                    if(currency != null && countryCode == currency.substring(0,2)){
                        flagMap[currency] = flag
                    }
                }

            }else {
                // Xử lý khi không có thông tin quốc gia nào cho loại tiền tệ này
                Log.e("API", "No country info founded!!")
                showError("Error get country info!")
            }


        }else {
            Log.e("Get flag error", "Error: ${response.errorBody()?.string()}")
            showError("Get list countries currencies error!")
        }


    }


    // update adapter of spinner from and spinner to after getting the flag map
    private fun updateSpinner(spinnerFrom: Spinner, spinnerTo: Spinner){
        val adapter = SpinnerItemAdapter(this, flagMap)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // set default value of currency from is USD
        spinnerFrom.setSelection(adapter.getPositionByKey("USD"))

        // set default value of currency to is VND
        spinnerTo.setSelection(adapter.getPositionByKey("VND"))
    }

    // show error by toast when can not call api or get an internet trouble
    private fun showError(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

}