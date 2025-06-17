package com.github.nomore.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import java.io.Serializable

// ==================== Activity Result Extensions ====================

/**
 * Create Activity Result Launcher with callbacks
 */
fun AppCompatActivity.createActivityResultLauncher(
    onSuccess: (Intent?) -> Unit = {},
    onCancelled: () -> Unit = {},
    onError: (Int, Intent?) -> Unit = { _, _ -> }
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> onSuccess(result.data)
            Activity.RESULT_CANCELED -> onCancelled()
            else -> onError(result.resultCode, result.data)
        }
    }
}

/**
 * Helper function để put data vào Intent
 */
fun Intent.putData(key: String, data: Any) {
    when (data) {
        is String -> putExtra(key, data)
        is Int -> putExtra(key, data)
        is Long -> putExtra(key, data)
        is Boolean -> putExtra(key, data)
        is Double -> putExtra(key, data)
        is Float -> putExtra(key, data)
        is Parcelable -> putExtra(key, data)
        is Serializable -> putExtra(key, data)
        else -> throw IllegalArgumentException("Unsupported data type: ${data::class.simpleName}")
    }
}

/**
 * Launch Activity For Result using ActivityResultLauncher
 */
fun ActivityResultLauncher<Intent>.launchActivity(
    context: Context,
    activity: Class<*>,
    key: String = "",
    data: Any? = null,
    flags: Int? = null
) {
    try {
        val intent = Intent(context, activity).apply {
            if (key.isNotEmpty() && data != null) {
                putData(key, data)
            }
            flags?.let { addFlags(it) }
        }
        launch(intent)
    } catch (e: Exception) {
        logE("ActivityExtensions", "Error launching activity for result: ${e.message}")
    }
}

/**
 * Type-safe launch Activity For Result
 */
inline fun <reified T : AppCompatActivity> ActivityResultLauncher<Intent>.launchActivityForResult(
    context: Context,
    key: String = "",
    data: Any? = null,
    flags: Int? = null
) {
    launchActivity(context, T::class.java, key, data, flags)
}

/**
 * Return data to parent Activity
 */
fun Activity.returnData(
    resultCode: Int = Activity.RESULT_OK,
    keyReturn: String,
    dataReturn: Any
) {
    require(keyReturn.isNotEmpty()) { "Key cannot be empty" }

    try {
        val resultIntent = Intent().apply {
            putData(keyReturn, dataReturn)
        }
        setResult(resultCode, resultIntent)
        finish()
    } catch (e: Exception) {
        logE("ActivityExtensions", "Error returning data: ${e.message}")
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}

/**
 * Return multiple data to parent Activity
 */
fun Activity.returnMultipleData(
    resultCode: Int = Activity.RESULT_OK,
    dataMap: Map<String, Any>
) {
    require(dataMap.isNotEmpty()) { "Data map cannot be empty" }

    try {
        val resultIntent = Intent().apply {
            dataMap.forEach { (key, value) ->
                putData(key, value)
            }
        }
        setResult(resultCode, resultIntent)
        finish()
    } catch (e: Exception) {
        logE("ActivityExtensions", "Error returning multiple data: ${e.message}")
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}

/**
 * Cancel and return to parent Activity
 */
fun Activity.returnCancelled() {
    setResult(Activity.RESULT_CANCELED)
    finish()
}

// ==================== Activity Navigation ====================

/**
 * Start Activity (Enhanced Version)
 */
fun Activity.goToNewActivity(
    activity: Class<*>,
    isFinish: Boolean = false,
    key: String = "",
    data: Any? = null,
    flags: Int? = null
) {
    val intent = Intent(this, activity).apply {
        if (key.isNotEmpty() && data != null) {
            putExtraData(key, data)
        }
        flags?.let { addFlags(it) }
    }
    startActivity(intent)

    if (isFinish) {
        finish()
    }
}

/**
 * Type-safe Activity navigation
 */
inline fun <reified T : Activity> Activity.goToActivity(
    isFinish: Boolean = false,
    key: String = "",
    data: Any? = null,
    flags: Int? = null
) {
    goToNewActivity(T::class.java, isFinish, key, data, flags)
}

// ==================== Intent Data Handling ====================

/**
 * Enhanced putExtra function supporting more data types
 */
fun Intent.putExtraData(key: String, data: Any) {
    when (data) {
        is String -> putExtra(key, data)
        is Int -> putExtra(key, data)
        is Long -> putExtra(key, data)
        is Boolean -> putExtra(key, data)
        is Double -> putExtra(key, data)
        is Float -> putExtra(key, data)
        is Char -> putExtra(key, data)
        is Byte -> putExtra(key, data)
        is Short -> putExtra(key, data)
        is Parcelable -> putExtra(key, data)
        is Serializable -> putExtra(key, data)
        is IntArray -> putExtra(key, data)
        is LongArray -> putExtra(key, data)
        is BooleanArray -> putExtra(key, data)
        is FloatArray -> putExtra(key, data)
        is DoubleArray -> putExtra(key, data)
        is Array<*> -> handleArrayData(key, data)
        is ArrayList<*> -> handleArrayListData(key, data)
        else -> throw IllegalArgumentException("Unsupported data type: ${data::class.simpleName}")
    }
}

/**
 * Handle Array data types safely
 */
@Suppress("UNCHECKED_CAST")
private fun Intent.handleArrayData(key: String, data: Array<*>) {
    when {
        data.isArrayOf<String>() -> {
            putExtra(key, data as Array<String>)
        }
        data.isArrayOf<Parcelable>() -> {
            putExtra(key, data as Array<Parcelable>)
        }
        else -> throw IllegalArgumentException("Unsupported array type: ${data::class.simpleName}")
    }
}

/**
 * Handle ArrayList data types safely
 */
@Suppress("UNCHECKED_CAST")
private fun Intent.handleArrayListData(key: String, data: ArrayList<*>) {
    when {
        data.isEmpty() -> putStringArrayListExtra(key, arrayListOf())
        data.all { it is String } -> {
            putStringArrayListExtra(key, data as ArrayList<String>)
        }
        data.all { it is Int } -> {
            putIntegerArrayListExtra(key, data as ArrayList<Int>)
        }
        data.all { it is Parcelable } -> {
            putParcelableArrayListExtra(key, data as ArrayList<Parcelable>)
        }
        else -> throw IllegalArgumentException("Mixed or unsupported ArrayList type")
    }
}

/**
 * Get Intent Data (Enhanced Version with better type safety)
 */
inline fun <reified T> Activity.getIntentData(key: String, default: T? = null): T? {
    return try {
        when (T::class) {
            String::class -> intent.getStringExtra(key) as? T ?: default
            Int::class -> intent.getIntExtra(key, (default as? Int) ?: 0) as? T ?: default
            Long::class -> intent.getLongExtra(key, (default as? Long) ?: 0L) as? T ?: default
            Boolean::class -> intent.getBooleanExtra(key, (default as? Boolean) ?: false) as? T ?: default
            Double::class -> intent.getDoubleExtra(key, (default as? Double) ?: 0.0) as? T ?: default
            Float::class -> intent.getFloatExtra(key, (default as? Float) ?: 0f) as? T ?: default
            Char::class -> intent.getCharExtra(key, (default as? Char) ?: '\u0000') as? T ?: default
            else -> {
                // Handle Parcelable and Serializable
                val data = intent?.extras?.get(key)
                when {
                    data is T -> data
                    default != null -> default
                    else -> null
                }
            }
        }
    } catch (e: Exception) {
        logE("ActivityExtensions", "Error getting intent data: ${e.message}")
        default
    }
}

/**
 * Enhanced getIntentData for Parcelable with API 33+ support
 */
inline fun <reified T : Parcelable> Activity.getIntentParcelable(key: String): T? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<T>(key)
        }
    } catch (e: Exception) {
        logE("ActivityExtensions", "Error getting parcelable data: ${e.message}")
        null
    }
}

/**
 * Enhanced getIntentData for Serializable with API 33+ support
 */
inline fun <reified T : Serializable> Activity.getIntentSerializable(key: String): T? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(key) as? T
        }
    } catch (e: Exception) {
        logE("ActivityExtensions", "Error getting serializable data: ${e.message}")
        null
    }
}

// ==================== Lifecycle Extensions ====================

/**
 * onBackPressed()
 */
fun ComponentActivity.onBackPressedDispatcherCallback(
    lifecycleOwner: LifecycleOwner = this,
    enabled: Boolean = true,
    actionOnBackPressed: () -> Unit
) {
    val callback = object : OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            actionOnBackPressed()
        }
    }
    onBackPressedDispatcher.addCallback(lifecycleOwner, callback)
}

/**
 * Get Name Activity
 */
fun getActivityName(context: Context): String {
    return context.javaClass.simpleName
}