package com.github.nomore.base.utils

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun getSimpleDateTime(): String {
    val pattern = "dd/MM/yyyy HH:mm:ss"
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
    } else {
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
    }
}

// Kiểm tra có network connection không (cần thêm permission ACCESS_NETWORK_STATE)
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET)
}

// Lấy màu từ resources (tương thích với theme)
fun Context.getIdResColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

// Lấy string từ resources
fun Context.getIdResStringCompat(@StringRes stringRes: Int): String {
    return ContextCompat.getString(this, stringRes)
}
