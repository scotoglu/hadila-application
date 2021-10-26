package com.scoto.hadila.ui.problem.problem_listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.scoto.hadila.databinding.DialogAddToCollectionBinding
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.collections.CollectionsViewModel
import com.scoto.hadila.ui.collections.adapter.CollectionsAdapter
import com.scoto.hadila.ui.common.ItemSelectListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogAddToCollection(
    private val listener: ItemSelectListener
) : DialogFragment(), ItemSelectListener {

    private val viewModel: CollectionsViewModel by viewModels()

    private var _binding: DialogAddToCollectionBinding? = null
    private val binding: DialogAddToCollectionBinding get() = _binding!!

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
        _binding = DialogAddToCollectionBinding.inflate(layoutInflater, container, false)


        setupRV()
        populateRV()


        return binding.root
    }

    private fun setupRV() {
        binding.rvCollections.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun populateRV() {
        viewModel.collection.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {

                    with(CollectionsAdapter()) {
                        setCollections(response.data)
                        setListener(this@DialogAddToCollection)
                        binding.rvCollections.adapter = this
                    }

                }
                is Resources.Error -> {

                }
                is Resources.Loading -> {

                }
                else -> {
                }
            }
        })

    }

    override fun getItem(item: Any) {
        listener.getItem(item)
        dismiss()
    }

    companion object {
        const val TAG = "DialogAddToCollection"
    }
}