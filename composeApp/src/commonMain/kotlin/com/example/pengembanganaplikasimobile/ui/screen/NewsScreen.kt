package com.example.pengembanganaplikasimobile.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pengembanganaplikasimobile.data.model.Post
import com.example.pengembanganaplikasimobile.ui.state.UiState
import com.example.pengembanganaplikasimobile.viewmodel.NewsViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val selectedPost by viewModel.selectedPost.collectAsState()

    if (selectedPost != null) {
        NewsDetailScreen(post = selectedPost!!) {
            viewModel.selectPost(null)
        }
    } else {
        NewsListScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsListScreen(viewModel: NewsViewModel) {
    val state by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    // Menggunakan API PullRefresh yang stabil di KMP
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshNews() }
    )

    val galaxyBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF000000), Color(0xFF0B001E), Color(0xFF1B1464))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(galaxyBackground)
            .pullRefresh(pullRefreshState) // 👉 Modifier baru untuk pull-to-refresh
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Portal Berita Hangat",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF00D2FF),
                modifier = Modifier.padding(bottom = 4.dp, top = 24.dp)
            )
            Text(
                text = "Informasi Terhangat Jagat Raya",
                fontSize = 12.sp,
                color = Color(0xFF915AFF),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            when (val s = state) {
                is UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00D2FF))
                    }
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Sinyal Terganggu: ${s.message}", color = Color.Red)
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadNews() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF915AFF))
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
                is UiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 32.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(s.data) { item ->
                            NewsItemCard(post = item) {
                                viewModel.selectPost(item)
                            }
                        }
                    }
                }
            }
        }

        // 👉 Indikator animasi berputar saat ditarik
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF00D2FF),
            backgroundColor = Color(0xFF1B1464)
        )
    }
}

@Composable
fun NewsItemCard(post: Post, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color(0xFF00D2FF).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column {
            post.urlToImage?.let { imageUrl ->
                KamelImage(
                    resource = asyncPainterResource(data = imageUrl),
                    contentDescription = "Gambar Berita",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    onLoading = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    onFailure = { exception ->
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            // Menampilkan pesan error langsung di layar
                            Text(
                                text = exception.message ?: "Gagal memuat",
                                color = Color.Red,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = post.title ?: "Tanpa Judul",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF00D2FF),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = post.description ?: "Sentuh untuk melihat detail berita selengkapnya...",
                    fontSize = 14.sp,
                    color = Color(0xFFE0E0E0),
                    lineHeight = 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun NewsDetailScreen(post: Post, onBack: () -> Unit) {
    val galaxyBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF000000), Color(0xFF0B001E), Color(0xFF1B1464))
    )

    Column(modifier = Modifier.fillMaxSize().background(galaxyBackground).verticalScroll(rememberScrollState())) {
        IconButton(onClick = onBack, modifier = Modifier.padding(top = 24.dp, start = 8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
        }

        post.urlToImage?.let { imageUrl ->
            KamelImage(
                resource = asyncPainterResource(data = imageUrl),
                contentDescription = "Gambar Berita",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(250.dp),
                onLoading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                onFailure = { exception ->
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        // Menampilkan pesan error langsung di layar
                        Text(
                            text = exception.message ?: "Gagal memuat",
                            color = Color.Red,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title ?: "Tanpa Judul",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00D2FF)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = post.content ?: post.description ?: "Tidak ada detail konten yang tersedia untuk berita ini.",
                fontSize = 16.sp,
                color = Color(0xFFE0E0E0),
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            post.url?.let {
                Text(
                    text = "Sumber Artikel Asli: \n$it",
                    fontSize = 12.sp,
                    color = Color(0xFF915AFF)
                )
            }
        }
    }
}