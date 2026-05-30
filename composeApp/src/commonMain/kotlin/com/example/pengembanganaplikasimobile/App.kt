package com.example.pengembanganaplikasimobile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


private val BgDeep       = Color(0xFF050F08)
private val BgCard       = Color(0xFF0A1A0E)
private val AccentCyan   = Color(0xFF00E676)
private val AccentViolet = Color(0xFF69F0AE)
private val AccentAmber  = Color(0xFFB9F6CA)
private val TextPrimary  = Color(0xFFE8F5E9)
private val TextMuted    = Color(0xFF4A6655)
private val Live         = Color(0xFF00C853)


data class News(val id: Int, val title: String, val category: String, val tag: String = "")

class NewsManager {
    private val rawNews = listOf(
        News(1, "Ramadhan Tiba, Jutaan Warga Sambut Bulan Suci",  "Religi",     "🕌"),
        News(2, "Polri Resmikan 1176 Dapur MBG di Seluruh Nusantara", "Nasional","🏛️"),
        News(3, "BTR Raih Juara Dunia di Grand Finals MLBB",       "Game",      "🎮"),
        News(4, "Pria Sawit Join BoP: Fenomena Baru di Dunia Maya","Trending",  "🔥"),
        News(5, "Teknologi AI Terus Berkembang Pesat di Indonesia", "Tech",     "🤖"),
        News(6, "IHSG Melesat 2.3% di Tengah Sentimen Positif",    "Ekonomi",  "📈")
    )

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    val categories = listOf("Semua", "Religi", "Nasional", "Game", "Trending", "Tech", "Ekonomi")

    fun getNewsStream(filterCategory: String = "Semua"): Flow<News> = flow {
        var i = 0
        while (true) {
            emit(rawNews[i % rawNews.size])
            i++
            delay(2000L)
        }
    }
        .filter { news -> filterCategory == "Semua" || news.category == filterCategory }
        .map { news -> news.copy(title = "[${news.category}] ${news.title}") }

    fun markAsRead() { _readCount.value++ }

    suspend fun fetchNewsDetail(title: String): String = withContext(Dispatchers.Default) {
        delay(1200)
        "Laporan mendalam: $title. " +
                "Konten ini berhasil dimuat secara asynchronous menggunakan Kotlin Coroutines. " +
                "Data diproses di Dispatchers.Default."
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun App() {
    val newsManager = remember { NewsManager() }
    val scope       = rememberCoroutineScope()

    var currentNews by remember { mutableStateOf(News(0, "Menghubungkan ke server…", "Live", "📡")) }
    val totalRead   by newsManager.readCount.collectAsState()
    var selectedCategory by remember { mutableStateOf("Semua") }

    LaunchedEffect(selectedCategory) {
        currentNews = News(0, "Memfilter kategori: $selectedCategory…", "Live", "📡")
        newsManager.getNewsStream(selectedCategory).collect { news ->
            currentNews = news
        }
    }
    var detailText   by remember { mutableStateOf("") }
    var isFetching   by remember { mutableStateOf(false) }

    // Pulsing animation for LIVE dot
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.4f, targetValue = 1f, label = "alpha",
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // Decorative background blobs
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset((-80).dp, (-60).dp)
                .background(
                    Brush.radialGradient(listOf(AccentViolet.copy(alpha = 0.18f), Color.Transparent)),
                    CircleShape
                )
                .blur(60.dp)
        )
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .offset(60.dp, 80.dp)
                .background(
                    Brush.radialGradient(listOf(AccentCyan.copy(alpha = 0.12f), Color.Transparent)),
                    CircleShape
                )
                .blur(50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 52.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ── TOP BAR ──────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "NEWSFLOW",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp,
                        color = TextPrimary
                    )
                    Text(
                        "Arrauf Setiawan · 123140032",
                        fontSize = 10.sp,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                }

                // Notification badge
                Box {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BgCard)
                            .border(1.dp, AccentViolet.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔔", fontSize = 18.sp)
                    }
                    if (totalRead > 0) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(Live),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("$totalRead", fontSize = 9.sp, fontWeight = FontWeight.Bold,
                                color = Color.White)
                        }
                    }
                }
            }

            // ── FILTER KATEGORI ──────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "FILTER KATEGORI",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = TextMuted
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    newsManager.categories.take(4).forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) AccentCyan.copy(0.25f) else BgCard
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) AccentCyan else TextMuted.copy(0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                cat,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AccentCyan else TextMuted
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    newsManager.categories.drop(4).forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) AccentCyan.copy(0.25f) else BgCard
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) AccentCyan else TextMuted.copy(0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                cat,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AccentCyan else TextMuted
                            )
                        }
                    }
                }
            }

            // ── LIVE TICKER CARD ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                AccentViolet.copy(alpha = 0.25f),
                                AccentCyan.copy(alpha = 0.10f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(AccentViolet.copy(0.6f), AccentCyan.copy(0.3f))),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pulsing live dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Live.copy(alpha = pulse))
                        )
                        Text(
                            "SIARAN LANGSUNG",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = Live
                        )
                        Spacer(Modifier.weight(1f))
                        CategoryChip(currentNews.category)
                    }

                    AnimatedContent(
                        targetState = currentNews,
                        transitionSpec = {
                            (slideInVertically { it } + fadeIn()) togetherWith
                                    (slideOutVertically { -it } + fadeOut())
                        },
                        label = "news"
                    ) { news ->
                        Text(
                            "${news.tag}  ${news.title}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            lineHeight = 24.sp
                        )
                    }

                    Text(
                        "Diperbarui otomatis setiap 2 detik via Flow",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            // ── STATS ROW ────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Dibaca",
                    value = "$totalRead",
                    accent = AccentCyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Platform",
                    value = getPlatform().name,
                    accent = AccentAmber,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Status",
                    value = if (isFetching) "Loading…" else "Siap",
                    accent = if (isFetching) AccentViolet else Live,
                    modifier = Modifier.weight(1f)
                )
            }

            // ── ACTION BUTTON ────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(listOf(AccentViolet, AccentCyan.copy(0.9f)))
                    )
                    .clickable(enabled = !isFetching) {
                        newsManager.markAsRead()
                        isFetching = true
                        detailText = ""
                        scope.launch {
                            detailText = newsManager.fetchNewsDetail(currentNews.title)
                            isFetching = false
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isFetching) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Text("Mengambil detail…", color = Color.White,
                            fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("▶", fontSize = 16.sp, color = Color.White)
                        Text("Baca & Ambil Detail", color = Color.White,
                            fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            // ── DETAIL RESULT CARD ───────────────────────────
            AnimatedVisibility(
                visible = detailText.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BgCard)
                        .border(1.dp, AccentCyan.copy(0.3f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(16.dp)
                                    .background(AccentCyan, RoundedCornerShape(2.dp))
                            )
                            Text(
                                "LAPORAN DETAIL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = AccentCyan
                            )
                        }
                        Text(
                            detailText,
                            fontSize = 13.sp,
                            color = TextPrimary.copy(alpha = 0.9f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun CategoryChip(label: String) {
    val color = when (label) {
        "Tech"     -> AccentCyan
        "Game"     -> AccentViolet
        "Ekonomi"  -> AccentAmber
        "Nasional" -> Color(0xFF1B5E20)
        "Trending" -> Live
        else       -> TextMuted
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(label.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp, color = color)
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(BgCard)
            .border(1.dp, accent.copy(0.25f), RoundedCornerShape(14.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = accent,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(label, fontSize = 9.sp, color = TextMuted, letterSpacing = 1.sp,
            fontWeight = FontWeight.Medium)
    }
}