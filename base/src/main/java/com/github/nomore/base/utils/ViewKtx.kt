package com.github.nomore.base.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.nomore.base.R

// ==================== Toast Extensions ====================

/**
 * Show Toast
 */
fun Context.toastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastMessage(resId: Int) {
    Toast.makeText(this, getText(resId), Toast.LENGTH_SHORT).show()
}

fun Context.toastMessageLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastMessageLong(resId: Int) {
    Toast.makeText(this, getText(resId), Toast.LENGTH_LONG).show()
}

// ==================== View Visibility Extensions ====================

/**
 * Enhanced visibility extensions
 */
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.showIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

fun View.hideIf(condition: Boolean) {
    visibility = if (condition) View.GONE else View.VISIBLE
}

// ==================== Click Listeners Extensions ====================

/**
 * Enhanced anti-spam click listener with customizable delay
 */
fun View.setOnBlockSpamClickListener(
    delayMillis: Long = 1000L,
    action: (complete: () -> Unit) -> Unit
) {
    var isClicked = false

    this.setOnClickListener { view ->
        if (!isClicked) {
            isClicked = true
            action {
                isClicked = false
            }
            view.postDelayed({ isClicked = false }, delayMillis)
        }
    }
}

/**
 * Simple anti-spam click listener
 */
fun View.setOnSingleClickListener(
    delayMillis: Long = 1000L,
    action: () -> Unit
) {
    var lastClickTime = 0L

    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= delayMillis) {
            lastClickTime = currentTime
            action()
        }
    }
}

// ==================== Image Loading Extensions ====================

/**
 * Load Image with Glide (Enhanced with more options)
 */
fun Context.loadImageWithGlide(
    data: Any,
    imageView: ImageView,
    placeholder: Int = R.drawable.ic_place_holder,
    error: Int = R.drawable.ic_error,
    useCache: Boolean = false
) {
    var glideRequest = Glide.with(this)
        .load(data)
        .placeholder(placeholder)
        .error(error)

    if (!useCache) {
        glideRequest = glideRequest
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
    }

    glideRequest
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                logE("GLIDE", "onLoadFailed: $e")
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable?>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        })
        .into(imageView)
}

/**
 * Load Image with callback
 */
fun Context.loadImageWithCallback(
    data: Any,
    imageView: ImageView,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) {
    Glide.with(this)
        .load(data)
        .placeholder(R.drawable.ic_place_holder)
        .error(R.drawable.ic_error)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                onError?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable?>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess?.invoke()
                return false
            }
        })
        .into(imageView)
}

// ==================== ViewPager2 Extensions ====================

/**
 * Animation Fade Page Transformer
 */
class FadePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> page.alpha = 0f
            position <= 0 -> {
                page.alpha = 1f
                page.translationX = 0f
                page.scaleX = 1f
                page.scaleY = 1f
            }
            position <= 1 -> {
                page.alpha = 1 - position
                page.translationX = page.width * -position
            }
            else -> page.alpha = 0f
        }
    }
}

// ==================== Fragment Extensions ====================

/**
 * Get Fragment Name
 */
fun getFragmentName(fragment: Fragment): String {
    return fragment.javaClass.simpleName
}