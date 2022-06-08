package com.feri.workshop.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Produk(
    val id: String? = UUID.randomUUID().toString(),
    val nama: String? = null,
    val hargaPokok: Double? = null,
    val harga: Double? = null,
    val kategori: List<Kategori>? = null,
    val tipe: String? = null,
    val deskripsi: String? = null,
    var stocks: List<Stock>? = null,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
    val modifiedAt: Long? = Date().time,
    val modifiedBy: String? = null,
    val deletedBy: String? = null,
    val deletedAt: Long? = Date().time
) {

    fun getJumlahStock() = stocks?.map { it.jumlah ?: 0 }?.sum() ?: 0
    fun getJumlahTerjual() =
        stocks?.filter { it.isJual == true }.orEmpty().sumOf { (it.jumlah ?: 0) * -1 }

    fun getKategory() = if (kategori.orEmpty()
            .isEmpty()
    ) listOf(Kategori(id = "1122334455",nama = "Non-Kategori")) else kategori.orEmpty()

    @IgnoreExtraProperties
    data class Kategori(
        val id: String? = null,
        val harga: Double? = null,
        val nama: String? = null
    )

    object Tipe {
        val BARANG = "BARANG"
        val JASA = "JASA"
    }
}
