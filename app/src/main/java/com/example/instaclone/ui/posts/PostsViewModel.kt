package com.example.instaclone.ui.posts

import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instaclone.InstaCloneApplication
import com.example.instaclone.comman.Constants.TOKEN
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Result
import com.example.instaclone.data.DataStore.readStringFromDS
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.posts.models.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostsViewModel @ViewModelInject constructor(
    val repository: PostsRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel(){

    val posts = MutableLiveData<List<Post>>()
    val likedBy = MutableLiveData<Result<List<User>>>()

    fun getAllPosts() = viewModelScope.launch {
        val response = repository.getAllPosts("Bearer ${readStringFromDS(TOKEN,dataStore)}",readStringFromDS(UID,dataStore) ?: "")
        if (response.isSuccessful)
            posts.postValue(response.body()?.Result)
        else
            Log.d("MyActivity", "getAllPosts: error")
    }

    fun createNewPost(image: String, desc: String) = viewModelScope.launch {
        val body = HashMap<String,Any>()
        body["uid"] = readStringFromDS(UID,dataStore)?: ""
        body["image"] = image
        body["desc"] = desc
        val response = repository.createPost(body)
        if (response.isSuccessful)
            showToast("Post created successfully")
        else
            showToast("An error occurred...")
    }

    fun likePost(postId: String) = viewModelScope.launch {
        val body = HashMap<String,Any>()
        body["uid"] = readStringFromDS(UID,dataStore) ?: ""
        body["postId"] = postId
        repository.likePost(body)
    }

    fun getLikedBy(postId: String) = viewModelScope.launch {
        likedBy.postValue(Result.Loading())
        val response = repository.getLikedBy(postId,readStringFromDS(UID,dataStore) ?: "")
        if (response.isSuccessful)
            likedBy.postValue(Result.Success(response.body()!!))
        else
            likedBy.postValue(Result.Error(""))
    }

    private suspend fun showToast(msg: String) = withContext(Dispatchers.Main){
        Toast.makeText(InstaCloneApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}