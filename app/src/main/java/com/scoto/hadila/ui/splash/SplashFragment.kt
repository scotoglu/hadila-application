package com.scoto.hadila.ui.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.scoto.hadila.R
import com.scoto.hadila.databinding.FragmentSplashBinding
import com.scoto.hadila.helper.DataStoreManager
import com.scoto.hadila.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {


    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()


        dataStoreManager.authIDFlow.asLiveData().observe(viewLifecycleOwner, { uid ->
            if (!uid.isNullOrEmpty()) {
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToMainGraph())
            } else {
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginGraph())
            }
        })

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            activity?.window?.setDecorFitsSystemWindows(false)
//            activity?.window?.insetsController?.let {
//                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
//                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        } else {
//            @Suppress("DEPRECATION")
//            activity?.window?.decorView?.systemUiVisibility = (
//                    View.SYSTEM_UI_FLAG_FULLSCREEN
//                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
//        }

    }




    companion object {
        private const val TAG = "SplashFragment"
    }
}