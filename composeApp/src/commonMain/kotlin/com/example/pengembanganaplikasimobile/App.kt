package com.example.pengembanganaplikasimobile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pengembanganaplikasimobile.data.NoteRepository
import kotlinx.coroutines.flow.map
import navigation.AppNavigation

@Composable
fun App(repository: NoteRepository, dataStore: DataStore<Preferences>) {

    // 1. Ambil preferensi tema dari DataStore
    val themeKey = stringPreferencesKey("app_theme")
    val currentTheme by dataStore.data.map { it[themeKey] ?: "Galaxy" }.collectAsState("Galaxy")

    // 2. Definisi Skema Warna Galaxy (Gelap)
    val galaxyColorScheme = darkColorScheme(
        primary = Color(0xFFBB86FC),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF0B0B1E),
        surface = Color(0xFF1A1A2E),
        onPrimary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    // 3. Definisi Skema Warna Light (Terang)
    val lightColorScheme = lightColorScheme(
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFF5F5F5),
        onPrimary = Color.White,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    // 4. Terapkan logika if-else berdasarkan pilihan di DataStore
    val colorScheme = if (currentTheme == "Light") lightColorScheme else galaxyColorScheme

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        AppNavigation(repository = repository, dataStore = dataStore)
    }
}