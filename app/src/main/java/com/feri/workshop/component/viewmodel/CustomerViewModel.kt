package com.feri.workshop.component.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.repository.WorkshopRepository
import com.feri.workshop.repository.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: WorkshopRepository) :
    ViewModel() {
    val customers = mutableStateOf<List<Customer>>(emptyList())

    fun addCustomer(
        customer: Customer,
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        repository.addCustomer(
            customer = customer,
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
        repository.listCustomer(query,onSuccess = {
            customers.value = it
        })
    }



    fun reset() {
        customers.value = emptyList()
    }
}