package com.example.data.repository

import android.content.Context
import android.util.Log
import okhttp3.*
import java.io.IOException

class FonnteService(private val context: Context) {
    private val client = OkHttpClient()
    private val prefs = context.getSharedPreferences("gank_store_prefs", Context.MODE_PRIVATE)

    fun sendNotification(
        customerName: String,
        customerPhone: String,
        serviceNumber: String,
        deviceModel: String,
        complaint: String,
        estimatedCost: Double,
        statusDisplayName: String,
        statusKey: String,
        warrantyDays: Int
    ) {
        val enabled = prefs.getBoolean("fonnte_enabled", false)
        val token = prefs.getString("fonnte_token", "") ?: ""

        if (!enabled || token.isBlank()) {
            Log.d("FonnteService", "Fonnte notifications are disabled or token is blank.")
            return
        }

        // Get template based on statusKey
        val templateKey = "fonnte_template_${statusKey.lowercase()}"
        val defaultTemplate = getDefaultTemplate(statusKey)
        val template = prefs.getString(templateKey, defaultTemplate) ?: defaultTemplate

        val storeName = prefs.getString("store_name", "GANK SERVICE") ?: "GANK SERVICE"

        // Format cost: e.g., 150000 -> Rp 150.000
        val formattedCost = try {
            val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID"))
            format.format(estimatedCost).replace("Rp", "Rp ").replace(",00", "")
        } catch (e: Exception) {
            "Rp " + String.format("%,.0f", estimatedCost)
        }

        // Replace placeholders
        val finalMessage = template
            .replace("{nama_toko}", storeName)
            .replace("{no_servis}", serviceNumber)
            .replace("{nama_pelanggan}", customerName)
            .replace("{tipe_perangkat}", deviceModel)
            .replace("{keluhan}", complaint)
            .replace("{status}", statusDisplayName)
            .replace("{biaya}", formattedCost)
            .replace("{garansi}", warrantyDays.toString())

        // Format phone number to clean digits
        val formattedPhone = formatPhoneNumber(customerPhone)

        // Make async network call
        val formBody = FormBody.Builder()
            .add("target", formattedPhone)
            .add("message", finalMessage)
            .build()

        val request = Request.Builder()
            .url("https://api.fonnte.com/send")
            .header("Authorization", token)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FonnteService", "Failed to send Fonnte message", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.d("FonnteService", "Response from Fonnte: Code ${response.code}, Body: $body")
            }
        })
    }

    private fun formatPhoneNumber(phone: String): String {
        var clean = phone.replace(Regex("[^0-9]"), "")
        if (clean.startsWith("0")) {
            clean = "62" + clean.substring(1)
        } else if (!clean.startsWith("62")) {
            clean = "62" + clean
        }
        return clean
    }

    fun getDefaultTemplate(statusKey: String): String {
        return when (statusKey) {
            "CHECK_IN" -> "Halo {nama_pelanggan}, perangkat {tipe_perangkat} dengan No. Servis {no_servis} telah berhasil didaftarkan di {nama_toko} dengan keluhan: {keluhan}. Status saat ini: {status}. Terima kasih!"
            "DIAGNOSIS" -> "Halo {nama_pelanggan}, perangkat {tipe_perangkat} ({no_servis}) sedang dalam tahap diagnosa/pemeriksaan oleh teknisi kami. Status saat ini: {status}."
            "WAITING_APPROVAL" -> "Halo {nama_pelanggan}, diagnosa untuk perangkat {tipe_perangkat} ({no_servis}) telah selesai. Estimasi biaya perbaikan adalah {biaya}. Mohon konfirmasi persetujuan Anda melalui chat ini. Terima kasih!"
            "REPAIR" -> "Halo {nama_pelanggan}, perangkat {tipe_perangkat} ({no_servis}) sedang dalam proses perbaikan oleh teknisi kami. Status saat ini: {status}."
            "QUALITY_CONTROL" -> "Halo {nama_pelanggan}, perbaikan perangkat {tipe_perangkat} ({no_servis}) telah selesai dan saat ini sedang dalam pengujian (Quality Control) untuk memastikan semua fungsi normal."
            "COMPLETED" -> "Halo {nama_pelanggan}, kabar baik! Perbaikan perangkat {tipe_perangkat} ({no_servis}) di {nama_toko} TELAH SELESAI dan siap diambil. Total biaya: {biaya}. Silakan datang ke toko kami."
            "PICKED_UP" -> "Halo {nama_pelanggan}, terima kasih telah mempercayakan perbaikan {tipe_perangkat} ({no_servis}) di {nama_toko}. Garansi perbaikan berlaku selama {garansi} hari. Semoga sehat selalu!"
            "CANCELLED" -> "Halo {nama_pelanggan}, servis untuk perangkat {tipe_perangkat} ({no_servis}) telah dibatalkan. Perangkat dapat diambil kembali di {nama_toko}. Terima kasih."
            else -> "Halo {nama_pelanggan}, status perbaikan perangkat {tipe_perangkat} ({no_servis}) di {nama_toko} saat ini adalah: {status}."
        }
    }
}
