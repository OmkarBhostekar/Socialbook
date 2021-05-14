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
                R.id.userActivityFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener { _, d, _ ->
            when(d.id){
                R.id.loginFragment,
                R.id.registerFragment -> showToolbarAndBottomNav(toolbar = false,bottomNav = false)
                R.id.postDetailFragment -> showToolbarAndBottomNav(toolbar = true,bottomNav = false)
                R.id.profileFragment -> showToolbarAndBottomNav(toolbar = false,bottomNav = true)
                else -> showToolbarAndBottomNav(toolbar = true,bottomNav = true)
            }
        }

        binding.fabAdd.setOnClickListener {
            selectImageFromGallery()
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

    @AfterPermissionGranted(GALLERY_PERMISSION_REQUEST_CODE)
    private fun selectImageFromGallery() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            val intent = Intent().setType("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            if (packageManager != null){
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
            }
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Mirrorscore Needs to access your storage so you can select images",
                GALLERY_PERMISSION_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                PICK_IMAGE_REQUEST_CODE -> {
                    data?.data?.let {
                        CropImage.activity(it)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                            .start(this)
                    }
                }
                else -> {
                    val image = CropImage.getActivityResult(data).uri.path
                    image?.let {
                        navController.navigate(R.id.newPostFragment,Bundle().apply {
                            putString("imagePath",it)
                        })
                    }
                }
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp()
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this)
    }
}