package com.example.instaclone.ui.auth

import com.example.instaclone.ui.auth.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/login")
    suspend fun login(
        @Body body: HashMap<String,Any>
    ) : Response<User>

    @POST("/auth/register")
    suspend fun register(
            @Body body: HashMap<String,Any>
    ) : Response<User>
}