package com.scoto.hadila.ui.problem.problem_listing

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.databinding.FragmentProblemsListingBinding
import com.scoto.hadila.ext.hide
import com.scoto.hadila.ext.show
import com.scoto.hadila.ext.snackbar
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import com.scoto.hadila.ui.common.ItemSelectListener
import com.scoto.hadila.ui.problem.problem_listing.adapter.ProblemListingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProblemsListingFragment :
    BaseFragment<FragmentProblemsListingBinding>(R.layout.fragment_problems_listing),
    ItemSelectListener {

    private val viewModel: ProblemListingViewModel by viewModels()
    private val args: ProblemsListingFragmentArgs by navArgs()
    private var LIST_TYPE: Int? = null
    private lateinit var fab: FloatingActionButton


    override fun getItem(item: Any) {
        val navController = findNavController()
        navController.navigate(
            ProblemsListingFragmentDirections.actionProblemsListingFragmentToProblemDetailsFragment(
                item as Problem
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            LIST_TYPE = it.LISTTYPE
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRv()
        when (LIST_TYPE) {
            LIST_ALL -> {
                //0
                populateRVWithAll()
            }
            LIST_FAVOURITES -> {
                //1
                populateRvWithFavourites()
            }
            LIST_BY_COLLECTION -> {
                //2
                var title = ""
                args.let {
                    title = it.BYCOLLECTION.toString()
                }
                populateRvByCollection(title)
            }
            LIST_BYCATEGORY -> {
                var category = ""
                args.let {
                    category = it.BYCATEGORY.toString()
                }
                populateRvByCategory(category)
            }
        }

        fab = (activity as AppCompatActivity).findViewById(R.id.fab_btn_add)
    }


    private fun setupRv() {
        binding.rvProblems.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )

        }
    }

    private fun populateRvByCategory(category: String) {
        viewModel.getProblemByCategory(category)
        viewModel.byCategory.observe(viewLifecycleOwner,{response ->
            when (response) {
                is Resources.Success -> {
                    with(ProblemListingAdapter()) {
                        setListener(this@ProblemsListingFragment)
                        setProblems(response.data)
                        binding.rvProblems.adapter = this
                    }
                    binding.progressbarLayout.executePendingBindings()
                }
                is Resources.Error -> {
                    binding.progressbarLayout.progressbar.hide()
                    this.snackbar("Failed to load...", fab)
                }
                is Resources.Loading -> {
                    binding.progressbarLayout.progressbar.show()
                }
                else -> {
                }
            }
        })

    }

    private fun populateRVWithAll() {
        viewModel.problems.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    with(ProblemListingAdapter()) {
                        setListener(this@ProblemsListingFragment)
                        setProblems(response.data)
                        binding.rvProblems.adapter = this
                    }
                    binding.progressbarLayout.executePendingBindings()
                }
                is Resources.Error -> {
                    binding.progressbarLayout.progressbar.hide()
                    this.snackbar("Failed to load...", fab)
                }
                is Resources.Loading -> {
                    binding.progressbarLayout.progressbar.show()
                }
                else -> {
                }
            }

        })

    }

    private fun populateRvByCollection(title: String) {
        viewModel.getProblemByCollection(title)
        viewModel.byCollection.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    with(ProblemListingAdapter()) {
                        setListener(this@ProblemsListingFragment)
                        setProblems(response.data)
                        binding.rvProblems.adapter = this
                    }
                    binding.progressbarLayout.executePendingBindings()
                }
                is Resources.Error -> {
                    binding.progressbarLayout.progressbar.hide()
                    this.snackbar("Failed to load...", fab)
                }
                is Resources.Loading -> {
                    binding.progressbarLayout.progressbar.show()
                }
                else -> {
                }
            }

        })
    }

    private fun populateRvWithFavourites() {
        viewModel.favourites.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    with(ProblemListingAdapter()) {
                        setListener(this@ProblemsListingFragment)
                        setProblems(response.data)
                        binding.rvProblems.adapter = this
                    }
                    binding.progressbarLayout.executePendingBindings()
                }
                is Resources.Error -> {
                    binding.progressbarLayout.progressbar.hide()
                    this.snackbar("Failed to load...", fab)
                }
                is Resources.Loading -> {
                    binding.progressbarLayout.progressbar.show()
                }
                else -> {
                }
            }

        })
    }


    companion object {
        private const val TAG = "ProblemsListingFragment"
        private const val LIST_ALL = 0
        private const val LIST_FAVOURITES: Int = 1
        private const val LIST_BY_COLLECTION: Int = 2
        private const val LIST_BYCATEGORY: Int = 3
    }
}