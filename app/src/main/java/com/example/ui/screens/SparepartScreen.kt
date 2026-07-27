package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.*
import com.example.data.model.SparepartItem
import com.example.ui.theme.GankColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SparepartScreen(
    spareparts: List<SparepartItem>,
    onAddSparepart: (barcode: String, name: String, category: String, stock: Int, purchasePrice: Double, sellingPrice: Double, rackLocation: String) -> Unit,
    onUpdateStock: (id: Int, change: Int) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    val filteredSpareparts = spareparts.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.category.contains(searchQuery, ignoreCase = true) ||
        it.barcode.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeoSectionHeader(title = "INVENTORI SPAREPART", subtitle = "Kelola stok komponen & rak")
            NeoBrutalistButton(
                text = "+ Sparepart",
                onClick = { showAddDialog = true },
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Add,
                testTag = "btn_add_sparepart"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        NeoBrutalistTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = "Cari Sparepart / Barcode",
            placeholder = "Nama barang, LCD, Baterai, Rak...",
            testTag = "input_search_sparepart"
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredSpareparts.isEmpty()) {
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
                Text(
                    text = "Sparepart tidak ditemukan.",
                    fontWeight = FontWeight.Bold,
                    color = GankColors.Steel
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredSpareparts, key = { it.id }) { item ->
                    val isLowStock = item.stock <= item.minStock

                    NeoBrutalistCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        backgroundColor = if (isLowStock) GankColors.White else GankColors.White
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = GankColors.Ink
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isLowStock) GankColors.Warning else GankColors.GankYellow)
                                    .border(2.dp, GankColors.Ink)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.category,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp,
                                    color = GankColors.Ink
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Barcode: ${item.barcode}", fontSize = 11.sp, color = GankColors.Steel)
                            Text(text = "Lokasi: ${item.rackLocation}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Ink)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Jual: ${currencyFormatter.format(item.sellingPrice)}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = GankColors.Ink
                                )
                                Text(
                                    text = "Modal: ${currencyFormatter.format(item.purchasePrice)}",
                                    fontSize = 11.sp,
                                    color = GankColors.Steel
                                )
                            }

                            // Stock Control Box
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (isLowStock) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Low Stock",
                                        tint = GankColors.Warning,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                NeoBrutalistButton(
                                    text = "-",
                                    onClick = { if (item.stock > 0) onUpdateStock(item.id, -1) },
                                    containerColor = GankColors.Paper
                                )

                                Text(
                                    text = "${item.stock} unit",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = if (isLowStock) GankColors.Danger else GankColors.Ink,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                NeoBrutalistButton(
                                    text = "+",
                                    onClick = { onUpdateStock(item.id, 1) },
                                    containerColor = GankColors.NeonBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Sparepart Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("TAMBAH SPAREPART BARU", fontWeight = FontWeight.Black) },
            text = {
                var barcode by remember { mutableStateOf("") }
                var name by remember { mutableStateOf("") }
                var category by remember { mutableStateOf("LCD") }
                var stockStr by remember { mutableStateOf("5") }
                var buyStr by remember { mutableStateOf("") }
                var sellStr by remember { mutableStateOf("") }
                var rack by remember { mutableStateOf("Rak A-01") }

                Column(modifier = Modifier.fillMaxWidth()) {
                    NeoBrutalistTextField(value = name, onValueChange = { name = it }, label = "Nama Barang *")
                    NeoBrutalistTextField(value = category, onValueChange = { category = it }, label = "Kategori", placeholder = "LCD, Baterai, IC, Flex")
                    NeoBrutalistTextField(value = barcode, onValueChange = { barcode = it }, label = "Barcode / Kode Unik")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NeoBrutalistTextField(value = buyStr, onValueChange = { buyStr = it }, label = "Harga Modal", modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        NeoBrutalistTextField(value = sellStr, onValueChange = { sellStr = it }, label = "Harga Jual", modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NeoBrutalistTextField(value = stockStr, onValueChange = { stockStr = it }, label = "Stok Awal", modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        NeoBrutalistTextField(value = rack, onValueChange = { rack = it }, label = "Lokasi Rak", modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddDialog = false }) { Text("Batal", fontWeight = FontWeight.Bold, color = GankColors.Ink) }
                        Spacer(modifier = Modifier.width(8.dp))
                        NeoBrutalistButton(
                            text = "Simpan",
                            onClick = {
                                if (name.isNotBlank()) {
                                    val stock = stockStr.toIntOrNull() ?: 0
                                    val buy = buyStr.toDoubleOrNull() ?: 0.0
                                    val sell = sellStr.toDoubleOrNull() ?: 0.0
                                    onAddSparepart(barcode, name, category, stock, buy, sell, rack)
                                    showAddDialog = false
                                }
                            },
                            containerColor = GankColors.GankYellow
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
}
