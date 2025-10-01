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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.example.engvocab.R
import com.example.engvocab.data.model.Story
import com.example.engvocab.data.model.SubReadingTopic
import com.example.engvocab.ui.navigation.Screen
import com.example.engvocab.util.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesScreen(
    navController: NavHostController,
    readingId: String,
    subTopicId: String,
    viewModel: ReadingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.storiesUiState.collectAsState()
    val subReadingTopicTitle by viewModel.subReadingTopicTitle.collectAsState()

    LaunchedEffect(readingId) {
        viewModel.fetchStories(readingId, subTopicId)
        viewModel.fetchSubReadingTopicById(readingId, subTopicId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = subReadingTopicTitle ?: "Stories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.round_arrow_back_ios_new),
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
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

            uiState.stories.isNotEmpty() -> {
                StoriesGrid(innerPadding, uiState.stories, navController, readingId, subTopicId)
            }

            else -> {
                Text(text = "Không có Reading nào được tìm thấy.")
            }

        }
    }
}

@Composable
fun StoriesGrid(
    innerPadding: PaddingValues,
    stories: List<Story>,
    navController: NavHostController,
    readingId: String,
    subTopicId: String
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        items(
            items = stories,
            key = {topic -> topic.id ?: topic.title.orEmpty()  }) { topic ->
            StoryGridItem(
                topic = topic,
                onClick = {
                    val id = topic.id
                    if (id != null) {
                        navController.navigate(Screen.StoryScreen.createRoute(
                            readingId = readingId,
                            subTopicId = subTopicId,
                            storyId = id
                        ))
                    } else {
                        Log.e("TopicScreen", "Topic ID is missing for: ${topic.title}")
                    }
                }
            )
        }
    }
}

@Composable
fun StoryGridItem(topic: Story, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Màu nền của thẻ
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageLoader = remember {
                ImageLoader.Builder(context)
                    .components {
                        // Đăng ký SVG Decoder
                        add(SvgDecoder.Factory())
                    }
                    .build()
            }

            // 2. Tiêu đề
            Text(
                text = topic.title.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2, // Giới hạn 2 dòng
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            // 1. Hình ảnh (AsyncImage từ Coil)
            AsyncImage(
                model = topic.image.orEmpty(),
                contentDescription = "Image for ${topic.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    // chia tỉ lệ 16:9 hoặc 4:3... cho ảnh
//                    .aspectRatio(1f / 1f)
                    .wrapContentSize()
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = topic.description.orEmpty(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
                maxLines = 3,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}