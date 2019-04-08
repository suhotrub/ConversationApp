package com.suhotrub.conversations.ui.util.ui


import android.content.Context
import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.view.ViewGroup
import com.suhotrub.conversations.R

fun Snackbar.config(context: Context): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params

    this.view.background = context.getDrawable(R.drawable.bg_snackbar_default)

    ViewCompat.setElevation(this.view, 6f)
    return this
}

fun Snackbar.config(context: Context, @DrawableRes bgDrawableRes: Int): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params

    this.view.background = context.getDrawable(bgDrawableRes)

    ViewCompat.setElevation(this.view, 6f)
    return this
}