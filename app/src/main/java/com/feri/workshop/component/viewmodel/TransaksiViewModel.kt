package com.feri.workshop.component.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.data.WorkshopRepository
import com.feri.workshop.data.model.*
import com.feri.workshop.utils.toFormattedString
import com.feri.workshop.utils.toRupiahCurrency
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TransaksiViewModel @Inject constructor(val repository: WorkshopRepository) : ViewModel() {
    //create transaksi
    val selectedCustomer = mutableStateOf<Customer?>(null)
    val selectedMobil = mutableStateOf<Mobil?>(null)
    val selectedProduk = mutableStateOf<ArrayList<Transaksi.Cart>>(ArrayList())
    val othersPayment = mutableStateOf<ArrayList<Transaksi.OtherPayment>>(ArrayList())
    val selectedMetodePembayaran = mutableStateOf("")
    val keterangan = mutableStateOf("")
    val enablePPn = mutableStateOf(true)
    val enableDiskon = mutableStateOf(true)
    val ppnValue = mutableStateOf(10)
    val diskonValue = mutableStateOf(0.0)
    val paidValue = mutableStateOf(0)
    val newKM = mutableStateOf(0)
    val oldKM = mutableStateOf(0)
    val createAt = mutableStateOf(0L)
    val selectedStatus= mutableStateOf(Transaksi.STATUS.Menunggu)
    val selectedType= mutableStateOf("Hari ini")

    //list transaksi
    val listTransaksi = mutableStateOf<List<Transaksi>>(emptyList())
    val isLoading = mutableStateOf(true)

    //detail transaksi
    val selectedTransaksi = mutableStateOf<Transaksi?>(null)

    init {
        getTransaksi(refresh = true)
    }

    fun getTransaksi(
        query: String = "",
        refresh: Boolean = false,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: (List<Transaksi>) -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        repository.getTransaksi(
            query = query,
            refresh = refresh,
            isLoading = {
                this.isLoading.value = it
                isLoading(it)
            },
            onSuccess = {
                listTransaksi.value = it
                onSuccess(it)
            },
            onFailed = onFailed
        )
    }

    fun searchCustomer(
        notelp: String,
        nopol: String,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.searchCustomerDanMobil(
            notelp = notelp,
            nopol = nopol,
            isLoading = isLoading,
            onSuccess = { customer, mobil ->
                setCustomerdanMobil(customer, mobil)
                onSuccess()
            },
            onFailed = onFailed
        )
    }

    fun setCustomerdanMobil(customer: Customer?, mobil: Mobil?) {
        selectedCustomer.value = customer
        selectedMobil.value = mobil
        oldKM.value = mobil?.newKM ?: 0
    }

    fun reset() {
        selectedCustomer.value = null
        selectedMobil.value = null
        selectedProduk.value = ArrayList()
        othersPayment.value = ArrayList()
        selectedMetodePembayaran.value = ""
        keterangan.value = ""
        enablePPn.value = true
        enableDiskon.value = true
        ppnValue.value = 10
        diskonValue.value = 0.0
        paidValue.value = 0
        oldKM.value = 0
        newKM.value = 0
        createAt.value = Calendar.getInstance().timeInMillis
    }

    fun addProduk(
        jumlah: Int = 1,
        produk: Produk,
        kategori: Produk.Kategori,
        onError: (String) -> Unit = {}
    ) {
        val index =
            selectedProduk.value.indexOfFirst {
                println(kategori.nama +" "+ it.kategori?.nama)
                it.produk?.id == produk.id && it.kategori?.id == kategori.id
            }
        val arrayList = ArrayList(selectedProduk.value)
        if (jumlah > produk.getJumlahStock() && produk.tipe == Produk.Tipe.BARANG) {
            onError("jumlah melebihi batas stocks")
            return
        }
        if (jumlah > 0) {
            if (index ?: 0 >= 0) {
                arrayList.set(index, Transaksi.Cart(jumlah, produk, kategori))
            } else {
                arrayList.add(Transaksi.Cart(1, produk, kategori))
            }
        } else {
            arrayList.removeAt(index ?: 0)
        }
        selectedProduk.value = arrayList
    }

    fun addBiayaLainnya(nama: String, nilai: Double) {
        val array = ArrayList(othersPayment.value)
        array.add(Transaksi.OtherPayment(nama, nilai))
        othersPayment.value = array
    }

    fun removeBiayaLainnya(index: Int) {
        val array = ArrayList(othersPayment.value)
        array.removeAt(index)
        othersPayment.value = array
    }

    fun createTransaksi(
        isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        var transaksi = Transaksi(
            id = "TRX" + selectedMobil.value?.nopol?.replace(
                " ",
                ""
            ) + Date().toFormattedString("yyMMddHHmm"),
            mobil = selectedMobil.value?.copy(
                oldKM = oldKM.value,
                newKM = if (newKM.value <= oldKM.value) oldKM.value else newKM.value
            ),
            customer = selectedCustomer.value,
            carts = selectedProduk.value,
            enablePPn = enablePPn.value,
            ppnValue = ppnValue.value,
            enableDiskon = enableDiskon.value,
            diskonValue = diskonValue.value,
            otherPayment = othersPayment.value,
            keterangan = keterangan.value,
            metodePembayaran = selectedMetodePembayaran.value,
            paidValue = paidValue.value.toDouble(),
            createdBy = Firebase.auth.currentUser?.email,
            createdAt = createAt.value
        )
        val updateInfo =
            listOf(Date().toFormattedString("yyyy/MM/dd") + " " + transaksi.paidValue.toRupiahCurrency() + " (" + Firebase.auth.currentUser?.email + ")")
        if (transaksi.metodePembayaran == "Sebagian") {
            transaksi = transaksi.copy(updateInfo = updateInfo)
        }
        repository.addTransaksi(
            transaksi = transaksi,
            isLoading = {
                isLoading(it)
            },
            onSuccess = {
                selectedTransaksi.value = transaksi
                getTransaksi()
                onSuccess()
                reset()
            },
            onFailed = {
                onFailed(it)
            })
    }

    fun updateTransaksi(
        transaksi: Transaksi?,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        repository.updateTransaksi(
            transaksi = transaksi,
            isLoading = isLoading,
            onSuccess = {
                onSuccess()
                selectedTransaksi.value = transaksi
                getTransaksi()
            },
            onFailed = onFailed
        )
    }
}