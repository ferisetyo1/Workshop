package com.feri.workshop.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toFormattedString(outputPattern: String? = "yyyy-MM-dd'T'HH:mm:ssZ"): String {
    val outputDateFormatter = SimpleDateFormat(outputPattern, Locale.getDefault())

    return try {
        outputDateFormatter.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun getDateNow(outputPattern: String? = "yyyy-MM-dd'T'HH:mm:ss'Z'"): String {
    val c = Calendar.getInstance().time
    val df = SimpleDateFormat(outputPattern, Locale("in", "ID"))
    return df.format(c)
}
