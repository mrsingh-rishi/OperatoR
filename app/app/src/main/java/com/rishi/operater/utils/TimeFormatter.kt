package com.rishi.operater.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeFormatter {
    private val format = SimpleDateFormat("HH:mm:ss", Locale.US)

    fun now(): String = format.format(Date())
}
