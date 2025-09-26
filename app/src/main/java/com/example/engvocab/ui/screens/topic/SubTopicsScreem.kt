package com.example.engvocab.ui.screens.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.engvocab.R
import com.example.engvocab.data.model.SubTopic
import com.example.engvocab.data.model.SubTopicUiState
import com.example.engvocab.ui.navigation.Screen
import com.example.engvocab.util.AppViewModelProvider
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubTopics(
    navController: NavHostController,
    topicId: String,
    viewModel: TopicViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.subTopicUiState

    LaunchedEffect(topicId) {
        viewModel.loadSubTopics(topicId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Topics",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.round_arrow_back_ios_new), // Cần import Icons.Filled.ArrowBack
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
        when (uiState) {
            SubTopicUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SubTopicUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is SubTopicUiState.Success -> SubTopicsList(
                navController,
                innerPadding,
                uiState.subTopics
            )

            SubTopicUiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No subtopics found for this topic.",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}

@Composable
fun SubTopicsList(
    navController: NavController,
    innerPadding: PaddingValues,
    subTopics: List<SubTopic>
) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(innerPadding),
//        contentPadding = PaddingValues(16.dp)
    ) {
        items(subTopics, key = { it.url.orEmpty() }) { subTopic ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .clickable {
//                        val name = subTopic.name
//                        if (name != null) {
//                            // mã hoá
//                            val encodedTopicName = java.net.URLEncoder.encode(
//                                name,
//                                java.nio.charset.StandardCharsets.UTF_8.toString()
//                            )
//                            // điều hướng
//                            navController.navigate(
//                                Screen.VocabularyOfTopic.createRoute(
//                                    encodedTopicName
//                                )
//                            )
//                        }
                        subTopic.name?.let { name ->
                            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                            navController.navigate(Screen.VocabularyOfTopic.createRoute(encodedName))
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    subTopic.name?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

