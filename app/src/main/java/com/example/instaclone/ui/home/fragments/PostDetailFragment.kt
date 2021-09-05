package com.example.instaclone.ui.home.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.transition.TransitionInflater
import android.view.View
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.text.color
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.comman.Constants
import com.example.instaclone.data.DataStore
import com.example.instaclone.databinding.FragmentPostDetailBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.MainActivity
import com.example.instaclone.ui.home.HomeViewModel
import com.example.instaclone.ui.home.adapters.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment(R.layout.fragment_post_detail){

    private var _binding: FragmentPostDetailBinding? = null
    private val binding: FragmentPostDetailBinding
        get() = _binding!!
    private val args: PostDetailFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by viewModels()
    lateinit var adapter: CommentsAdapter

    @Inject
    lateinit var dataStore: androidx.datastore.core.DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostDetailBinding.bind(view)
        (activity as MainActivity).setToolbarTitle("Comments")

        (activity as MainActivity).showToolbarAndBottomNav(toolbar = false,bottomNav = false)

        binding.apply {
            adapter = CommentsAdapter()
            rvComments.adapter = adapter
            args.userImage?.let {
                Glide.with(requireContext())
                .load(it)
                .into(ivPostUserProfile)
            }
            tvPostDescription.text = SpannableStringBuilder()
                .color(Color.BLACK){
                    bold { append("${args.username}  ") }
                }
                .append(args.description)
            btnPost.setOnClickListener {
                if (etComment.text.toString().isNotEmpty()){
                    viewModel.newComment(etComment.text.toString(),args.postId!!)
                    etComment.setText("")
                    adapter.refresh()
                }
            }
            lifecycleScope.launch {
                Glide.with(requireContext())
                    .load(DataStore.readStringFromDS(Constants.USER_IMAGE, dataStore))
                    .into(binding.ivProfileImage)
            }
        }

        args.postId?.let { fetchComments(it) }

    }

    private fun fetchComments(postId: String) {
        lifecycleScope.launch {
            viewModel.getCommentsFlow(postId)
                .collect {
                    adapter.submitData(viewLifecycleOwner.lifecycle,it)
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}