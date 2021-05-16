package com.example.instaclone.ui.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instaclone.comman.Constants.TOKEN
import com.example.instaclone.comman.Resource
import com.example.instaclone.ui.auth.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel(){

    val userProfile = MutableLiveData<Resource<User>>()

    fun getProfile(uid: String) = viewModelScope.launch {
        try{
            userProfile.postValue(Resource.Loading())
            val response = repository.getProfile(uid,
                "Bearer ${com.example.instaclone.data.DataStore.readStringFromDS(TOKEN,dataStore)}")
            if (response.isSuccessful)
                userProfile.postValue(Resource.Success(response.body()!!.Result!!))
            else
                userProfile.postValue(Resource.Error("An Error Occurred"))
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }

    fun getProfilePostsFlow(uid: String) = repository.getProfilePosts(uid)

}