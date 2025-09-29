@file:Suppress("DEPRECATION")

package com.example.engvocab.ui.screens.home

import android.R.attr.name
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import com.example.engvocab.R
import com.example.engvocab.data.model.PronunciationDetail
import com.example.engvocab.data.model.Sense
import com.example.engvocab.data.model.Vocabulary
import com.example.engvocab.data.repository.VocabRepository
import com.example.engvocab.ui.navigation.Screen
import com.example.engvocab.util.playAudio
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabDetail(
    navController: NavHostController,
    vocabId: String
) {

    val owner = LocalSavedStateRegistryOwner.current
    val factory = remember(owner) {
        object : AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                handle["vocabId"] = vocabId

                return VocabDetailViewModel(
                    savedStateHandle = handle,
                    repository = VocabRepository()
                ) as T
            }

        }
    }
    val viewModel: VocabDetailViewModel = viewModel(factory = factory)

    val context = LocalContext.current
    val uiState = viewModel.uiState
    val vocabulary = uiState.vocabulary

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "3000 - 5000 Oxford Words",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                    ) {
                        Icon(
                            painterResource(R.drawable.round_arrow_back_ios_new),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.error != null -> {
                    Text(
                        "Lỗi: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                vocabulary != null -> {
                    VocabularyDetailContent(
                        vocabulary = vocabulary,
                        context = context,
                        navController
                    )
                }

                else -> {
                    Text(
                        "Không tìm thấy từ vựng",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VocabularyDetailContent(
    vocabulary: Vocabulary,
    context: Context,
    navController: NavController
) {
    val usPhonetic = vocabulary.phonetics?.us
    val ukPhonetic = vocabulary.phonetics?.uk

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setHandleAudioBecomingNoisy(true)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build(),
                true
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_DESTROY -> exoPlayer.release()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (exoPlayer.isReleased.not()) {
                exoPlayer.release()
            }
        }
    }
    // ----------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.Start
    ) {
        //Từ vựng và từ loại
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                vocabulary.word ?: "N/A", fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.alignByBaseline()
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = vocabulary.partOfSpeech?.let { "($it)" } ?: "", fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.End,
                modifier = Modifier.alignByBaseline()
            )
        }

        //phonetics
        Spacer(Modifier.height(8.dp))
        // PHÁT ÂM (US)
        usPhonetic?.let {
            PronunciationRow(
                detail = it,
                label = "US",
                context = context,
                exoPlayer = exoPlayer
            )
        }
        // PHÁT ÂM (UK)
        ukPhonetic?.let {
            PronunciationRow(
                detail = it,
                label = "UK",
                context = context,
                exoPlayer = exoPlayer
            )
        }

// Topics
        vocabulary.topics?.takeIf { it.isNotEmpty() }?.let { topics ->
            Text(
                text = "Topics:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                topics.mapNotNull { it.name }.forEach { topicName ->
                    Text(
                        text = topicName.replaceFirstChar { it.uppercase() },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .clickable {
                                val encodedName = URLEncoder.encode(
                                    topicName.trim(),
                                    StandardCharsets.UTF_8.toString()
                                )
                                navController.navigate(
                                    Screen.VocabularyOfTopic.createRoute(
                                        encodedName
                                    )
                                )
                            }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Divider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(16.dp))

//      Senses Display
        Text(
            "Define:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))

        vocabulary.senses?.forEachIndexed { index, sense ->
            DefinitionItem(index + 1, sense)
        }
    }
}

@Composable
fun DefinitionItem(index: Int, sense: Sense) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        // Định nghĩa
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "$index. ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                sense.definition ?: "None Define",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Ví dụ
        sense.examples?.forEach { example ->
            Row(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
                Text(
                    "• ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    example,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Xử lý các sub-sense (nếu có)
        sense.senses?.forEachIndexed { subIndex, subSense ->
            SubDefinitionItem(index, subIndex + 1, subSense)
        }
    }
}

@Composable
fun SubDefinitionItem(mainIndex: Int, subIndex: Int, sense: Sense) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
        // Định nghĩa Sub-Sense
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "$mainIndex.$subIndex. ",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                sense.definition ?: "None SubDefine",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Ví dụ Sub-Sense
        sense.examples?.forEach { example ->
            Text(
                "    - $example",
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PronunciationRow(
    detail: PronunciationDetail,
    label: String,
    context: Context,
    exoPlayer: ExoPlayer
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                detail.audio?.let { url ->
                    playAudio(context, exoPlayer, url)
                }
            },
            enabled = detail.audio != null,
            colors = IconButtonDefaults.iconButtonColors()
        ) {
            Icon(
                painter = painterResource(R.drawable.round_volume_up_24),
                contentDescription = "$label Audio",
                tint = if (detail.audio != null) MaterialTheme.colorScheme.primary else Color.LightGray
            )
        }
        Text(
            "$label: ${detail.text ?: "N/A"}", fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
