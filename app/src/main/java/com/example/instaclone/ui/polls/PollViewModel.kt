package com.example.instaclone.ui.polls

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PollViewModel @Inject constructor(
    private val repository: PollRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    fun getPolls() = repository.gePolls(dataStore).cachedIn(viewModelScope)

}