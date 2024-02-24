package com.example.myapplication.youtube.playlist

data class PlaylistItem(
    val kind: String,
    val tag: String,
    val id: String,
    val snippet: Snippet,
    val contentDetails: ContentDetails
)

