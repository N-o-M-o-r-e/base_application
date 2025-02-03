package com.github.nomore.base.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.github.nomore.base.R

fun Context.toastMessenger(textResId: Int) {
    val text = getString(textResId)
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.loadImageWithGlide(data: Any, imageView: ImageView) {
    Glide.with(this).load(data).placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_error)
        .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, m: Any?, y: Target<Drawable>, isF: Boolean
            ): Boolean {
                Log.e("__GLIDE", "onLoadFailed: $e")
                return false
            }

            override fun onResourceReady(
                r: Drawable, m: Any, t: Target<Drawable>?, d: DataSource, isF: Boolean
            ): Boolean {
                return false
            }
        }).into(imageView)
}