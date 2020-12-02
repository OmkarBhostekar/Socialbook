package com.example.instaclone.ui.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
        val api: AuthApi
) {

    suspend fun loginUser(body: HashMap<String,Any>) = api.login(body)

    suspend fun registerUser(body: HashMap<String,Any>) = api.register(body)

}