package com.feri.workshop.component.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.utils.toFormattedString
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RentangTanggalViewModel @Inject constructor() :ViewModel(){
    val startDate= mutableStateOf(Date())
    val endDate= mutableStateOf(Date())
    val onFilter= mutableStateOf(false)

    init {
        reset()
    }

    fun reset() {
        onFilter.value=false
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        startDate.value=cal.time
        cal.set(Calendar.HOUR, 23)
        cal.set(Calendar.MINUTE, 59)
        endDate.value=cal.time
    }

}