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

fun Date.isDateInCurrentWeek(): Boolean {
    val currentCalendar = Calendar.getInstance()
    val week = currentCalendar[Calendar.WEEK_OF_YEAR]
    val year = currentCalendar[Calendar.YEAR]
    val targetCalendar = Calendar.getInstance()
    targetCalendar.time = this
    val targetWeek = targetCalendar[Calendar.WEEK_OF_YEAR]
    val targetYear = targetCalendar[Calendar.YEAR]
    return week == targetWeek && year == targetYear
}

fun Date.isDateInCurrentMonth(): Boolean {
    val currentCalendar = Calendar.getInstance()
    val month = currentCalendar[Calendar.MONTH]
    val year = currentCalendar[Calendar.YEAR]
    val targetCalendar = Calendar.getInstance()
    targetCalendar.time = this
    val targetMonth = targetCalendar[Calendar.MONTH]
    val targetYear = targetCalendar[Calendar.YEAR]
    return month == targetMonth && year == targetYear
}