package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import com.example.horrorsawokenandroid.R
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun CombatScreen(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
    ) {

        // Act Background
        Image(
            painter = painterResource(
                // TODO make the background depending on the current act
//                id = when (state.currentAct) {
//                    1 -> R.drawable.castle
//                    2 -> R.drawable.act2background
//                    3 -> R.drawable.act3background
//                    else -> R.drawable.act4background
                R.drawable.castle
            ),
            contentDescription = "Act background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Hero image in the background
        Image(
            painter = painterResource(id = R.drawable.hero),
            contentDescription = "Hero",
            modifier = Modifier
                .size(400.dp)
                .scale(1.4f) // how much the image is scaled up ( x 1.4 )
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = (-50).dp),
            contentScale = ContentScale.Fit
        )

        // EVERYTHING ELSE GOES ON TOP
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "HORRORS AWOKEN",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // PLAYER
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HERO",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // PLAYER HEALTH BAR
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(
                                Color(0xFF1A1A1A),
                                RoundedCornerShape(5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        // RED FILLED HEALTH
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(
                                    (
                                            state.player.currentHealth.toFloat() /
                                                    state.player.maxHealth.toFloat()
                                            ).coerceIn(0f, 1f)
                                )
                                .height(24.dp)
                                .background(
                                    Color(0xFFB71C1C),
                                    RoundedCornerShape(5.dp)
                                )
                                .align(Alignment.CenterStart)
                        )

                        // HP text inside the bar
                        Text(
                            text = "${state.player.currentHealth}/${state.player.maxHealth} HP",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // EXPERIENCE BAR XP
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(
                                Color(0xFF1A1A1A),
                                RoundedCornerShape(5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        // BLUE FILLED XP
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(
                                    (
                                            state.player.experience.toFloat() /
                                                    state.player.xpNeededToLevelUp.toFloat()
                                            ).coerceIn(0f, 1f)
                                )
                                .height(24.dp)
                                .background(
                                    Color(0xFF1976D2),
                                    RoundedCornerShape(5.dp)
                                )
                                .align(Alignment.CenterStart)
                        )

                        // XP text inside the bar
                        Text(
                            text = "${state.player.experience} / ${state.player.xpNeededToLevelUp} XP",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${state.player.goldInPocket} G",
                        color = Color(0xFFFFD54F)
                    )
                }


                // MONSTER
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.monster?.name ?: "",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE53935),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .size(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        state.monster?.imageRes?.let { imageRes ->
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = state.monster?.name,
                                modifier = Modifier.size(250.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    state.monster?.let { monster ->
                        Text(
                            text = "${monster.currentHealth}/${monster.maxHealth} HP",
                            color = Color.White
                        )

                        LinearProgressIndicator(
                            progress = {
                                if (monster.maxHealth > 0) {
                                    monster.currentHealth /
                                            monster.maxHealth.toFloat()
                                } else {
                                    0f
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // COMBAT LOG
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .weight(1f) // This will make the box take the remaining space
                    .height(120.dp)
                    .background(
                        Color(0xFF151515).copy(alpha = 0.5f),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = state.encounterLog.ifBlank {
                        "No encounter started."
                    },
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LOOT
            if (state.lootAvailable) {
                Button(
                    onClick = viewModel::lootItem,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("LOOT ITEM")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // ATTACK BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CombatButton(
                    text = "ATTACK",
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::normalAttack
                )

                if (state.player.techniqueBloodLustIsLearned) {
                    CombatButton(
                        text = "BLOOD LUST",
                        modifier = Modifier.weight(1f),
                        onClick = viewModel::bloodLustAttack
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CombatButton(
                    text = "SWIFT",
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::swiftAttack,
                    highlighted = state.playerDodgedFlag
                )

                CombatButton(
                    text = "ROAR",
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::roarAttack
                )

                CombatButton(
                    text = "DIVINE",
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::divineAttack
                )

                CombatButton(
                    text = "GUARD",
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::guardAttack
                )
            }
        }

        // TOWN / CONTINUE
        if (state.monsterDefeated) {

            // TOWN - LEFT SIDE
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Button(
                    onClick = viewModel::goToTown,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = null
                ) {
                    Text(
                        text = "<< TOWN",
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // CONTINUE - RIGHT SIDE
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            ) {
                Button(
                    onClick = viewModel::continueAfterMonsterDefeated,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = null
                ) {
                    Text(
                        text = "CONTINUE >>",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CombatButton(
    text: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = if (highlighted) {
            ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7B1FA2)
            )
        } else {
            ButtonDefaults.buttonColors()
        }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}