package com.example.instaclone.ui.home.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.comman.Constants
import com.example.instaclone.comman.Constants.USER_IMAGE
import com.example.instaclone.comman.Resource
import com.example.instaclone.data.DataStore.readStringFromDS
import com.example.instaclone.databinding.FragmentNewPostBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.MainActivity
import com.example.instaclone.ui.home.HomeViewModel
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class NewPostFragment : Fragment(R.layout.fragment_new_post){

    private var _binding: FragmentNewPostBinding? = null
    private val binding: FragmentNewPostBinding
        get() = _binding!!
    private val args: NewPostFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by viewModels()
    private var image: String? = ""

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewPostBinding.bind(view)
        image = args.imagePath

        if (image == null){
            selectImageFromGallery()
        }else{
            Glide.with(requireContext()).load(image!!).into(binding.ivNewPostImage)
        }

        (activity as MainActivity).showToolbarAndBottomNav(toolbar = false,bottomNav = false)

        val storageRef = FirebaseStorage.getInstance().reference

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
           image?.let {
               val imageRef = storageRef.child("post_images/${System.currentTimeMillis()}.jpg")
               val file = Uri.fromFile(File(image!!))
               imageRef.putFile(file)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful){
                           imageRef.downloadUrl.addOnSuccessListener {
                               viewModel.createNewPost(it.toString(),binding.etCaption.text.toString())
                           }
                       }
                   }
           }
        }
        lifecycleScope.launch {
            Glide.with(requireContext())
                .load(readStringFromDS(USER_IMAGE,dataStore))
                .into(binding.ivProfileImage)
        }
        viewModel.postCreated.observe(viewLifecycleOwner,{
            if (it is Resource.Success){
                findNavController().navigate(R.id.homeFragment)
            }
        })
    }

    @AfterPermissionGranted(Constants.GALLERY_PERMISSION_REQUEST_CODE)
    private fun selectImageFromGallery() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            val intent = Intent().setType("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST_CODE)
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Mirrorscore Needs to access your storage so you can select images",
                Constants.GALLERY_PERMISSION_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
            data?.data?.let {
                CropImage.activity(it)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                    .start(requireContext(),this)
            }
        }else{
            image = CropImage.getActivityResult(data).uri.path
            image?.let {
                Glide.with(requireContext()).load(image!!).into(binding.ivNewPostImage)
            }
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}