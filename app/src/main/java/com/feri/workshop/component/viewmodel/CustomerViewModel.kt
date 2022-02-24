package com.feri.workshop.component.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.repository.WorkshopRepository
import com.feri.workshop.repository.model.Customer
import com.feri.workshop.repository.model.Mobil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: WorkshopRepository) :
    ViewModel() {
    val selectedCustomer = mutableStateOf<Customer?>(null)
    val customers = mutableStateOf<List<Customer>>(emptyList())

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

    fun listCustomer(query: String = "") {
        repository.listCustomer(query, onSuccess = {
            customers.value = it
        })
    }


    fun reset() {
        customers.value = emptyList()
    }

    fun getMobil(
        customerid: String?,
        onSuccess: (List<Mobil>) -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.listMobil(
            customerId = customerid.orEmpty(),
            onSuccess = onSuccess,
            onFailed = onFailed
        )
    }
}