package com.feri.workshop.data

import android.content.Context
import androidx.core.text.isDigitsOnly
import com.feri.workshop.data.model.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@ActivityScoped
class WorkshopRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
) {
    val firestore = Firebase.firestore
    val userCollection = firestore.collection("user")
    val produkCollection = firestore.collection("produk")
    val kategoriProdukCollection = firestore.collection("kategoriProduk")
    val customerCollection = firestore.collection("customer")
    val transaksiCollection = firestore.collection("transaksi")
    val personCollection = firestore.collection("person")

    fun addCustomer(
        customer: Customer,
        mobil: Mobil,
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        isLoading(true)

        customerCollection.whereEqualTo("notelp", customer.notelp.orEmpty()).limit(1).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    println(it.result.documents)
                    if (it.result.documents.isEmpty()) {
                        customerCollection.document(customer.id.orEmpty()).set(customer)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    customerCollection.document(customer.id.orEmpty())
                                        .collection("mobil")
                                        .document(mobil.id.orEmpty())
                                        .set(mobil.copy(customerid = customer.id))
                                        .addOnCompleteListener {
                                            isLoading(false)
                                            if (it.isSuccessful) {
                                                onSuccess()
                                            } else {
                                                onFailed(it.exception?.message.orEmpty())
                                            }
                                        }
                                } else {
                                    isLoading(false)
                                    onFailed(it.exception?.message.orEmpty())
                                }
                            }
                    } else {
                        isLoading(false)
                        onFailed("Nomor telfon sudah dipakai pelanggan lain")
                    }
                } else {
                    isLoading(false)
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        isLoading(true)
        customerCollection.whereEqualTo("notelp", customer.notelp.orEmpty()).limit(1).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    println(it.result.documents)
                    if (it.result.documents.isEmpty()) {
                        customerCollection.document(customer.id.orEmpty()).set(customer)
                            .addOnCompleteListener {
                                isLoading(false)
                                if (it.isSuccessful) {
                                    onSuccess()
                                } else {
                                    onFailed(it.exception?.message.orEmpty())
                                }
                            }
                    } else {
                        if (it.result.documents.first().toObject(Customer::class.java)?.id == customer.id) {
                            customerCollection.document(customer.id.orEmpty()).set(customer)
                                .addOnCompleteListener {
                                    isLoading(false)
                                    if (it.isSuccessful) {
                                        onSuccess()
                                    } else {
                                        onFailed(it.exception?.message.orEmpty())
                                    }
                                }
                        } else {
                            isLoading(false)
                            onFailed("Nomor telfon sudah dipakai pelanggan lain")
                        }
                    }
                } else {
                    isLoading(false)
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun listCustomer(
        query: String = "",
        refresh: Boolean = false,
        onSuccess: (List<Customer>) -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        isLoading(true)
        var sql: Query = customerCollection

        if (query.isNotEmpty()) sql =
            sql.orderBy(
                when (query.isDigitsOnly()) {
                    true -> "notelp"
                    else -> "nama"
                }
            ).startAt(query.lowercase()).endAt(query.lowercase() + "\uf8ff")
        else sql = sql.orderBy("createdAt", Query.Direction.DESCENDING)
        sql.get(if (refresh) Source.DEFAULT else Source.CACHE).addOnCompleteListener {
            isLoading(false)
            it.result.documents.map { it }
            if (it.isSuccessful) {
                onSuccess(it.result.toObjects(Customer::class.java))
            } else {
                onFailed(it.exception?.message.orEmpty())
            }
        }
    }

    fun listMobil(
        customerId: String = "",
        onSuccess: (List<Mobil>) -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        isLoading(true)
        customerCollection.document(customerId).collection("mobil").get().addOnCompleteListener {
            isLoading(false)
            if (it.isSuccessful) {
                onSuccess(it.result.toObjects(Mobil::class.java))
            } else {
                onFailed(it.exception?.message.orEmpty())
            }
        }
    }

    fun addMobil(
        mobil: Mobil,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        isLoading(true)
        val reff = customerCollection.document(mobil.customerid.orEmpty()).collection("mobil")
        reff.whereEqualTo("nopol", mobil.nopol.orEmpty()).limit(1).get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.documents.isEmpty()) {
                    reff.document(mobil.id.orEmpty()).set(mobil).addOnCompleteListener {
                        isLoading(false)
                        if (it.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailed(it.exception?.message.orEmpty())
                        }
                    }
                } else {
                    if (it.result.documents.first().toObject(Mobil::class.java)?.id == mobil.id) {
                        reff.document(mobil.id.orEmpty()).set(mobil).addOnCompleteListener {
                            isLoading(false)
                            if (it.isSuccessful) {
                                onSuccess()
                            } else {
                                onFailed(it.exception?.message.orEmpty())
                            }
                        }
                    } else {
                        isLoading(false)
                        onFailed("Plat nomor ini sudah ditambahkan")
                    }
                }
            } else {
                isLoading(false)
                onFailed(it.exception?.message.orEmpty())
            }
        }
    }

    fun deleteCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
        isLoading: (Boolean) -> Unit
    ) {
        isLoading(true)
        customerCollection.document(customer.id.orEmpty()).delete().addOnCompleteListener {
            isLoading(false)
            if (it.isSuccessful) {
                onSuccess()
            } else {
                onFailed(it.exception?.message.orEmpty())
            }
        }
    }

    fun deleteMobil(
        mobil: Mobil,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
        isLoading: (Boolean) -> Unit
    ) {
        customerCollection.document(mobil.customerid.orEmpty()).collection("mobil")
            .document(mobil.id.orEmpty()).delete().addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun listKategoriProduk(
        refresh: Boolean = false,
        onSuccess: (List<KategoriProduk>) -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        isLoading(true)
        kategoriProdukCollection.orderBy("nama").get(if (refresh) Source.DEFAULT else Source.CACHE)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess(it.result.toObjects(KategoriProduk::class.java))
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun addKategoriProduk(
        kategori: KategoriProduk,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit = {}
    ) {
        isLoading(true)
        kategoriProdukCollection.document(kategori.id.orEmpty()).set(kategori)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun deleteKategori(
        kategori: KategoriProduk,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        isLoading(true)
        kategoriProdukCollection.document(kategori.id.orEmpty()).delete()
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun addProduk(
        produk: Produk,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        isLoading(true)
        produkCollection.document(produk.id.orEmpty())
            .set(produk.apply { stocks = stocks?.map { it.copy(produkId = produk.id) } })
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun listProduk(
        query: String = "",
        refresh: Boolean = false,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: (List<Produk>) -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        isLoading(true)
        var sql: Query = produkCollection

        if (query.isNotEmpty()) sql =
            sql.orderBy("nama").startAt(query.lowercase()).endAt(query.lowercase() + "\uf8ff")
        else sql = sql.orderBy("createdAt", Query.Direction.DESCENDING)
        sql.get(if (refresh) Source.DEFAULT else Source.CACHE)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess(it.result.toObjects(Produk::class.java))
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun addStock(
        produk: Produk, stock: Stock,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        isLoading(true)
        produkCollection.document(produk.id.orEmpty())
            .update("stocks", FieldValue.arrayUnion(stock)).addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun removeStock(
        produk: Produk,
        stock: Stock,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        isLoading(true)
        produkCollection.document(produk.id.orEmpty())
            .update("stocks", FieldValue.arrayRemove(stock)).addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun editProduk(
        produk: Produk,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        isLoading(true)
        produkCollection.document(produk.id.orEmpty())
            .set(produk).addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun searchCustomerDanMobil(
        notelp: String,
        nopol: String,
        isLoading: (Boolean) -> Unit,
        onSuccess: (Customer, Mobil) -> Unit,
        onFailed: (String) -> Unit
    ) {
        isLoading(true)
        customerCollection.whereEqualTo("notelp", notelp).limit(1).get()
            .addOnCompleteListener { taskCust ->
                if (taskCust.isSuccessful) {
                    if (taskCust.result.documents.isNotEmpty()) {
                        val customer = taskCust.result.documents.first()
                        customer!!.reference.collection("mobil").whereEqualTo("nopol", nopol)
                            .limit(1).get().addOnCompleteListener { taskMobil ->
                                isLoading(false)
                                if (taskMobil.isSuccessful) {
                                    if (taskMobil.result.documents.isNotEmpty()) {
                                        val mobil = taskMobil.result.documents.first()
                                        customer.toObject(Customer::class.java)
                                            ?.let { mobil!!.toObject(Mobil::class.java)
                                                ?.let { it1 -> onSuccess(it, it1) } }
                                    } else {
                                        onFailed("Data mobil tidak ditemukan")
                                    }
                                } else {
                                    onFailed(taskMobil.exception?.message.orEmpty())
                                }
                            }
                    } else {
                        isLoading(false)
                        onFailed("Data pelanggan tidak ditemukan")
                    }
                } else {
                    isLoading(false)
                    onFailed(taskCust.exception?.message.orEmpty())
                }
            }
    }

    fun addTransaksi(
        transaksi: Transaksi, isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        transaksiCollection.document(transaksi.id.orEmpty()).set(transaksi)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    runBlocking {
                        transaksi.carts?.filter { it.produk?.tipe == Produk.Tipe.BARANG }?.forEach {
                            addStock(
                                produk = it.produk!!,
                                stock = Stock(
                                    jumlah = -(it.jumlah?.toLong() ?: 0),
                                    deskripsi = "Digunakan untuk transaksi ${transaksi.id}"
                                )
                            )
                        }
                        addMobil(mobil = transaksi.mobil!!.copy(lastTransaksion = Date().time))
                    }
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun getTransaksi(
        query: String,
        refresh: Boolean = false,
        isLoading: (Boolean) -> Unit,
        onSuccess: (List<Transaksi>) -> Unit,
        onFailed: (String) -> Unit
    ) {
        isLoading(true)
        var sql: Query = transaksiCollection

        if (query.isNotEmpty()) sql =
            sql.orderBy("id").startAt(query.lowercase()).endAt(query.lowercase() + "\uf8ff")
        else sql = sql.orderBy("createdAt", Query.Direction.DESCENDING)
        sql.get(if (refresh) Source.DEFAULT else Source.CACHE)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess(it.result.toObjects(Transaksi::class.java))
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun updateTransaksi(
        transaksi: Transaksi?,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        transaksiCollection.document(transaksi?.id.orEmpty()).set(transaksi!!)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }

    fun getPersons(
        refresh: Boolean = false,
        isLoading: (Boolean) -> Unit,
        onSuccess: (List<Person>) -> Unit,
        onFailed: (String) -> Unit
    ) {
        personCollection.get(if (refresh) Source.DEFAULT else Source.CACHE)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess(it.result.toObjects(Person::class.java))
                } else {
                    onFailed(it.exception?.message.orEmpty())
                }
            }
    }
}