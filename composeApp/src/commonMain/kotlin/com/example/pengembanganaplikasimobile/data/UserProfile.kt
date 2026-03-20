package com.example.pengembanganaplikasimobile.data

import androidx.compose.ui.graphics.Color


data class UserProfile(
    val name: String,
    val title: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String
)

data class JadwalItem(
    val hari: String,
    val mataKuliah: String,
    val jam: String,
    val ruang: String,
    val warna: Color
)


data class ProfileUiState(
    val profile: UserProfile = UserProfile(
        name     = "Arrauf Setiawan Muhammad Jabar",
        title    = "Mahasiswa Teknik Informatika",
        bio      = "Saya adalah mahasiswa semester 6 di Institut Teknologi Sumatera " +
                "pada program studi teknik informatika, Saya Memiliki minat di bidang " +
                "teknologi dan selalu ingin belajar hal baru setiap harinya.",
        email    = "arrauf.123140032@student.itera.ac.id",
        phone    = "+62 87776127425",
        location = "Lampung Selatan, Indonesia"
    ),
    val isDarkMode: Boolean = false,
    val editName: String    = "",
    val editBio: String     = ""
)