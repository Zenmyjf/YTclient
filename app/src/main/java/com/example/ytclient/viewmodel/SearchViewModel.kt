package com.example.ytclient.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytclient.BuildConfig
import com.example.ytclient.api.RetrofitInstance
import com.example.ytclient.model.VideoItem
import com.example.ytclient.model.toVideoItem
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    var videos by mutableStateOf<List<VideoItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val apiKey: String get() = BuildConfig.YOUTUBE_API_KEY

    fun search(query: String) {
        if (query.isBlank()) return
        if (apiKey.isBlank()) {
            errorMessage = "No API key configured. Add YOUTUBE_API_KEY as a GitHub secret."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitInstance.api.search(query = query, apiKey = apiKey)
                videos = response.items.mapNotNull { it.toVideoItem() }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Something went wrong while searching."
            } finally {
                isLoading = false
            }
        }
    }

    fun loadTrending() {
        if (apiKey.isBlank()) {
            errorMessage = "No API key configured. Add YOUTUBE_API_KEY as a GitHub secret."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitInstance.api.trending(apiKey = apiKey)
                videos = response.items.map { it.toVideoItem() }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Something went wrong while loading trending videos."
            } finally {
                isLoading = false
            }
        }
    }
}
