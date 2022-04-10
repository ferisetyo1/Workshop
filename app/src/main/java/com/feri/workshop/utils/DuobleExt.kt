package com.feri.workshop.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun Double?.toRupiahCurrency(): String {
    return NumberFormat
        .getCurrencyInstance(Locale("in", "ID")).apply {
            maximumFractionDigits = 0
        }
        .format(this?:0.0)
}

fun Double?.toCurrency(): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
        maximumFractionDigits = 0
    }
    val decimalFormat = (nf as DecimalFormat).decimalFormatSymbols
    decimalFormat.currencySymbol = ""
    nf.decimalFormatSymbols = decimalFormat
    return nf.format(this?:0.0)
}

fun Double.prettyNum(): String {
    val f = DecimalFormat("#.##")
    return if (Math.abs(this / 1000000) >= 1) {
        f.format(this / 1000000).toString()
    } else if (Math.abs(this / 1000) >= 1) {
        f.format(this / 1000).toString()
    } else {
        f.format(this).toString()
    }
}