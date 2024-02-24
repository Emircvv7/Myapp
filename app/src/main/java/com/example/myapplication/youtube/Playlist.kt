package com.example.myapplication.youtube

import java.io.Serializable

data class Playlist(val id: Int, val title: String, val videoCount: Int, val description: String = "") : Serializable
