package com.example.instaclone.ui.home.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentPostDetailBinding

class PostDetailFragment : Fragment(R.layout.fragment_post_detail){

    private var _binding: FragmentPostDetailBinding? = null
    private val binding: FragmentPostDetailBinding
        get() = _binding!!
    private val args: PostDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostDetailBinding.bind(view)

        val transition = TransitionInflater.from(activity).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

        Glide.with(activity).load(args.image).into(binding.ivPostImage)
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}