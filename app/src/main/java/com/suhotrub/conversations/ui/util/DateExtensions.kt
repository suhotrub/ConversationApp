package com.suhotrub.conversations.ui.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.toISOString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    return sdf.format(this)
}

fun String.parseISOString(): Date {
    try {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:s'Z'").parse(this)
    } catch (ignored: Throwable) {
    }
    try {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(this)
    } catch (ignored: Throwable) {
    }
    try {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").parse(this)
    } catch (ignored: Throwable) {
    }
    try {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssss'Z'").parse(this)
    } catch (ignored: Throwable) {
    }
    return Date()
}

fun String.toHHmm() = SimpleDateFormat("HH:mm").format(parseISOString())
