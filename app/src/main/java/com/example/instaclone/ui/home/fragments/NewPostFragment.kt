package com.example.instaclone.ui.home.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.instaclone.R
import com.example.instaclone.comman.Resource
import com.example.instaclone.databinding.FragmentNewPostBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.home.HomeViewModel
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class NewPostFragment : Fragment(R.layout.fragment_new_post){

    private var _binding: FragmentNewPostBinding? = null
    private val binding: FragmentNewPostBinding
        get() = _binding!!
    private val args: NewPostFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewPostBinding.bind(view)

        args.imagePath?.let { binding.ivNewPostImage.setImage(it) }

        val storageRef = FirebaseStorage.getInstance().reference

        args.imagePath?.let { image ->
            binding.btnCreatePost.setOnClickListener {
                val imageRef = storageRef.child("post_images/${System.currentTimeMillis()}.jpg")
                val file = Uri.fromFile(File(image))
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
        viewModel.postCreated.observe(viewLifecycleOwner,{
            if (it is Resource.Success){
                findNavController().navigate(R.id.homeFragment)
            }
        })
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}