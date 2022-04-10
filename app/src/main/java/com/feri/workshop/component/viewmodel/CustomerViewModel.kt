package com.feri.workshop.component.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.data.WorkshopRepository
import com.feri.workshop.data.model.Customer
import com.feri.workshop.data.model.Mobil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: WorkshopRepository) :
    ViewModel() {
    val selectedCustomer = mutableStateOf<Customer?>(null)
    val selectedMobil = mutableStateOf<Mobil?>(null)
    val customers = mutableStateOf<List<Customer>>(emptyList())
    val mobils = mutableStateOf<List<Mobil>>(emptyList())
    val isLoadingList = mutableStateOf(false)

    init {
        listCustomer(refresh = true)
    }

    fun addCustomer(
        customer: Customer,
        mobil: Mobil,
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        repository.addCustomer(
            customer = customer,
            mobil = mobil,
            onSuccess = {
                onSuccess()
                reset()
                listCustomer()
            },
            onFailed = onFailed,
            isLoading = isLoading
        )
    }

    fun listCustomer(query: String = "",refresh:Boolean=false) {
        repository.listCustomer(query = query,refresh = refresh, onSuccess = {
            customers.value = it
        }, isLoading = { isLoadingList.value = it })
    }


    fun reset() {
        customers.value = emptyList()
    }

    fun getMobil(customerid: String?, isLoading: (Boolean) -> Unit = {}) {
        repository.listMobil(
            customerId = customerid.orEmpty(),
            onSuccess = {
                mobils.value = it
            },
            isLoading = isLoading
        )
    }

    fun addMobil(
        mobil: Mobil,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.addMobil(
            mobil = mobil.copy(customerid = selectedCustomer.value?.id),
            isLoading = isLoading,
            onSuccess = {
                getMobil(selectedCustomer.value?.id)
                onSuccess()
            },
            onFailed = onFailed
        )
    }

    fun updateCustomer(
        customer: Customer,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.updateCustomer(
            customer = customer,
            onSuccess = {
                selectedCustomer.value = customer
                onSuccess()
                reset()
                listCustomer()
            },
            onFailed = onFailed,
            isLoading = isLoading
        )
    }

    fun deleteCustomer(
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
        isLoading: (Boolean) -> Unit
    ) {
        selectedCustomer.value?.let {
            repository.deleteCustomer(
                customer = it,
                onSuccess = {
                    onSuccess()
                    reset()
                    listCustomer()
                },
                onFailed = onFailed,
                isLoading = isLoading
            )
        }
    }

    fun deleteMobil(
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
        isLoading: (Boolean) -> Unit
    ) {
        selectedMobil.value?.let {
            repository.deleteMobil(
                mobil = it,
                onSuccess = {
                    getMobil(selectedCustomer.value?.id)
                    onSuccess()
                },
                onFailed = onFailed,
                isLoading = isLoading
            )
        }
    }

}