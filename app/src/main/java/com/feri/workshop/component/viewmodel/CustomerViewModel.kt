package com.feri.workshop.component.viewmodel

import androidx.lifecycle.ViewModel
import com.feri.workshop.repository.WorkshopRepository
import com.feri.workshop.repository.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: WorkshopRepository) :
    ViewModel() {
    fun addCustomer(
        customer: Customer,
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        repository.addCustomer(
            customer = customer,
            onSuccess = onSuccess,
            onFailed = onFailed,
            isLoading = isLoading
        )
    }
}