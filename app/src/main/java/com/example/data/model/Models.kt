package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

enum class ServiceStatus(val displayName: String) {
    CHECK_IN("CHECK IN"),
    DIAGNOSIS("DIAGNOSA"),
    WAITING_APPROVAL("MENUNGGU APPROVAL"),
    REPAIR("DALAM PERBAIKAN"),
    QUALITY_CONTROL("QUALITY CONTROL"),
    COMPLETED("SELESAI"),
    PICKED_UP("SUDAH DIAMBIL"),
    CANCELLED("DIBATALKAN")
}

@Entity(tableName = "services")
@JsonClass(generateAdapter = true)
data class ServiceOrder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceNumber: String,
    val customerName: String,
    val customerPhone: String,
    val deviceModel: String,
    val imei: String = "",
    val complaint: String,
    val diagnosis: String = "",
    val estimatedCost: Double = 0.0,
    val downPayment: Double = 0.0,
    val status: String = ServiceStatus.CHECK_IN.name,
    val technicianName: String = "Teknisi Utama",
    val warrantyDays: Int = 30,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val capitalCost: Double = 0.0
)

@Entity(tableName = "spareparts")
data class SparepartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barcode: String,
    val name: String,
    val category: String,
    val stock: Int,
    val minStock: Int = 5,
    val purchasePrice: Double,
    val sellingPrice: Double,
    val rackLocation: String = "Rak A-1"
)

@Entity(tableName = "finance")
data class FinanceTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "INCOME" or "EXPENSE"
    val category: String,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val address: String = "",
    val totalServices: Int = 1,
    val totalSpending: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
