package com.feri.workshop

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val list= arrayListOf<String>()
    fun getString() = list
    fun addString(string: String) = list.add(string)

}
