package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaclone.R
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Resource
import com.example.instaclone.databinding.FollowersBottomSheetBinding
import com.example.instaclone.ui.MainActivity
import com.example.instaclone.ui.home.HomeViewModel
import com.example.instaclone.ui.profile.ProfileViewModel
import com.example.instaclone.ui.profile.adapters.FollowersAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentBottomSheet : BottomSheetDialogFragment(),FollowersAdapter.OnClickListener{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.followers_bottom_sheet,container,false)
    }

    private var _binding: FollowersBottomSheetBinding? = null
    private val binding: FollowersBottomSheetBinding
        get() = _binding!!
    private val args: FragmentBottomSheetArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FollowersBottomSheetBinding.bind(view)

        val type = args.type
        val id = args.id

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.progressBar.isVisible = true
        when(type){
            "like" -> {
                binding.tvTitle.text = "Post is Liked by"
                homeViewModel.getLikedBy(id)
                homeViewModel.likedBy.observe(viewLifecycleOwner,{
                    if (it is Resource.Success){
                        binding.progressBar.isVisible = false
                        lifecycleScope.launch {
                            binding.rvFollowers.adapter = FollowersAdapter(it.data!!,
                                com.example.instaclone.data.DataStore.readStringFromDS(UID,dataStore) ?: "",
                                this@FragmentBottomSheet
                            )
                        }
                    }
                })
            }
            "followers" -> {
                (activity as MainActivity).showToolbar(false)
                binding.tvTitle.text = "User Followers"
                profileViewModel.getUserFollowers(id)
                profileViewModel.followers.observe(viewLifecycleOwner,{
                    if (it is Resource.Success){
                        binding.progressBar.isVisible = false
                        lifecycleScope.launch {
                            binding.rvFollowers.adapter = FollowersAdapter(it.data!!,
                                com.example.instaclone.data.DataStore.readStringFromDS(UID,dataStore) ?: "",
                                this@FragmentBottomSheet
                            )
                        }
                    }
                })
            }
            "following" -> {
                (activity as MainActivity).showToolbar(false)
                binding.tvTitle.text = "User Following"
                profileViewModel.getUserFollowing(id)
                profileViewModel.followers.observe(viewLifecycleOwner,{
                    if (it is Resource.Success){
                        binding.progressBar.isVisible = false
                        lifecycleScope.launch {
                            binding.rvFollowers.adapter = FollowersAdapter(it.data!!,
                                com.example.instaclone.data.DataStore.readStringFromDS(UID,dataStore) ?: "",
                                this@FragmentBottomSheet
                            )
                        }
                    }
                })
            }
        }
    }

    override fun onViewProfileClick(uid: String) {
        findNavController().navigate(R.id.searchedProfileFragment,Bundle().apply {
            putString("userId",uid)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}