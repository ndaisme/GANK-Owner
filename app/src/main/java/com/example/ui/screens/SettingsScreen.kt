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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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

    // Fonnte settings state
    var fonnteEnabled by remember { mutableStateOf(prefs.getBoolean("fonnte_enabled", false)) }
    var fonnteToken by remember { mutableStateOf(prefs.getString("fonnte_token", "") ?: "") }

    val defaultCheckIn = "Halo {nama_pelanggan}, perangkat {tipe_perangkat} dengan No. Servis {no_servis} telah berhasil didaftarkan di {nama_toko} dengan keluhan: {keluhan}. Status saat ini: {status}. Terima kasih!"
    var templateCheckIn by remember { mutableStateOf(prefs.getString("fonnte_template_check_in", defaultCheckIn) ?: defaultCheckIn) }

    val defaultDiagnosis = "Halo {nama_pelanggan}, perangkat {tipe_perangkat} ({no_servis}) sedang dalam tahap diagnosa/pemeriksaan oleh teknisi kami. Status saat ini: {status}."
    var templateDiagnosis by remember { mutableStateOf(prefs.getString("fonnte_template_diagnosis", defaultDiagnosis) ?: defaultDiagnosis) }

    val defaultApproval = "Halo {nama_pelanggan}, diagnosa untuk perangkat {tipe_perangkat} ({no_servis}) telah selesai. Estimasi biaya perbaikan adalah {biaya}. Mohon konfirmasi persetujuan Anda melalui chat ini. Terima kasih!"
    var templateApproval by remember { mutableStateOf(prefs.getString("fonnte_template_waiting_approval", defaultApproval) ?: defaultApproval) }

    val defaultRepair = "Halo {nama_pelanggan}, perangkat {tipe_perangkat} ({no_servis}) sedang dalam proses perbaikan oleh teknisi kami. Status saat ini: {status}."
    var templateRepair by remember { mutableStateOf(prefs.getString("fonnte_template_repair", defaultRepair) ?: defaultRepair) }

    val defaultQc = "Halo {nama_pelanggan}, perbaikan perangkat {tipe_perangkat} ({no_servis}) telah selesai dan saat ini sedang dalam pengujian (Quality Control) untuk memastikan semua fungsi normal."
    var templateQc by remember { mutableStateOf(prefs.getString("fonnte_template_quality_control", defaultQc) ?: defaultQc) }

    val defaultCompleted = "Halo {nama_pelanggan}, kabar baik! Perbaikan perangkat {tipe_perangkat} ({no_servis}) di {nama_toko} TELAH SELESAI dan siap diambil. Total biaya: {biaya}. Silakan datang ke toko kami."
    var templateCompleted by remember { mutableStateOf(prefs.getString("fonnte_template_completed", defaultCompleted) ?: defaultCompleted) }

    val defaultPickedUp = "Halo {nama_pelanggan}, terima kasih telah mempercayakan perbaikan {tipe_perangkat} ({no_servis}) di {nama_toko}. Garansi perbaikan berlaku selama {garansi} hari. Semoga sehat selalu!"
    var templatePickedUp by remember { mutableStateOf(prefs.getString("fonnte_template_picked_up", defaultPickedUp) ?: defaultPickedUp) }

    val defaultCancelled = "Halo {nama_pelanggan}, servis untuk perangkat {tipe_perangkat} ({no_servis}) telah dibatalkan. Perangkat dapat diambil kembali di {nama_toko}. Terima kasih."
    var templateCancelled by remember { mutableStateOf(prefs.getString("fonnte_template_cancelled", defaultCancelled) ?: defaultCancelled) }

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

                // WhatsApp Fonnte Settings Card
                item {
                    var showTemplatesEditor by remember { mutableStateOf(false) }
                    NeoBrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = GankColors.White
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "INTEGRASI WHATSAPP (FONNTE)",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = GankColors.Ink
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GankColors.GankYellow)
                                        .border(1.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("FONNTE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = GankColors.Ink)
                                }
                            }
                            Text(
                                text = "Kirim notifikasi WhatsApp otomatis ke pelanggan saat ada perubahan status servis.",
                                fontSize = 11.sp,
                                color = GankColors.Steel
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // NeoBrutalist Switch/Toggle row for Fonnte Enable/Disable
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (fonnteEnabled) GankColors.GankYellow else GankColors.White)
                                    .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                                    .clickable { fonnteEnabled = !fonnteEnabled }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = GankColors.Ink,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Aktifkan WhatsApp Otomatis",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 13.sp,
                                            color = GankColors.Ink
                                        )
                                        Text(
                                            text = if (fonnteEnabled) "Notifikasi otomatis AKTIF" else "Notifikasi otomatis NONAKTIF",
                                            fontSize = 10.sp,
                                            color = GankColors.Steel,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .border(2.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                                        .background(if (fonnteEnabled) GankColors.Ink else GankColors.White, RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (fonnteEnabled) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = GankColors.GankYellow,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            NeoBrutalistTextField(
                                value = fonnteToken,
                                onValueChange = { fonnteToken = it },
                                label = "Fonnte API Token",
                                placeholder = "Masukkan token dari fonnte.com",
                                testTag = "input_fonnte_token"
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Expandable Template Messages Accordion
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GankColors.Paper)
                                    .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                                    .clickable { showTemplatesEditor = !showTemplatesEditor }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Store,
                                        contentDescription = null,
                                        tint = GankColors.Ink,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Kustomisasi Template Pesan WA",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp,
                                        color = GankColors.Ink
                                    )
                                }
                                Icon(
                                    imageVector = if (showTemplatesEditor) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = GankColors.Ink,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            if (showTemplatesEditor) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(GankColors.Paper, RoundedCornerShape(6.dp))
                                        .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Gunakan placeholder berikut untuk menyisipkan data dinamis:\n" +
                                                    "• {nama_pelanggan} : Nama pelanggan\n" +
                                                    "• {no_servis} : Nomor nota servis\n" +
                                                    "• {tipe_perangkat} : Tipe HP / Laptop\n" +
                                                    "• {keluhan} : Keluhan kerusakan\n" +
                                                    "• {status} : Status terbaru (cth. DIAGNOSA)\n" +
                                                    "• {biaya} : Estimasi atau total biaya\n" +
                                                    "• {garansi} : Jumlah hari garansi\n" +
                                                    "• {nama_toko} : Nama toko Anda",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = GankColors.Steel,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                NeoBrutalistTextField(
                                    value = templateCheckIn,
                                    onValueChange = { templateCheckIn = it },
                                    label = "Template CHECK IN (Pendaftaran)",
                                    singleLine = false,
                                    placeholder = defaultCheckIn,
                                    testTag = "input_template_checkin"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeoBrutalistTextField(
                                    value = templateDiagnosis,
                                    onValueChange = { templateDiagnosis = it },
                                    label = "Template DIAGNOSA (Pemeriksaan)",
                                    singleLine = false,
                                    placeholder = defaultDiagnosis,
                                    testTag = "input_template_diagnosis"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeoBrutalistTextField(
                                    value = templateApproval,
                                    onValueChange = { templateApproval = it },
                                    label = "Template WAITING APPROVAL (Nunggu Konfirmasi)",
                                    singleLine = false,
                                    placeholder = defaultApproval,
                                    testTag = "input_template_approval"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeoBrutalistTextField(
                                    value = templateRepair,
                                    onValueChange = { templateRepair = it },
                                    label = "Template REPAIR (Dalam Perbaikan)",
                                    singleLine = false,
                                    placeholder = defaultRepair,
                                    testTag = "input_template_repair"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeoBrutalistTextField(
                                    value = templateQc,
                                    onValueChange = { templateQc = it },
                                    label = "Template QUALITY CONTROL (Pengujian)",
                                    singleLine = false,
                                    placeholder = defaultQc,
                                    testTag = "input_template_qc"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeoBrutalistTextField(
                                    value = templateCompleted,
                                    onValueChange = { templateCompleted = it },
                                    label = "Template COMPLETED (Selesai)",
                                    singleLine = false,
                                    placeholder = defaultCompleted,
                                    testTag = "input_template_completed"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeoBrutalistTextField(
                                    value = templatePickedUp,
                                    onValueChange = { templatePickedUp = it },
                                    label = "Template PICKED UP (Sudah Diambil)",
                                    singleLine = false,
                                    placeholder = defaultPickedUp,
                                    testTag = "input_template_pickedup"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeoBrutalistTextField(
                                    value = templateCancelled,
                                    onValueChange = { templateCancelled = it },
                                    label = "Template CANCELLED (Dibatalkan)",
                                    singleLine = false,
                                    placeholder = defaultCancelled,
                                    testTag = "input_template_cancelled"
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            NeoBrutalistButton(
                                text = "Simpan Pengaturan WhatsApp",
                                onClick = {
                                    prefs.edit().apply {
                                        putBoolean("fonnte_enabled", fonnteEnabled)
                                        putString("fonnte_token", fonnteToken)
                                        putString("fonnte_template_check_in", templateCheckIn)
                                        putString("fonnte_template_diagnosis", templateDiagnosis)
                                        putString("fonnte_template_waiting_approval", templateApproval)
                                        putString("fonnte_template_repair", templateRepair)
                                        putString("fonnte_template_quality_control", templateQc)
                                        putString("fonnte_template_completed", templateCompleted)
                                        putString("fonnte_template_picked_up", templatePickedUp)
                                        putString("fonnte_template_cancelled", templateCancelled)
                                        apply()
                                    }
                                    Toast.makeText(context, "Pengaturan WhatsApp Fonnte berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                },
                                containerColor = GankColors.GankYellow,
                                icon = Icons.Default.Save,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "btn_save_fonnte_settings"
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
            Box(modifier = Modifier.weight(1f)) {
                CiCdScreen()
            }
        }
    }
}
