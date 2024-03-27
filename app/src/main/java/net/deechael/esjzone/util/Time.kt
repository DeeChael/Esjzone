package net.deechael.esjzone.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

fun currentDateString(): String {
    return FORMAT.format(Date())
}

fun String.formattedDate(): Date {
    return FORMAT.parse(this)!!
}