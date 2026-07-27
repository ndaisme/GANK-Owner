package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.*
import com.example.data.model.ServiceOrder
import com.example.data.model.ServiceStatus
import com.example.ui.theme.GankColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ServiceListScreen(
    services: List<ServiceOrder>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: ServiceStatus?,
    onSelectFilter: (ServiceStatus?) -> Unit,
    onAdvanceStatus: (ServiceOrder) -> Unit,
    onAddService: (customerName: String, customerPhone: String, deviceModel: String, imei: String, complaint: String, cost: Double, dp: Double) -> Unit,
    showAddDialogInitially: Boolean = false
) {
    var showAddDialog by remember { mutableStateOf(showAddDialogInitially) }
    var selectedServiceDetail by remember { mutableStateOf<ServiceOrder?>(null) }
    val context = LocalContext.current
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Section: Title & Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeoSectionHeader(title = "MANAJEMEN SERVIS", subtitle = "Daftar nota & perbaikan")
            NeoBrutalistButton(
                text = "+ Tambah",
                onClick = { showAddDialog = true },
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Add,
                testTag = "btn_add_service_main"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Input
        NeoBrutalistTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = "Pencarian Cepat",
            placeholder = "Cari No. Servis, IMEI, Nama, atau No. HP...",
            testTag = "input_search_service"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Chips Horizontal Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (selectedFilter == null) GankColors.Ink else GankColors.White)
                        .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                        .clickable { onSelectFilter(null) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "SEMUA (${services.size})",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = if (selectedFilter == null) GankColors.White else GankColors.Ink
                    )
                }
            }

            items(ServiceStatus.values()) { status ->
                val count = services.count { it.status == status.name }
                val isSelected = selectedFilter == status
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) GankColors.GankYellow else GankColors.White)
                        .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                        .clickable { onSelectFilter(if (isSelected) null else status) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${status.displayName} ($count)",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = GankColors.Ink
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Service List
        if (services.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GankColors.White)
                    .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = GankColors.Steel
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data servis tidak ditemukan.",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = GankColors.Ink
                    )
                    Text(
                        text = "Coba ubah kata kunci pencarian atau tambah nota servis baru.",
                        fontSize = 12.sp,
                        color = GankColors.Steel
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(services, key = { it.id }) { service ->
                    val statusEnum = try {
                        ServiceStatus.valueOf(service.status)
                    } catch (e: Exception) {
                        ServiceStatus.CHECK_IN
                    }

                    NeoBrutalistCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clickable { selectedServiceDetail = service },
                        backgroundColor = GankColors.White
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = service.serviceNumber,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = GankColors.Ink
                            )
                            GSStatusChip(status = statusEnum)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${service.deviceModel} — ${service.customerName}",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = GankColors.Ink
                        )
                        if (service.imei.isNotBlank()) {
                            Text(
                                text = "IMEI: ${service.imei}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GankColors.Steel
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Keluhan: ${service.complaint}",
                            fontSize = 12.sp,
                            color = GankColors.Ink,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estimasi: ${currencyFormatter.format(service.estimatedCost)}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = GankColors.Ink
                                )
                                Text(
                                    text = "DP: ${currencyFormatter.format(service.downPayment)}",
                                    fontSize = 11.sp,
                                    color = GankColors.Steel
                                )
                            }

                            if (statusEnum != ServiceStatus.PICKED_UP && statusEnum != ServiceStatus.CANCELLED) {
                                NeoBrutalistButton(
                                    text = "Lanjut Flow →",
                                    onClick = { onAdvanceStatus(service) },
                                    containerColor = GankColors.NeonBlue,
                                    testTag = "btn_advance_${service.id}"
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GankColors.Paper)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Arsip Selesai", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Add Service Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "TAMBAH NOTA SERVIS BARU",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            },
            text = {
                var name by remember { mutableStateOf("") }
                var phone by remember { mutableStateOf("") }
                var device by remember { mutableStateOf("") }
                var imei by remember { mutableStateOf("") }
                var complaint by remember { mutableStateOf("") }
                var costStr by remember { mutableStateOf("") }
                var dpStr by remember { mutableStateOf("") }

                Column(modifier = Modifier.fillMaxWidth()) {
                    NeoBrutalistTextField(value = name, onValueChange = { name = it }, label = "Nama Pelanggan *")
                    NeoBrutalistTextField(value = phone, onValueChange = { phone = it }, label = "No. HP / WhatsApp *", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                    NeoBrutalistTextField(value = device, onValueChange = { device = it }, label = "Tipe / Merk HP *", placeholder = "cth. iPhone 13 / Samsung S23")
                    NeoBrutalistTextField(value = imei, onValueChange = { imei = it }, label = "IMEI (Opsional)")
                    NeoBrutalistTextField(value = complaint, onValueChange = { complaint = it }, label = "Keluhan Kerusakan *", singleLine = false)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NeoBrutalistTextField(value = costStr, onValueChange = { costStr = it }, label = "Estimasi Biaya", modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        NeoBrutalistTextField(value = dpStr, onValueChange = { dpStr = it }, label = "DP (Opsional)", modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showAddDialog = false }) {
                            Text("Batal", fontWeight = FontWeight.Bold, color = GankColors.Ink)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        NeoBrutalistButton(
                            text = "Simpan Servis",
                            onClick = {
                                if (name.isNotBlank() && phone.isNotBlank() && device.isNotBlank() && complaint.isNotBlank()) {
                                    val cost = costStr.toDoubleOrNull() ?: 0.0
                                    val dp = dpStr.toDoubleOrNull() ?: 0.0
                                    onAddService(name, phone, device, imei, complaint, cost, dp)
                                    showAddDialog = false
                                }
                            },
                            containerColor = GankColors.GankYellow,
                            testTag = "btn_submit_service"
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {},
            containerColor = GankColors.White,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.border(3.dp, GankColors.Ink, RoundedCornerShape(12.dp))
        )
    }

    // Modal Service Detail Dialog
    selectedServiceDetail?.let { service ->
        val statusEnum = try { ServiceStatus.valueOf(service.status) } catch (e: Exception) { ServiceStatus.CHECK_IN }

        AlertDialog(
            onDismissRequest = { selectedServiceDetail = null },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "DETAIL NOTA ${service.serviceNumber}", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    GSStatusChip(status = statusEnum)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Pelanggan: ${service.customerName} (${service.customerPhone})", fontWeight = FontWeight.Bold)
                    Text(text = "Unit HP: ${service.deviceModel}")
                    if (service.imei.isNotBlank()) Text(text = "IMEI: ${service.imei}", fontSize = 12.sp, color = GankColors.Steel)
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GankColors.Paper)
                            .border(2.dp, GankColors.Ink)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "Keluhan Awal:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = service.complaint, fontSize = 13.sp)
                            if (service.diagnosis.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Diagnosa Teknisi:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(text = service.diagnosis, fontSize = 13.sp, color = GankColors.Steel)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Biaya Total: ${currencyFormatter.format(service.estimatedCost)}", fontWeight = FontWeight.Black)
                    Text(text = "DP Terbayar: ${currencyFormatter.format(service.downPayment)}", fontSize = 12.sp)
                    Text(text = "Sisa Pelunasan: ${currencyFormatter.format(service.estimatedCost - service.downPayment)}", fontWeight = FontWeight.Bold, color = GankColors.Danger)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons inside detail
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NeoBrutalistButton(
                            text = "Kirim WA",
                            onClick = {
                                val message = "Halo ${service.customerName}, update nota *${service.serviceNumber}* untuk unit *${service.deviceModel}* saat ini berstatus: *${statusEnum.displayName}*. Total estimasi: ${currencyFormatter.format(service.estimatedCost)}. Terima kasih — GANK SERVICE."
                                val url = "https://api.whatsapp.com/send?phone=${service.customerPhone}&text=${Uri.encode(message)}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            containerColor = GankColors.Success,
                            contentColor = GankColors.White,
                            icon = Icons.Default.Send
                        )

                        TextButton(onClick = { selectedServiceDetail = null }) {
                            Text("Tutup", fontWeight = FontWeight.Black, color = GankColors.Ink)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {},
            containerColor = GankColors.White,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.border(3.dp, GankColors.Ink, RoundedCornerShape(12.dp))
        )
    }
}
