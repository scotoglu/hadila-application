package com.scoto.hadila.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Problem(
    var pId: String? = null,
    var pTitle: String? = null,
    var pBody: String? = null,
    var solution: String? = null,
    var category: List<String>? = null,
    var sLink: List<WebSource>? = null,
    var sVideo: List<WebSource>? = null,
    @ServerTimestamp var timestamp: Date? = null,
    var isFavourite: Boolean = false,
    var collection: Collection? = null
) : Parcelable