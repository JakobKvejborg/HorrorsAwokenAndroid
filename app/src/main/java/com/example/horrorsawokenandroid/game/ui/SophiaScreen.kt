package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.horrorsawokenandroid.R
import com.example.horrorsawokenandroid.game.model.NoOpSoundPlayer
import com.example.horrorsawokenandroid.game.model.SoundPlayer

@Composable
fun SophiaScreen(
    viewModel: GameViewModel,
    onClose: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    val backgroundImage = if (state.sophiaIsDead) {
        R.drawable.sophiadead
    } else {
        R.drawable.sophiaalive
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Sophia",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // TYPEWRITER TEXT — CENTER
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            if (state.sophiaIsDead) {
                TypewriterText(
                    text = "You find Sophia's lifeless body in the void. The darkness has consumed her. Your heart sinks as you realize you've failed to save her." +
                            "\nThe Hero now must journey back through time, wielding the wisdom of past battles to change the fate of Sophia. Use the given Modifier to gain some small advantage, and be stronger than before. " +
                            "Many Modifiers can be collected at random, and any number can be activated each run. Good luck Hero! \n\n[ NEW MODIFIER UNLOCKED ]", // TODO if no more modifiers can be unlocked
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    holdMs = 100000,
                    letterDelayMs= 10,
                )
            } else {
                TypewriterText(
                    text = "Sophia embraces you with tears streaming down her face. You have saved her, and the world seems just a bit brighter.\n\n" +
                            "\"About time! What took you so long?\" she says smiling, with tears flooding her eyes. Sophia returns with you out of the door behind you.\n\n" +
                            "Congratulations Hero, you have finally done it.\n" +
                            "But one last challenge remains - if you dare.",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    holdMs = 100000,
                    letterDelayMs= 10,
                )
            }
        }

        // CLOSE BUTTON — BOTTOM RIGHT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = {
                    if (state.sophiaIsDead) {
                        onClose()
                    } else {
                        viewModel.markSophiaSaved()
                        viewModel.setCurrentScreen(GameScreen.TownAct5)
                    }
                },
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                Text(
                    text = if (state.sophiaIsDead)
                        "TRAVEL BACK IN TIME ->"
                    else
                        "BACK TO TOWN ->",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.White.copy(alpha = 0.60f),
                            offset = Offset(0f, 0f),
                            blurRadius = 8f
                        )
                    )
                )
            }
        }
    }
}