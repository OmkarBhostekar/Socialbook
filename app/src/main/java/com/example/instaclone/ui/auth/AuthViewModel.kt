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
import com.example.instaclone.comman.Constants.TOKEN
import com.example.instaclone.comman.Constants.UID
import com.example.instaclone.comman.Constants.USER_IMAGE
import com.example.instaclone.comman.Constants.USER_NAME
import com.example.instaclone.data.DataStore.saveBooleeanToDS
import com.example.instaclone.data.DataStore.saveStringToDS
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
            response.body()!!.Result!!.token?.let {
                saveBooleeanToDS(IS_LOGGED_IN,true,dataStore)
                saveStringToDS(TOKEN, it,dataStore)
            }
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
            response.body()!!.Result!!.token?.let {
                saveBooleeanToDS(IS_LOGGED_IN,true,dataStore)
                saveStringToDS(TOKEN, it,dataStore)
            }
            // notify ui to registered
            _loginUiState.value = LoginUiState.Success
        }else{
            _loginUiState.value = LoginUiState.Error("Error")
        }
    }


    private suspend fun saveUser(user: User) {
        saveStringToDS(UID,user._id,dataStore)
        saveStringToDS(BIO,user.bio!!,dataStore)
        saveStringToDS(CREATED_AT,user.createdAt!!,dataStore)
        saveStringToDS(EMAIL,user.email!!,dataStore)
        saveStringToDS(NAME,user.name!!,dataStore)
        saveStringToDS(USER_NAME,user.username!!,dataStore)
        saveStringToDS(USER_IMAGE,user.userImage!!,dataStore)
        saveBooleeanToDS(IS_LOGGED_IN,true,dataStore)
    }



    sealed class LoginUiState{
        object Success: LoginUiState()
        data class Error(val message: String): LoginUiState()
        object Loading: LoginUiState()
        object Empty: LoginUiState()
    }
}