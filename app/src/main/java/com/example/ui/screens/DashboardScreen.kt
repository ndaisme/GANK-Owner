package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.FinanceTransaction
import com.example.data.model.ServiceOrder
import com.example.data.model.ServiceStatus
import com.example.data.model.SparepartItem
import com.example.ui.MainTab
import com.example.ui.theme.GankColors
import java.text.NumberFormat
import java.util.Locale

private data class PerformanceStats(
    val income: Double,
    val expense: Double,
    val profit: Double,
    val servicesCount: Int
)

@Composable
fun DashboardScreen(
    services: List<ServiceOrder>,
    spareparts: List<SparepartItem>,
    transactions: List<FinanceTransaction>,
    totalIncome: Double,
    onNavigateTab: (MainTab) -> Unit,
    onAddServiceClick: () -> Unit
) {
    val activeServices = services.filter {
        it.status != ServiceStatus.PICKED_UP.name && it.status != ServiceStatus.CANCELLED.name
    }
    val readyPickup = services.filter { it.status == ServiceStatus.COMPLETED.name }
    val lowStockCount = spareparts.count { it.stock <= it.minStock }

    var statTimeframe by remember { mutableIntStateOf(0) } // 0: Minggu Ini, 1: Bulan Ini, 2: Tahun Ini

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    // Performance statistics calculation
    val now = System.currentTimeMillis()
    val weekCutoff = now - (7L * 24 * 60 * 60 * 1000)
    val monthCutoff = now - (30L * 24 * 60 * 60 * 1000)
    val yearCutoff = now - (365L * 24 * 60 * 60 * 1000)

    val weekIncome = transactions.filter { it.type == "INCOME" && it.timestamp >= weekCutoff }.sumOf { it.amount }
    val weekExpense = transactions.filter { it.type == "EXPENSE" && it.timestamp >= weekCutoff }.sumOf { it.amount }
    val weekStats = PerformanceStats(weekIncome, weekExpense, weekIncome - weekExpense, services.count { it.createdAt >= weekCutoff })

    val monthIncome = transactions.filter { it.type == "INCOME" && it.timestamp >= monthCutoff }.sumOf { it.amount }
    val monthExpense = transactions.filter { it.type == "EXPENSE" && it.timestamp >= monthCutoff }.sumOf { it.amount }
    val monthStats = PerformanceStats(monthIncome, monthExpense, monthIncome - monthExpense, services.count { it.createdAt >= monthCutoff })

    val yearIncome = transactions.filter { it.type == "INCOME" && it.timestamp >= yearCutoff }.sumOf { it.amount }
    val yearExpense = transactions.filter { it.type == "EXPENSE" && it.timestamp >= yearCutoff }.sumOf { it.amount }
    val yearStats = PerformanceStats(yearIncome, yearExpense, yearIncome - yearExpense, services.count { it.createdAt >= yearCutoff })

    val currentStats = when (statTimeframe) {
        0 -> weekStats
        1 -> monthStats
        else -> yearStats
    }

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
                    TextButton(onClick = { onNavigateTab(MainTab.SETTINGS) }) {
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

        // Section: STATISTIK PERFORMA BISNIS (Minggu, Bulan, Tahun)
        item {
            NeoSectionHeader(
                title = "STATISTIK PERFORMA BISNIS",
                subtitle = "Analisis Omset, Laba Bersih & Unit Servis"
            )

            // Timeframe Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GankColors.Ink)
                    .border(2.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                listOf("Minggu Ini", "Bulan Ini", "Tahun Ini").forEachIndexed { index, label ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (statTimeframe == index) GankColors.GankYellow else GankColors.Ink)
                            .clickable { statTimeframe = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = if (statTimeframe == index) GankColors.Ink else GankColors.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stat Cards Grid for current Period
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeoBrutalistCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    backgroundColor = GankColors.White
                ) {
                    Text("Pendapatan", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        currencyFormatter.format(currentStats.income),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = GankColors.Success
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Total Omset", fontSize = 10.sp, color = GankColors.Steel)
                }

                NeoBrutalistCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    backgroundColor = GankColors.White
                ) {
                    Text("Laba Bersih", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        currencyFormatter.format(currentStats.profit),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (currentStats.profit >= 0) GankColors.Ink else GankColors.Danger
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Margin Net", fontSize = 10.sp, color = GankColors.Steel)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeoBrutalistCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp),
                    backgroundColor = GankColors.White
                ) {
                    Text("Pengeluaran Kas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        currencyFormatter.format(currentStats.expense),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = GankColors.Danger
                    )
                }

                NeoBrutalistCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp),
                    backgroundColor = GankColors.NeonBlue
                ) {
                    Text("Unit Servis Masuk", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Ink)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${currentStats.servicesCount} Unit",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = GankColors.Ink
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comparative 3-Column Table Card
            NeoBrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = GankColors.White
            ) {
                Column {
                    Text("RINGKASAN PERBANDINGAN PERIODE", fontWeight = FontWeight.Black, fontSize = 12.sp, color = GankColors.Ink)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                            Text("PERIODE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Mingguan (7hr)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Bulanan (30hr)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Tahunan (1th)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1.2f), horizontalAlignment = Alignment.End) {
                            Text("OMSET", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(currencyFormatter.format(weekStats.income), fontSize = 11.sp, fontWeight = FontWeight.Black, color = GankColors.Success)
                            Text(currencyFormatter.format(monthStats.income), fontSize = 11.sp, fontWeight = FontWeight.Black, color = GankColors.Success)
                            Text(currencyFormatter.format(yearStats.income), fontSize = 11.sp, fontWeight = FontWeight.Black, color = GankColors.Success)
                        }
                        Column(modifier = Modifier.weight(1.2f), horizontalAlignment = Alignment.End) {
                            Text("PROFIT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(currencyFormatter.format(weekStats.profit), fontSize = 11.sp, fontWeight = FontWeight.Black)
                            Text(currencyFormatter.format(monthStats.profit), fontSize = 11.sp, fontWeight = FontWeight.Black)
                            Text(currencyFormatter.format(yearStats.profit), fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
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
