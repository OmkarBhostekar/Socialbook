package com.example.instaclone.ui.polls

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.instaclone.ui.polls.paging.PollsPagingSource
import javax.inject.Inject

class PollRepository @Inject constructor(
    private val api: PollApi
) {

    fun gePolls(dataStore: DataStore<Preferences>) =
        Pager(
            config = PagingConfig(
            pageSize = 10,
            maxSize = 1000,
            enablePlaceholders = false
            ),
            pagingSourceFactory = { PollsPagingSource(api,dataStore = dataStore) }
        ).flow

    suspend fun addOrUpdateVote(pollId: String, token: String, body: HashMap<String,Any>) = api.addOrUpdateVote(pollId, token, body)
}