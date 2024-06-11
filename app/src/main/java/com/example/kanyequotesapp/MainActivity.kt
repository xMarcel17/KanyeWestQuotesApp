package com.example.kanyequotesapp // Upewnij się, że ta linia jest poprawna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var quoteTextView: TextView
    private lateinit var fetchQuoteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quoteTextView = findViewById(R.id.quoteTextView)
        fetchQuoteButton = findViewById(R.id.fetchQuoteButton)

        fetchQuoteButton.setOnClickListener {
            fetchQuote()
        }
    }

    private fun fetchQuote() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.kanye.rest/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    quoteTextView.text = "Failed to load quote"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val jsonObject = JSONObject(it)
                    val quote = jsonObject.getString("quote")

                    runOnUiThread {
                        val fadeIn = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_in)
                        quoteTextView.text = quote
                        quoteTextView.startAnimation(fadeIn)
                    }
                }
            }
        })
    }
}
