package com.example.pengembanganaplikasimobile.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val dbFile = File(System.getProperty("user.home"), "notes.db")
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")

        // Cek apakah tabel sudah ada
        if (!dbFile.exists()) {
            NotesDatabase.Schema.create(driver)
        }
        return driver
    }
}