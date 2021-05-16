package com.example.instaclone.ui.profile.paging

import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.instaclone.comman.Constants
import com.example.instaclone.data.DataStore
import com.example.instaclone.ui.home.HomeApi
import com.example.instaclone.ui.home.models.Post
import com.example.instaclone.ui.profile.ProfileApi
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class ProfilePostsPagingSource(
    private val api: ProfileApi,
    private val datastore: androidx.datastore.core.DataStore<Preferences>,
    private val uid: String
    ) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val position = params.key ?: 1
        return try {
            val response = api.getProfilePosts(
                uid,
                "Bearer ${
                DataStore.readStringFromDS(
                    Constants.TOKEN,
                    datastore
                )}",
                position)
            LoadResult.Page(
                data = response.Result!!.data,
                prevKey = if (position == 1) null else position-1,
                nextKey = if (position != response.Result.totalPages) position+1 else null
            )
        }catch (e: SocketTimeoutException){
            LoadResult.Error(e)
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return null
    }

}