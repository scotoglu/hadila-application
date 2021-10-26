package com.scoto.hadila.ui.problem.details

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.data.model.Collection
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.data.model.UserLog
import com.scoto.hadila.data.model.WebSource
import com.scoto.hadila.databinding.FragmentProblemDetailsBinding
import com.scoto.hadila.ext.snackbar
import com.scoto.hadila.ui.BaseFragment
import com.scoto.hadila.ui.common.ItemSelectListener
import com.scoto.hadila.ui.problem.problem_listing.DialogAddToCollection
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProblemDetailsFragment :
    BaseFragment<FragmentProblemDetailsBinding>(R.layout.fragment_problem_details),
    ItemSelectListener {


    private val args: ProblemDetailsFragmentArgs by navArgs()
    private lateinit var problem: Problem
    private lateinit var mMenu: Menu
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppbar: BottomAppBar
    private val detailsViewModel: DetailsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            problem = it.PROBLEM
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.problem = problem

        problem.category?.let {
            addChipsToLayout(it)
        }

        val sourceList: MutableList<WebSource> = mutableListOf()
        problem.sLink?.forEach {
            sourceList.add(it)
        }
        problem.sVideo?.forEach {
            sourceList.add(it)
        }
        addSourcesToLayout(sourceList)

        bottomAppbar =
            (activity as AppCompatActivity).findViewById(R.id.bottom_app_bar)
        fab =
            (activity as AppCompatActivity).findViewById(R.id.fab_btn_add)

        bottomAppbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        bottomAppbar.fabAnimationMode = BottomAppBar.FAB_ANIMATION_MODE_SLIDE
        fab.setImageResource(R.drawable.ic_collection_add)
        fab.setOnClickListener {
            activity?.supportFragmentManager?.let {
                DialogAddToCollection(this).show(it, DialogAddToCollection.TAG)
            }

        }
    }

    override fun getItem(item: Any) {
        val collection = item as Collection
        val titleOf: String = collection.title.toString()
        problem.collection = Collection().apply {
            title = titleOf
        }
        detailsViewModel.update(problem)
        saveToLog(2)
        this.snackbar("Added to ${collection.title}",fab)
    }

    private fun addChipsToLayout(chipList: List<String>) {
        for (c in chipList) {
            val chip = layoutInflater.inflate(R.layout.item_chip_category, null, false) as Chip
            chip.text = c
            binding.linearLayoutTagGroup.addView(chip)
        }
    }

    private fun addSourcesToLayout(sourceList: MutableList<WebSource>) {
        sourceList.forEach { source ->
            val textView = layoutInflater.inflate(
                R.layout.item_textview_details_fragment,
                null,
                false
            ) as TextView
            val txtWithUrl = "<a href=${source.url}>${source.title}</a>"
            textView.isClickable = true
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.text = Html.fromHtml(txtWithUrl, Html.FROM_HTML_MODE_COMPACT)
            binding.gridLayoutSources.addView(textView)
        }
    }

    private fun addFavourites() {
        val menuItem = mMenu.findItem(R.id.favourite)
        val favouriteIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_32)
        val favoriteIconBorder =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border_32)
        when {
            problem.isFavourite -> {//if it is favourite
                problem.isFavourite = false
                menuItem.icon = favoriteIconBorder
                detailsViewModel.update(problem)
                saveToLog(2)
            }
            !problem.isFavourite -> {//if it is not favourite
                problem.isFavourite = true
                menuItem.icon = favouriteIcon
                detailsViewModel.update(problem)
                saveToLog(2)
            }
        }
    }

    private fun delete() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("DELETE")
        alertDialog.setMessage("Are you sure?")
        alertDialog.setPositiveButton("Yes") { dialog, _ ->
            detailsViewModel.delete(problem)
            dialog.dismiss()
            this.snackbar("Problem deleted.",fab)
            saveToLog(3)
            findNavController().popBackStack()
        }
        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun saveToLog(type: Int) {
        val log = UserLog()
        when (type) {

            2 -> {
                log.apply {
                    logLevel = 2
                    logContent = "Problem Updated"
                }
            }
            3 -> {
                log.apply {
                    logLevel = 3
                    logContent = "Problem Deleted"
                }
            }
        }
        detailsViewModel.saveLog(log)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_problem_details, menu)
        mMenu = menu
        if (problem.isFavourite) {
            mMenu.findItem(R.id.favourite)?.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_32)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                delete()
            }
            R.id.favourite -> {
                addFavourites()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomAppbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        fab.setImageResource(R.drawable.ic_baseline_add_32)
    }
}