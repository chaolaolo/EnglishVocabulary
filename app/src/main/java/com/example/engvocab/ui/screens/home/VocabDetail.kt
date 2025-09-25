package com.example.engvocab.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavHostController
import com.example.engvocab.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabDetail(navController: NavHostController) {
    val context = LocalContext.current

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
                    containerColor = MaterialTheme.colorScheme.background, // 👉 đổi màu nền tại đây
                    titleContentColor = MaterialTheme.colorScheme.onBackground, // 👉 màu chữ
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground // 👉 màu icon
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
                ) {
                    Text(
                        "Chaolaolo", fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "indefinite article", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.End,
                        modifier = Modifier.alignByBaseline()
                    )
                }

                //phonetics
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.round_volume_up_24),
                        contentDescription = "Volume",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Clicked US Audio", Toast.LENGTH_SHORT)
                        }
                    )
                    Text(
                        "/e/", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.round_volume_up_24),
                        contentDescription = "Volume",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Clicked UK Audio", Toast.LENGTH_SHORT)
                        }
                    )
                    Text(
                        "/e/", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

//                Senses Display

            }
        }

    }
}