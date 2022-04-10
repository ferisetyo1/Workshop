package com.feri.workshop.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Person(
    val email: String? = null,
    val nama: String? = null,
    val level: Int? = null,
    val nohp: String? = null,
    val alamat: String? = null,
    val noktp: String? = null,
    val canAddProduk:Boolean?=null,
    val canEditProduk:Boolean?=null,
    val canDeleteProduk:Boolean?=null,
) {

}