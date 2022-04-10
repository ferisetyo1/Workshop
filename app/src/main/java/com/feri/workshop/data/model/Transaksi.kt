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
    val otherPayment: List<OtherPayment>? = null,
    val keterangan: String? = null,
    val metodePembayaran: String? = null,
    val status: String? = STATUS.Menunggu,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
    val updateInfo:List<String>?=null,
    val paidValue:Double?=null
) {

    fun getTotal(): Double {
        val subtotal = carts.orEmpty().sumOf { it.getTotal() }
        val others = otherPayment.orEmpty().sumOf { it.value ?: 0.0 }
        val ppn = if (enablePPn == true) (subtotal * (ppnValue ?: 0)) / 100 else 0.0
        return subtotal+others+ppn
    }

    object STATUS {
        const val Menunggu = "Menunggu"
        const val Diproses = "Diproses"
        const val Selesai = "Selesai"
        const val Dibatalkan = "Dibatalkan"
    }

    data class Cart(val jumlah: Int? = null, val produk: Produk? = null) {
        fun getTotal() = (jumlah ?: 0) * (produk?.getFixHarga() ?: 0.0)
    }

    data class OtherPayment(val name: String? = null, val value: Double? = null)
}
