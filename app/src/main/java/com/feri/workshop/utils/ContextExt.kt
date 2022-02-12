package com.feri.workshop.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String, long: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, long).show()
}