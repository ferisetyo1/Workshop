package com.feri.workshop.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Stock(
    val id: String? = UUID.randomUUID().toString(),
    val produkId: String? = null,
    val jumlah: Long? = null,
    val deskripsi: String? = null,
    val isJual: Boolean? = false,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
)
