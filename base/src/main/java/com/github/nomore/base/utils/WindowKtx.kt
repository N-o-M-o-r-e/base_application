package com.github.nomore.base.utils

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.github.nomore.base.R

/**
 * System Bars Mode Configuration
 */
sealed class SystemBarsMode {
    data object Coating : SystemBarsMode()  // Hiển thị Status Bar và Navigation Bar đè lên toàn bộ View (nội dung trải dài, không padding)
    data object Distinct : SystemBarsMode()  // Hiển thị Status Bar và Navigation Bar, có padding, có thể tùy chỉnh màu trong theme
    data object Immersive : SystemBarsMode()  // Ẩn hoàn toàn Status Bar và Navigation Bar (chế độ toàn màn hình)
}

/**
 * Status Bar State Configuration
 */
sealed class StatusBarState {
    data object Light : StatusBarState() // Icon sáng, phù hợp với nền tối
    data object Dark : StatusBarState() // Icon tối, phù hợp với nền sáng
}

/**
 * Configure Window View with System Bars and Status Bar
 */
fun Activity.setWindowView(
    view: View,
    actionSystemBars: SystemBarsMode,
    statusBarState: StatusBarState? = null
) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        WindowInsetsCompat.CONSUMED
        when (actionSystemBars) {
            SystemBarsMode.Coating -> {
                v.setPadding(0, 0, 0, 0)
            }

            SystemBarsMode.Distinct -> {
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                // Thiết lập status bar dựa trên statusBarState
                statusBarState?.let { state ->
                    when (state) {
                        StatusBarState.Light -> { // trạng thái statusBar tối - icon sáng
                            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
                            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
                        }

                        StatusBarState.Dark -> { // trạng thái statusBar sáng - icon tối
                            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
                            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
                        }
                    }
                } ?: run {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.black)
                    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
                }
            }

            SystemBarsMode.Immersive -> {
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                v.setPadding(0, 0, 0, 0)
            }
        }
        WindowInsetsCompat.CONSUMED
    }
}

/**
 * Configure Window View with custom colors
 */
fun Activity.setWindowView(
    view: View,
    actionSystemBars: SystemBarsMode,
    statusBarColor: Int? = null,
    isLightStatusBar: Boolean = false
) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        when (actionSystemBars) {
            SystemBarsMode.Coating -> {
                v.setPadding(0, 0, 0, 0)
            }

            SystemBarsMode.Distinct -> {
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                // Thiết lập status bar với màu custom
                statusBarColor?.let { color ->
                    window.statusBarColor = color
                    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = isLightStatusBar
                } ?: run {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.black)
                    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
                }
            }

            SystemBarsMode.Immersive -> {
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                v.setPadding(0, 0, 0, 0)
            }
        }
        WindowInsetsCompat.CONSUMED
    }
}

/**
 * Quick setup methods for common configurations
 */
fun Activity.setupEdgeToEdgeWithDistinctBars(
    view: View,
    statusBarState: StatusBarState = StatusBarState.Dark
) {

    setWindowView(view, SystemBarsMode.Distinct, statusBarState)
}

fun Activity.setupEdgeToEdgeWithCoatingBars(view: View) {
    setWindowView(view, SystemBarsMode.Coating)
}

fun Activity.setupFullScreenImmersive(view: View) {
    setWindowView(view, SystemBarsMode.Immersive)
}

/**
 * Setup with custom status bar color
 */
fun Activity.setupEdgeToEdgeWithCustomColor(
    view: View,
    statusBarColor: Int,
    isLightStatusBar: Boolean = false
) {
    setWindowView(view, SystemBarsMode.Distinct, statusBarColor, isLightStatusBar)
}