package com.feri.workshop.repository.model

import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toCustomer() = Customer(
    id = if (this.contains("id")) this.getString("id") else null,
    nama = if (this.contains("nama")) this.getString("nama") else null,
    notelp = if (this.contains("notelp")) this.getString("notelp") else null,
    alamat = if (this.contains("alamat")) this.getString("alamat") else null,
    lastTransaksion = if (this.contains("lastTransaksion")) this.getString("lastTransaksion") else null,
    isdelete = if (this.contains("isdelete")) this.getBoolean("isdelete") else null,
    createdBy = if (this.contains("createdBy")) this.getString("createdBy") else null,
    createdAt = if (this.contains("createdAt")) this.getLong("createdAt") else null,
)

fun DocumentSnapshot.toMobil() = Mobil(
    id = if (this.contains("id")) this.getString("id") else null,
    customerid = if (this.contains("customerid")) this.getString("customerid") else null,
    merk = if (this.contains("merk")) this.getString("merk") else null,
    nopol = if (this.contains("nopol")) this.getString("nopol") else null,
    tahun = if (this.contains("tahun")) this.getString("tahun") else null,
    tipe = if (this.contains("tipe")) this.getString("tipe") else null,
    warna = if (this.contains("warna")) this.getString("warna") else null,
    keterangan = if (this.contains("keterangan")) this.getString("keterangan") else null,
    nomesin = if (this.contains("nomesin")) this.getString("nomesin") else null,
    silinder = if (this.contains("silinder")) this.getString("silinder") else null,
    isdelete = if (this.contains("isdelete")) this.getBoolean("isdelete") else null,
    norangka = if (this.contains("norangka")) this.getString("norangka") else null,
    createdBy = if (this.contains("createdBy")) this.getString("createdBy") else null,
    createdAt = if (this.contains("createdAt")) this.getLong("createdAt") else null,
)