package com.scoto.hadila.ui.problem.add_problem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.scoto.hadila.R
import com.scoto.hadila.data.model.WebSource
import com.scoto.hadila.databinding.InputDialogFragmentBinding
import com.scoto.hadila.ext.hide
import com.scoto.hadila.ext.isURLValid
import com.scoto.hadila.ext.show
import com.scoto.hadila.ui.problem.add_problem.SourceInputDialogListener

class SourceInputDialog(
    private val type: Int,
    val listener: SourceInputDialogListener
) : DialogFragment() {


    private var _binding: InputDialogFragmentBinding? = null
    private val binding: InputDialogFragmentBinding get() = _binding!!

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InputDialogFragmentBinding.inflate(layoutInflater, container, false)

        binding.tvSourceType.text = when (type) {
            1 -> "LINK"
            2 -> "VIDEO"
            else -> {
                ""
            }
        }

        binding.btnAdd.setOnClickListener {
            getSourceDetails()
        }

        return binding.root
    }

    private fun getSourceDetails() {
        val title = binding.etTitle.text.toString()
        val sourceText = binding.etSource.text.toString()
        if (sourceText.isURLValid()) {
            val source = WebSource(title, sourceText)
            binding.tvSourceFormatControl.hide()
            listener.onDialogData(source, type)
            dismiss()
        } else {
            binding.tvSourceFormatControl.text = getString(R.string.invalid_url)
            binding.tvSourceFormatControl.show()
        }

    }

    companion object {
        const val TAG = "SourceInputDialog"
    }
}