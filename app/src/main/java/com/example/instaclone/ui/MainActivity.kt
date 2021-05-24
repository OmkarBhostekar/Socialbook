package com.example.instaclone.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instaclone.R
import com.example.instaclone.comman.Constants
import com.example.instaclone.comman.Constants.GALLERY_PERMISSION_REQUEST_CODE
import com.example.instaclone.comman.Constants.PICK_IMAGE_FOR_PROFILE_REQUEST_CODE
import com.example.instaclone.comman.Constants.PICK_IMAGE_REQUEST_CODE
import com.example.instaclone.databinding.ActivityMainBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

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
                R.id.inboxFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener { _, d, _ ->
            when(d.id){
                R.id.loginFragment,
                R.id.newPostFragment,
                R.id.editProfileFragment,
                R.id.registerFragment -> showToolbarAndBottomNav(toolbar = false,bottomNav = false)
                R.id.postDetailFragment -> showToolbarAndBottomNav(toolbar = true,bottomNav = false)
                R.id.searchFragment,
                R.id.searchedProfileFragment,
                R.id.profileFragment -> showToolbarAndBottomNav(toolbar = false,bottomNav = true)
                else -> showToolbarAndBottomNav(toolbar = true,bottomNav = true)
            }
        }

        binding.fabAdd.setOnClickListener {
            navController.navigate(R.id.newPostFragment)
        }

        binding.bottomNav.setupWithNavController(navController)
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