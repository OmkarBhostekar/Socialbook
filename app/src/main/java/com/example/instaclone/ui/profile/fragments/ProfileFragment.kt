package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.instaclone.R
import com.example.instaclone.comman.Constants.BIO
import com.example.instaclone.comman.Constants.FOLLOWERS_COUNT
import com.example.instaclone.comman.Constants.FOLLOWING_COUNT
import com.example.instaclone.comman.Constants.NAME
import com.example.instaclone.comman.Constants.POSTS_COUNT
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Constants.USER_IMAGE
import com.example.instaclone.comman.Constants.USER_NAME
import com.example.instaclone.comman.Resource
import com.example.instaclone.comman.SpannedGridLayoutManager
import com.example.instaclone.data.DataStore.readIntFromDS
import com.example.instaclone.data.DataStore.readStringFromDS
import com.example.instaclone.databinding.FragmentProfileBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.home.models.Post
import com.example.instaclone.ui.profile.ProfileViewModel
import com.example.instaclone.ui.profile.adapters.ProfilePostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile),ProfilePostsAdapter.OnClickListener {

    private var _bindng: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
    get() = _bindng!!
    private val viewModel: ProfileViewModel by activityViewModels()
    lateinit var adapter: ProfilePostsAdapter
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentProfileBinding.bind(view)

        adapter = ProfilePostsAdapter(this)

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

        binding.btnFollow.apply {
            text = "Edit Profile"
            setTextColor(ContextCompat.getColor(requireContext(),R.color.colorDarkBlue))
            background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_followed)
            setOnClickListener {
                // navigate to edit profile
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
        }

        viewModel.myProfile.observe(viewLifecycleOwner,{
            if (it is Resource.Success){
                it.data?.let { user ->
                    binding.apply {
                        btnBack.visibility = View.GONE
                        ivUserImage.setImage(user.userImage ?: "")
                        tvUserName.text = user.username
                        tvName.text = user.name
                        tvBio.text = user.bio
                        tvPostCount.text = (user.posts ?: 0).toString()
                        tvFollowerCount.text = (user.followers?.size ?: 0).toString()
                        tvFollowingCount.text = (user.following?.size ?: 0).toString()
                        btnFollowers.setOnClickListener {
                            findNavController().navigate(
                                ProfileFragmentDirections.actionProfileFragmentToFragmentBottomSheet("followers",user._id)
                            )
                        }
                        btnFollowing.setOnClickListener {
                            findNavController().navigate(
                                ProfileFragmentDirections.actionProfileFragmentToFragmentBottomSheet("following",user._id)
                            )
                        }
                    }
                }
            }
        })
        lifecycleScope.launch {
            fetchProfilePosts(readStringFromDS(UID,dataStore) ?: "")
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
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSinglePostFragment(post))
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}