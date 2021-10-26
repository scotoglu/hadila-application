package com.scoto.hadila.ui.login.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.scoto.hadila.R
import com.scoto.hadila.databinding.FragmentLoginBinding
import com.scoto.hadila.ext.*
import com.scoto.hadila.helper.DataStoreManager
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding>(R.layout.fragment_login),
    View.OnClickListener {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //save data here
        val email = binding.email.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        val rememberMe = binding.checkboxRememberMe.isChecked
        outState.putString("email", email)
        outState.putString("password", password)
        outState.putBoolean("remember_me", rememberMe)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.isLogin.observe(viewLifecycleOwner, {
            binding.progressbar.show()
            when (it) {
                is Resources.Success -> {
                    //Navigate to home
                    binding.progressbar.hide()
                    if (binding.checkboxRememberMe.isChecked) {
                        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                            Log.d(TAG, "onViewCreated: DataStoreManager")
                            dataStoreManager.saveAuthID(it.data)
                        }
                    }
                    this.snackbar("Login Successfully",null)
                    findNavController().navigate(R.id.action_loginFragment_to_main_graph)

                }
                is Resources.Error -> {
                    binding.progressbar.hide()
                    //Display login failed
                    this.snackbar("Failed to login try again.",null)

                }
                is Resources.Loading -> {
                    binding.progressbar.show()
                }
            }
        })
setListeners()
        savedStateHandle(savedInstanceState)

    }

    private fun savedStateHandle(instance: Bundle?) {
        //read data here
        instance.let {
            val email: String? = it?.getString("email")
            val password: String? = it?.getString("password")
            val rememberMe: Boolean? = it?.getBoolean("remember_me")
            binding.email.editText?.setText(email)
            binding.password.editText?.setText(password)
            if (rememberMe != null) {
                binding.checkboxRememberMe.isChecked = rememberMe
            }

        }

    }

    private fun login() {
        val email = binding.email.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        if (email.isEmailValid()) {
            if (password.isPasswordValid()) {
                loginViewModel.login(email, password)
            } else {
                binding.password.error = getString(R.string.invalid_password)
            }
        } else {
            binding.email.error = getString(R.string.invalid_email)
        }

    }


    private fun setListeners() {
        binding.apply {
            btnRegister.setOnClickListener(this@LoginFragment)
            resetPassBtn.setOnClickListener(this@LoginFragment)
            loginBtn.setOnClickListener(this@LoginFragment)
        }
    }

    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.loginBtn -> {
                login()
            }
            R.id.btn_register -> {
                navController.navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.resetPassBtn -> {
                navController.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}