package com.scoto.hadila.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class UserLog(
    var uId: String? = null,
    var logLevel: Int? = null,
    var logContent: String? = null,
    @ServerTimestamp var timestamp: Date? = null,
    var logId: String? = null
)