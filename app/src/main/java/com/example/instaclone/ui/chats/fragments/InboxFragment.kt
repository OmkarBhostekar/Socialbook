package com.example.instaclone.ui.chats.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentInboxBinding

class InboxFragment : Fragment(R.layout.fragment_inbox){

    private var _binding: FragmentInboxBinding? = null
    private val binding: FragmentInboxBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInboxBinding.bind(view)
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}