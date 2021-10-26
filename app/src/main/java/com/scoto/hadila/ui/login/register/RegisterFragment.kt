package com.scoto.hadila.ui.login.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scoto.hadila.R
import com.scoto.hadila.databinding.FragmentRegisterBinding
import com.scoto.hadila.ext.*
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        registerViewModel.response.observe(viewLifecycleOwner, { response ->
            val navController = findNavController()
            when (response) {
                is Resources.Success -> {


                    binding.progressbar.hide()
                    this.snackbar("Redirect to home...", null)
                    binding.tvSuccess.show()
                    // After user created Firebase Auth automatically sign-in, so I have redirect to home page
                    navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToMainGraph())

                }
                is Resources.Error -> {
                    binding.progressbar.hide()
                    this.snackbar("Failed.. Try again", null)
                }
                is Resources.Loading -> {
                    binding.progressbar.show()
                }
            }
        })
        savedStateHandler(savedInstanceState)
        setListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val email = binding.email.editText?.text.toString()
        val pass = binding.password.editText?.text.toString()
        val pass2 = binding.passwordRepeat.editText?.text.toString()

        outState.putString("email", email)
        outState.putString("pass", pass)
        outState.putString("pass2", pass2)

    }

    private fun savedStateHandler(instance: Bundle?) {
        instance.let {
            val email = it?.getString("email")
            val pass = it?.getString("pass")
            val pass2 = it?.getString("pass2")

            binding.email.editText?.setText(email)
            binding.password.editText?.setText(pass)
            binding.passwordRepeat.editText?.setText(pass2)
        }
    }

    private fun setListeners() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val email = binding.email.editText?.text.toString()
        val pass = binding.password.editText?.text.toString()
        val pass2 = binding.passwordRepeat.editText?.text.toString()

        if (email.isEmailValid()) {
            if (pass.isPasswordValid() && pass2.isPasswordValid() && pass == pass2) {
                binding.progressbar.show()
                registerViewModel.register(email, pass)
            } else {
                binding.password.error = getString(R.string.unmatched_password)
                binding.passwordRepeat.error = getString(R.string.unmatched_password)
            }
        } else {
            binding.email.error = getString(R.string.invalid_email)
        }

    }


}

