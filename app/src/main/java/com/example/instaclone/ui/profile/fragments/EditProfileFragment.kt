package com.example.instaclone.ui.profile.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.comman.Constants
import com.example.instaclone.comman.Resource
import com.example.instaclone.databinding.FragmentEditProfileBinding
import com.example.instaclone.ui.MainActivity
import com.example.instaclone.ui.profile.ProfileViewModel
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile){

    private var _binding: FragmentEditProfileBinding? = null
    private val binding: FragmentEditProfileBinding
        get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        (activity as MainActivity).showToolbarAndBottomNav(toolbar = false,bottomNav = false)

        viewModel.myProfile.observe(viewLifecycleOwner,{
            if (it is Resource.Success){
                it.data?.let { user ->
                    Glide.with(requireContext()).load(user.userImage).into(binding.ivProfileImage)
                    binding.etUserName.setText(user.username)
                    binding.etName.setText(user.name)
                    binding.etBio.setText(user.bio)
                    binding.btnChangeProfile.setOnClickListener {
                        selectImageFromGallery()
                    }
                    binding.btnSave.setOnClickListener {
                        val body = HashMap<String,Any>()
                        body["username"] = binding.etUserName.text.toString()
                        body["name"] = binding.etName.text.toString()
                        body["bio"] = binding.etBio.text.toString()
                        viewModel.updateProfile(body)
                    }
                }
            }
        })
    }

    @AfterPermissionGranted(Constants.GALLERY_PERMISSION_REQUEST_CODE)
    private fun selectImageFromGallery() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            val intent = Intent().setType("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, Constants.PICK_IMAGE_FOR_PROFILE_REQUEST_CODE)
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
        if (resultCode == RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_FOR_PROFILE_REQUEST_CODE){
                data?.data?.let {
                    CropImage.activity(it)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .start(requireContext(),this)
                }
            }else {
                val image = CropImage.getActivityResult(data).uri.path
                image?.let {
                    // upload image
                    uploadImage(it)
                }
            }
        }
    }

    private fun uploadImage(imagePath: String) {
        val imageRef = FirebaseStorage.getInstance().reference.child("profile_images/${System.currentTimeMillis()}.jpg")
        val file = Uri.fromFile(File(imagePath))
        imageRef.putFile(file)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    imageRef.downloadUrl.addOnSuccessListener {
                        val body = HashMap<String,Any>()
                        body["userImage"] = it.toString()
                        viewModel.updateProfile(body)
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}