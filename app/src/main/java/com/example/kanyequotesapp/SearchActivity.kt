package com.example.kanyequotesapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class SearchActivity : AppCompatActivity() {

    private lateinit var searchField: EditText
    private lateinit var searchButton: Button
    private lateinit var searchResultsRecyclerView: RecyclerView
    private val allQuotes = mutableListOf<String>()
    private val displayedQuotes = mutableListOf<String>()
    private lateinit var quoteAdapter: QuoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchField = findViewById(R.id.searchField)
        searchButton = findViewById(R.id.searchButton)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)

        // Load quotes from JSON
        loadQuotesFromJson()

        setupRecyclerView()

        searchButton.setOnClickListener {
            val query = searchField.text.toString().trim()
            if (query.isNotBlank()) {
                filterQuotes(query)
            }
        }
    }

    private fun setupRecyclerView() {
        quoteAdapter = QuoteAdapter(displayedQuotes)
        searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = quoteAdapter
        }
    }

    private fun loadQuotesFromJson() {
        val json: String
        try {
            val inputStream: InputStream = assets.open("kanye_quotes.json")
            json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val quote = jsonObject.getString("quote")
                allQuotes.add(quote)
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    private fun filterQuotes(query: String) {
        val lowerCaseQuery = query.lowercase()
        displayedQuotes.clear()
        displayedQuotes.addAll(allQuotes.filter { it.lowercase().contains(lowerCaseQuery) })
        quoteAdapter.notifyDataSetChanged()
    }
}
