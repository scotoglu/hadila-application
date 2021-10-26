package com.scoto.hadila.ui.problem.add_problem

import com.scoto.hadila.data.model.WebSource

interface SourceInputDialogListener {
    fun onDialogData(source: WebSource, type: Int)
}