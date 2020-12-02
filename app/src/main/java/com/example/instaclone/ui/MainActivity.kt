package com.example.instaclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instaclone.R
import com.example.instaclone.databinding.ActivityMainBinding
import com.example.instaclone.databinding.ToolbarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.title = ""
        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.userActivityFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener { c, d, a ->
            when(d.id){
                R.id.loginFragment,
                R.id.registerFragment -> showToolbarAndBottomNav(toolbar = false,bottomNav = false)
                R.id.profileFragment -> showToolbarAndBottomNav(toolbar = false,bottomNav = true)
                else -> showToolbarAndBottomNav(toolbar = true,bottomNav = true)
            }
        }

        binding.bottomNav.setupWithNavController(navController)
    }

    private fun showToolbarAndBottomNav(toolbar: Boolean, bottomNav: Boolean){
        binding.toolbar.root.isVisible = toolbar
        binding.bottomNav.isVisible = bottomNav
        binding.bottomAppbar.isVisible = bottomNav
        binding.fabAdd.isVisible = bottomNav
    }
}