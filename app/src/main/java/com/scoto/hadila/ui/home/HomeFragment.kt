package com.scoto.hadila.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.databinding.FragmentHomeBinding
import com.scoto.hadila.ext.hide
import com.scoto.hadila.ext.show
import com.scoto.hadila.ext.snackbar
import com.scoto.hadila.helper.DataStoreManager
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import com.scoto.hadila.ui.common.ItemSelectListener
import com.scoto.hadila.ui.home.adapter.HomeRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    ItemSelectListener {

    private lateinit var selectedProblem: Problem
    private lateinit var fab: FloatingActionButton
    private val homeViewModel: HomeViewModel by viewModels()


    @Inject
    lateinit var dataStoreManager: DataStoreManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        setupRVProblems()
        populateRvProblems()
        fab =
            (activity as AppCompatActivity).findViewById(R.id.fab_btn_add)

        fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddProblemFragment())
        }

setListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        when (item.itemId) {
            R.id.settings -> {
                //Navigate to settings
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
            }
            R.id.logout -> {
                //logout from app
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
            }


        }
        return super.onOptionsItemSelected(item)
    }


    private fun setListeners() {
        binding.apply {
            cardViewCategories.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                    HomeFragmentDirections.actionHomeFragmentToCategoriesFragment()
                )
            )
            cardViewFavourites.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                    HomeFragmentDirections.actionHomeFragmentToProblemsListingFragment(
                        LIST_FAVOURITES, "Favourites", null, null
                    )
                )
            )
            cardViewCollections.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                    HomeFragmentDirections.actionHomeFragmentToCollectionsFragment()
                )
            )

            //createNavigationClickListener  returns View.OnClikListener so calling  lambda not working
//            btnFloatingAction.setOnClickListener(
//                Navigation.createNavigateOnClickListener(
//                    HomeFragmentDirections.actionHomeFragmentToAddProblemFragment()
//                )
//            )

        }
    }

    override fun getItem(item: Any) {
        item.let { selected ->
            selectedProblem = selected as Problem
        }
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToProblemDetailsFragment(
                selectedProblem
            )
        )

    }

    private fun setupRVProblems() {
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

    private fun populateRvProblems() {
        homeViewModel.problems.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    with(HomeRecyclerAdapter(requireContext())) {
                        setProblems(response.data as MutableList<Problem>)
                        binding.rvProblems.adapter = this
                        setListener(this@HomeFragment)
                    }
                    binding.includedProgressbar.progressbar.hide()
                }
                is Resources.Loading -> {
                    binding.includedProgressbar.progressbar.show()
                }
                is Resources.Error -> {
                    binding.includedProgressbar.progressbar.hide()
                    this.snackbar("Failed to load...", fab)
                }

            }
        })
    }

    companion object {
        private const val TAG = "HomeFragment"
        private const val LIST_FAVOURITES: Int = 1


    }
}