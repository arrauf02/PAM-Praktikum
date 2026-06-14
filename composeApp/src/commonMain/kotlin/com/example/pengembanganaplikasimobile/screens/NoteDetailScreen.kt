package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.GalaxyWrapper
import com.example.pengembanganaplikasimobile.data.NoteRepository

@Composable
fun NoteDetailScreen(noteId: Long, repository: NoteRepository, onBack: () -> Unit) {
    // Mengambil data catatan dari database berdasarkan ID
    val note by repository.getNoteById(noteId).collectAsState(initial = null)

    GalaxyWrapper {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Kembali
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D2C5E))
            ) {
                Text("← Kembali", color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (note != null) {
                // Menampilkan Judul
                Text(
                    text = note!!.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFFBB86FC)
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(20.dp))

                // Menampilkan Isi Catatan
                Text(
                    text = note!!.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            } else {
                // Tampilan jika data masih di-load
                CircularProgressIndicator(
                    color = Color(0xFF03DAC6),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}