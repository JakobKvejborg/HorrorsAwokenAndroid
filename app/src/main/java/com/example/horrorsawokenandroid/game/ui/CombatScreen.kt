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

@Composable
fun CombatScreen(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "HORROR'S AWOKEN",
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

                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                Color(0xFF202020),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PLAYER",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${state.player.currentHealth}/${state.player.maxHealth} HP",
                        color = Color.White
                    )

                    LinearProgressIndicator(
                        progress = {
                            if (state.player.maxHealth > 0) {
                                state.player.currentHealth /
                                        state.player.maxHealth.toFloat()
                            } else {
                                0f
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

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
                        text = state.monster?.name ?: "NO ENCOUNTER",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE53935),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                Color(0xFF301010),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        state.monster?.imageRes?.let { imageRes ->
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = state.monster?.name,
                                modifier = Modifier.size(150.dp),
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
                    .weight(1f)
                    .background(
                        Color(0xFF151515),
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