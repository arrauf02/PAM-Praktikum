package com.example.pengembanganaplikasimobile.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pengembanganaplikasimobile.data.NoteRepository
import com.example.pengembanganaplikasimobile.db.NoteEntity

@Composable
fun NoteListScreen(repository: NoteRepository, onNoteClick: (Long) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val notes by repository.searchNotes(searchQuery).collectAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Fitur Search sesuai Tugas 7
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Galaxy Notes...", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFBB86FC), // Ungu Galaxy
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(notes) { note ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onNoteClick(note.id) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)) // Biru Gelap
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(note.title, color = Color(0xFFBB86FC), style = MaterialTheme.typography.titleMedium)
                        Text(note.content, color = Color.White)
                    }
                }
            }
        }
    }
}