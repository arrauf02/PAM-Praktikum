import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.painterResource
import pengembanganaplikasimobile.composeapp.generated.resources.Res
import pengembanganaplikasimobile.composeapp.generated.resources.profile_photo


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


@Composable
fun ProfileHeader(
    name: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF283593))
                )
            )
            .padding(vertical = 36.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
            ) {
                Image(
                    painter = painterResource(Res.drawable.profile_photo),
                    contentDescription = "Foto Profil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = title,
                color = Color(0xFFB0BEC5),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun InfoItem(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8EAF6)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = Color(0xFF9E9E9E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                color = Color(0xFF212121),
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}


@Composable
fun ProfileCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = title,
                color = Color(0xFF1A237E),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            content()
        }
    }
}


@Composable
fun BioSection(
    bio: String,
    modifier: Modifier = Modifier
) {
    ProfileCard(title = "TENTANG SAYA", modifier = modifier) {
        Text(
            text = bio,
            color = Color(0xFF616161),
            fontSize = 14.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}


@Composable
fun JadwalRow(item: JadwalItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Strip warna kiri sebagai penanda hari
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(item.warna)
        )

        // Info jadwal
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.mataKuliah,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF212121)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "🕐 ${item.jam}",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = "📍 ${item.ruang}",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }
        }

        // Badge hari
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(item.warna.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = item.hari,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = item.warna
            )
        }
    }
}


@Composable
fun JadwalDialog(onDismiss: () -> Unit) {
    val jadwalList = listOf(
        JadwalItem("Sen", "Pengembangan Aplikasi Mobile", "09.30 - 12.00", "Labtek 3", Color(0xFF3949AB)),
        JadwalItem("Sen", "Big Data ",                    "13.00 - 15.30", "GK2 126", Color(0xFF3949AB)),
        JadwalItem("Sel", "Kriptografi",                  "15.00 - 17.30", "GK2 303", Color(0xFF00897B)),
        JadwalItem("Rab", "Visualisasi Data",             "07.30 - 10.00", "GK2 321", Color(0xFF00897B)),
        JadwalItem("Rab", "Manajemen Basis Data",         "13.00 - 15.30", "GK2 327", Color(0xFFE53935)),
        JadwalItem("Jum", "Keamanan Siber",               "07.30 - 10.00", "Labtek 1 Labkom 2", Color(0xFF8E24AA)),
    )

    // Kelompokkan per hari
    val hariOrder = listOf("Sen", "Sel", "Rab", "Kam", "Jum")
    val grouped = jadwalList.groupBy { it.hari }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "📅 Jadwal Kuliah",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )

                HorizontalDivider(
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Render per hari secara urut
                hariOrder.forEach { hari ->
                    val items = grouped[hari] ?: return@forEach

                    // Label hari
                    val namaHari = mapOf(
                        "Sen" to "Senin",
                        "Sel" to "Selasa",
                        "Rab" to "Rabu",
                        "Kam" to "Kamis",
                        "Jum" to "Jumat"
                    )
                    Text(
                        text = namaHari[hari] ?: hari,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E9E9E),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 2.dp)
                    )

                    items.forEach { item ->
                        JadwalRow(item = item)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A237E)
                    )
                ) {
                    Text("Tutup")
                }
            }
        }
    }
}


@Composable
fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var bio  by remember { mutableStateOf(profile.bio) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Edit Profil",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = { onSave(profile.copy(name = name, bio = bio)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3949AB)
                        )
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}


@Composable
fun ActionButtons(
    onEditClick: () -> Unit,
    onJadwalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onEditClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3949AB)
            )
        ) {
            Text("✏", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profil", fontWeight = FontWeight.Medium)
        }

        Button(
            onClick = onJadwalClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00897B)
            )
        ) {
            Text("📅", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Jadwal", fontWeight = FontWeight.Medium)
        }
    }
}


@Composable
fun MyProfileScreen() {
    var profile by remember {
        mutableStateOf(
            UserProfile(
                name = "Arrauf Setiawan Muhammad Jabar",
                title = "Mahasiswa Teknik Informatika",
                bio = "Saya adalah mahasiswa semester 6 di Institut Teknologi Sumatera " +
                        "pada program studi teknik informatika, Saya Memiliki minat di bidang teknologi dan selalu ingin belajar hal baru setiap harinya.",
                email = "arrauf.123140032@student.itera.ac.id",
                phone = "+62 87776127425",
                location = "Lampung Selatan, Indonesia"
            )
        )
    }

    var showEditDialog   by remember { mutableStateOf(false) }
    var showJadwalDialog by remember { mutableStateOf(false) }
    var isInfoVisible    by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    if (showEditDialog) {
        EditProfileDialog(
            profile = profile,
            onDismiss = { showEditDialog = false },
            onSave = { updated ->
                profile = updated
                showEditDialog = false
            }
        )
    }

    if (showJadwalDialog) {
        JadwalDialog(onDismiss = { showJadwalDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        ProfileHeader(name = profile.name, title = profile.title)

        Spacer(modifier = Modifier.height(16.dp))

        ActionButtons(
            onEditClick   = { showEditDialog = true },
            onJadwalClick = { showJadwalDialog = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        BioSection(bio = profile.bio)

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            onClick = { isInfoVisible = !isInfoVisible },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = if (isInfoVisible) "▲  Sembunyikan Info" else "▼  Tampilkan Info",
                color = Color(0xFF3949AB),
                fontWeight = FontWeight.Medium
            )
        }

        AnimatedVisibility(
            visible = isInfoVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -20 }),
            exit = fadeOut()
        ) {
            ProfileCard(title = "INFORMASI KONTAK") {
                InfoItem(emoji = "✉️", label = "EMAIL",   value = profile.email)
                HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp))
                InfoItem(emoji = "📞", label = "TELEPON", value = profile.phone)
                HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp))
                InfoItem(emoji = "📍", label = "LOKASI",  value = profile.location)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun App() {
    MaterialTheme {
        MyProfileScreen()
    }
}