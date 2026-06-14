package com.example.pengembanganaplikasimobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(dataStore: DataStore<Preferences>) {
    val scope = rememberCoroutineScope()

    // Key untuk DataStore
    val sortKey = stringPreferencesKey("sort_order")
    val themeKey = stringPreferencesKey("app_theme")

    // Ambil data (observe) dari DataStore
    val sortOrder by dataStore.data.map { it[sortKey] ?: "DESC" }.collectAsState("DESC")
    val appTheme by dataStore.data.map { it[themeKey] ?: "Galaxy" }.collectAsState("Galaxy")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Teks menggunakan warna dinamis mengikuti tema
        Text("Arrauf Setiawan Muhammad Jabar", color = MaterialTheme.colorScheme.onBackground)
        Text("NIM: 123140032", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        Text("Pengaturan DataStore", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        // Tombol 1: Toggle Urutan
        Button(
            onClick = {
                scope.launch {
                    dataStore.edit { it[sortKey] = if (sortOrder == "DESC") "ASC" else "DESC" }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Toggle Urutan: $sortOrder", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol 2: Toggle Tema (Baru ditambahkan)
        Button(
            onClick = {
                scope.launch {
                    dataStore.edit { it[themeKey] = if (appTheme == "Galaxy") "Light" else "Galaxy" }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Ubah Tema: $appTheme", color = Color.Black)
        }
    }
}