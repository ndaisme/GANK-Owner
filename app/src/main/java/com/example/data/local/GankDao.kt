package com.example.data.local

import androidx.room.*
import com.example.data.model.CustomerEntity
import com.example.data.model.FinanceTransaction
import com.example.data.model.ServiceOrder
import com.example.data.model.SparepartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY updatedAt DESC")
    fun getAllServices(): Flow<List<ServiceOrder>>

    @Query("SELECT * FROM services WHERE id = :id")
    suspend fun getServiceById(id: Int): ServiceOrder?

    @Query("SELECT * FROM services WHERE serviceNumber LIKE '%' || :query || '%' OR customerName LIKE '%' || :query || '%' OR customerPhone LIKE '%' || :query || '%' OR imei LIKE '%' || :query || '%' OR deviceModel LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchServices(query: String): Flow<List<ServiceOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceOrder): Long

    @Update
    suspend fun updateService(service: ServiceOrder)

    @Query("UPDATE services SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM services WHERE id = :id")
    suspend fun deleteService(id: Int)

    @Query("SELECT COUNT(*) FROM services")
    suspend fun countServices(): Int
}

@Dao
interface SparepartDao {
    @Query("SELECT * FROM spareparts ORDER BY name ASC")
    fun getAllSpareparts(): Flow<List<SparepartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSparepart(sparepart: SparepartItem): Long

    @Update
    suspend fun updateSparepart(sparepart: SparepartItem)

    @Query("UPDATE spareparts SET stock = stock + :change WHERE id = :id")
    suspend fun updateStock(id: Int, change: Int)

    @Query("SELECT COUNT(*) FROM spareparts")
    suspend fun countSpareparts(): Int
}

@Dao
interface FinanceDao {
    @Query("SELECT * FROM finance ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<FinanceTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: FinanceTransaction): Long

    @Query("SELECT COUNT(*) FROM finance")
    suspend fun countTransactions(): Int
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Query("SELECT COUNT(*) FROM customers")
    suspend fun countCustomers(): Int
}
