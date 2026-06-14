package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.GalaxyWrapper

// UBAH Int MENJADI Long DI SINI 👇
@Composable
fun NoteDetailScreen(noteId: Long, onBack: () -> Unit) {
    GalaxyWrapper {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🛰️ Detail Transmisi", color = Color.LightGray)
            Text("ID Catatan: $noteId", style = MaterialTheme.typography.headlineSmall, color = Color.Yellow)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Kembali ke Orbit", color = Color.White)
            }
        }
    }
}