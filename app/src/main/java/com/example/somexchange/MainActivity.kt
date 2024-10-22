package com.example.somexchange

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Create the rates map
        val ratesMap: Map<String, Float> = mapOf(
            "USD" to 85.5f,
            "EUR" to 92.65f,
            "JPY" to 0.57f,
            "GBP" to 111.24f,
            "AUD" to 57.13f
        )

        // Extract keys for the Spinner and add an empty string at the beginning
        val currencyKeys = mutableListOf("-")  // Start with an empty string
        currencyKeys.addAll(ratesMap.keys)     // Add the currency keys

        // Initialize the Spinner
        val currencySpinner: Spinner = findViewById(R.id.currency_spinner)

        // Create an ArrayAdapter using the currency keys
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyKeys)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        currencySpinner.adapter = adapter

        val amountView = findViewById<EditText>(R.id.amount_edit)
        val rateView = findViewById<EditText>(R.id.rate_edit)
        val somView = findViewById<TextView>(R.id.som_result)

        // Set an OnItemSelectedListener on the Spinner
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View,
                position: Int,
                id: Long
            ) {
                // Get the selected currency
                val selectedCurrency = currencySpinner.selectedItem as String

                // Update the EditText with the corresponding rate if a valid currency is selected
                if (selectedCurrency.isNotEmpty()) {
                    val rate = ratesMap[selectedCurrency]
                    rateView.setText(rate?.toString() ?: "")
                } else {
                    rateView.setText("") // Clear the EditText if the selection is empty
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
                rateView.setText("") // Clear the EditText if nothing is selected
            }
        }

        val button = findViewById<Button>(R.id.submit_button)

        button.setOnClickListener {
            val amount = amountView.text.toString().toFloatOrNull()
            val rate = rateView.text.toString().toFloatOrNull()

            if (amount != null && rate != null) {
                val result = (amount * rate).toString()
                somView.text = result
            } else {
                somView.text = "Amount or rate is null"
            }

            // Hide the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}