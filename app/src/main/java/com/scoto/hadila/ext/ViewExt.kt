package com.scoto.hadila.ext

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.scoto.hadila.R

fun Fragment.snackbar(text: String, anchorView: View?) {
    view?.let {
        Snackbar.make(it, text, Snackbar.LENGTH_LONG)
            .setBackgroundTint(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.transparent_black
                )
            )
            .setAnchorView(anchorView)
            .show()
    }
}

fun View.hide(): View {
    visibility = View.GONE
    return this
}

fun View.show(): View {
    visibility = View.VISIBLE
    return this
}