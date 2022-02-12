package com.feri.workshop

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainViewModel : ViewModel() {
    val currentAccount = mutableStateOf<FirebaseUser?>(null)

    init {
        checkAccount()
    }

    fun checkAccount(){
        currentAccount.value=Firebase.auth.currentUser
    }
}
