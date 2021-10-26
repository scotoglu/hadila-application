package com.scoto.hadila.ext

import android.util.Patterns
import com.scoto.hadila.util.FIREBASE_AUTH_MIN_PASSWORD_LENGTH


fun String.isEmailValid(): Boolean {
    if (this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
        return true
    }
    return false
}


fun String.isPasswordValid(): Boolean {
    val pass = this.trim()
    //FIREBASE_AUTH_MIN_PASSWORD_LENGTH = 6
    if (this.isNotEmpty() && pass.length >= FIREBASE_AUTH_MIN_PASSWORD_LENGTH) {
        return true
    }
    return false
}

fun String.isTextAlphabetic(): Boolean =
    this.matches("^[a-zA-Z]*$".toRegex())

fun String.isURLValid(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}