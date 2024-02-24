package com.example.myapplication.youtube.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityPlaylistBinding
import com.example.myapplication.youtube.data.repository.YouTubeRepository
import com.example.myapplication.youtube.playlist.PlaylistItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class PlaylistActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPlaylistBinding.inflate(layoutInflater)
    }

    private val repository: YouTubeRepository by inject()
    private lateinit var adapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        adapter = PlaylistAdapter { playlistItem ->
            onPlaylistItemSelected(playlistItem)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PlaylistActivity)
            adapter = this@PlaylistActivity.adapter
        }

        // Launch a coroutine to fetch playlists
        CoroutineScope(Dispatchers.Main).launch {
            getPlaylistsAndUpdateUI()
        }
    }

    private fun onPlaylistItemSelected(playlistItem: PlaylistItem) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("playlistItemId", playlistItem.id)
        startActivity(intent)
    }

    private suspend fun getPlaylistsAndUpdateUI() = withContext(Dispatchers.IO) {
        val playlistItemsResponse = repository.getPlaylists()

        val playlistItems = playlistItemsResponse.items

        withContext(Dispatchers.Main) {
            adapter.submitList(playlistItems)
        }
    }
}
