package com.example.ytclient.api

import com.example.ytclient.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {

    @GET("search")
    suspend fun search(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") apiKey: String
    ): SearchResponse

    @GET("videos")
    suspend fun trending(
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "US",
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") apiKey: String
    ): TrendingResponse
}
