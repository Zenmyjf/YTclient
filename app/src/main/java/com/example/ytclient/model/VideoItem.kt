package com.example.ytclient.model

data class VideoItem(
    val videoId: String,
    val title: String,
    val channelTitle: String,
    val thumbnailUrl: String
)

// --- Raw response models matching YouTube Data API v3 "search" endpoint ---

data class SearchResponse(
    val items: List<SearchItem> = emptyList()
)

data class SearchItem(
    val id: SearchId,
    val snippet: SearchSnippet
)

data class SearchId(
    val videoId: String? = null
)

data class SearchSnippet(
    val title: String,
    val channelTitle: String,
    val thumbnails: Thumbnails
)

data class Thumbnails(
    val medium: Thumbnail? = null,
    val high: Thumbnail? = null,
    val default: Thumbnail? = null
)

data class Thumbnail(
    val url: String
)

// --- Raw response models matching YouTube Data API v3 "videos" endpoint (used for trending) ---

data class TrendingResponse(
    val items: List<TrendingItem> = emptyList()
)

data class TrendingItem(
    val id: String,
    val snippet: SearchSnippet
)

fun TrendingItem.toVideoItem(): VideoItem {
    val thumb = snippet.thumbnails.high?.url
        ?: snippet.thumbnails.medium?.url
        ?: snippet.thumbnails.default?.url
        ?: ""
    return VideoItem(
        videoId = id,
        title = snippet.title,
        channelTitle = snippet.channelTitle,
        thumbnailUrl = thumb
    )
}

fun SearchItem.toVideoItem(): VideoItem? {
    val id = id.videoId ?: return null
    val thumb = snippet.thumbnails.high?.url
        ?: snippet.thumbnails.medium?.url
        ?: snippet.thumbnails.default?.url
        ?: ""
    return VideoItem(
        videoId = id,
        title = snippet.title,
        channelTitle = snippet.channelTitle,
        thumbnailUrl = thumb
    )
}
