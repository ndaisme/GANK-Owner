package com.example.data.repository

import com.example.data.local.CustomerDao
import com.example.data.local.FinanceDao
import com.example.data.local.ServiceDao
import com.example.data.local.SparepartDao
import com.example.data.model.CustomerEntity
import com.example.data.model.FinanceTransaction
import com.example.data.model.ServiceOrder
import com.example.data.model.ServiceStatus
import com.example.data.model.SparepartItem
import kotlinx.coroutines.flow.Flow

class GankRepository(
    private val serviceDao: ServiceDao,
    private val sparepartDao: SparepartDao,
    private val financeDao: FinanceDao,
    private val customerDao: CustomerDao
) {
    val allServices: Flow<List<ServiceOrder>> = serviceDao.getAllServices()
    val allSpareparts: Flow<List<SparepartItem>> = sparepartDao.getAllSpareparts()
    val allTransactions: Flow<List<FinanceTransaction>> = financeDao.getAllTransactions()
    val allCustomers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()

    fun searchServices(query: String): Flow<List<ServiceOrder>> {
        return if (query.isBlank()) {
            serviceDao.getAllServices()
        } else {
            serviceDao.searchServices(query)
        }
    }

    suspend fun addService(service: ServiceOrder): Long = serviceDao.insertService(service)

    suspend fun updateService(service: ServiceOrder) = serviceDao.updateService(service)

    suspend fun updateStatus(id: Int, status: ServiceStatus) = serviceDao.updateStatus(id, status.name)

    suspend fun deleteService(id: Int) = serviceDao.deleteService(id)

    suspend fun addSparepart(sparepart: SparepartItem): Long = sparepartDao.insertSparepart(sparepart)

    suspend fun updateSparepart(sparepart: SparepartItem) = sparepartDao.updateSparepart(sparepart)

    suspend fun updateStock(id: Int, change: Int) = sparepartDao.updateStock(id, change)

    suspend fun addTransaction(transaction: FinanceTransaction): Long = financeDao.insertTransaction(transaction)

    suspend fun addCustomer(customer: CustomerEntity): Long = customerDao.insertCustomer(customer)

    suspend fun seedSampleDataIfEmpty() {
        if (serviceDao.countServices() == 0) {
            val sampleServices = listOf(
                ServiceOrder(
                    serviceNumber = "GS-2026-001",
                    customerName = "Budi Santoso",
                    customerPhone = "081234567890",
                    deviceModel = "Samsung Galaxy S23 Ultra",
                    imei = "358912345678901",
                    complaint = "Layar pecah & touch tidak merespon sebagian",
                    diagnosis = "LCD Original AMOLED pecah, butuh ganti modul LCD",
                    estimatedCost = 1850000.0,
                    downPayment = 500000.0,
                    status = ServiceStatus.REPAIR.name,
                    technicianName = "Andi (Spesialis LCD)",
                    warrantyDays = 30
                ),
                ServiceOrder(
                    serviceNumber = "GS-2026-002",
                    customerName = "Dewi Lestari",
                    customerPhone = "082198765432",
                    deviceModel = "iPhone 13 Pro",
                    imei = "359812345678902",
                    complaint = "Baterai cepat habis & kembung",
                    diagnosis = "Health baterai 68%, penggantian baterai Vizz 3500mAh",
                    estimatedCost = 450000.0,
                    downPayment = 0.0,
                    status = ServiceStatus.COMPLETED.name,
                    technicianName = "Rian (Spesialis Apple)",
                    warrantyDays = 60
                ),
                ServiceOrder(
                    serviceNumber = "GS-2026-003",
                    customerName = "Eko Prasetyo",
                    customerPhone = "085611223344",
                    deviceModel = "Xiaomi Redmi Note 12",
                    imei = "867812345678903",
                    complaint = "Mati total terkena cipratan air",
                    diagnosis = "Short pada jalur VDD_MAIN dekat IC Power PM8350",
                    estimatedCost = 350000.0,
                    downPayment = 100000.0,
                    status = ServiceStatus.DIAGNOSIS.name,
                    technicianName = "Andi (Spesialis Hardware)",
                    warrantyDays = 30
                ),
                ServiceOrder(
                    serviceNumber = "GS-2026-004",
                    customerName = "Siti Rahma",
                    customerPhone = "087855667788",
                    deviceModel = "OPPO Reno 8 5G",
                    imei = "861212345678904",
                    complaint = "Tidak bisa ngecas / Port longgar",
                    diagnosis = "Flexible charger & conektor Type-C aus",
                    estimatedCost = 200000.0,
                    downPayment = 200000.0,
                    status = ServiceStatus.PICKED_UP.name,
                    technicianName = "Rian",
                    warrantyDays = 30
                )
            )
            sampleServices.forEach { serviceDao.insertService(it) }
        }

        if (sparepartDao.countSpareparts() == 0) {
            val sampleSpareparts = listOf(
                SparepartItem(barcode = "SP-LCD-S23U", name = "LCD Original Samsung S23 Ultra", category = "LCD", stock = 3, minStock = 2, purchasePrice = 1400000.0, sellingPrice = 1850000.0, rackLocation = "Rak A-01"),
                SparepartItem(barcode = "SP-BAT-IP13P", name = "Baterai High Cap iPhone 13 Pro", category = "Baterai", stock = 8, minStock = 3, purchasePrice = 280000.0, sellingPrice = 450000.0, rackLocation = "Rak B-02"),
                SparepartItem(barcode = "SP-FLX-RN8", name = "Flexible Charging OPPO Reno 8", category = "Flex Board", stock = 12, minStock = 5, purchasePrice = 80000.0, sellingPrice = 200000.0, rackLocation = "Rak C-03"),
                SparepartItem(barcode = "SP-IC-PM8350", name = "IC Power PM8350 Qualcomm", category = "IC Chipset", stock = 2, minStock = 3, purchasePrice = 120000.0, sellingPrice = 250000.0, rackLocation = "Rak D-01")
            )
            sampleSpareparts.forEach { sparepartDao.insertSparepart(it) }
        }

        if (financeDao.countTransactions() == 0) {
            val sampleFinance = listOf(
                FinanceTransaction(type = "INCOME", category = "Servis HP", amount = 450000.0, description = "Pelunasan Servis GS-2026-002 iPhone 13 Pro"),
                FinanceTransaction(type = "INCOME", category = "Servis HP", amount = 200000.0, description = "Pelunasan Servis GS-2026-004 OPPO Reno 8"),
                FinanceTransaction(type = "EXPENSE", category = "Beli Sparepart", amount = 560000.0, description = "Restok 2x Baterai iPhone 13 Pro"),
                FinanceTransaction(type = "INCOME", category = "DP Servis", amount = 500000.0, description = "DP Servis GS-2026-001 Samsung S23 Ultra")
            )
            sampleFinance.forEach { financeDao.insertTransaction(it) }
        }

        if (customerDao.countCustomers() == 0) {
            val sampleCustomers = listOf(
                CustomerEntity(name = "Budi Santoso", phone = "081234567890", address = "Jl. Merdeka No. 45, Jakarta", totalServices = 2, totalSpending = 2300000.0),
                CustomerEntity(name = "Dewi Lestari", phone = "082198765432", address = "Jl. Sudirman No. 12, Bandung", totalServices = 1, totalSpending = 450000.0),
                CustomerEntity(name = "Eko Prasetyo", phone = "085611223344", address = "Jl. Gatot Subroto No. 8, Surabaya", totalServices = 1, totalSpending = 350000.0),
                CustomerEntity(name = "Siti Rahma", phone = "087855667788", address = "Jl. Pemuda No. 88, Semarang", totalServices = 1, totalSpending = 200000.0)
            )
            sampleCustomers.forEach { customerDao.insertCustomer(it) }
        }
    }
}
