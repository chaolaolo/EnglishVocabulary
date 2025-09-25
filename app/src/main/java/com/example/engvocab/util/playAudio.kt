package com.example.engvocab.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

//@androidx.annotation.OptIn(UnstableApi::class)
fun playAudio(context: Context, exoPlayer: ExoPlayer, audioUrl: String) {
    try {
        val mediaItem = MediaItem.fromUri(audioUrl)

        exoPlayer.stop()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.e("AudioPlayer", "Lỗi phát âm thanh ExoPlayer: ${error.message}")
                Toast.makeText(context, "Lỗi: Không thể phát file.", Toast.LENGTH_LONG).show()
                exoPlayer.removeListener(this)
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if (state == Player.STATE_ENDED) {
                    exoPlayer.seekTo(0)
                    exoPlayer.playWhenReady = false
                    exoPlayer.removeListener(this)
                }
            }
        })

    } catch (e: Exception) {
        Log.e("AudioPlayer", "Lỗi cấu hình ExoPlayer: ${e.message}")
        Toast.makeText(context, "Lỗi phát âm thanh", Toast.LENGTH_SHORT).show()
    }
}