package com.example.instaclone.ui.auth

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instaclone.comman.Constants.BIO
import com.example.instaclone.comman.Constants.CREATED_AT
import com.example.instaclone.comman.Constants.EMAIL
import com.example.instaclone.comman.Constants.FOLLOWERS_COUNT
import com.example.instaclone.comman.Constants.FOLLOWING_COUNT
import com.example.instaclone.comman.Constants.IS_LOGGED_IN
import com.example.instaclone.comman.Constants.NAME
import com.example.instaclone.comman.Constants.POSTS_COUNT
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Constants.USER_IMAGE
import com.example.instaclone.comman.Constants.USER_NAME
import com.example.instaclone.ui.auth.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
        @Assisted savedStateHandle: SavedStateHandle,
        private val dataStore: DataStore<Preferences>,
        private val repository: AuthRepository
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun login(
            email: String,
            password: String
    ) = viewModelScope.launch {
        _loginUiState.value = LoginUiState.Loading
        val body = HashMap<String,Any>()
        body["email"] = email
        body["pass"] = password
        val response = repository.loginUser(body)
        if (response.isSuccessful){
            // save user to datastore
            saveUser(user = response.body()!!)
            delay(1000L)
            _loginUiState.value = LoginUiState.Success
        }else{
            _loginUiState.value = LoginUiState.Error("Error")
        }
    }


    fun register(
            name: String,
            username: String,
            email: String,
            password: String,
    ) = viewModelScope.launch {
        _loginUiState.value = LoginUiState.Loading
        val body = HashMap<String,Any>()
        body["name"] = name
        body["username"] = username
        body["email"] = email
        body["pass"] = password
        val response = repository.registerUser(body)
        if (response.isSuccessful){
            // save user to datastore
            saveUser(user = response.body()!!)
            // notify ui to registered
            _loginUiState.value = LoginUiState.Success
        }else{
            _loginUiState.value = LoginUiState.Error("Error")
        }
    }


    private suspend fun saveUser(user: User) {
        saveStringToDS(UID,user._id)
        saveStringToDS(BIO,user.bio)
        saveStringToDS(CREATED_AT,user.createdAt)
        saveStringToDS(EMAIL,user.email)
        saveStringToDS(NAME,user.name)
        saveStringToDS(USER_NAME,user.username)
        saveStringToDS(USER_IMAGE,user.userImage)
        saveIntToDS(FOLLOWERS_COUNT,user.followers.size)
        saveIntToDS(FOLLOWING_COUNT,user.following.size)
        saveIntToDS(POSTS_COUNT,user.posts.size)
        saveBooleeanToDS(IS_LOGGED_IN,true)
    }

    suspend fun saveStringToDS(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit {settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun saveIntToDS(key: String, value: Int) {
        val dataStoreKey = preferencesKey<Int>(key)
        dataStore.edit {settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun saveBooleeanToDS(key: String, value: Boolean) {
        val dataStoreKey = preferencesKey<Boolean>(key)
        dataStore.edit {settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun readStringFromDS(key: String) : String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
    suspend fun readIntFromDS(key: String) : Int? {
        val dataStoreKey = preferencesKey<Int>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
    suspend fun readBooleanFromDS(key: String) : Boolean {
        val dataStoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey] ?: false
    }

    sealed class LoginUiState{
        object Success: LoginUiState()
        data class Error(val message: String): LoginUiState()
        object Loading: LoginUiState()
        object Empty: LoginUiState()
    }
}