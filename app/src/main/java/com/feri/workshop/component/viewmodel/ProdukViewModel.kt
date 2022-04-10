package com.feri.workshop.component.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.data.WorkshopRepository
import com.feri.workshop.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProdukViewModel @Inject constructor(private val repository: WorkshopRepository) :
    ViewModel() {

    var selectedproduct = mutableStateOf<Produk?>(null)
    val kategoriProduk = mutableStateOf<List<KategoriProduk>>(emptyList())
    val loadingListProduk = mutableStateOf(false)
    val listProduk = mutableStateOf<List<Produk>>(emptyList())

    init {
        getKategori(refresh = true)
        listProduk(refresh = true)
    }

    fun getKategori(isLoading: (Boolean) -> Unit = {}, refresh: Boolean = false) {
        repository.listKategoriProduk(
            refresh = refresh,
            onSuccess = {
                kategoriProduk.value = it
            },
            isLoading = isLoading
        )
    }

    fun addKategori(
        kategori: KategoriProduk,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.addKategoriProduk(
            kategori = kategori,
            isLoading = isLoading,
            onSuccess = {
                onSuccess()
                getKategori()
            },
            onFailed = onFailed
        )
    }

    fun deleteKategori(
        kategori: KategoriProduk,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.deleteKategori(
            kategori = kategori,
            isLoading = isLoading,
            onSuccess = {
                onSuccess()
                kategoriProduk.value = ArrayList(kategoriProduk.value).filter { it.id != it.id }
                getKategori()
            },
            onFailed = onFailed
        )
    }

    fun addProduk(
        produk: Produk,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.addProduk(
            produk = produk,
            isLoading = isLoading,
            onSuccess = {
                onSuccess()
                listProduk()
            },
            onFailed = onFailed
        )
    }

    fun listProduk(
        query: String = "",
        refresh: Boolean = false,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        listProduk.value = emptyList()
        repository.listProduk(
            query = query,
            refresh = refresh,
            isLoading = {
                isLoading(it)
                loadingListProduk.value = it
            },
            onSuccess = {
                onSuccess()
                listProduk.value = it
            },
            onFailed = onFailed
        )
    }

    fun addStock(
        stock: Stock,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        repository.addStock(
            selectedproduct.value!!,
            stock = stock,
            isLoading = isLoading,
            onSuccess = {
                onSuccess()
                val stocks = ArrayList(selectedproduct.value?.stocks.orEmpty())
                stocks.add(stock)
                selectedproduct.value = selectedproduct.value?.copy(stocks = stocks)
                listProduk()
            }, onFailed = onFailed
        )
    }

    fun removeStock(
        stock: Stock, isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        repository.removeStock(
            selectedproduct.value!!,
            stock,
            isLoading = isLoading,
            onSuccess = {
                onSuccess()
                val stocks = ArrayList(selectedproduct.value?.stocks.orEmpty())
                stocks.remove(stock)
                selectedproduct.value = selectedproduct.value?.copy(stocks = stocks)
                listProduk()
            }, onFailed = onFailed)
    }

    fun editProduk(
        produk: Produk,
        isLoading: (Boolean) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        repository.editProduk(
            produk,
            isLoading = isLoading,
            onSuccess = {
                onSuccess()
                selectedproduct.value = produk
                listProduk()
            }, onFailed = onFailed
        )
    }
}