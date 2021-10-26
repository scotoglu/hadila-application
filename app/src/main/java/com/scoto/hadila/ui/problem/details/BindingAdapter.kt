package com.scoto.hadila.ui.problem.details

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.scoto.hadila.R
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("dateText")
    fun setDateText(view: TextView, date: Date) {
        val dateFormat = SimpleDateFormat("d MMM yyyy")
        when (view.id) {
            R.id.tv_date -> view.text = dateFormat.format(date)
            else -> {
                ""
            }
        }
    }
}