package com.example.instaclone.ui.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.instaclone.comman.Constants.BIO
import com.example.instaclone.comman.Constants.FOLLOWERS_COUNT
import com.example.instaclone.comman.Constants.FOLLOWING_COUNT
import com.example.instaclone.comman.Constants.NAME
import com.example.instaclone.comman.Constants.POSTS_COUNT
import com.example.instaclone.comman.Constants.TOKEN
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Constants.USER_IMAGE
import com.example.instaclone.comman.Constants.USER_NAME
import com.example.instaclone.comman.Resource
import com.example.instaclone.data.DataStore.readStringFromDS
import com.example.instaclone.data.DataStore.saveIntToDS
import com.example.instaclone.data.DataStore.saveStringToDS
import com.example.instaclone.ui.auth.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel(){

    val searchedProfile = MutableLiveData<Resource<User>>()
    val myProfile = MutableLiveData<Resource<User>>()
    var profileLoaded = false
    val followers = MutableLiveData<Resource<List<User>>>()
    val profileUpdated = MutableLiveData(false)
    var token = ""

    init {
        viewModelScope.launch {
            token = "Bearer ${readStringFromDS(TOKEN,dataStore)}"
        }
    }

    fun getMyProfile() = viewModelScope.launch {
        if (!profileLoaded){
            try{
                myProfile.postValue(Resource.Loading())
                val response = repository.getProfile(
                    readStringFromDS(UID,dataStore),
                    "Bearer ${readStringFromDS(TOKEN,dataStore)}"
                )
                if (response.isSuccessful) {
                    myProfile.postValue(Resource.Success(response.body()!!.Result!!))
                    saveProfileData(response.body()!!.Result)
                    profileLoaded = true
                }
            }catch(e: Exception){
            }catch(e: SocketTimeoutException){
            }
        }
    }

    private suspend fun saveProfileData(user: User?) {
        user?.let {
            saveStringToDS(NAME, user.name!!, dataStore)
            saveStringToDS(USER_NAME, user.username!!, dataStore)
            saveStringToDS(USER_IMAGE, user.userImage!!, dataStore)
            saveStringToDS(BIO, user.bio!!, dataStore)
            saveIntToDS(FOLLOWERS_COUNT, user.followers?.size ?: 0, dataStore)
            saveIntToDS(FOLLOWING_COUNT, user.following?.size ?: 0, dataStore)
            saveIntToDS(POSTS_COUNT, user.posts ?: 0, dataStore)
        }
    }

    fun getSearchedProfile(uid: String) = viewModelScope.launch {
        try{
            searchedProfile.postValue(Resource.Loading())
            val response = repository.getProfile(uid,
                "Bearer ${readStringFromDS(TOKEN,dataStore)}")
            if (response.isSuccessful)
                searchedProfile.postValue(Resource.Success(response.body()!!.Result!!))
            else
                searchedProfile.postValue(Resource.Error("An Error Occurred"))
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun followUser(uid: String) = viewModelScope.launch {
        try{
            repository.followUser(
                uid = uid,
                token = "Bearer ${readStringFromDS(TOKEN,dataStore)}"
            )
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun unfollowUser(uid: String) = viewModelScope.launch {
        try{
            repository.unfollowUser(
                uid = uid,
                token = "Bearer ${readStringFromDS(TOKEN,dataStore)}"
            )
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun updateProfile(body :HashMap<String, Any>) = viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = repository.updateUser(
                body = body,
                token = "Bearer ${readStringFromDS(TOKEN,dataStore)}"
            )
            if (response.isSuccessful){
                profileLoaded = false
                getMyProfile()
            }
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun getUserFollowers(uid: String) = viewModelScope.launch(Dispatchers.IO) {
        try{
            followers.postValue(Resource.Loading())
            val response = repository.getUserFollowers(
                uid = uid,
                token = "Bearer ${readStringFromDS(TOKEN,dataStore)}"
            )
            if (response.isSuccessful)
                followers.postValue(Resource.Success(response.body()!!.Result!!))
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun getUserFollowing(uid: String) = viewModelScope.launch(Dispatchers.IO) {
        try{
            followers.postValue(Resource.Loading())
            val response = repository.getUserFollowing(
                uid = uid,
                token = "Bearer ${readStringFromDS(TOKEN,dataStore)}"
            )
            if (response.isSuccessful)
                followers.postValue(Resource.Success(response.body()!!.Result!!))
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun getProfilePostsFlow(uid: String) = repository.getProfilePosts(uid)

    fun searchProfileFlow(q: String) = repository.searchProfile(
        token = token,
        q = q
    ).cachedIn(viewModelScope)

}