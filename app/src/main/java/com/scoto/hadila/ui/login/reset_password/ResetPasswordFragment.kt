package com.scoto.hadila.ui.login.reset_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.scoto.hadila.R
import com.scoto.hadila.databinding.FragmentResetPasswordBinding
import com.scoto.hadila.ext.*
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment :
    BaseFragment<FragmentResetPasswordBinding>(R.layout.fragment_reset_password) {


    private val resetPassViewModel: ResetPassViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnResetPassword.setOnClickListener {
            resetPassword()
        }
        resetPassViewModel.response.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    this.snackbar("Successfully", null)
                    binding.progressbar.hide()
                    binding.textviewInfoMessage.show()
                }
                is Resources.Error -> {
                    binding.progressbar.hide()
                    this.snackbar("Something is wrong. Check email and try again", null)
                }
                is Resources.Loading -> {
                    binding.progressbar.show()
                }
                else -> {
                }
            }
        })
        savedInstanceState.let {
            val email = it?.getString("email")
            binding.email.editText?.setText(email)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val email: String = binding.email.editText?.text.toString()
        outState.putString("email", email)
    }

    private fun resetPassword() {
        val email = binding.email.editText?.text.toString()
        if (email.isEmailValid()) {
            binding.progressbar.show()
            context?.hideSoftKeyboard(binding.email)
            resetPassViewModel.resetPassword(email)
        } else {
            binding.email.error = getString(R.string.invalid_email)
        }

    }

}