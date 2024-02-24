package com.example.myapplication.youtube.playlist

data class PlaylistItemsResponse(
    val kind: String,
    val tag: String,
    val nextPageToken: String,
    val items: List<PlaylistItem>
)

