package com.feri.workshop.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Mobil(
    val id: String? = UUID.randomUUID().toString(),
    val customerid: String? = null,
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
    val isdelete: Boolean? = false,
    val lastTransaksion: Long? = null,
    val oldKM:Int?=null,
    val newKM:Int?=null,
) {
    object TipeMobil {
        const val automatic = "automatic"
        const val manual = "manual"
    }
}