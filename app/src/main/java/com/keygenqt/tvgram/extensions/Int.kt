package com.keygenqt.tvgram.extensions

import android.text.format.DateFormat
import android.widget.ImageView
import java.util.*

/**
 * Timestamp to date
 */
fun Int.toDate(): String {
    val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = this * 1000L
    return DateFormat.format("dd MMMM yyyy", calendar).toString()
}