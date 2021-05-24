package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.comman.Resource
import com.example.instaclone.comman.SpannedGridLayoutManager
import com.example.instaclone.databinding.FragmentProfileBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.home.models.Post
import com.example.instaclone.ui.profile.ProfileViewModel
import com.example.instaclone.ui.profile.adapters.ProfilePostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchedProfileFragment : Fragment(R.layout.fragment_profile), ProfilePostsAdapter.OnClickListener {

    private var _bindng: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _bindng!!
    private val args: SearchedProfileFragmentArgs by navArgs()
    private val viewModel: ProfileViewModel by viewModels()
    lateinit var adapter: ProfilePostsAdapter
    private var followed = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentProfileBinding.bind(view)

        adapter = ProfilePostsAdapter(this)
        args.userId?.let {
            viewModel.getSearchedProfile(it)
            fetchProfilePosts(it)
        }

        binding.rvProfilePosts.adapter = adapter
        val manager = SpannedGridLayoutManager(
            { position -> // Conditions for 2x2 items
                if (position % 13 == 0 || position % 13 == 7) {
                    SpannedGridLayoutManager.SpanInfo(2, 2)
                } else {
                    SpannedGridLayoutManager.SpanInfo(1, 1)
                }
            },
            3,  // number of columns
            1f // how big is default item
        )

        binding.rvProfilePosts.layoutManager = manager

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.searchedProfile.observe(viewLifecycleOwner,{
            if (it is Resource.Success) {
                binding.apply {
                    it.data?.let { user ->
                        Glide.with(context).load(user.userImage!!).into(ivUserImage)
                        tvUserName.text = user.username
                        tvName.text = user.name
                        if (user.bio != null)
                            tvBio.text = user.bio
                        else
                            tvBio.visibility = View.GONE
                        tvPostCount.text = (user.posts ?: 0).toString()
                        tvFollowerCount.text = (user.followers?.size ?: 0).toString()
                        tvFollowingCount.text = (user.following?.size ?: 0).toString()
                        btnFollowers.setOnClickListener {
                            findNavController().navigate(
                                SearchedProfileFragmentDirections.actionSearchedProfileFragmentToFragmentBottomSheet("followers",user._id)
                            )
                        }
                        btnFollowing.setOnClickListener {
                            findNavController().navigate(
                                SearchedProfileFragmentDirections.actionSearchedProfileFragmentToFragmentBottomSheet("following",user._id)
                            )
                        }
                        followed = user.followed ?: false
                        if (user.followed!!){
                            setFollowButton(followed = true)
                        }
                        btnFollow.setOnClickListener {
                            if (followed){
                                viewModel.unfollowUser(user._id)
                                setFollowButton(followed = false)
                            }else{
                                viewModel.followUser(user._id)
                                setFollowButton(followed = true)
                            }
                            followed = !followed
                        }
                    }
                }
            }
        })
    }

    private fun setFollowButton(followed: Boolean) {
        if (followed){
            binding.btnFollow.apply {
                text = "Followed"
                setTextColor(ContextCompat.getColor(requireContext(),R.color.colorDarkBlue))
                background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_followed)
            }
        }else{
            binding.btnFollow.apply {
                text = "Follow"
                setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                background = ContextCompat.getDrawable(requireContext(),R.drawable.gradient_main)
            }
        }
    }

    private fun fetchProfilePosts(uid: String) {
        lifecycleScope.launch {
            viewModel.getProfilePostsFlow(uid).collectLatest {
                adapter.submitData(viewLifecycleOwner.lifecycle,it)
            }
        }
    }

    override fun onClick(post: Post) {
        findNavController().navigate(SearchedProfileFragmentDirections.actionSearchedProfileFragmentToSinglePostFragment(post))
    }
    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}