package com.example.instaclone.ui.home.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.transition.TransitionInflater
import android.view.View
import androidx.core.text.bold
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentPostDetailBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.MainActivity
import com.example.instaclone.ui.home.HomeViewModel
import com.example.instaclone.ui.home.adapters.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment(R.layout.fragment_post_detail){

    private var _binding: FragmentPostDetailBinding? = null
    private val binding: FragmentPostDetailBinding
        get() = _binding!!
    private val args: PostDetailFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostDetailBinding.bind(view)

        val transition = TransitionInflater.from(activity).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

        (activity as MainActivity).setToolbarTitle("Comments")

        args.postId?.let { viewModel.getComments(it) }

        binding.apply {
            args.userImage?.let { ivProfileImage.setImage(it) }
            tvPostDescription.text = SpannableStringBuilder()
                .color(Color.BLACK){
                    bold { append("${args.username}  ") }
                }
                .append(args.description)
            btnPost.setOnClickListener {
                if (etComment.text.toString().isNotEmpty()){
                    viewModel.newComment(etComment.text.toString(),args.postId!!)
                }
            }
        }

        viewModel.comments.observe(viewLifecycleOwner,{
            binding.rvComments.adapter = CommentsAdapter(it)
        })

    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}