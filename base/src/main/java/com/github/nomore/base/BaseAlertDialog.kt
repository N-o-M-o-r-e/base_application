package com.github.nomore.base

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import androidx.core.graphics.drawable.toDrawable

abstract class BaseAlertDialog<Binding : ViewBinding>(
    context: Context,
    inflate: (LayoutInflater) -> Binding
) {
    protected var binding: Binding = inflate(LayoutInflater.from(context))
    private val dialog: AlertDialog = AlertDialog.Builder(context).create().apply {
        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setView(binding.root)
        setCanceledOnTouchOutside(false)
    }

    protected abstract fun initDialog()


    open fun show() {
        initDialog()
        dialog.show()
    }

    open fun dismiss() {
        dialog.dismiss()
    }

    open fun isShowing(): Boolean = dialog.isShowing
}