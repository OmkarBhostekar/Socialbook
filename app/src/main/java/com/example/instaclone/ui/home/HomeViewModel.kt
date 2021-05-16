package com.example.instaclone.ui.home

import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.instaclone.InstaCloneApplication
import com.example.instaclone.comman.Constants.TOKEN
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Resource
import com.example.instaclone.data.DataStore.readStringFromDS
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.home.models.Comment
import com.example.instaclone.ui.home.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: HomeRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel(){

    val posts = MutableLiveData<List<Post>>()
    val comments = MutableLiveData<List<Comment>>()
    val likedBy = MutableLiveData<Resource<List<User>>>()
    val postCreated = MutableLiveData<Resource<String>>()


    fun getPostsFlow() = repository.getPostsFlow().cachedIn(viewModelScope)

    fun createNewPost(image: String, desc: String) = viewModelScope.launch {
        val body = HashMap<String,Any>()
        body["image"] = image
        body["desc"] = desc
        val response = repository.createPost("Bearer ${readStringFromDS(TOKEN,dataStore)}",body)
        if (response.isSuccessful)
            postCreated.postValue(Resource.Success("Post created Successfully"))
        else
            postCreated.postValue(Resource.Error("An Error Occurred"))
    }

    fun likePost(postId: String) = viewModelScope.launch {
        val body = HashMap<String,Any>()
        repository.likePost(postId,"Bearer ${readStringFromDS(TOKEN,dataStore)}")
    }

    fun getLikedBy(postId: String) = viewModelScope.launch {
        likedBy.postValue(Resource.Loading())
        val response = repository.getLikedBy(postId,"Bearer ${readStringFromDS(TOKEN,dataStore)}")
        if (response.isSuccessful)
            likedBy.postValue(Resource.Success(response.body()!!.Result!!))
        else
            likedBy.postValue(Resource.Error(""))
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
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun getCommentsFlow(postId: String) = repository.getCommentsFlow(postId).cachedIn(viewModelScope)

    private suspend fun showToast(msg: String) = withContext(Dispatchers.Main){
        Toast.makeText(InstaCloneApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}