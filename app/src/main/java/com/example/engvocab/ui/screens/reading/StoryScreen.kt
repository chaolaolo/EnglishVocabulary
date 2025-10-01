package com.example.engvocab.ui.screens.reading

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.example.engvocab.R
import com.example.engvocab.data.model.Story
import com.example.engvocab.data.model.SubReadingTopic
import com.example.engvocab.ui.navigation.Screen
import com.example.engvocab.util.AppViewModelProvider
import com.example.engvocab.util.playAudio
import com.example.engvocab.util.rememberExoPlayerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    navController: NavHostController,
    readingId: String,
    subTopicId: String,
    storyId: String,
    viewModel: ReadingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.storyDetailUiState.collectAsState()
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build(),
                true // handleAudioFocus=true
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    LaunchedEffect(readingId, subTopicId, storyId) {
        exoPlayer.stop()
        viewModel.fetchStoryDetail(readingId, subTopicId, storyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.round_arrow_back_ios_new),
                            contentDescription = "Back"
                        )
                    }
                },
                windowInsets = WindowInsets(0.dp)
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lỗi: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            uiState.story != null -> {
                StoryDetailContent(
                    story = uiState.story!!,
                    innerPadding,
                    exoPlayer = exoPlayer
                )
            }

            else -> {
                Text(text = "Không có Reading nào được tìm thấy.")
            }

        }
    }
}


@Composable
fun StoryDetailContent(
    story: Story, innerPadding: PaddingValues,
    exoPlayer: ExoPlayer
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // ảnh
            val imageUrl = story.details?.imageUrl ?: story.image
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = story.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            // Title
            story.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            story.details?.audioUrl?.let { audioUrl ->
                AudioPlayerControls(
                    audioUrl = audioUrl,
                    exoPlayer = exoPlayer,
                    onPlayClick = { url ->
                        playAudio(context, exoPlayer, url)
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // HeaderTitle từ StoryDetails
            story.details?.headerTitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                )
            }

            // content
            story.details?.content?.let { content ->
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AudioPlayerControls(
    audioUrl: String,
    exoPlayer: ExoPlayer,
    onPlayClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val playerState = rememberExoPlayerState(player = exoPlayer)

    val isPlaying = playerState.isPlaying
    val playbackState = playerState.playbackState

    Column(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {

            // Nút Play/Pause
            IconButton(
                onClick = {
                    if (isPlaying) {
                        exoPlayer.pause()
                    } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
                        exoPlayer.play()
                    } else {
                        onPlayClick(audioUrl)
                    }
                },
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            ) {
                Icon(
                    painter = painterResource(
                        if (isPlaying) R.drawable.round_pause_24 else R.drawable.round_play_arrow_24
                    ),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize(0.8f)
                )
            }
        }
        Text(
            text = "00:00:01",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )
    }
}