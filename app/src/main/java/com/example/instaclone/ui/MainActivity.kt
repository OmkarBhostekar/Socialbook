package com.example.instaclone.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instaclone.R
import com.example.instaclone.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim) }
    private val toBottomRight: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_right_anim) }
    private val fromBottomRight: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_right_anim) }
    private val toBottomLeft: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_left_anim) }
    private val fromBottomLeft: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_left_anim) }

    private var clicked = false

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
                R.id.pollsFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener { _, d, _ ->
            when(d.id){
                R.id.searchFragment,
                R.id.singlePostFragment,
                R.id.searchedProfileFragment,
                R.id.inboxFragment,
                R.id.pollsFragment,
                R.id.profileFragment -> showToolbarAndBottomNav(toolbar = false,bottomNav = true)
                R.id.homeFragment -> showToolbarAndBottomNav(toolbar = true,bottomNav = true)
                else -> showToolbarAndBottomNav(toolbar = false,bottomNav = false)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            setVisibility(clicked)
            setAnimation(clicked)
            clicked = !clicked
            navController.navigate(R.id.newPostFragment)
        }

        binding.floatingActionButton3.setOnClickListener {
            setVisibility(clicked)
            setAnimation(clicked)
            clicked = !clicked
            navController.navigate(R.id.newPollFragment)
        }

        binding.fabAdd.setOnClickListener {
            addButtonClicked()
        }
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun addButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked){
            binding.floatingActionButton.visibility = View.VISIBLE
            binding.floatingActionButton3.visibility = View.VISIBLE
        }else{
            binding.floatingActionButton.visibility = View.INVISIBLE
            binding.floatingActionButton3.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked){
            binding.floatingActionButton.startAnimation(fromBottomRight)
            binding.floatingActionButton3.startAnimation(fromBottomLeft)
            binding.fabAdd.startAnimation(rotateOpen)
        }else{
            binding.floatingActionButton.startAnimation(toBottomRight)
            binding.floatingActionButton3.startAnimation(toBottomLeft)
            binding.fabAdd.startAnimation(rotateClose)
        }
    }

    private fun showToolbarAndBottomNav(toolbar: Boolean, bottomNav: Boolean){
        binding.toolbar.root.isVisible = toolbar
        binding.bottomNav.isVisible = bottomNav
        binding.bottomAppbar.isVisible = bottomNav
        binding.fabAdd.isVisible = bottomNav
    }

    fun setToolbarTitle(title:String) {
        binding.toolbar.tvTitle.text = title
    }


    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this)
    }

    fun showToolbar(b: Boolean) {
        binding.toolbar.root.isVisible = b
    }
}