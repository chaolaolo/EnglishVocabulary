package com.example.engvocab.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdMobBanner(modifier: Modifier = Modifier) {
    val adUnitId = "ca-app-pub-3940256099942544/6300978111"

    AndroidView(
        factory = { context->
            AdView(context).apply{
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId

                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}