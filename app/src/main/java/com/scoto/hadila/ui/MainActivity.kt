package com.scoto.hadila.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scoto.hadila.R
import com.scoto.hadila.ext.hide
import com.scoto.hadila.ext.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomAppBar: BottomAppBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navController = findNavController(R.id.fragment)
        bottomAppBar = findViewById(R.id.bottom_app_bar)

        //navigationIcon
        bottomAppBar.setNavigationOnClickListener {
            navController.navigate(R.id.homeFragment)
        }

        setupNavigation()
    }


    private fun setupNavigation() {
        //Top level fragments that not added to backstack of nav

        appBarConfigurations()

        //navigationIcon


        navControllerNavigation()
        bottomAppBarNavigateTo()

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun appBarConfigurations() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.homeFragment,
                R.id.problemsListingFragment,
                R.id.profileFragment,
                R.id.categoriesFragment,
                R.id.splashFragment,
                R.id.collectionsFragment
            ), null
        )
    }

    private fun navControllerNavigation() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.splashFragment -> {
                    hideBottomAppBarAndFab()
                }
                R.id.loginFragment -> {
                    hideBottomAppBarAndFab()
                }
                R.id.registerFragment -> {
                    hideBottomAppBarAndFab()
                }
                R.id.resetPasswordFragment -> {
                    hideBottomAppBarAndFab()
                }
                else -> {
                    showBottomAppBarAndFab()
                }
            }
        }
    }

    private fun bottomAppBarNavigateTo() {
        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
//                R.id.homeFragment -> {
//                    navController.navigate(R.id.homeFragment)
//                    true
//                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.categoriesFragment -> {
                    navController.navigate(R.id.categoriesFragment)
                    true
                }
                R.id.problemsListingFragment -> {
                    navController.navigate(R.id.problemsListingFragment)
                    true
                }
                R.id.collectionsFragment -> {
                    navController.navigate(R.id.collectionsFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun hideBottomAppBarAndFab() {
        findViewById<FloatingActionButton>(R.id.fab_btn_add).hide()
        bottomAppBar.hide()
    }

    private fun showBottomAppBarAndFab() {
        findViewById<FloatingActionButton>(R.id.fab_btn_add).show()
        bottomAppBar.show()
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}