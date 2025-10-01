package com.example.engvocab.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null

    private val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    fun loadAd(onAdDismissed: () -> Unit) {
        val activity = context as? Activity ?: return

        InterstitialAd.load(
            context,
            AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("AdManager", "Ad failed to load: ${adError.message}")
                    interstitialAd = null
                    onAdDismissed()
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("AdManager", "Ad was loaded.")
                    interstitialAd = ad
                    showAd(activity, onAdDismissed)
                }
            }
        )
    }

    private fun showAd(activity: Activity, onAdDismissed: () -> Unit) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("AdManager", "Ad was dismissed.")
                onAdDismissed()
                interstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d("AdManager", "Ad failed to show: ${adError.message}")
                onAdDismissed()
                interstitialAd = null
            }
        }

        interstitialAd?.show(activity)
            ?: run {
                Log.d("AdManager", "Ad wasn't ready to show.")
                onAdDismissed()
            }
    }
}