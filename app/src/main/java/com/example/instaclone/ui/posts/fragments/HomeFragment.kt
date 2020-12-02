package com.example.instaclone.ui.posts.fragments

import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaclone.R
import com.example.instaclone.comman.Constants.IS_LOGGED_IN
import com.example.instaclone.databinding.FragmentHomeBinding
import com.example.instaclone.ui.posts.PostsViewModel
import com.example.instaclone.ui.posts.adapters.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), PostsAdapter.OnClickListener {

    private var _bindng: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() =  _bindng!!
    private val viewModel: PostsViewModel by viewModels()
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentHomeBinding.bind(view)

        viewModel.getAllPosts()
        val postsAdapter = PostsAdapter(this)
        binding.rvPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.posts.observe(viewLifecycleOwner,{
            postsAdapter.posts = it
        })
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            readBooleanFromDS(IS_LOGGED_IN)?.let {
                if (it){
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
            }
        }
    }

    private suspend fun readBooleanFromDS(key: String) : Boolean? {
        val dataStoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override fun onLike(postId: String) {
        viewModel.likePost(postId)
    }

    override fun viewLikes(postId: String) {
        // show dialog that will show users who liked that post
        viewModel.getLikedBy(postId)
    }
}