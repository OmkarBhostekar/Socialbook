package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaclone.R
import com.example.instaclone.comman.Result
import com.example.instaclone.databinding.FollowersBottomSheetBinding
import com.example.instaclone.ui.posts.PostsViewModel
import com.example.instaclone.ui.profile.adapters.FollowersAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentBottomSheet : BottomSheetDialogFragment(){

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
    private val postsViewModel: PostsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FollowersBottomSheetBinding.bind(view)

        val type = args.type
        val id = args.id

        binding.progressBar.isVisible = true

        if (type == "like"){
            binding.tvTitle.text = "Post is Liked by"
            postsViewModel.getLikedBy(id)
            postsViewModel.likedBy.observe(viewLifecycleOwner,{
                if (it is Result.Success){
                    binding.progressBar.isVisible = false
                    binding.rvFollowers.adapter = FollowersAdapter(it.data!!)
                    binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
                }
            })
        }

    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}