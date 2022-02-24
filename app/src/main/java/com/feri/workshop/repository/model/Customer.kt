package com.feri.workshop.repository.model

import java.util.*

data class Customer(
    val id: String? = UUID.randomUUID().toString(),
    val nama: String? = null,
    val notelp: String? = null,
    val alamat: String? = null,
    val lastTransaksion: String? = null,
    val createdAt: Long? = Date().time,
    val createdBy: String? = null,
    val isdelete: Boolean?=false
)
