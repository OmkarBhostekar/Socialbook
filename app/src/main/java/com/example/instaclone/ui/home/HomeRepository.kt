package com.example.instaclone.ui.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.instaclone.ui.home.paging.CommentsPagingSource
import com.example.instaclone.ui.home.paging.PostsPagingSource
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: HomeApi,
    private val datastore: DataStore<Preferences>
) {

    suspend fun getAllPosts(token: String) = api.getAllPosts(token)

    fun getPostsFlow() =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 1000,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostsPagingSource(api,datastore) }
        ).flow

    suspend fun createPost(token: String,body: HashMap<String,Any>) = api.createNewPost(token, body)

    suspend fun likePost(postId: String,token: String) = api.likePost(postId, token)

    suspend fun getLikedBy(postId: String,token: String) = api.getLikedBy(postId, token)

    fun getCommentsFlow(postId: String) =
        Pager(
            config =  PagingConfig(
                pageSize = 10,
                maxSize = 1000,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CommentsPagingSource(api,postId,datastore) }
        ).flow

    suspend fun commentOnPost(postId: String,token: String,body: HashMap<String,Any>)
    = api.commentOnPost(postId, token, body)

}