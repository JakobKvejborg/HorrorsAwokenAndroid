package com.example.horrorsawokenandroid.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

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
    fun divineAttackImage(
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
                        .height(75.dp)
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