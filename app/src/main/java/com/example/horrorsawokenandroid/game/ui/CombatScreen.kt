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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.scale
import com.example.horrorsawokenandroid.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import com.example.horrorsawokenandroid.game.model.Player
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle

/*
This class is handles the encounter screen and the logic during a battle with a monster
 */

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

        // Everything else goes on top of the background and hero image
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
                                Color(0xFF000000), // player health bar background color
                                RoundedCornerShape(5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

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
                                    Color(0xFF891515), // foreground player health bar color
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

                    Spacer(modifier = Modifier.height(6.dp))

                    PlayerStatsPanel(player = state.player)
                }

                // MONSTER / LOOT
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.lootAvailable && state.droppedItem != null) {

                        // LOOT
                        Text(
                            text = "",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF090909),
                                        Color(0xFFFFD700)
                                    )
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(56.dp)) // how far down the loot image appears

                        Box(
                            modifier = Modifier.size(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                                Image(
                                    painter = painterResource(id = R.drawable.lootblood),
                                    contentDescription = "loot",
                                    modifier = Modifier
                                        .size(152.dp) // size of the loot image
                                        .clickable {
                                            viewModel.playerCollectsLoot()
                                        },
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }

                     else {

                        // MONSTER
                        Text(
                            text = state.monster?.name ?: "",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF090909),
                                        Color(0xFF8B0000)
                                    )
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier.size(250.dp),
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

            // COMBAT ENCOUNTER LOG
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .weight(1f) // This will make the box take the remaining space
                    .weight(1f)
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

            // ATTACK BUTTONS
            CombatButton(
                text = "ATTACK",
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::normalAttack
            )

            Spacer(modifier = Modifier.height(6.dp))

            // SECOND ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (state.player.techniqueBloodLustIsLearned) {
                    CombatButton(
                        text = "BLOOD LUST",
                        modifier = Modifier.weight(1f),
                        onClick = viewModel::bloodLustAttack
                    )
                }


                if (state.player.TechniqueSwiftIsLearned) {
                    CombatButton(
                        text = "SWIFT",
                        modifier = Modifier.weight(1f),
                        onClick = viewModel::swiftAttack
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // THIRD ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (state.player.TechniqueRoarIsLearned) {
                    CombatButton(
                        text = "ROAR",
                        modifier = Modifier.weight(1f),
                        onClick = viewModel::roarAttack
                    )
                }

                if (state.player.TechniqueDivineIsLearned) {
                    CombatButton(
                        text = "DIVINE",
                        modifier = Modifier.weight(1f),
                        onClick = viewModel::divineAttack
                    )
                }

                if (state.player.TechniqueGuardIsLearned) {
                    CombatButton(
                        text = "GUARD",
                        modifier = Modifier.weight(1f),
                        onClick = viewModel::guardAttack
                    )
                }
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
                        color = Color(0xFFADD8E6),
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
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF3E3E3E),
                        Color(0xFF151515)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Composable
private fun PlayerStatsPanel(player: Player) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF111111).copy(alpha = 0.35f),
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = "Lvl ${player.level}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatItem(
                label = "Dmg",
                value = "${player.damage}",
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = "Strgth",
                value = "${player.strength}",
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = "Armor",
                value = "${player.armor}",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatItem(
                label = "Crit%",
                value = "${player.critChance}%",
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = "CrtDmg",
                value = "${player.critDamage}%",
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = "Dodge",
                value = "${player.dodgeChance}%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatItem(
                label = "LifeSt",
                value = "${player.lifesteal}%",
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = "Regen",
                value = "${player.regeneration}",
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = "Gold",
                value = "${player.goldInPocket}",
                textColor = Color(0xFFFFD54F),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = MaterialTheme.typography.labelSmall.fontSize
        )

        Text(
            text = value,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        )
    }
}