package com.example.pengembanganaplikasimobile.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pengembanganaplikasimobile.data.NoteRepository
import com.example.pengembanganaplikasimobile.db.NoteEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun NoteListScreen(
    repository: NoteRepository,
    dataStore: DataStore<Preferences>,
    onNoteClick: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // 1. Ambil preferensi sorting dari DataStore
    val sortKey = stringPreferencesKey("sort_order")
    val sortOrder by dataStore.data.map { it[sortKey] ?: "DESC" }.collectAsState("DESC")

    // 2. Ambil data dari Repository
    val notes by if (searchQuery.isBlank()) {
        repository.getAllNotes(sortOrder).collectAsState(emptyList())
    } else {
        repository.searchNotes(searchQuery).collectAsState(emptyList())
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<NoteEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFFBB86FC)
            ) {
                Text("+", color = Color.Black)
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Galaxy Notes...", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFBB86FC),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. UI State yang proper (Empty State)
            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Orbit kosong. Belum ada transmisi tersimpan.", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(notes) { note ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onNoteClick(note.id) },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(note.title, color = Color(0xFFBB86FC), style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(note.content, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    // Tombol Edit
                                    TextButton(onClick = { noteToEdit = note }) {
                                        Text("Edit", color = Color(0xFF03DAC6))
                                    }
                                    // Tombol Hapus
                                    TextButton(onClick = {
                                        scope.launch { repository.deleteNote(note.id) }
                                    }) {
                                        Text("Hapus", color = Color(0xFFCF6679))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 4. Integrasi Dialog Add dan Edit
    if (showAddDialog || noteToEdit != null) {
        AddEditNoteDialog(
            initialTitle = noteToEdit?.title ?: "",
            initialContent = noteToEdit?.content ?: "",
            onDismiss = {
                showAddDialog = false
                noteToEdit = null
            },
            onSave = { title, content ->
                scope.launch {
                    if (noteToEdit != null) {
                        repository.updateNote(noteToEdit!!.id, title, content) // Update
                    } else {
                        repository.insertNote(title, content) // Create baru
                    }
                    showAddDialog = false
                    noteToEdit = null
                }
            }
        )
    }
}