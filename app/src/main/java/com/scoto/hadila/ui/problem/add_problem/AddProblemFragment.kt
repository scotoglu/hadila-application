package com.scoto.hadila.ui.problem.add_problem

import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.data.model.UserLog
import com.scoto.hadila.data.model.WebSource
import com.scoto.hadila.databinding.FragmentAddProblemBinding
import com.scoto.hadila.ext.snackbar
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddProblemFragment :
    BaseFragment<FragmentAddProblemBinding>(R.layout.fragment_add_problem),
    View.OnClickListener,
    SourceInputDialogListener {

    private val viewModel: AddProblemViewModel by viewModels()

    private var chipList: MutableList<String> = mutableListOf()

    private val linkSourceList = mutableListOf<WebSource>()
    private val videoSourceList = mutableListOf<WebSource>()

    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppbar: BottomAppBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isSuccessful.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    this.snackbar("Successfully added.", fab)
                    saveToLog()
                    findNavController().popBackStack()
                }
                is Resources.Error -> {
                    this.snackbar("Failed to add...", fab)
                }
                is Resources.Loading -> {
                }
            }
        })



        binding.etTagText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Log.d(TAG, "onClick: Action Done")
                    val text = binding.etTagText.text.toString()
                    addChipToGroup(text)
                    true
                }
                else -> {
                    Log.d(TAG, "onClick: Action not done")
                    false
                }
            }
        }

        bottomAppbar =
            (activity as AppCompatActivity).findViewById<BottomAppBar>(R.id.bottom_app_bar)
        fab =
            (activity as AppCompatActivity).findViewById<FloatingActionButton>(R.id.fab_btn_add)
        bottomAppbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        bottomAppbar.fabAnimationMode = BottomAppBar.FAB_ANIMATION_MODE_SLIDE
        fab.setImageResource(R.drawable.ic_save)
        fab.setOnClickListener {
            save()
        }

        setListeners()
    }

    private fun getProblemDetails(): Problem {
        val title = binding.etProblemTitle.text.toString()
        val explanation = binding.etProblemExplanation.text.toString()
        val problemSolution = binding.etProblemSolution.text.toString()

        return Problem().apply {
            pTitle = title
            pBody = explanation
            solution = problemSolution
            category = chipList
            sLink = linkSourceList
            sVideo = videoSourceList
            timestamp
            isFavourite
        }


    }

    private fun save() {
        val problem = getProblemDetails()
        viewModel.addProblem(problem)
    }

    private fun saveToLog() {
        val log = UserLog().apply {
            logContent = "Problem Added"
            logLevel = 1
        }
        viewModel.saveLog(log)
    }

    override fun onDialogData(source: WebSource, type: Int) {
        if (type == LINK_SOURCE) {
            addSourceLink(source)
        } else {
            addVideoSourceLink(source)
        }
    }

    private fun addChipToGroup(text: String) {
        if (text.isNotEmpty()) {
            text.trim()
            if (binding.categoryChipGroup.childCount <= 2) {
                val chip = layoutInflater.inflate(R.layout.item_chip_category, null, false) as Chip
                chip.id = ViewCompat.generateViewId()
                chip.text = text
                chip.setOnCloseIconClickListener {
                    chipList.remove(text)
                    binding.categoryChipGroup.removeView(chip)
                }
                chipList.add(text)
                binding.categoryChipGroup.addView(chip)
                binding.etTagText.text = null
            } else {
                this.snackbar("You cna add only two tag", fab)
            }
        } else {
            this.snackbar("Can't be empty", fab)
        }

    }


    private fun addVideoSourceLink(source: WebSource) {
        val link =
            layoutInflater.inflate(R.layout.item_textview_details_fragment, null, false) as TextView
        val title = if (source.title.isNullOrEmpty()) "Video" else source.title
        val txtWithUrl = "<a href=${source.url}>$title</a>"
        link.isClickable = true
        link.movementMethod = LinkMovementMethod.getInstance()
        link.text = Html.fromHtml(txtWithUrl, FROM_HTML_MODE_COMPACT)
        videoSourceList.add(source)
        binding.tvAddVideo.text = "Video(${linkSourceList.size})"
        binding.linearLayoutVideoResources.addView(link)
    }

    private fun addSourceLink(source: WebSource) {
        val link =
            layoutInflater.inflate(R.layout.item_textview_details_fragment, null, false) as TextView
        val title = if (source.title.isNullOrEmpty()) "Link" else source.title
        val txtWithUrl = "<a href=${source.url}>$title</a>"
        link.isClickable = true
        link.movementMethod = LinkMovementMethod.getInstance()
        link.text = Html.fromHtml(txtWithUrl, FROM_HTML_MODE_COMPACT)
        linkSourceList.add(source)
        binding.tvAddLink.text = "Link(${linkSourceList.size})"
        binding.linearLayoutLinkResources.addView(link)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
//            R.id.btn_save -> {
//                save()
//            }
            R.id.btn_add_chip -> {
                val text = binding.etTagText.text.toString()
                addChipToGroup(text)
            }
            R.id.tv_add_video -> {
                activity?.supportFragmentManager?.let {
                    SourceInputDialog(2, this).show(it, SourceInputDialog.TAG)
                }
            }
            R.id.tv_add_link -> {
                activity?.supportFragmentManager?.let {
                    SourceInputDialog(1, this).show(it, SourceInputDialog.TAG)
                }
            }
//            R.id.edit -> {
//                //navigate to edit fragment
//            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnAddChip.setOnClickListener(this@AddProblemFragment)
            //  btnSave.setOnClickListener(this@AddProblemFragment)
            tvAddLink.setOnClickListener(this@AddProblemFragment)
            tvAddVideo.setOnClickListener(this@AddProblemFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fab.setImageResource(R.drawable.ic_baseline_add_32)
        bottomAppbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    }

    companion object {
        private const val TAG = "AddProblemFragment"
        private const val VIDEO_SOURCE = 2
        private const val LINK_SOURCE = 1
    }

}