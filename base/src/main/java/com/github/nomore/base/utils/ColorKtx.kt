package com.github.nomore.base.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.material.R
import com.google.android.material.color.MaterialColors

@ColorInt
fun Context.colorSecondary(): Int {
    return this.colorAttr(R.attr.colorSecondary)
}

@ColorInt
fun Context.colorSurface(): Int {
    return this.colorAttr(R.attr.colorSurface)
}

@ColorInt
fun Context.colorSurfaceInverse(): Int {
    return this.colorAttr(R.attr.colorSurfaceInverse)
}

@ColorInt
fun Context.colorOnSurface(): Int {
    return this.colorAttr(R.attr.colorOnSurface)
}

@ColorInt
fun Context.colorOnSecondary(): Int {
    return this.colorAttr(R.attr.colorOnSecondary)
}

@ColorInt
fun Context.colorOnPrimary(): Int {
    return this.colorAttr(R.attr.colorOnPrimary)
}

@ColorInt
fun Context.colorPrimary(): Int {
    return this.colorAttr(R.attr.colorPrimary)
}

@ColorInt
fun Context.colorAccent(): Int {
    return this.colorAttr(R.attr.colorAccent)
}

@ColorInt
fun Context.colorError(): Int {
    return this.colorAttr(R.attr.colorError)
}

@ColorInt
fun Context.colorTransparent(): Int {
    return ContextCompat.getColor(this, android.R.color.transparent)
}


@ColorInt
fun Context.colorErrorContainer(): Int {
    return this.colorAttr(R.attr.colorErrorContainer)
}

@ColorInt
fun Context.colorTextPrimary(): Int {
    return this.colorAttr(android.R.attr.textColorPrimary)
}

@ColorInt
fun Context.colorAttr(
    @AttrRes colorAttributeResId: Int, @ColorInt defaultValue: Int = Color.BLACK,
): Int {
    return MaterialColors.getColor(this, colorAttributeResId, defaultValue)
}

@ColorInt
fun View.colorAttr(
    @AttrRes colorAttributeResId: Int, @ColorInt defaultValue: Int = Color.BLACK,
): Int {
    return MaterialColors.getColor(this, colorAttributeResId, defaultValue)
}

fun Context.colorAttrStateList(
    @AttrRes colorAttributeResId: Int,
    defaultValue: ColorStateList = ColorStateList.valueOf(Color.BLACK),
): ColorStateList {
    return MaterialColors.getColorStateList(this, colorAttributeResId, defaultValue)
}