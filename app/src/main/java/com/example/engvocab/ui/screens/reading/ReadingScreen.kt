package com.example.engvocab.ui.screens.reading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.engvocab.ui.navigation.Screen
import com.example.engvocab.util.AppViewModelProvider
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    navController: NavHostController,
    viewModel: ReadingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState by viewModel.readingsUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reading",
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
                windowInsets = WindowInsets(0.dp)
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                // 1. Hiển thị Loading
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                // 2. Hiển thị Error
                uiState.error != null -> {
                    Text(
                        text = "Lỗi: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // 3. Hiển thị Content (Danh sách Readings)
                uiState.readings.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(uiState.readings, key = { it.url.orEmpty() }) { reading ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                    .clickable {
                                        reading.id?.let { readingId ->
                                            val encodedId = URLEncoder.encode(readingId, StandardCharsets.UTF_8.toString())
                                             navController.navigate(Screen.SubReadingTopic.createRoute(encodedId))
                                        }
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                ) {
                                    reading.name?.let {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 18.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp, horizontal = 12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Hiển thị Empty State
                else -> {
                    Text(text = "Không có Reading nào được tìm thấy.")
                }
            }
        }
    }
}


