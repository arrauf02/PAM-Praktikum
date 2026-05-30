package com.example.pengembanganaplikasimobile.data

import com.example.pengembanganaplikasimobile.db.NotesDatabase
import com.example.pengembanganaplikasimobile.db.NoteEntity
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock // Gunakan ini untuk mengisi createdAt

class NoteRepository(database: NotesDatabase) {
    private val queries = database.noteQueries

    fun getAllNotes(): Flow<List<NoteEntity>> =
        queries.selectAll().asFlow().mapToList(Dispatchers.IO)

    fun searchNotes(query: String): Flow<List<NoteEntity>> =
        queries.searchNotes(query).asFlow().mapToList(Dispatchers.IO)

    suspend fun insertNote(title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        // Pastikan SEMUA parameter dilewatkan: (id, title, content, createdAt)
        // Kirim null pada parameter pertama agar ID auto-increment bekerja
        queries.insertNote(null, title, content, now)
    }

    suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }
}