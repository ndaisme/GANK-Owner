package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.*
import com.example.data.model.CustomerEntity
import com.example.ui.theme.GankColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CustomerScreen(
    customers: List<CustomerEntity>,
    onAddCustomer: (name: String, phone: String, address: String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    val filteredCustomers = customers.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.phone.contains(searchQuery)
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
            NeoSectionHeader(title = "DIREKTORI PELANGGAN", subtitle = "Database & riwayat pelanggan")
            NeoBrutalistButton(
                text = "+ Pelanggan",
                onClick = { showAddDialog = true },
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Add,
                testTag = "btn_add_customer"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        NeoBrutalistTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = "Cari Nama / No. HP Pelanggan",
            testTag = "input_search_customer"
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredCustomers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Pelanggan tidak ditemukan.", color = GankColors.Steel, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCustomers, key = { it.id }) { customer ->
                    NeoBrutalistCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        backgroundColor = GankColors.White
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = GankColors.Ink,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = customer.name,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp,
                                        color = GankColors.Ink
                                    )
                                    Text(
                                        text = "WA/HP: ${customer.phone}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GankColors.Steel
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${customer.totalServices}x Servis",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = GankColors.Ink
                                )
                                Text(
                                    text = currencyFormatter.format(customer.totalSpending),
                                    fontSize = 11.sp,
                                    color = GankColors.Success,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        if (customer.address.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Alamat: ${customer.address}",
                                fontSize = 11.sp,
                                color = GankColors.Steel,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("TAMBAH PELANGGAN BARU", fontWeight = FontWeight.Black) },
            text = {
                var name by remember { mutableStateOf("") }
                var phone by remember { mutableStateOf("") }
                var address by remember { mutableStateOf("") }

                Column(modifier = Modifier.fillMaxWidth()) {
                    NeoBrutalistTextField(value = name, onValueChange = { name = it }, label = "Nama Lengkap *")
                    NeoBrutalistTextField(value = phone, onValueChange = { phone = it }, label = "No. HP / WhatsApp *", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                    NeoBrutalistTextField(value = address, onValueChange = { address = it }, label = "Alamat Toko / Rumah")

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddDialog = false }) { Text("Batal", fontWeight = FontWeight.Bold, color = GankColors.Ink) }
                        Spacer(modifier = Modifier.width(8.dp))
                        NeoBrutalistButton(
                            text = "Simpan Pelanggan",
                            onClick = {
                                if (name.isNotBlank() && phone.isNotBlank()) {
                                    onAddCustomer(name, phone, address)
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
