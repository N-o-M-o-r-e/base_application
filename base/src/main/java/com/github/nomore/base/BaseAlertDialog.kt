package com.github.nomore.base

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

abstract class BaseAlertDialog<Binding : ViewBinding>(
    context: Context,
    inflate: (LayoutInflater) -> Binding
) {
    protected var binding: Binding = inflate(LayoutInflater.from(context))
    private val dialog: AlertDialog = AlertDialog.Builder(context).create().apply {
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setView(binding.root)
        setCanceledOnTouchOutside(false)
    }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initAction()

    open fun show() {
        initView()
        initData()
        initAction()
        dialog.show()
    }

    open fun dismiss() {
        dialog.dismiss()
    }

    open fun isShowing(): Boolean = dialog.isShowing
}