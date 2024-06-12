package com.example.kanyequotesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var quoteTextView: TextView
    private lateinit var fetchQuoteButton: Button
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private val allQuotes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quoteTextView = findViewById(R.id.quoteTextView)
        fetchQuoteButton = findViewById(R.id.fetchQuoteButton)
        searchButton = findViewById(R.id.searchButton)
        progressBar = findViewById(R.id.progressBar)

        fetchQuoteButton.setOnClickListener {
            fetchQuote()
        }

        searchButton.setOnClickListener {
            startSearchActivity()
        }
    }

    private fun fetchQuote() {
        // Show progressBar and hide TextView
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
            quoteTextView.visibility = View.GONE
        }

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.kanye.rest/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    quoteTextView.text = "Failed to load quote"
                    quoteTextView.visibility = View.VISIBLE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val jsonObject = JSONObject(it)
                    val quote = jsonObject.getString("quote")

                    runOnUiThread {
                        allQuotes.add(quote)
                        val fadeIn = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_in)
                        quoteTextView.text = quote
                        quoteTextView.startAnimation(fadeIn)
                        progressBar.visibility = View.GONE
                        quoteTextView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun startSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
