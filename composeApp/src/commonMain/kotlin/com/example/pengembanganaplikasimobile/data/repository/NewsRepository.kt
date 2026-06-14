package com.example.pengembanganaplikasimobile.data.repository

import com.example.pengembanganaplikasimobile.data.model.NewsResponse
import com.example.pengembanganaplikasimobile.data.model.Post
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsRepository(private val client: HttpClient) {

    private val apiKey = "b8c1120342114614bb6c273db11a3039"

    private val url = "https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=$apiKey"

    suspend fun fetchPosts(): List<Post> {
        return try {
            val response: NewsResponse = client.get(url).body()
            response.articles?.shuffled() ?: emptyList()
        } catch (e: Exception) {

            throw Exception("Gagal tarik data: ${e.message}")
        }
    }
}