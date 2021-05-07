package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.example.instaclone.R
import com.example.instaclone.databinding.Fragment2Binding

class Fragment2 : Fragment(R.layout.fragment_2){

    private var _binding: Fragment2Binding? = null
    private val binding: Fragment2Binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = Fragment2Binding.bind(view)

        val transition = TransitionInflater.from(activity).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}