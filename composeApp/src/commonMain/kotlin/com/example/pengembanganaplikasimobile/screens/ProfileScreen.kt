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
    val sortKey = stringPreferencesKey("sort_order")
    val sortOrder by dataStore.data.map { it[sortKey] ?: "DESC" }.collectAsState("DESC")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Arrauf Setiawan Muhammad Jabar", color = Color.White) // Identitas Arrauf
        Text("NIM: 123140032", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        Text("Pengaturan (DataStore)", color = Color(0xFFBB86FC))
        Button(onClick = {
            scope.launch {
                dataStore.edit { it[sortKey] = if (sortOrder == "DESC") "ASC" else "DESC" }
            }
        }) {
            Text("Toggle Urutan: $sortOrder")
        }
    }
}