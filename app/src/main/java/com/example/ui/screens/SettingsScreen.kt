package com.example.ui.screens

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

    // Store settings state
    var storeName by remember { mutableStateOf("GANK SERVICE") }
    var storePhone by remember { mutableStateOf("0812-3456-7890") }
    var storeAddress by remember { mutableStateOf("Jl. Merdeka No. 45, Jakarta") }
    var storeTagline by remember { mutableStateOf("Spesialis Repair Smartphone & Laptop") }
    var storeReceiptNote by remember { mutableStateOf("Garansi berlaku 30 hari. Wajib menyertakan nota ini saat klaim.") }

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
                        tint = GankColors.Ink,
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
                modifier = Modifier.fillMaxSize(),
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
                                testTag = "input_store_name"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storePhone,
                                onValueChange = { storePhone = it },
                                label = "No. WhatsApp / Telepon Toko *",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                testTag = "input_store_phone"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storeAddress,
                                onValueChange = { storeAddress = it },
                                label = "Alamat Lengkap Toko",
                                testTag = "input_store_address"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storeTagline,
                                onValueChange = { storeTagline = it },
                                label = "Slogan / Tagline Toko",
                                testTag = "input_store_tagline"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NeoBrutalistTextField(
                                value = storeReceiptNote,
                                onValueChange = { storeReceiptNote = it },
                                label = "Catatan Syarat & Garansi di Nota",
                                testTag = "input_store_note"
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            NeoBrutalistButton(
                                text = "Simpan Pengaturan Toko",
                                onClick = {
                                    Toast.makeText(context, "Pengaturan toko berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                },
                                containerColor = GankColors.GankYellow,
                                icon = Icons.Default.Save,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "btn_save_store_settings"
                            )
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
            Box(modifier = Modifier.fillMaxSize()) {
                CiCdScreen()
            }
        }
    }
}
