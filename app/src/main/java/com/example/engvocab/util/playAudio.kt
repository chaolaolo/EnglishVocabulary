package com.example.engvocab.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

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

class ExoPlayerState(private val player: Player) : Player.Listener {
    var isPlaying by mutableStateOf(player.isPlaying)
    var playbackState by mutableStateOf(player.playbackState)

    init {
        player.addListener(this)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        this.playbackState = playbackState
    }

    fun dispose() {
        player.removeListener(this)
    }
}

@Composable
fun rememberExoPlayerState(player: Player): ExoPlayerState {
    val state = remember(player) {
        ExoPlayerState(player)
    }

    DisposableEffect(state) {
        onDispose {
            state.dispose()
        }
    }
    return state
}