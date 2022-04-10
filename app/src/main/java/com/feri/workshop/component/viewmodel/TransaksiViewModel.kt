package com.feri.workshop.component.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.data.WorkshopRepository
import com.feri.workshop.data.model.Customer
import com.feri.workshop.data.model.Mobil
import com.feri.workshop.data.model.Produk
import com.feri.workshop.data.model.Transaksi
import com.feri.workshop.utils.toFormattedString
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
    val ppnValue = mutableStateOf(10)
    val paidValue= mutableStateOf(0)

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
    }

    fun reset() {
        selectedCustomer.value = null
        selectedMobil.value = null
        selectedProduk.value = ArrayList()
        othersPayment.value = ArrayList()
        selectedMetodePembayaran.value = ""
        keterangan.value = ""
        enablePPn.value = true
        ppnValue.value = 10
        paidValue.value=0

    }

    fun addProduk(jumlah: Int = 1, produk: Produk, onError: (String) -> Unit = {}) {
        val index = selectedProduk.value.indexOfFirst { it.produk?.id == produk.id }
        val arrayList = ArrayList(selectedProduk.value ?: emptyList())
        if (jumlah > produk.getJumlahStock() && produk.tipe == Produk.Tipe.BARANG) {
            onError("jumlah melebihi batas stocks")
            return
        }
        if (jumlah > 0) {
            if (index ?: 0 >= 0) {
                arrayList.set(index, Transaksi.Cart(jumlah, produk))
            } else {
                arrayList.add(Transaksi.Cart(1, produk))
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
        val transaksi = Transaksi(
            id = "TRX" + Date().toFormattedString("yyyyMMddHHmmss") + selectedCustomer.value?.notelp.orEmpty(),
            mobil = selectedMobil.value,
            customer = selectedCustomer.value,
            carts = selectedProduk.value,
            enablePPn = enablePPn.value,
            ppnValue = ppnValue.value,
            otherPayment = othersPayment.value,
            keterangan = keterangan.value,
            metodePembayaran = selectedMetodePembayaran.value,
            paidValue = paidValue.value.toDouble(),
        )
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