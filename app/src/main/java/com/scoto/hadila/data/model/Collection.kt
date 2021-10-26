package com.scoto.hadila.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(
    var title: String? = null
) : Parcelable
