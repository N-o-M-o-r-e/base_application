package com.github.nomore.base

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import java.io.Serializable

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseActivity<Binding : ViewBinding>(private val inflate: Inflate<Binding>) :
    BaseView() {
    protected lateinit var binding: Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bindView() {
        binding = inflate(LayoutInflater.from(this), null, false)
        val mView = binding.root
        setContentView(mView)
        actionWindow()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun actionWindow() {
        // Đặt màu đen cho status bar và navigation bar
        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            @Suppress("DEPRECATION") window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
        }
    }

    /**
     * START ACTIVITY WITH RESULT
     */
    private val launcherStartActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val callback = callbackActivityResult
            callback?.invoke(it)
            callbackActivityResult = null
        }

    private var callbackActivityResult: ((result: ActivityResult?) -> Unit)? = null

    fun startActivityWithResult(
        intent: Intent, callback: (result: ActivityResult?) -> Unit
    ) {
        kotlin.runCatching {
            callbackActivityResult = callback
            launcherStartActivityResult.launch(intent)
        }.getOrElse {
            callback(null)
        }
    }/*
    * START ACTIVITY WITH RESULT
    * */

    /*
    * REQUEST PERMISSION
    * */
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        permissionCallback?.invoke(results.takeIf { it.isNotEmpty() }?.all { it.value } ?: false)
    }

    private var permissionCallback: ((granted: Boolean) -> Unit)? = null

    fun startRequestPermissions(
        permissions: Array<String>, callback: (granted: Boolean) -> Unit
    ) {
        runCatching {
            permissionCallback = callback
            requestPermissionsLauncher.launch(permissions)
        }.getOrElse {
            it.printStackTrace()
            callback(false)
        }
    }

}

@Suppress("DEPRECATION")
abstract class BaseView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        initAds()
        initData()
        initView()
        initAction()
        initViewModel()
    }


    /**
     * Note: Hide both the status bar and the navigation bar
     */


    protected abstract fun bindView()
    protected abstract fun initAds()
    protected abstract fun initViewModel()
    protected abstract fun initData()
    protected abstract fun initView()
    protected abstract fun initAction()

    /**
     * Note: action user focus view
     */

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }
            }
        }

        return super.dispatchTouchEvent(motionEvent)
    }


    protected fun goToNewActivity(
        activity: Class<*>,
        isFinish: Boolean = false,
        key: String = "",
        data: Any? = null,
        flags: Int? = null // Truyền các flag nếu cần thiết (ví dụ : flagClearOldTask)
    ) {
        val intent = Intent(this, activity).apply {
            // Truyền dữ liệu với key
            if (key.isNotEmpty() && data != null) {
                when (data) {
                    is Parcelable -> putExtra(key, data)
                    is Serializable -> putExtra(key, data)
                    else -> throw IllegalArgumentException("Data must be Parcelable or Serializable")
                }
            }

            // Thêm các flags nếu có
            flags?.let {
                addFlags(it)
            }
        }
        startActivity(intent)

        if (isFinish) {
            finish()
        }

    }

    protected inline fun <reified T> getIntentData(key: String, default: T? = null): T? {
        val data = intent?.extras?.get(key)
        return when {
            data is T -> data
            default != null -> default
            else -> null
        }
    }


    class FadePageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val pageWidth = page.width
            if (position < -1) {
                page.alpha = 0f
            } else if (position <= 0) {
                page.alpha = 1f
                page.translationX = 0f
                page.scaleX = 1f
                page.scaleY = 1f
            } else if (position <= 1) {
                page.alpha = 1 - position
                page.translationX = pageWidth * -position
            } else {
                page.alpha = 0f
            }
        }
    }
}