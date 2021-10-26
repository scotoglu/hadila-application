package com.scoto.hadila.ui.profile.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.scoto.hadila.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("setLogColor")
    fun setLogColor(textView: TextView, logLevel: Int) {
        when (logLevel) {
            1 -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.blue))
            }
            2 -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.green))
            }
            3 -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.red))
            }

        }
    }
    @JvmStatic
    @BindingAdapter("setLogLevel")
    fun setLogLevel(textView: TextView, logLevel: Int) {
        when (logLevel) {
            1 -> {
                textView.text = "ADDING"
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.blue))
            }
            2 -> {
                textView.text = "UPDATING"
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.green))
            }
            3 -> {
                textView.text = "DELETING"
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.red))
            }
            else -> {
                textView.text = ""
            }
        }
    }
}