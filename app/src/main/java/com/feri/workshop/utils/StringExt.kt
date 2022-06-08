package com.feri.workshop.utils

import android.util.Patterns
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import java.text.SimpleDateFormat
import java.util.*


fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.toFormattedDate(
    inputPattern: String = "yyyy-MM-dd HH:mm:ss",
    outputPattern: String = "dd MMM yyyy • HH:mm"
): String {
    if (this.isNullOrEmpty()) {
        return ""
    }

    val inputDateFormatter = SimpleDateFormat(inputPattern, Locale.getDefault())
    val outputDateFormatter = SimpleDateFormat(outputPattern, Locale.getDefault())

    return try {
        val date = inputDateFormatter.parse(this)

        if (date != null) {
//            DateUtils.getRelativeTimeSpanString(
//                date.time,
//                Calendar.getInstance().timeInMillis,
//                DateUtils.MINUTE_IN_MILLIS
//            ).toString()
            outputDateFormatter.format(date)
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}

fun String.capitalizeWords() = split(' ').joinToString(" ", transform = {
    it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
})

fun String?.toDateorNull(
    inputPattern: String = "yyyy-MM-dd HH:mm:ss",
): Date? {
    if (this.isNullOrEmpty()) {
        return null
    }

    val inputDateFormatter = SimpleDateFormat(inputPattern, Locale.getDefault())

    return try {
        inputDateFormatter.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String?.toColor():Color{
    return Color(android.graphics.Color.parseColor(this))
}

fun String?.removedat():String{
    this?.let {
        return if (it.contains("@")){
            it.split("@")[0]
        }else{
            it
        }
    }?:
    return ""
}