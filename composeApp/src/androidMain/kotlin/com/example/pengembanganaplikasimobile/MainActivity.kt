package com.example.pengembanganaplikasimobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pengembanganaplikasimobile.db.DatabaseDriverFactory
import com.example.pengembanganaplikasimobile.db.NotesDatabase
import com.example.pengembanganaplikasimobile.data.NoteRepository
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi Database (butuh Context 'this')
        val driver = DatabaseDriverFactory(this).createDriver()
        val database = NotesDatabase(driver)
        val repository = NoteRepository(database)

        // 2. Inisialisasi DataStore
        val dataStore = PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                File(applicationContext.filesDir, "datastore/settings.preferences_pb").absolutePath.toPath()
            }
        )

        setContent {
            // 3. Masukkan ke App()
            App(repository = repository, dataStore = dataStore)
        }
    }
}