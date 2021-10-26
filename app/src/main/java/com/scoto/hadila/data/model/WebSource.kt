package com.scoto.hadila.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebSource(val title: String? = null, val url: String? = null) : Parcelable