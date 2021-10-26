package com.scoto.hadila.ui.categories

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.databinding.FragmentCategoriesBinding
import com.scoto.hadila.ext.hide
import com.scoto.hadila.ext.show
import com.scoto.hadila.ext.snackbar
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import com.scoto.hadila.ui.categories.adapter.CategoriesAdapter
import com.scoto.hadila.ui.common.ItemSelectListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment :
    BaseFragment<FragmentCategoriesBinding>(R.layout.fragment_categories),
    ItemSelectListener {


    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private lateinit var fab: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvCategories()
        populateRvCategories()
        fab =
            (activity as AppCompatActivity).findViewById<FloatingActionButton>(R.id.fab_btn_add)
        fab.hide()
    }


    private fun setupRvCategories() {
        binding.recylerviewCategories.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun populateRvCategories() {
        categoriesViewModel.response.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    with(CategoriesAdapter()) {
                        setCategories(response.data as MutableList<String>)
                        setListener(this@CategoriesFragment)
                        binding.recylerviewCategories.adapter = this
                    }
                    if (response.data.isEmpty()) {
                        binding.tvEmptyList.show()
                    } else {
                        binding.tvEmptyList.hide()
                    }
                    binding.progressBar.hide()
                }
                is Resources.Loading -> {
                    binding.progressBar.show()
                }
                is Resources.Error -> {
                    binding.progressBar.hide()
                    this.snackbar("Failed to loading", fab)
                }
            }
        })

    }

    override fun getItem(item: Any) {
        val category = item as String
        val label = category.capitalize()
        val navController = findNavController()
        navController.navigate(
            CategoriesFragmentDirections.actionCategoriesFragmentToProblemsListingFragment(
                LIST_BYCATEGORY, label, null, category
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fab.show()
    }

    companion object {
        private const val LIST_BYCATEGORY: Int = 3
    }
}