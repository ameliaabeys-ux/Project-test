package com.example.components

import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.CoffeeBg

@Composable
fun HeroVideoPlayer(
    videoUrl: String = "https://d8j0ntlcm91z4.cloudfront.net/user_38xzZboKViGWJOttwIXH07lWA1P/hf_20260707_003042_3d2380a6-1ce6-4407-a2e2-cfec46546407.mp4",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isVideoReady by remember { mutableStateOf(false) }

    // Ambient pulsing glow for loading / fallback
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(430.dp)
            .background(CoffeeBg)
    ) {
        // Fallback / Atmosphere background behind video
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    // Deep rich coffee warm background
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF6E3214).copy(alpha = pulseAlpha),
                                Color(0xFF381508),
                                CoffeeBg
                            ),
                            center = Offset(size.width * 0.5f, size.height * 0.35f),
                            radius = size.width * 0.9f
                        )
                    )
                }
        )

        // Native Hardware Video Player via TextureView + MediaPlayer
        var mediaPlayerRef by remember { mutableStateOf<MediaPlayer?>(null) }

        DisposableEffect(Unit) {
            onDispose {
                mediaPlayerRef?.run {
                    try {
                        if (isPlaying) stop()
                        reset()
                        release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                mediaPlayerRef = null
            }
        }

        AndroidView(
            factory = { ctx ->
                val textureView = TextureView(ctx)
                textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                        val surfaceObj = Surface(surface)
                        try {
                            val player = MediaPlayer().apply {
                                setSurface(surfaceObj)
                                setDataSource(ctx, Uri.parse(videoUrl))
                                isLooping = true
                                setVolume(0f, 0f) // muted as requested
                                setOnPreparedListener { mp ->
                                    mp.start()
                                    isVideoReady = true
                                    adjustAspectRatio(textureView, width, height, mp.videoWidth, mp.videoHeight)
                                }
                                setOnVideoSizeChangedListener { mp, vw, vh ->
                                    adjustAspectRatio(textureView, width, height, vw, vh)
                                }
                                prepareAsync()
                            }
                            mediaPlayerRef = player
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                        mediaPlayerRef?.let { mp ->
                            adjustAspectRatio(textureView, width, height, mp.videoWidth, mp.videoHeight)
                        }
                    }

                    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                        surfaceObjOrNull(surface)
                        return true
                    }

                    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
                }
                textureView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Bottom gradient overlay: fading from transparent at 52% to the background color (#180A06) at 100%
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.52f to Color.Transparent,
                            0.78f to CoffeeBg.copy(alpha = 0.72f),
                            1.0f to CoffeeBg
                        )
                    )
                )
        )
    }
}

private fun surfaceObjOrNull(surfaceTexture: SurfaceTexture) {
    try {
        Surface(surfaceTexture).release()
    } catch (_: Exception) {}
}

/**
 * Implements object-fit: cover with object-position: center top
 */
private fun adjustAspectRatio(textureView: TextureView, viewWidth: Int, viewHeight: Int, videoWidth: Int, videoHeight: Int) {
    if (viewWidth == 0 || viewHeight == 0 || videoWidth == 0 || videoHeight == 0) return

    val scaleX: Float
    val scaleY: Float

    val viewAspect = viewWidth.toFloat() / viewHeight.toFloat()
    val videoAspect = videoWidth.toFloat() / videoHeight.toFloat()

    if (viewAspect > videoAspect) {
        scaleX = 1f
        scaleY = (viewWidth.toFloat() / videoWidth.toFloat()) / (viewHeight.toFloat() / videoHeight.toFloat())
    } else {
        scaleX = (viewHeight.toFloat() / videoHeight.toFloat()) / (viewWidth.toFloat() / videoWidth.toFloat())
        scaleY = 1f
    }

    val matrix = Matrix()
    // Position center top
    val pivotX = viewWidth / 2f
    val pivotY = 0f

    matrix.setScale(scaleX, scaleY, pivotX, pivotY)
    textureView.setTransform(matrix)
}
