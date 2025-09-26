package com.example.engvocab.ui.screens.search

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.engvocab.R
import com.example.engvocab.data.model.Vocabulary
import com.example.engvocab.ui.navigation.Screen
import com.example.engvocab.ui.screens.home.HomeViewModel
import com.example.engvocab.util.playAudio
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val displayList = if (uiState.searchQuery.isNotBlank() || uiState.isSearching) {
        uiState.searchResults
    } else {
        uiState.vocabularyOnPage
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { newText ->
                            // Gọi hàm tìm kiếm mỗi khi văn bản thay đổi
                            viewModel.searchVocabulary(newText)
                        },
                        placeholder = { Text("Nhập từ để tìm kiếm...") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.searchVocabulary("") }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Clear Search")
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        ),
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background, // 👉 đổi màu nền tại đây
                    titleContentColor = MaterialTheme.colorScheme.onBackground, // 👉 màu chữ
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground // 👉 màu icon
                ),
                windowInsets = WindowInsets(0.dp)
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                )
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                uiState.error != null -> {
                    Text(
                        text = "Lỗi: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        items(displayList) { vocab ->
                            VocabularyCard(
                                item = vocab,
                                onClick = {
                                    vocab.id?.let { id ->
                                        val encodedId =
                                            URLEncoder.encode(id, StandardCharsets.UTF_8.toString())
                                        navController.navigate(
                                            Screen.VocabDetail.createRoute(
                                                encodedId
                                            )
                                        )
                                    }
                                },
                            )
                        }

                        if (displayList.isEmpty() && uiState.searchQuery.isNotBlank() && !uiState.isSearching) {
                            item {
                                Text(
                                    text = "Không tìm thấy từ vựng nào.",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else if (uiState.vocabularyOnPage.isEmpty() && uiState.searchQuery.isBlank() && uiState.currentPage > 1) {
                            item {
                                Text(
                                    text = "No more data",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PageCounter(
    currentPage: Int,
    totalPages: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Trang trước
        IconButton(
            onClick = onPreviousClick,
            enabled = currentPage > 1, // Vô hiệu hóa khi ở trang 1
        ) {
            Icon(
                // Sử dụng Icon đơn giản, bạn có thể thay bằng Arrow icon
                painter = painterResource(R.drawable.round_arrow_back_ios_new),
                contentDescription = "Previous Page",
                tint = if (currentPage > 1) MaterialTheme.colorScheme.primary else Color.LightGray
            )
        }
        Spacer(Modifier.width(8.dp))
        // Hiển thị số trang (1/100)
        Card(
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Text(
                text = "$currentPage/$totalPages",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        // Nút Trang tiếp theo
        IconButton(
            onClick = onNextClick,
            enabled = currentPage < totalPages, // Vô hiệu hóa khi ở trang cuối
        ) {
            Icon(
                painter = painterResource(R.drawable.round_arrow_forward_ios),
                contentDescription = "Next Page",
                tint = if (currentPage < totalPages) MaterialTheme.colorScheme.primary else Color.LightGray
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VocabularyCard(
    item: Vocabulary,
    onClick: () -> Unit = {}
) {
    // Lấy phiên âm (ưu tiên US, nếu không có thì UK, nếu không thì rỗng)
    val phoneticsText = item.phonetics?.us?.text ?: item.phonetics?.uk?.text ?: ""
    // Lấy Audio URL (ưu tiên US, nếu không có thì UK)
    val audioUrl = item.phonetics?.us?.audio ?: item.phonetics?.uk?.audio

    val context = LocalContext.current

    // Sử dụng remember/DisposableEffect để quản lý ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Tối ưu cho phát âm thanh
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

    // Xử lý vòng đời (lifecycle) của Player
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    // Dừng phát khi ứng dụng bị tạm dừng (pause)
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    // Giải phóng tài nguyên khi Composables bị hủy
                    exoPlayer.release()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // Đảm bảo giải phóng nếu chưa được gọi từ ON_DESTROY
            if (exoPlayer.isReleased.not()) {
                exoPlayer.release()
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Column {
                // Từ và Từ loại
                Row(verticalAlignment = Alignment.Bottom) {
                    // Từ (Word)
                    Text(
                        text = item.word ?: "N/A",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(8.dp))
                    // Phiên âm (Phonetics)
                    Text(
                        text = phoneticsText,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Spacer(Modifier.height(4.dp))

                //topics name
                Text(
                    text = ("topics: " + item.topics?.joinToString(", ") { it.name ?: "N/A" }),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

            }
        }
    }
}
