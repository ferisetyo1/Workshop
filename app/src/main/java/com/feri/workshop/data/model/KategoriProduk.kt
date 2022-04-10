package com.feri.workshop.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class KategoriProduk(
    val id: String? = UUID.randomUUID().toString(),
    val nama: String? = null,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
    val isdelete: Boolean?=false
)
