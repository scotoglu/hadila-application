package com.scoto.hadila.ui.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import com.scoto.hadila.databinding.DialogCreateCollectionBinding

class DialogCreateCollection(
    private val listener: ICreateDialogListener
) : DialogFragment() {
    private var _binding: DialogCreateCollectionBinding? = null
    private val binding: DialogCreateCollectionBinding
        get() = _binding!!

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
    ): View? {
        _binding = DialogCreateCollectionBinding.inflate(layoutInflater, container, false)
        binding.btnAdd.setOnClickListener {
            addCollection()
        }

        binding.etCollection.setOnEditorActionListener { textView, action, keyEvent ->
            when (action) {
                EditorInfo.IME_ACTION_DONE -> {
                    addCollection()
                    true
                }
                else -> false
            }
        }
        return binding.root
    }

    private fun addCollection() {
        val title = binding.etCollection.text.toString()
        listener.getTitle(title)
        binding.etCollection.text = null
    }


    companion object {
        const val TAG = "CreateCollectionDialog"
    }
}