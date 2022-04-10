package com.feri.workshop.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Produk(
    val id: String? = UUID.randomUUID().toString(),
    val nama: String? = null,
    val harga: Double? = null,
    val kategori: String? = null,
    val tipe: String? = null,
    val diskon: Double? = null,
    val deskripsi: String? = null,
    var stocks: List<Stock>? = null,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
) {

    fun getFixHarga() = (harga ?: 0.0) - (diskon ?: 0.0)
    fun getJumlahStock() = stocks?.map { it.jumlah ?: 0 }?.sum() ?: 0

    object Tipe {
        val BARANG = "BARANG"
        val JASA = "JASA"
    }
}
