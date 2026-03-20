package com.example.pengembanganaplikasimobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pengembanganaplikasimobile.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ============================================================
// ProfileViewModel — extends ViewModel(), mengelola state
// dengan MutableStateFlow & expose sebagai StateFlow
// ProfileUiState diimpor dari data layer
// ============================================================
class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Toggle dark/light mode — state disimpan di ViewModel
    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    // Pre-fill TextField dengan data profil saat ini sebelum edit
    fun prepareEdit() {
        _uiState.update { current ->
            current.copy(
                editName = current.profile.name,
                editBio  = current.profile.bio
            )
        }
    }

    // State hoisting — dipanggil setiap ketikan TextField nama
    fun onEditNameChange(value: String) {
        _uiState.update { it.copy(editName = value) }
    }

    // State hoisting — dipanggil setiap ketikan TextField bio
    fun onEditBioChange(value: String) {
        _uiState.update { it.copy(editBio = value) }
    }

    // Simpan hasil edit ke profil, update ViewModel state
    fun saveProfile() {
        _uiState.update { current ->
            current.copy(
                profile = current.profile.copy(
                    name = current.editName.ifBlank { current.profile.name },
                    bio  = current.editBio.ifBlank { current.profile.bio }
                )
            )
        }
    }
}