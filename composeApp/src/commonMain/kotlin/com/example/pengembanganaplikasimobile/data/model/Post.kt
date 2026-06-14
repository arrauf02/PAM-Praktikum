package com.example.pengembanganaplikasimobile.data.model
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val articles: List<Post>
)

@Serializable
data class Post(
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null, // Ditambahkan untuk gambar
    val content: String? = null     // Ditambahkan untuk detail berita
)