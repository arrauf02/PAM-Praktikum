package com.example.pengembanganaplikasimobile.data


import com.example.pengembanganaplikasimobile.db.NotesDatabase
import com.example.pengembanganaplikasimobile.db.NoteEntity
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import app.cash.sqldelight.coroutines.mapToOneOrNull

class NoteRepository(database: NotesDatabase) {
    private val queries = database.noteQueries

    // Mengambil data berdasarkan preferensi sortOrder dari DataStore
    fun getAllNotes(sortOrder: String): Flow<List<NoteEntity>> {
        return if (sortOrder == "ASC") {
            queries.selectAllAsc().asFlow().mapToList(Dispatchers.IO)
        } else {
            queries.selectAllDesc().asFlow().mapToList(Dispatchers.IO)
        }
    }

    fun searchNotes(query: String): Flow<List<NoteEntity>> =
        queries.searchNotes(query).asFlow().mapToList(Dispatchers.IO)

    suspend fun insertNote(title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        queries.insertNote(null, title, content, now)
    }

    // Fungsi Update untuk fitur Edit
    suspend fun updateNote(id: Long, title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        queries.updateNote(title, content, now, id)
    }
    // Ambil detail catatan berdasarkan ID
    fun getNoteById(id: Long): Flow<NoteEntity?> {
        return queries.selectNoteById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }
    suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }
}

