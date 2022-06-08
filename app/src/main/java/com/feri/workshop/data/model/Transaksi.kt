package com.feri.workshop.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class Transaksi(
    val id: String? = UUID.randomUUID().toString(),
    val mobil: Mobil? = null,
    val customer: Customer? = null,
    val carts: List<Cart>? = null,
    val ppnValue: Int? = null,
    val enablePPn: Boolean? = null,
    val diskonValue: Double? = null,
    val enableDiskon: Boolean? = null,
    val otherPayment: List<OtherPayment>? = null,
    val keterangan: String? = null,
    val metodePembayaran: String? = null,
    val status: String? = STATUS.Menunggu,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
    val updateInfo: List<String>? = null,
    val paidValue: Double? = null,
    val canceledBy: String? = null,
    val procededBy: String? = null,
    val finishedBy: String? = null,
) {

    fun getTotal(): Double {
        val subtotal = carts.orEmpty().sumOf { it.getTotal() }
        val others = otherPayment.orEmpty().sumOf { it.value ?: 0.0 }
        val ppn = if (enablePPn == true) (subtotal * (ppnValue ?: 0)) / 100 else 0.0
        return (subtotal.toInt() + others.toInt() + ppn.toInt() - (diskonValue
            ?: 0.0).toInt()).toDouble()
    }

    object STATUS {
        const val Menunggu = "Menunggu"
        const val Diproses = "Diproses"
        const val Selesai = "Selesai"
        const val Dibatalkan = "Dibatalkan"
    }

    data class Cart(
        val jumlah: Int? = null,
        val produk: Produk? = null,
        val kategori: Produk.Kategori? = null
    ) {
        fun getTotal() = (jumlah ?: 0) * (kategori?.harga ?: (produk?.harga ?: 0.0))
    }

    data class OtherPayment(val name: String? = null, val value: Double? = null)
}
