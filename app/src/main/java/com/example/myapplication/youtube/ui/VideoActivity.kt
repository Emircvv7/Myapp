// VideoActivity.kt
package com.example.myapplication.youtube.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityVideoBinding
import com.example.myapplication.youtube.Video
import com.example.myapplication.youtube.data.repository.YouTubeRepository
import com.example.myapplication.youtube.playlist.PlaylistItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class VideoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityVideoBinding.inflate(layoutInflater)
    }

    private val repository: YouTubeRepository by inject()
    private lateinit var adapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        adapter = VideoAdapter { videoItem ->
            onVideoItemSelected(videoItem)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@VideoActivity)
            adapter = this@VideoActivity.adapter
        }

        val playlistItemId = intent.getStringExtra("playlistItemId")
        if (playlistItemId != null) {
            lifecycleScope.launch {
                getVideosAndUpdateUI(playlistItemId)
            }
        }
    }

    private fun onVideoItemSelected(videoItem: PlaylistItem) {
        // Handle video item selection
    }

    private suspend fun getVideosAndUpdateUI(playlistId: String) = withContext(Dispatchers.IO) {
        val videoItemsResponse = repository.getVideos(playlistId)

        val videoItems = videoItemsResponse.items

        withContext(Dispatchers.Main) {
            adapter.submitList(videoItems)
        }
    }
}
