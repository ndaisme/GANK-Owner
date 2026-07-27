package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.*
import com.example.data.model.FinanceTransaction
import com.example.ui.theme.GankColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FinanceScreen(
    transactions: List<FinanceTransaction>,
    onAddTransaction: (type: String, category: String, amount: Double, description: String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }

    val totalIncome = transactions.filter { it.type == "INCOME" }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    val netBalance = totalIncome - totalExpense

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
            NeoSectionHeader(title = "KAS & KEUANGAN", subtitle = "Arus kas & laporan transaksi")
            NeoBrutalistButton(
                text = "+ Transaksi",
                onClick = { showAddDialog = true },
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Add,
                testTag = "btn_add_transaction"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Balance Cards
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            NeoBrutalistCard(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                backgroundColor = GankColors.GankYellow
            ) {
                Text(text = "Saldo Bersih Toko", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Ink)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currencyFormatter.format(netBalance),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = GankColors.Ink
                )
            }

            NeoBrutalistCard(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                backgroundColor = GankColors.White
            ) {
                Text(text = "Pemasukan", fontSize = 10.sp, color = GankColors.Success, fontWeight = FontWeight.Bold)
                Text(text = currencyFormatter.format(totalIncome), fontSize = 13.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Pengeluaran", fontSize = 10.sp, color = GankColors.Danger, fontWeight = FontWeight.Bold)
                Text(text = currencyFormatter.format(totalExpense), fontSize = 13.sp, fontWeight = FontWeight.Black)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        NeoSectionHeader(title = "RIWAYAT TRANSAKSI", subtitle = "Catatan Pemasukan & Pengeluaran")

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada transaksi kas.", color = GankColors.Steel, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(transactions, key = { it.id }) { tx ->
                    val isIncome = tx.type == "INCOME"

                    NeoBrutalistCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        backgroundColor = GankColors.White
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tx.description,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = GankColors.Ink,
                                    maxLines = 1
                                )
                                Text(
                                    text = tx.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GankColors.Steel
                                )
                            }

                            Text(
                                text = (if (isIncome) "+ " else "- ") + currencyFormatter.format(tx.amount),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = if (isIncome) GankColors.Success else GankColors.Danger
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
            title = { Text("CATAT TRANSAKSI KAS", fontWeight = FontWeight.Black) },
            text = {
                var isIncome by remember { mutableStateOf(true) }
                var category by remember { mutableStateOf("Servis HP") }
                var amountStr by remember { mutableStateOf("") }
                var desc by remember { mutableStateOf("") }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NeoBrutalistButton(
                            text = "Pemasukan (+)",
                            onClick = { isIncome = true },
                            containerColor = if (isIncome) GankColors.Success else GankColors.Paper,
                            contentColor = if (isIncome) GankColors.White else GankColors.Ink,
                            modifier = Modifier.weight(1f)
                        )
                        NeoBrutalistButton(
                            text = "Pengeluaran (-)",
                            onClick = { isIncome = false },
                            containerColor = if (!isIncome) GankColors.Danger else GankColors.Paper,
                            contentColor = if (!isIncome) GankColors.White else GankColors.Ink,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    NeoBrutalistTextField(value = category, onValueChange = { category = it }, label = "Kategori", placeholder = "Servis, Beli Alat, Operasional...")
                    NeoBrutalistTextField(value = amountStr, onValueChange = { amountStr = it }, label = "Nominal Rp *", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    NeoBrutalistTextField(value = desc, onValueChange = { desc = it }, label = "Keterangan *")

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddDialog = false }) { Text("Batal", fontWeight = FontWeight.Bold, color = GankColors.Ink) }
                        Spacer(modifier = Modifier.width(8.dp))
                        NeoBrutalistButton(
                            text = "Simpan Kas",
                            onClick = {
                                val amount = amountStr.toDoubleOrNull() ?: 0.0
                                if (amount > 0 && desc.isNotBlank()) {
                                    onAddTransaction(if (isIncome) "INCOME" else "EXPENSE", category, amount, desc)
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
