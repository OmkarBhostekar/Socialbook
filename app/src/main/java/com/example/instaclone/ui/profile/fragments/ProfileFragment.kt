package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.instaclone.R
import com.example.instaclone.comman.Resource
import com.example.instaclone.databinding.FragmentProfileBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.profile.ProfileViewModel
import com.example.instaclone.ui.profile.adapters.ProfilePostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile),ProfilePostsAdapter.OnClickListener {

    private var _bindng: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
    get() = _bindng!!
    private val args: ProfileFragmentArgs by navArgs()
    private val viewModel: ProfileViewModel by viewModels()
    lateinit var adapter: ProfilePostsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentProfileBinding.bind(view)

        adapter = ProfilePostsAdapter(this)
        args.userId?.let {
            viewModel.getProfile(it)
            fetchProfilePosts(it)
        }

        binding.rvProfilePosts.adapter = adapter

        viewModel.userProfile.observe(viewLifecycleOwner,{
            if (it is Resource.Success) {
                binding.apply {
                    it.data?.let { user ->
                        ivUserImage.setImage(user.userImage!!)
                        tvUserName.text = user.username
                        tvName.text = user.name
                        tvBio.text = user.bio
                        tvPostCount.text = (user.posts?.size ?: 0).toString()
                        tvFollowerCount.text = (user.followers?.size ?: 0).toString()
                        tvFollowingCount.text = (user.following?.size ?: 0).toString()
//                        if (user.isFollowed!!){
//                            btnFollow.text = "Followed"
//                        }
                    }
                }
            }
        })
    }

    private fun fetchProfilePosts(uid: String) {
        lifecycleScope.launch {
            viewModel.getProfilePostsFlow(uid).collectLatest {
                adapter.submitData(viewLifecycleOwner.lifecycle,it)
            }
        }
    }

    override fun onClick(postId: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}