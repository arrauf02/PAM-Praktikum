package com.example.pengembanganaplikasimobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pengembanganaplikasimobile.data.repository.NewsRepository
import com.example.pengembanganaplikasimobile.ui.state.UiState
import com.example.pengembanganaplikasimobile.data.model.Post
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // State KHUSUS untuk mengatur muncul/hilangnya icon Pull-to-Refresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    // State untuk Detail Screen
    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost = _selectedPost.asStateFlow()

    init { loadNews() }

    // Fungsi ini dipanggil SAAT APLIKASI PERTAMA KALI DIBUKA (Muncul loading di tengah)
    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val posts = repository.fetchPosts()
                _uiState.value = UiState.Success(posts)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Gagal memuat berita")
            }
        }
    }

    // Fungsi ini dipanggil SAAT LAYAR DITARIK KE BAWAH
    fun refreshNews() {
        viewModelScope.launch {
            _isRefreshing.value = true // 1. Munculkan icon pull-to-refresh di atas

            try {
                // Tarik data baru TANPA menghapus list berita yang sudah ada di layar
                val posts = repository.fetchPosts()
                _uiState.value = UiState.Success(posts)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Gagal memuat berita")
            } finally {
                // 2. PENTING: Sembunyikan icon pull-to-refresh setelah berhasil/gagal
                _isRefreshing.value = false
            }
        }
    }

    fun selectPost(post: Post?) {
        _selectedPost.value = post
    }
}