package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_explore) {

    private var _bindng: FragmentExploreBinding? = null
    private val binding:FragmentExploreBinding
        get() =  _bindng!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentExploreBinding.bind(view)

//        binding.ivPost.setOnClickListener {
//            val extras = FragmentNavigatorExtras(
//                binding.ivPost to "image_big"
//                )
//            findNavController().navigate(R.id.action_searchFragment_to_fragment2,
//            null,null,extras
//            )
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}