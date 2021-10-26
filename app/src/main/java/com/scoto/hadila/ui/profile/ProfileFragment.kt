package com.scoto.hadila.ui.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.databinding.FragmentProfileBinding
import com.scoto.hadila.ext.snackbar
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import com.scoto.hadila.ui.profile.adapter.UserLogAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment() :
    BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile),
    View.OnClickListener {
    private lateinit var fab: FloatingActionButton
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setupRvUserLog()
        populateRvUserLog()

        fab = (activity as AppCompatActivity).findViewById(R.id.fab_btn_add)
        fab.hide()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btnAccount.setOnClickListener(this@ProfileFragment)
            btnUserLogs.setOnClickListener(this@ProfileFragment)
            btnLogout.setOnClickListener(this@ProfileFragment)
        }
    }

    private fun logout() {
        profileViewModel.logout()

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_account -> {
                accountVisible()
            }
            R.id.btn_userLogs -> {
                userLogVisible()
            }
            R.id.btn_logout -> {
                logout()
            }
        }
    }


    private fun userLogVisible() {
        binding.relativeLayoutAccount.visibility = View.GONE
        binding.relativeLayoutUserLogs.visibility = View.VISIBLE
    }

    private fun accountVisible() {
        binding.relativeLayoutAccount.visibility = View.VISIBLE
        binding.relativeLayoutUserLogs.visibility = View.GONE
    }

    private fun setupRvUserLog() {
        binding.rvUserLogs.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        }
    }

    private fun populateRvUserLog() {
        profileViewModel.logs.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    with(UserLogAdapter()) {
                        setLogs(response.data)
                        binding.rvUserLogs.adapter = this
                    }
                }
                is Resources.Error -> {
                    this.snackbar("Failed to load logs", null)
                }
                else -> {
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fab.show()
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}