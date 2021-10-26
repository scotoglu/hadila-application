package com.scoto.hadila.ui.common

import android.view.View
import androidx.annotation.DrawableRes
import com.google.android.material.bottomappbar.BottomAppBar
import com.scoto.hadila.R

interface IFabConfigListener {
    fun onConfigChanges(
        @DrawableRes icon: Int = R.drawable.ic_baseline_add_32,
        visibility: Int = View.VISIBLE,
        fabPosition: Int = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    )
}