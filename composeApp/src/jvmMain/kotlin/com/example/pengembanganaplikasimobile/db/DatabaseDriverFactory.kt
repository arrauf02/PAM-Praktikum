package com.example.pengembanganaplikasimobile.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File // Jangan lupa tambahkan import ini

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // Cek apakah file database sudah ada di direktori project
        val dbFile = File("notes.db")
        val isNewDatabase = !dbFile.exists()

        // Buat driver dengan path absolut
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")

        // Hanya eksekusi create table JIKA database ini baru dibuat
        if (isNewDatabase) {
            NotesDatabase.Schema.create(driver)
        }

        return driver
    }
}