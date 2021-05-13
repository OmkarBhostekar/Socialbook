package com.example.instaclone.ui.home

import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instaclone.InstaCloneApplication
import com.example.instaclone.comman.Constants.TOKEN
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Result
import com.example.instaclone.data.DataStore.readStringFromDS
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.home.models.Comment
import com.example.instaclone.ui.home.models.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class HomeViewModel @ViewModelInject constructor(
    val repository: HomeRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel(){

    val posts = MutableLiveData<List<Post>>()
    val comments = MutableLiveData<List<Comment>>()
    val likedBy = MutableLiveData<Result<List<User>>>()

    fun getAllPosts() = viewModelScope.launch {
        val response = repository.getAllPosts("Bearer ${readStringFromDS(TOKEN,dataStore)}")
        if (response.isSuccessful)
            posts.postValue(response.body()?.Result)
    }

    fun createNewPost(image: String, desc: String) = viewModelScope.launch {
        val body = HashMap<String,Any>()
        body["uid"] = readStringFromDS(UID,dataStore)?: ""
        body["image"] = image
        body["desc"] = desc
        val response = repository.createPost("Bearer ${readStringFromDS(TOKEN,dataStore)}",body)
        if (response.isSuccessful)
            showToast("Post created successfully")
        else
            showToast("An error occurred...")
    }

    fun likePost(postId: String) = viewModelScope.launch {
        val body = HashMap<String,Any>()
        repository.likePost(postId,"Bearer ${readStringFromDS(TOKEN,dataStore)}")
    }

    fun getLikedBy(postId: String) = viewModelScope.launch {
        likedBy.postValue(Result.Loading())
        val response = repository.getLikedBy(postId,"Bearer ${readStringFromDS(TOKEN,dataStore)}")
        if (response.isSuccessful)
            likedBy.postValue(Result.Success(response.body()!!.Result!!))
        else
            likedBy.postValue(Result.Error(""))
    }

    fun newComment(text: String,postId: String) = viewModelScope.launch {
        try{
            val body = HashMap<String, Any>()
            body["text"] = text
            val response = repository.commentOnPost(
                postId,
                "Bearer ${readStringFromDS(TOKEN,dataStore)}",
                body
            )
            if (response.isSuccessful)
                getComments(postId)
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun getComments(postId: String) = viewModelScope.launch {
        try{
            val response = repository.getAllCommentsOfPost(postId,"Bearer ${readStringFromDS(TOKEN,dataStore)}")
            if (response.isSuccessful && response.body()!!.Result != null && response.body()!!.Result!!.isNotEmpty())
                comments.postValue(response.body()!!.Result!!)
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    private suspend fun showToast(msg: String) = withContext(Dispatchers.Main){
        Toast.makeText(InstaCloneApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}