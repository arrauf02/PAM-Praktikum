package com.example.pengembanganaplikasimobile

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.pengembanganaplikasimobile.data.NoteRepository
import com.example.pengembanganaplikasimobile.db.DatabaseDriverFactory
import com.example.pengembanganaplikasimobile.db.NotesDatabase
import okio.Path.Companion.toPath

fun main() = application {
    // 1. Inisialisasi Database SQLDelight untuk Desktop
    val driver = DatabaseDriverFactory().createDriver()
    val database = NotesDatabase(driver)
    val repository = NoteRepository(database)

    // 2. Inisialisasi DataStore
    val dataStore = PreferenceDataStoreFactory.createWithPath(
        produceFile = { "settings.preferences_pb".toPath() }
    )

    Window(onCloseRequest = ::exitApplication, title = "Galaxy Notes App") {
        // 3. Masukkan parameter ke dalam fungsi App() agar error di baris 12 hilang
        App(repository = repository, dataStore = dataStore)
    }
}