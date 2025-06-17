package com.github.nomore.base.utils

import android.util.Log
import com.github.nomore.base.BuildConfig

/**
 * Enhanced Logging Extensions
 */

/**
 * Show Log with enhanced formatting
 */
fun logI(tag: String, data: String) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, "ℹ️: $data")
    }
}

fun logD(tag: String, data: String) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, "✅: $data")
    }
}

fun logV(tag: String, data: String) {
    if (BuildConfig.DEBUG) {
        Log.v(tag, "🔍: $data")
    }
}

fun logW(tag: String, data: String) {
    if (BuildConfig.DEBUG) {
        Log.w(tag, "⚠️: $data")
    }
}

fun logE(tag: String, data: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, "❌: $data")
    }
}

fun logWTF(tag: String, data: String) {
    if (BuildConfig.DEBUG) {
        Log.wtf(tag, "💥: $data")
    }
}

/**
 * Enhanced logging with throwable support
 */
fun logE(tag: String, data: String, throwable: Throwable?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, "❌: $data", throwable)
    }
}

fun logW(tag: String, data: String, throwable: Throwable?) {
    if (BuildConfig.DEBUG) {
        Log.w(tag, "⚠️: $data", throwable)
    }
}

/**
 * Logging with automatic tag based on class name
 */
inline fun <reified T> T.logI(data: String) {
    logI(T::class.java.simpleName, data)
}

inline fun <reified T> T.logD(data: String) {
    logD(T::class.java.simpleName, data)
}

inline fun <reified T> T.logV(data: String) {
    logV(T::class.java.simpleName, data)
}

inline fun <reified T> T.logW(data: String) {
    logW(T::class.java.simpleName, data)
}

inline fun <reified T> T.logE(data: String) {
    logE(T::class.java.simpleName, data)
}

inline fun <reified T> T.logE(data: String, throwable: Throwable?) {
    logE(T::class.java.simpleName, data, throwable)
}

/**
 * Performance logging
 */
inline fun <T> measureTimeAndLog(tag: String, operation: String, block: () -> T): T {
    val startTime = System.currentTimeMillis()
    val result = block()
    val endTime = System.currentTimeMillis()
    logD(tag, "$operation took ${endTime - startTime}ms")
    return result
}