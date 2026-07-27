package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.*
import com.example.data.model.ServiceOrder
import com.example.data.model.ServiceStatus
import com.example.data.model.SparepartItem
import com.example.ui.MainTab
import com.example.ui.theme.GankColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    services: List<ServiceOrder>,
    spareparts: List<SparepartItem>,
    totalIncome: Double,
    onNavigateTab: (MainTab) -> Unit,
    onAddServiceClick: () -> Unit
) {
    val activeServices = services.filter {
        it.status != ServiceStatus.PICKED_UP.name && it.status != ServiceStatus.CANCELLED.name
    }
    val readyPickup = services.filter { it.status == ServiceStatus.COMPLETED.name }
    val lowStockCount = spareparts.count { it.stock <= it.minStock }

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Branding Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GankColors.Ink)
                    .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "GANK SERVICE",
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = GankColors.GankYellow
                        )
                        Text(
                            text = "Repair • Manage • Grow — ERP Servis HP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = GankColors.Paper
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GankColors.NeonBlue)
                            .border(2.dp, GankColors.White)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "OWNER MODE",
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp,
                            color = GankColors.Ink
                        )
                    }
                }
            }
        }

        // CI/CD Banner Status
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GankColors.White)
                    .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "CI/CD Status",
                        tint = GankColors.Success,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "GitHub Actions Build Compatible",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = GankColors.Ink
                        )
                        Text(
                            text = "./gradlew assembleDebug & gradle-wrapper ready",
                            fontSize = 11.sp,
                            color = GankColors.Steel
                        )
                    }
                    TextButton(onClick = { onNavigateTab(MainTab.CICD) }) {
                        Text(
                            text = "Lihat Detail",
                            fontWeight = FontWeight.Black,
                            color = GankColors.Ink,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Metric Stat Cards Grid
        item {
            NeoSectionHeader(title = "RINGKASAN OPERASIONAL", subtitle = "Kondisi toko terkini")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Stat 1: Total Pendapatan
                    NeoBrutalistCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        backgroundColor = GankColors.White
                    ) {
                        Text(text = "Pendapatan Total", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currencyFormatter.format(totalIncome),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = GankColors.Ink
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Dari DP & Pelunasan", fontSize = 10.sp, color = GankColors.Success, fontWeight = FontWeight.Bold)
                    }

                    // Stat 2: Servis Aktif
                    NeoBrutalistCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        backgroundColor = GankColors.GankYellow
                    ) {
                        Text(text = "Servis Diproses", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Ink)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${activeServices.size} Unit",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = GankColors.Ink
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Butuh pengerjaan", fontSize = 10.sp, color = GankColors.Ink, fontWeight = FontWeight.Bold)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Stat 3: Ready Pick Up
                    NeoBrutalistCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        backgroundColor = GankColors.NeonBlue
                    ) {
                        Text(text = "Siap Diambil", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Ink)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${readyPickup.size} Unit",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = GankColors.Ink
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Selesai QC", fontSize = 10.sp, color = GankColors.Ink, fontWeight = FontWeight.Bold)
                    }

                    // Stat 4: Low Stock Warning
                    NeoBrutalistCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        backgroundColor = if (lowStockCount > 0) GankColors.Warning else GankColors.White
                    ) {
                        Text(text = "Stok Menipis", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Ink)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$lowStockCount Item",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = GankColors.Ink
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (lowStockCount > 0) "Perlu re-stock" else "Stok aman",
                            fontSize = 10.sp,
                            color = GankColors.Ink,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Quick Actions
        item {
            NeoSectionHeader(title = "AKSI CEPAT", subtitle = "Shortcut utama operasional")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeoBrutalistButton(
                    text = "+ Servis Baru",
                    onClick = onAddServiceClick,
                    modifier = Modifier.weight(1f),
                    containerColor = GankColors.GankYellow,
                    icon = Icons.Default.Add,
                    testTag = "btn_add_service_quick"
                )
                NeoBrutalistButton(
                    text = "Sparepart",
                    onClick = { onNavigateTab(MainTab.SPAREPARTS) },
                    modifier = Modifier.weight(1f),
                    containerColor = GankColors.White,
                    icon = Icons.Default.Build
                )
            }
        }

        // Active Service List Preview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NeoSectionHeader(title = "SERVIS AKTIF TERKINI", subtitle = "Daftar antrian perbaikan")
                TextButton(onClick = { onNavigateTab(MainTab.SERVICES) }) {
                    Text("Lihat Semua", fontWeight = FontWeight.Black, color = GankColors.Ink)
                }
            }
        }

        if (activeServices.isEmpty()) {
            item {
                NeoBrutalistCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    backgroundColor = GankColors.White
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada servis aktif. Klik '+ Servis Baru' untuk memulai.", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GankColors.Steel)
                    }
                }
            }
        } else {
            items(activeServices.take(3)) { service ->
                val statusEnum = try {
                    ServiceStatus.valueOf(service.status)
                } catch (e: Exception) {
                    ServiceStatus.CHECK_IN
                }

                NeoBrutalistCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
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
                            fontSize = 14.sp,
                            color = GankColors.Ink
                        )
                        GSStatusChip(status = statusEnum)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${service.deviceModel} — ${service.customerName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GankColors.Ink
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Keluhan: ${service.complaint}",
                        fontSize = 12.sp,
                        color = GankColors.Steel,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Estimasi: ${currencyFormatter.format(service.estimatedCost)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = GankColors.Ink
                        )
                        Text(
                            text = "Teknisi: ${service.technicianName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GankColors.Steel
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
