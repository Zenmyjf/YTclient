package com.example.ytclient.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Plays a video via YouTube's official embeddable IFrame Player.
 * Ads are shown by YouTube itself as part of the official embed — this is expected
 * and required for ToS compliance. "Music Mode" only changes the surrounding UI
 * (collapses the video visually), it does not modify or strip anything from YouTube's player.
 */
@Composable
fun PlayerScreen(videoId: String, onBack: () -> Unit) {
    var musicMode by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (musicMode) "Music Mode" else "Now Playing") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { musicMode = !musicMode }) {
                        Icon(
                            imageVector = if (musicMode) Icons.Default.VideoLibrary else Icons.Default.MusicNote,
                            contentDescription = "Toggle music mode"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // The WebView always stays mounted so playback (and audio) keeps running
            // whether we're in music mode or normal video mode — only its visible size changes.
            YouTubePlayerWebView(
                videoId = videoId,
                collapsed = musicMode,
                onWebViewReady = { webViewRef = it },
                onPlaybackStateChanged = { playing -> isPlaying = playing }
            )

            if (musicMode) {
                MusicModeControls(
                    isPlaying = isPlaying,
                    onPlayPause = {
                        val js = if (isPlaying) "ytPause();" else "ytPlay();"
                        webViewRef?.evaluateJavascript(js, null)
                        isPlaying = !isPlaying
                    }
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun YouTubePlayerWebView(
    videoId: String,
    collapsed: Boolean,
    onWebViewReady: (WebView) -> Unit,
    onPlaybackStateChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val callback by rememberUpdatedState(onPlaybackStateChanged)

    AndroidView(
        modifier = if (collapsed) {
            Modifier
                .fillMaxWidth()
                .height(1.dp) // kept mounted (not removed) so audio playback continues
        } else {
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        },
        factory = {
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.domStorageEnabled = true
                webChromeClient = WebChromeClient()

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onReady() { /* no-op hook, available for future use */ }

                    @JavascriptInterface
                    fun onStateChange(state: Int) {
                        // YT.PlayerState: PLAYING = 1, PAUSED = 2
                        when (state) {
                            1 -> callback(true)
                            2 -> callback(false)
                        }
                    }
                }, "AndroidBridge")

                val html = context.assets.open("player.html")
                    .bufferedReader()
                    .use { it.readText() }
                    .replace("VIDEO_ID_PLACEHOLDER", videoId)

                loadDataWithBaseURL(
                    "https://www.youtube.com",
                    html,
                    "text/html",
                    "utf-8",
                    null
                )

                onWebViewReady(this)
            }
        }
    )
}

@Composable
private fun MusicModeControls(isPlaying: Boolean, onPlayPause: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.height(120.dp).fillMaxWidth(),
                tint = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onPlayPause) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        modifier = Modifier.height(64.dp).fillMaxWidth()
                    )
                }
            }
            Text(
                text = "Audio keeps playing while you browse other tabs of this screen.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
