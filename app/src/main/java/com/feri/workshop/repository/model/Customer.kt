package com.feri.workshop.repository.model

import java.util.*

data class Customer(
    val id: String? = UUID.randomUUID().toString(),
    val nama: String? = null,
    val notelp: String? = null,
    val alamat: String? = null,
    val lastTransaksion: String? = null,
    val mobil: List<Mobil>? = null,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
    val isdelete: Boolean?=false
) {

    data class Mobil(
        val id: String? = UUID.randomUUID().toString(),
        val merk: String? = null,
        val nopol: String? = null,
        val tipe: String? = null,
        val tahun: String? = null,
        val silinder: String? = null,
        val warna: String? = null,
        val norangka: String? = null,
        val nomesin: String? = null,
        val keterangan: String? = null,
        val createdAt: Long? = Date().time,
        val createdBy: String? = null,
        val isdelete: Boolean?=false
    )

    object TipeMobil {
        const val automatic = "automatic"
        const val manual = "manual"
    }

}
