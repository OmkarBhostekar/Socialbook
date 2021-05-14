package com.example.instaclone.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentNewPostBinding
import com.example.instaclone.extension.setImage

class NewPostFragment : Fragment(R.layout.fragment_new_post){

    private var _binding: FragmentNewPostBinding? = null
    private val binding: FragmentNewPostBinding
        get() = _binding!!
    private val args: NewPostFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewPostBinding.bind(view)

        args.imagePath?.let { binding.ivNewPostImage.setImage(it) }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}