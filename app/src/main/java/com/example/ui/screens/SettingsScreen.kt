package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.NeoBrutalistButton
import com.example.components.NeoBrutalistCard
import com.example.components.NeoBrutalistTextField
import com.example.components.NeoSectionHeader
import com.example.ui.theme.GankColors

@Composable
fun SettingsScreen(
    initialSubTab: Int = 0, // 0: Toko, 1: CI/CD
    onClearDummyData: () -> Unit = {},
    onResetSampleData: () -> Unit = {}
) {
    var activeSubTab by remember { mutableIntStateOf(initialSubTab) }
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("gank_store_prefs", Context.MODE_PRIVATE) }

    // Store settings state loaded from SharedPreferences
    var storeName by remember { mutableStateOf(prefs.getString("store_name", "GANK SERVICE") ?: "GANK SERVICE") }
    var storeOwner by remember { mutableStateOf(prefs.getString("store_owner", "Budi Santoso (Owner)") ?: "Budi Santoso (Owner)") }
    var storePhone by remember { mutableStateOf(prefs.getString("store_phone", "0812-3456-7890") ?: "0812-3456-7890") }
    var storeEmail by remember { mutableStateOf(prefs.getString("store_email", "gankservice@gmail.com") ?: "gankservice@gmail.com") }
    var storeAddress by remember { mutableStateOf(prefs.getString("store_address", "Jl. Merdeka No. 45, Ruko Blok B2") ?: "Jl. Merdeka No. 45, Ruko Blok B2") }
    var storeCity by remember { mutableStateOf(prefs.getString("store_city", "Jakarta Pusat, 10110") ?: "Jakarta Pusat, 10110") }
    var storeMapsLink by remember { mutableStateOf(prefs.getString("store_maps", "Seberang Bank BCA | Buka 09.00 - 21.00") ?: "Seberang Bank BCA | Buka 09.00 - 21.00") }
    var storeTagline by remember { mutableStateOf(prefs.getString("store_tagline", "Spesialis Repair Smartphone & Laptop") ?: "Spesialis Repair Smartphone & Laptop") }
    var storeReceiptNote by remember { mutableStateOf(prefs.getString("store_note", "Garansi berlaku 30 hari. Wajib menyertakan nota ini saat klaim.") ?: "Garansi berlaku 30 hari. Wajib menyertakan nota ini saat klaim.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        NeoSectionHeader(
            title = "PENGATURAN APLIKASI",
            subtitle = "Konfigurasi profil toko & status CI/CD build"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sub-tab Navigation (Toko vs CI/CD Status)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(GankColors.Ink)
                .border(2.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (activeSubTab == 0) GankColors.GankYellow else GankColors.Ink)
                    .clickable { activeSubTab = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = if (activeSubTab == 0) GankColors.Ink else GankColors.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Profil Toko",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = if (activeSubTab == 0) GankColors.Ink else GankColors.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (activeSubTab == 1) GankColors.NeonBlue else GankColors.Ink)
                    .clickable { activeSubTab = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (activeSubTab == 1) GankColors.Ink else GankColors.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CI/CD Build Status",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = if (activeSubTab == 1) GankColors.Ink else GankColors.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeSubTab == 0) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    NeoBrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = GankColors.White
                    ) {
                        Column {
                            Text(
                                text = "INFORMASI TOKO & NOTA",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = GankColors.Ink
                            )
                            Text(
                                text = "Data ini akan digunakan pada kop nota servis dan cetak invoice pelanggan.",
                                fontSize = 11.sp,
                                color = GankColors.Steel
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            NeoBrutalistTextField(
                                value = storeName,
                                onValueChange = { storeName = it },
                                label = "Nama Toko / Bengkel *",
                                placeholder = "cth. GANK SERVICE",
                                testTag = "input_store_name"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storeOwner,
                                onValueChange = { storeOwner = it },
                                label = "Nama Pemilik / Penanggung Jawab",
                                placeholder = "cth. Budi Santoso",
                                testTag = "input_store_owner"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    NeoBrutalistTextField(
                                        value = storePhone,
                                        onValueChange = { storePhone = it },
                                        label = "No. WhatsApp Toko *",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                        placeholder = "0812-3456-7890",
                                        testTag = "input_store_phone"
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    NeoBrutalistTextField(
                                        value = storeEmail,
                                        onValueChange = { storeEmail = it },
                                        label = "Email Toko",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                        placeholder = "gankservice@gmail.com",
                                        testTag = "input_store_email"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storeAddress,
                                onValueChange = { storeAddress = it },
                                label = "Alamat Jalan / Ruko / Gedung *",
                                placeholder = "cth. Jl. Merdeka No. 45, Ruko Blok B2",
                                testTag = "input_store_address"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    NeoBrutalistTextField(
                                        value = storeCity,
                                        onValueChange = { storeCity = it },
                                        label = "Kota / Kode Pos",
                                        placeholder = "cth. Jakarta Pusat, 10110",
                                        testTag = "input_store_city"
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    NeoBrutalistTextField(
                                        value = storeMapsLink,
                                        onValueChange = { storeMapsLink = it },
                                        label = "Patokan / Jam Operasional",
                                        placeholder = "cth. Buka 09.00 - 21.00",
                                        testTag = "input_store_maps"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storeTagline,
                                onValueChange = { storeTagline = it },
                                label = "Slogan / Tagline Toko",
                                placeholder = "cth. Spesialis Repair Smartphone & Laptop",
                                testTag = "input_store_tagline"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storeReceiptNote,
                                onValueChange = { storeReceiptNote = it },
                                label = "Catatan Syarat & Garansi di Nota",
                                placeholder = "cth. Garansi berlaku 30 hari...",
                                testTag = "input_store_note"
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            NeoBrutalistButton(
                                text = "Simpan Profil & Alamat Toko",
                                onClick = {
                                    prefs.edit().apply {
                                        putString("store_name", storeName)
                                        putString("store_owner", storeOwner)
                                        putString("store_phone", storePhone)
                                        putString("store_email", storeEmail)
                                        putString("store_address", storeAddress)
                                        putString("store_city", storeCity)
                                        putString("store_maps", storeMapsLink)
                                        putString("store_tagline", storeTagline)
                                        putString("store_note", storeReceiptNote)
                                        apply()
                                    }
                                    Toast.makeText(context, "Profil & Alamat $storeName berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                },
                                containerColor = GankColors.GankYellow,
                                icon = Icons.Default.Save,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "btn_save_store_settings"
                            )
                        }
                    }
                }

                // Live Kop Nota Preview Card
                item {
                    NeoBrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = GankColors.Paper
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "PREVIEW KOP NOTA SERVIS",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    color = GankColors.Ink
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GankColors.Ink)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("LIVE PREVIEW", fontSize = 9.sp, fontWeight = FontWeight.Black, color = GankColors.GankYellow)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                                    .background(GankColors.White)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = storeName.ifBlank { "NAMA TOKO" },
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = GankColors.Ink
                                )
                                Text(
                                    text = storeTagline.ifBlank { "Slogan / Tagline Toko" },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GankColors.Steel
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${storeAddress.ifBlank { "Alamat Jalan" }}${if (storeCity.isNotBlank()) ", $storeCity" else ""}",
                                    fontSize = 10.sp,
                                    color = GankColors.Steel,
                                    fontWeight = FontWeight.Medium
                                )
                                if (storeMapsLink.isNotBlank()) {
                                    Text(
                                        text = "📍 $storeMapsLink",
                                        fontSize = 9.sp,
                                        color = GankColors.Steel
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "WA: ${storePhone.ifBlank { "-" }}${if (storeEmail.isNotBlank()) " | Email: $storeEmail" else ""}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GankColors.Ink
                                )
                                if (storeOwner.isNotBlank()) {
                                    Text(
                                        text = "Penanggung Jawab: $storeOwner",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GankColors.Steel
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(GankColors.Ink)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Catatan Syarat Garansi: ${storeReceiptNote.ifBlank { "-" }}",
                                    fontSize = 9.sp,
                                    color = GankColors.Steel,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                item {
                    var showClearDialog by remember { mutableStateOf(false) }
                    var showResetDialog by remember { mutableStateOf(false) }

                    NeoBrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = GankColors.White
                    ) {
                        Column {
                            Text(
                                text = "MANAJEMEN DATA & DATABASE",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = GankColors.Ink
                            )
                            Text(
                                text = "Kelola atau bersihkan data contoh (dummy) untuk reset database toko.",
                                fontSize = 11.sp,
                                color = GankColors.Steel
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            NeoBrutalistButton(
                                text = "Hapus Semua Data Dummy",
                                onClick = { showClearDialog = true },
                                containerColor = GankColors.Danger,
                                icon = Icons.Default.Delete,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "btn_clear_dummy_data"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistButton(
                                text = "Muat Ulang Data Contoh (Reset)",
                                onClick = { showResetDialog = true },
                                containerColor = GankColors.GankYellow,
                                icon = Icons.Default.Refresh,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "btn_reset_sample_data"
                            )
                        }
                    }

                    if (showClearDialog) {
                        AlertDialog(
                            onDismissRequest = { showClearDialog = false },
                            title = { Text("HAPUS DATA DUMMY?", fontWeight = FontWeight.Black) },
                            text = { Text("Semua data servis, stok, transaksi kas, dan pelanggan dummy akan dihapus secara permanen.", fontSize = 13.sp) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        onClearDummyData()
                                        showClearDialog = false
                                        Toast.makeText(context, "Data dummy berhasil dihapus!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text("Ya, Hapus Semua", fontWeight = FontWeight.Black, color = GankColors.Danger)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showClearDialog = false }) {
                                    Text("Batal", fontWeight = FontWeight.Bold, color = GankColors.Ink)
                                }
                            },
                            containerColor = GankColors.White,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.border(3.dp, GankColors.Ink, RoundedCornerShape(12.dp))
                        )
                    }

                    if (showResetDialog) {
                        AlertDialog(
                            onDismissRequest = { showResetDialog = false },
                            title = { Text("MUAT DUMMY AWAL?", fontWeight = FontWeight.Black) },
                            text = { Text("Database akan di-reset kembali ke data contoh bawaan.", fontSize = 13.sp) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        onResetSampleData()
                                        showResetDialog = false
                                        Toast.makeText(context, "Data contoh berhasil dimuat ulang!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text("Ya, Reset Data", fontWeight = FontWeight.Black, color = GankColors.Ink)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showResetDialog = false }) {
                                    Text("Batal", fontWeight = FontWeight.Bold, color = GankColors.Ink)
                                }
                            },
                            containerColor = GankColors.White,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.border(3.dp, GankColors.Ink, RoundedCornerShape(12.dp))
                        )
                    }
                }

                item {
                    NeoBrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = GankColors.White
                    ) {
                        Column {
                            Text(
                                text = "VERSI APLIKASI & LINGKUNGAN",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = GankColors.Ink
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Versi: 1.0.0 (Gank Service Mobile)", fontSize = 12.sp, color = GankColors.Steel)
                            Text("Build Target: Android 14+ (compileSdk 36)", fontSize = 12.sp, color = GankColors.Steel)
                            Text("Database Local: Room DB SQLite Native", fontSize = 12.sp, color = GankColors.Steel)
                            Text("Styling System: Neo-Brutalist Bold Outline", fontSize = 12.sp, color = GankColors.Steel)
                        }
                    }
                }
            }
        } else {
            // Embed CI/CD Screen directly inside Settings
            Box(modifier = Modifier.weight(1f)) {
                CiCdScreen()
            }
        }
    }
}
