package com.feri.workshop.repository

import android.content.Context
import com.feri.workshop.repository.model.Customer
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

@ActivityScoped
class WorkshopRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
) {
    val firestore = Firebase.firestore
    val userCollection = firestore.collection("user")
    val produkCollection = firestore.collection("produk")
    val customerCollection = firestore.collection("customer")

    fun addCustomer(
        customer: Customer,
        onSuccess: () -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        isLoading(true)
        customerCollection.document(customer.id.orEmpty()).set(customer).addOnCompleteListener {
            isLoading(false)
            if (it.isSuccessful) {
                onSuccess()
            } else {
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
        customerCollection.document(customer.id.orEmpty()).set(customer).addOnCompleteListener {
            isLoading(false)
            if (it.isSuccessful) {
                onSuccess()
            } else {
                onFailed(it.exception?.message.orEmpty())
            }
        }
    }

    fun listCustomer(
        query: String = "",
        onSuccess: (List<Customer>) -> Unit = {},
        onFailed: (String) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ) {
        isLoading(true)
        var sql: Query = customerCollection
        if (query.isNotEmpty()) sql = sql.orderBy("nama").startAt(query.lowercase()).endAt(query.lowercase() + "\uf8ff")
        else sql = sql.orderBy("createdAt", Query.Direction.DESCENDING)
        sql.get().addOnCompleteListener {
            isLoading(false)
            if (it.isSuccessful) {
                onSuccess(it.result.toObjects(Customer::class.java))
            } else {
                onFailed(it.exception?.message.orEmpty())
            }
        }
    }

}