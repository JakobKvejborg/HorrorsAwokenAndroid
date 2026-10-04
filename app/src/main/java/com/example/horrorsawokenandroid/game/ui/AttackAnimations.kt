package com.example.horrorsawokenandroid.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import com.example.horrorsawokenandroid.R

class AttackAnimations {

    @Composable
    fun monsterShake(
        trigger: Int
    ): Float {

        val monsterShake = remember {
            Animatable(0f)
        }

        LaunchedEffect(trigger) {
            if (trigger > 0) {

                monsterShake.snapTo(0f)

                monsterShake.animateTo(
                    targetValue = 2f,
                    animationSpec = tween(50)
                )

                monsterShake.animateTo(
                    targetValue = -2f,
                    animationSpec = tween(50)
                )

                monsterShake.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(50)
                )
            }
        }

        return monsterShake.value
    }

    @Composable
    fun heroDodgeShake(
        trigger: Int
    ): Float {

        val heroShake = remember {
            Animatable(0f)
        }

        LaunchedEffect(trigger) {
            if (trigger > 0) {

                heroShake.snapTo(0f)

                heroShake.animateTo(
                    targetValue = -4f,
                    animationSpec = tween(110)
                )

                heroShake.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(50)
                )
            }
        }

        return heroShake.value
    }

    @Composable
    fun divineAttackEffect(
        trigger: Int
    ): Float {

        val alpha = remember {
            Animatable(0f)
        }

        LaunchedEffect(trigger) {
            if (trigger > 0) {

                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(10) // Fade in timer
                )

                delay(200) // how long the image should stay visible

                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(950) // fade out
                )
            }
        }

        return alpha.value
    }

    @Composable
    fun swiftAttackEffect(
        trigger: Int
    ): Float {

        val alpha = remember {
            Animatable(0f)
        }

        LaunchedEffect(trigger) {
            if (trigger > 0) {

                alpha.snapTo(0f)

                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(10)
                )

                delay(60)

                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(160)
                )
            }
        }

        return alpha.value
    }

    @Composable
    fun BuffIcon(isActive: Boolean) {
        if (!isActive) return

        val infiniteTransition = rememberInfiniteTransition(label = "buffIconPulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.91f,
            targetValue = 1.00f,
            animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
            label = "buffIconScale"
        )

        Image(
            painter = painterResource(R.drawable.guardshield4), // image of the buff icon
            contentDescription = "Buff active",
            modifier = Modifier
                .size(20.dp)
                .scale(scale)
        )
    }

    @Composable
    fun bloodSplatterCritEffect(
        trigger: Int
    ): Float {

        val alpha = remember {
            Animatable(0f)
        }

        LaunchedEffect(trigger) {
            if (trigger > 0) {

                alpha.snapTo(0f)

                // Fade in
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(1) // fade in
                )

                delay(150) // How long the image stays fully visible

                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(330) // fade out
                )
            }
        }

        return alpha.value
    }

    @Composable
    fun guardEffect(
        trigger: Int
    ): Float {

        val alpha = remember {
            Animatable(0f)
        }

        LaunchedEffect(trigger) {
            if (trigger > 0) {

                alpha.snapTo(0f)

                // Fade in
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(1) // fade in
                )

                delay(550) // How long the image stays fully visible

                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(230) // fade out
                )
            }
        }

        return alpha.value
    }

    @Composable
    fun NormalSlashEffect(
        trigger: Int
    ) {
        val progress = remember { Animatable(0f) }
        val alpha = remember { Animatable(0f) }

        LaunchedEffect(trigger) {
            if (trigger > 0) {
                progress.snapTo(0f)
                alpha.snapTo(1f)

                // Blade sweeps across
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(15, easing = LinearEasing)
                )

                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(20) // cut fades out
                )
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (progress.value <= 0f || alpha.value <= 0f) return@Canvas

            val start = Offset(size.width * 0.15f, size.height * 0.12f)
            val end = Offset(size.width * 0.85f, size.height * 0.88f)
            val head = start + (end - start) * progress.value

            // Tail is transparent, head is bright
            val trail = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color.White),
                start = start,
                end = head
            )

            // Soft glow
            drawLine(
                brush = trail,
                start = start,
                end = head,
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
                alpha = 0.08f * alpha.value
            )

            // Sharp core
            drawLine(
                brush = trail,
                start = start,
                end = head,
                strokeWidth = 0.9.dp.toPx(),
                cap = StrokeCap.Round,
                alpha = 0.45f * alpha.value
            )
        }
    }

    @Composable
    fun GuardHealGlow(
        trigger: Int
    ) {
        val glowAlpha = remember {
            Animatable(0f)
        }
        // 💡 Neon Green color (Bright with a slight mint/lime punch for that healing look)
        val neonGreen = Color(0xFF00FF66)

        LaunchedEffect(trigger) {
            if (trigger > 0) {
                glowAlpha.snapTo(0f)
                glowAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(10) // Fade in timer
                )

                delay(1) // How long the effect stays visible at full power

                glowAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(670) // Fade out timer
                )
            }
        }

        if (glowAlpha.value > 0f) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Left side glow
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(880.dp) // how far up the screen the effect goes
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    neonGreen.copy(
                                        alpha = 0.25f * glowAlpha.value
                                    )
                                )
                            )
                        )
                )

                // Right side glow
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(880.dp) // how far up the screen the effect goes
                        .align(Alignment.BottomEnd)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    neonGreen.copy(
                                        alpha = 0.25f * glowAlpha.value
                                    )
                                )
                            )
                        )
                )
            }
        }
    }

    @Composable
    fun BloodLustGlow(
        trigger: Int
    ) {
        val glowAlpha = remember {
            Animatable(0f)
        }
        var bloodRed = Color(0xFFB30000)

        LaunchedEffect(trigger) {
            if (trigger > 0) {

                glowAlpha.snapTo(0f)
                glowAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(10) // Fade in timer
                )

                delay(1) // How long the effect should stay visible

                glowAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(870)  // Fade out timer
                )
            }
        }

        if (glowAlpha.value > 0f) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                // Bottom glow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    bloodRed.copy(
                                        alpha = 0.35f * glowAlpha.value // higher number, less transparent
                                    )
                                )
                            )
                        )
                )

                // Left side glow
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(880.dp) // how far up the screen the effect goes
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    bloodRed.copy(
                                        alpha = 0.15f * glowAlpha.value
                                    )
                                )
                            )
                        )
                )

                // Right side glow
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(880.dp) // how far up the screen the effect goes
                        .align(Alignment.BottomEnd)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    bloodRed.copy(
                                        alpha = 0.15f * glowAlpha.value
                                    )
                                )
                            )
                        )
                )
            }
        }
    }
}