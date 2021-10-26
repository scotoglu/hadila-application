package com.scoto.hadila.ui.collections

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.data.model.Collection
import com.scoto.hadila.databinding.FragmentCollectionsBinding
import com.scoto.hadila.databinding.ItemCollectionsBinding
import com.scoto.hadila.ext.hide
import com.scoto.hadila.ext.show
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import com.scoto.hadila.ui.collections.adapter.CollectionsAdapter
import com.scoto.hadila.ui.common.ItemSelectListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CollectionsFragment :
    BaseFragment<FragmentCollectionsBinding>(R.layout.fragment_collections),
    ICreateDialogListener,
    ItemSelectListener {


    private val viewModel: CollectionsViewModel by viewModels()

    override fun getTitle(text: String) {
        val collection = Collection().apply {
            title = text
        }
        Log.d(TAG, "getTitle: Collection  :${collection.title}")
        viewModel.addCollection(
            collection
        )
    }

    override fun getItem(item: Any) {
        //navigate to
        Log.d(TAG, "getItem: Item $item")
        val collection = item as Collection
        val navController = findNavController()
        val fragmentLabel = collection.title.toString().capitalize(Locale.ROOT)
        val title = collection.title.toString()
        navController.navigate(
            CollectionsFragmentDirections.actionCollectionsFragmentToProblemsListingFragment(
                LIST_BY_COLLECTION, fragmentLabel, title, null
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).findViewById<FloatingActionButton>(R.id.fab_btn_add)
            .setOnClickListener {
                //open create collection dialog
                activity?.supportFragmentManager?.let {
                    DialogCreateCollection(this).show(it, DialogCreateCollection.TAG)
                }
            }
        setupRvCollections()
        populateRvCollections()
        setHasOptionsMenu(true)

    }

    private fun setupRvCollections() {
        binding.rvCollections.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun populateRvCollections() {
        viewModel.collection.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    binding.includedProgressbar.progressbar.hide()
                    with(CollectionsAdapter()) {
                        setCollections(response.data)
                        setListener(this@CollectionsFragment)
                        binding.rvCollections.adapter = this
                    }

                }
                is Resources.Error -> {
                    binding.includedProgressbar.progressbar.hide()
                }
                is Resources.Loading -> {
                    binding.includedProgressbar.progressbar.show()
                }
                else -> {
                }
            }
        })
    }


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflate = MenuInflater(requireContext())
        inflate.inflate(R.menu.menu_delete, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            Log.d(TAG, "onContextItemSelected: Delete clicked")
        }
        return super.onContextItemSelected(item)
    }


    companion object {
        private const val LIST_BY_COLLECTION: Int = 2
        private const val TAG = "CollectionsFragment"
    }
}