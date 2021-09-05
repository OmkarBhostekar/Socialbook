package com.example.instaclone.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentHomeBinding
import com.example.instaclone.ui.MainActivity
import com.example.instaclone.ui.home.HomeViewModel
import com.example.instaclone.ui.home.adapters.PostsAdapter
import com.example.instaclone.ui.home.models.Post
import com.example.instaclone.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), PostsAdapter.OnClickListener {

    private var _bindng: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() =  _bindng!!
    private val viewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentHomeBinding.bind(view)

        (activity as MainActivity).setToolbarTitle("SocialBook")

        val postsAdapter = PostsAdapter(this)
        binding.rvPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        postsAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }

        profileViewModel.getMyProfile()

        lifecycleScope.launch {
            viewModel.getPostsFlow().collectLatest {
                postsAdapter.submitData(viewLifecycleOwner.lifecycle,it)
            }
        }

    }

    override fun onLike(postId: String) {
        viewModel.likePost(postId)
    }

    override fun viewLikes(postId: String) {
        // show dialog that will show users who liked that post
        findNavController().navigate(R.id.action_homeFragment_to_fragmentBottomSheet,Bundle().apply {
            putString("type","like")
            putString("id",postId)
        })
    }

    override fun onClick(post: Post) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(
            post.user.username,post.user.userImage,post._id,post.description,post.user._id
        ))
    }

    override fun onUserClick(uid: String) {
        findNavController().navigate(R.id.searchedProfileFragment,Bundle().apply {
            putString("userId",uid)
        })
    }
}