package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CustomerEntity
import com.example.data.model.FinanceTransaction
import com.example.data.model.ServiceOrder
import com.example.data.model.ServiceStatus
import com.example.data.model.SparepartItem
import com.example.data.repository.FonnteService
import com.example.data.repository.GankRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainTab(val title: String) {
    DASHBOARD("Dashboard"),
    SERVICES("Servis"),
    SPAREPARTS("Sparepart"),
    FINANCE("Kas"),
    CUSTOMERS("Pelanggan"),
    SETTINGS("Pengaturan")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GankRepository
    private val fonnteService = FonnteService(application)

    private val _selectedTab = MutableStateFlow(MainTab.DASHBOARD)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<ServiceStatus?>(null)
    val selectedStatusFilter: StateFlow<ServiceStatus?> = _selectedStatusFilter.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GankRepository(
            db.serviceDao(),
            db.sparepartDao(),
            db.financeDao(),
            db.customerDao()
        )

        viewModelScope.launch {
            repository.seedSampleDataIfEmpty()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val services: StateFlow<List<ServiceOrder>> = _searchQuery
        .flatMapLatest { query ->
            repository.searchServices(query)
        }
        .combine(_selectedStatusFilter) { list, filter ->
            if (filter == null) list else list.filter { it.status == filter.name }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spareparts: StateFlow<List<SparepartItem>> = repository.allSpareparts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<FinanceTransaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(filter: ServiceStatus?) {
        _selectedStatusFilter.value = filter
    }

    fun addService(
        customerName: String,
        customerPhone: String,
        deviceModel: String,
        imei: String,
        complaint: String,
        estimatedCost: Double,
        downPayment: Double
    ) {
        viewModelScope.launch {
            val count = services.value.size + 101
            val number = "GS-2026-$count"
            val order = ServiceOrder(
                serviceNumber = number,
                customerName = customerName,
                customerPhone = customerPhone,
                deviceModel = deviceModel,
                imei = imei,
                complaint = complaint,
                estimatedCost = estimatedCost,
                downPayment = downPayment,
                status = ServiceStatus.CHECK_IN.name
            )
            repository.addService(order)

            if (downPayment > 0) {
                repository.addTransaction(
                    FinanceTransaction(
                        type = "INCOME",
                        category = "DP Servis",
                        amount = downPayment,
                        description = "DP Servis $number ($deviceModel - $customerName)"
                    )
                )
            }

            // Automatically track customer
            repository.addCustomer(
                CustomerEntity(
                    name = customerName,
                    phone = customerPhone,
                    totalServices = 1,
                    totalSpending = downPayment
                )
            )

            // Trigger Fonnte WhatsApp notification
            fonnteService.sendNotification(
                customerName = customerName,
                customerPhone = customerPhone,
                serviceNumber = number,
                deviceModel = deviceModel,
                complaint = complaint,
                estimatedCost = estimatedCost,
                statusDisplayName = ServiceStatus.CHECK_IN.displayName,
                statusKey = ServiceStatus.CHECK_IN.name,
                warrantyDays = 30
            )
        }
    }

    fun advanceServiceStatus(service: ServiceOrder) {
        val currentStatus = try {
            ServiceStatus.valueOf(service.status)
        } catch (e: Exception) {
            ServiceStatus.CHECK_IN
        }

        val nextStatus = when (currentStatus) {
            ServiceStatus.CHECK_IN -> ServiceStatus.DIAGNOSIS
            ServiceStatus.DIAGNOSIS -> ServiceStatus.WAITING_APPROVAL
            ServiceStatus.WAITING_APPROVAL -> ServiceStatus.REPAIR
            ServiceStatus.REPAIR -> ServiceStatus.QUALITY_CONTROL
            ServiceStatus.QUALITY_CONTROL -> ServiceStatus.COMPLETED
            ServiceStatus.COMPLETED -> ServiceStatus.PICKED_UP
            ServiceStatus.PICKED_UP -> ServiceStatus.PICKED_UP
            ServiceStatus.CANCELLED -> ServiceStatus.CANCELLED
        }

        viewModelScope.launch {
            repository.updateStatus(service.id, nextStatus)

            // If picked up, record final payment if any
            if (nextStatus == ServiceStatus.PICKED_UP) {
                val remaining = service.estimatedCost - service.downPayment
                if (remaining > 0) {
                    repository.addTransaction(
                        FinanceTransaction(
                            type = "INCOME",
                            category = "Pelunasan Servis",
                            amount = remaining,
                            description = "Pelunasan ${service.serviceNumber} (${service.deviceModel})"
                        )
                    )
                }
            }

            // Trigger Fonnte WhatsApp notification for the updated status
            fonnteService.sendNotification(
                customerName = service.customerName,
                customerPhone = service.customerPhone,
                serviceNumber = service.serviceNumber,
                deviceModel = service.deviceModel,
                complaint = service.complaint,
                estimatedCost = service.estimatedCost,
                statusDisplayName = nextStatus.displayName,
                statusKey = nextStatus.name,
                warrantyDays = service.warrantyDays
            )
        }
    }

    fun addSparepart(
        barcode: String,
        name: String,
        category: String,
        stock: Int,
        purchasePrice: Double,
        sellingPrice: Double,
        rackLocation: String
    ) {
        viewModelScope.launch {
            val item = SparepartItem(
                barcode = if (barcode.isBlank()) "SP-${System.currentTimeMillis() % 10000}" else barcode,
                name = name,
                category = category,
                stock = stock,
                purchasePrice = purchasePrice,
                sellingPrice = sellingPrice,
                rackLocation = rackLocation
            )
            repository.addSparepart(item)
        }
    }

    fun updateSparepartStock(id: Int, change: Int) {
        viewModelScope.launch {
            repository.updateStock(id, change)
        }
    }

    fun addTransaction(type: String, category: String, amount: Double, description: String) {
        viewModelScope.launch {
            repository.addTransaction(
                FinanceTransaction(
                    type = type,
                    category = category,
                    amount = amount,
                    description = description
                )
            )
        }
    }

    fun addCustomer(name: String, phone: String, address: String) {
        viewModelScope.launch {
            repository.addCustomer(
                CustomerEntity(
                    name = name,
                    phone = phone,
                    address = address
                )
            )
        }
    }

    fun clearAllDummyData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    fun resetSampleData() {
        viewModelScope.launch {
            repository.resetSampleData()
        }
    }
}
