package com.feri.workshop

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.repository.WorkshopRepository
import com.feri.workshop.repository.model.Customer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: WorkshopRepository) : ViewModel() {
    val currentAccount = mutableStateOf(Firebase.auth.currentUser)

    init {
        checkAccount()
//        repository.addCustomer(customer = Customer(
//            nama = "Mbak sri",
//            notelp = "089768686867",
//            alamat = "yosowilangun",
//            keterangan = "gada",
//            mobil = listOf(Customer.Mobil(nopol = "N 1234 Z",merk = "vario",tipe = Customer.TipeMobil.automatic))
//        ), onSuccess = {}, onFailed = {})
    }

    fun checkAccount() {
        Firebase.auth.addAuthStateListener {
            currentAccount.value = it.currentUser
        }
    }

    fun logOut() {
        Firebase.auth.signOut()
    }

}
