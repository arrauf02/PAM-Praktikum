package com.example.pengembanganaplikasimobile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import com.example.pengembanganaplikasimobile.data.JadwalItem
import com.example.pengembanganaplikasimobile.data.ProfileUiState
import com.example.pengembanganaplikasimobile.ui.theme.AppColors
import com.example.pengembanganaplikasimobile.viewmodel.ProfileViewModel
import org.jetbrains.compose.resources.painterResource
import pengembanganaplikasimobile.composeapp.generated.resources.Res
import pengembanganaplikasimobile.composeapp.generated.resources.profile_photo


@Composable
fun animColor(light: Color, dark: Color, isDark: Boolean): Color =
    animateColorAsState(
        targetValue   = if (isDark) dark else light,
        animationSpec = tween(durationMillis = 300),
        label         = "colorAnim"
    ).value


@Composable
fun ProfileHeader(
    name: String,
    title: String,
    isDark: Boolean = false,
    modifier: Modifier = Modifier
) {

    val gradientStart = animColor(AppColors.headerLightStart, AppColors.headerDarkStart, isDark)
    val gradientEnd   = animColor(AppColors.headerLightEnd,   AppColors.headerDarkEnd,   isDark)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(gradientStart, gradientEnd)
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
                    painter            = painterResource(Res.drawable.profile_photo),
                    contentDescription = "Foto Profil",
                    modifier           = Modifier.fillMaxSize(),
                    contentScale       = ContentScale.Crop
                )
            }
            Text(
                text       = name,
                color      = Color.White,
                fontSize   = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center
            )
            Text(
                text      = title,
                color     = Color(0xFFB0BEC5),
                fontSize  = 14.sp,
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
    isDark: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bgColor    = animColor(AppColors.lightIconBg, AppColors.darkIconBg, isDark)
    val labelColor = animColor(AppColors.lightHint,   AppColors.darkHint,   isDark)
    val textColor  = animColor(AppColors.lightText,   AppColors.darkText,   isDark)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text          = label,
                color         = labelColor,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
            Text(text = value, color = textColor, fontSize = 15.sp)
        }
    }
}


@Composable
fun ProfileCard(
    title: String,
    isDark: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val surfaceColor = animColor(AppColors.lightSurface, AppColors.darkSurface, isDark)
    val dividerColor = animColor(AppColors.lightDivider, AppColors.darkDivider, isDark)

    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors    = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text          = title,
                color         = AppColors.accentBlue,
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier      = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider(
                color     = dividerColor,
                thickness = 1.dp,
                modifier  = Modifier.padding(horizontal = 16.dp)
            )
            content()
        }
    }
}


@Composable
fun BioSection(
    bio: String,
    isDark: Boolean = false,
    modifier: Modifier = Modifier
) {
    val textColor = animColor(AppColors.lightSubtext, AppColors.darkSubtext, isDark)

    ProfileCard(title = "TENTANG SAYA", isDark = isDark, modifier = modifier) {
        Text(
            text       = bio,
            color      = textColor,
            fontSize   = 14.sp,
            lineHeight = 22.sp,
            modifier   = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun DarkModeToggle(
    isDark: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = animColor(AppColors.lightText, AppColors.darkText, isDark)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = if (isDark) "🌙  Mode Gelap" else "☀️  Mode Terang",
            fontSize   = 14.sp,
            fontWeight = FontWeight.Medium,
            color      = textColor
        )
        Switch(
            checked         = isDark,
            onCheckedChange = { onToggle() },
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = Color.White,
                checkedTrackColor   = AppColors.accentBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFBDBDBD)
            )
        )
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
            onClick  = onEditClick,
            modifier = Modifier.weight(1f),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = AppColors.accentBlue)
        ) {
            Text("✏", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profil", fontWeight = FontWeight.Medium)
        }
        Button(
            onClick  = onJadwalClick,
            modifier = Modifier.weight(1f),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = AppColors.accentTeal)
        ) {
            Text("📅", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Jadwal", fontWeight = FontWeight.Medium)
        }
    }
}


@Composable
fun EditProfileDialog(
    name: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    isDark: Boolean = false
) {
    val surfaceColor = animColor(AppColors.lightSurface, AppColors.darkSurface, isDark)
    val textColor    = animColor(AppColors.lightText,    AppColors.darkText,    isDark)
    val hintColor    = animColor(AppColors.lightHint,    AppColors.darkHint,    isDark)
    val dividerColor = animColor(AppColors.lightDivider, AppColors.darkDivider, isDark)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape    = RoundedCornerShape(16.dp),
            colors   = CardDefaults.cardColors(containerColor = surfaceColor),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text       = "Edit Profil",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = AppColors.headerLightStart
                )

                // Stateless TextField — state hoisting dari ViewModel
                OutlinedTextField(
                    value         = name,
                    onValueChange = onNameChange,
                    label         = { Text("Nama") },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = AppColors.accentBlue,
                        unfocusedBorderColor = dividerColor,
                        focusedLabelColor    = AppColors.accentBlue,
                        unfocusedLabelColor  = hintColor,
                        focusedTextColor     = textColor,
                        unfocusedTextColor   = textColor,
                        cursorColor          = AppColors.accentBlue
                    )
                )

                // Stateless TextField — state hoisting dari ViewModel
                OutlinedTextField(
                    value         = bio,
                    onValueChange = onBioChange,
                    label         = { Text("Bio") },
                    maxLines      = 4,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = AppColors.accentBlue,
                        unfocusedBorderColor = dividerColor,
                        focusedLabelColor    = AppColors.accentBlue,
                        unfocusedLabelColor  = hintColor,
                        focusedTextColor     = textColor,
                        unfocusedTextColor   = textColor,
                        cursorColor          = AppColors.accentBlue
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick  = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(8.dp)
                    ) { Text("Batal") }
                    Button(
                        onClick  = onSave,
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(8.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = AppColors.accentBlue
                        )
                    ) { Text("Simpan") }
                }
            }
        }
    }
}


@Composable
fun JadwalRow(item: JadwalItem, isDark: Boolean = false) {
    val textColor    = animColor(AppColors.lightText,    AppColors.darkText,    isDark)
    val subtextColor = animColor(AppColors.lightSubtext, AppColors.darkSubtext, isDark)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(item.warna)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = item.mataKuliah,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🕐 ${item.jam}",   fontSize = 12.sp, color = subtextColor)
                Text("📍 ${item.ruang}", fontSize = 12.sp, color = subtextColor)
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(item.warna.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text       = item.hari,
                fontSize   = 11.sp,
                fontWeight = FontWeight.Bold,
                color      = item.warna
            )
        }
    }
}


@Composable
fun JadwalDialog(onDismiss: () -> Unit, isDark: Boolean = false) {
    val jadwalList = listOf(
        JadwalItem("Sen", "Pengembangan Aplikasi Mobile", "09.30 - 12.00", "Labtek 3",         AppColors.accentBlue),
        JadwalItem("Sen", "Big Data",                    "13.00 - 15.30", "GK2 126",           AppColors.accentBlue),
        JadwalItem("Sel", "Kriptografi",                 "15.00 - 17.30", "GK2 303",           AppColors.accentTeal),
        JadwalItem("Rab", "Visualisasi Data",            "07.30 - 10.00", "GK2 321",           AppColors.accentTeal),
        JadwalItem("Rab", "Manajemen Basis Data",        "13.00 - 15.30", "GK2 327",           Color(0xFFE53935)),
        JadwalItem("Jum", "Keamanan Siber",              "07.30 - 10.00", "Labtek 1 Labkom 2", Color(0xFF8E24AA)),
    )
    val hariOrder = listOf("Sen", "Sel", "Rab", "Kam", "Jum")
    val namaHari  = mapOf("Sen" to "Senin", "Sel" to "Selasa",
        "Rab" to "Rabu", "Kam" to "Kamis", "Jum" to "Jumat")
    val grouped   = jadwalList.groupBy { it.hari }

    val surfaceColor = animColor(AppColors.lightSurface, AppColors.darkSurface, isDark)
    val dividerColor = animColor(AppColors.lightDivider, AppColors.darkDivider, isDark)
    val hintColor    = animColor(AppColors.lightHint,    AppColors.darkHint,    isDark)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape    = RoundedCornerShape(16.dp),
            colors   = CardDefaults.cardColors(containerColor = surfaceColor),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text       = "📅 Jadwal Kuliah",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = AppColors.headerLightStart
                )
                HorizontalDivider(
                    color    = dividerColor,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                hariOrder.forEach { hari ->
                    val items = grouped[hari] ?: return@forEach
                    Text(
                        text          = namaHari[hari] ?: hari,
                        fontSize      = 12.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color         = hintColor,
                        modifier      = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 2.dp)
                    )
                    items.forEach { JadwalRow(it, isDark) }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick  = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(8.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = AppColors.headerLightStart
                    )
                ) { Text("Tutup") }
            }
        }
    }
}


@Composable
fun MyProfileScreen() {
    val viewModel = remember { ProfileViewModel() }
    val uiState   by viewModel.uiState.collectAsState()

    val profile  = uiState.profile
    val isDark   = uiState.isDarkMode
    val bgColor  = animColor(AppColors.lightBg,         AppColors.darkBg,          isDark)
    val divColor = animColor(AppColors.lightCardStroke,  AppColors.darkCardStroke,  isDark)

    var showEditDialog   by remember { mutableStateOf(false) }
    var showJadwalDialog by remember { mutableStateOf(false) }
    var isInfoVisible    by remember { mutableStateOf(true) }
    val scrollState      = rememberScrollState()

    if (showEditDialog) {
        EditProfileDialog(
            name         = uiState.editName,
            bio          = uiState.editBio,
            onNameChange = { viewModel.onEditNameChange(it) },
            onBioChange  = { viewModel.onEditBioChange(it) },
            onDismiss    = { showEditDialog = false },
            onSave       = {
                viewModel.saveProfile()
                showEditDialog = false
            },
            isDark       = isDark
        )
    }

    if (showJadwalDialog) {
        JadwalDialog(onDismiss = { showJadwalDialog = false }, isDark = isDark)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(scrollState)
    ) {
        // 1. HEADER — sekarang ikut dark mode
        ProfileHeader(name = profile.name, title = profile.title, isDark = isDark)

        Spacer(modifier = Modifier.height(8.dp))

        // 2. DARK MODE TOGGLE — state di ViewModel
        DarkModeToggle(isDark = isDark, onToggle = { viewModel.toggleDarkMode() })

        // 3. ACTION BUTTONS
        ActionButtons(
            onEditClick = {
                viewModel.prepareEdit()
                showEditDialog = true
            },
            onJadwalClick = { showJadwalDialog = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 4. BIO SECTION
        BioSection(bio = profile.bio, isDark = isDark)

        Spacer(modifier = Modifier.height(4.dp))

        // 5. TOGGLE INFO KONTAK
        TextButton(
            onClick  = { isInfoVisible = !isInfoVisible },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text(
                text       = if (isInfoVisible) "▲  Sembunyikan Info" else "▼  Tampilkan Info",
                color      = AppColors.accentBlue,
                fontWeight = FontWeight.Medium
            )
        }

        // 6. INFO KONTAK
        AnimatedVisibility(
            visible = isInfoVisible,
            enter   = fadeIn() + slideInVertically(initialOffsetY = { -20 }),
            exit    = fadeOut()
        ) {
            ProfileCard(title = "INFORMASI KONTAK", isDark = isDark) {
                InfoItem("✉️", "EMAIL",   profile.email,    isDark)
                HorizontalDivider(color = divColor, thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp))
                InfoItem("📞", "TELEPON", profile.phone,    isDark)
                HorizontalDivider(color = divColor, thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp))
                InfoItem("📍", "LOKASI",  profile.location, isDark)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}