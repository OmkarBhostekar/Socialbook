package com.example.instaclone.ui.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.instaclone.ui.profile.paging.ProfilePostsPagingSource
import com.example.instaclone.ui.profile.paging.SearchProfilePagingSource
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val api: ProfileApi,
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getProfile(uid: String?,token:String?) = api.getProfile(uid,token)

    fun getProfilePosts(uid: String) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 1000,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProfilePostsPagingSource(api,dataStore,uid) }
        ).flow

    suspend fun followUser(uid: String, token: String) = api.followUser(uid, token)

    suspend fun unfollowUser(uid: String, token: String) = api.unfollowUser(uid, token)

    suspend fun updateUser(body: HashMap<String, Any>, token: String) = api.updateProfile(token, body)

    suspend fun getUserFollowers(uid: String, token: String) = api.getUserFollowers(uid, token)

    suspend fun getUserFollowing(uid: String, token: String) = api.getUserFollowing(uid, token)

    fun searchProfile(token: String,q: String) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 1000,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchProfilePagingSource(
                api,
                token,
                q
            ) }
        ).flow

}