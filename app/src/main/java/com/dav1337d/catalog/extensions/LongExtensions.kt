package com.dav1337d.catalog.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.timestampToDate(): String {
    try {
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(this * 1000)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}