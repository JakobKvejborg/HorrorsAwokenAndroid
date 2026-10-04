package com.example.horrorsawokenandroid.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horrorsawokenandroid.R

@Composable
fun TownScreen(
    viewModel: GameViewModel
) {
    val state by viewModel.uiState.collectAsState()
    state.introMonstersAreCompleted = true
    val currentAct = state.currentAct
    val interactionSource = remember { MutableInteractionSource() } // This makes the things clicked on not flicker

    // BACKGROUND ZOOM
    val backgroundScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        backgroundScale.animateTo(
            targetValue = 1.15f,
            animationSpec = tween(
                durationMillis = 50000,
                easing = LinearEasing
            )
        )
    }

    // TOWN
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { // What happens if you click the background in town
                if (currentAct == 1) {
                    viewModel.act1Quest1(true) // Opens act 1 quest 1 screen
                    viewModel.womanCryingSoundAct1Q1()
                } else if (currentAct == 3 && state.act4Quest1Started) {
                    viewModel.reforgeFrogScreenOpen(true) // Opens act 3 reforge frog screen
                    viewModel.act3TalkToFrogSound()
                } else if (currentAct == 2 && state.act4Quest1Started) {
                    viewModel.openAct2OptionalAreaScreen(true)
                }
            }
    ) {

        // BACKGROUND
        Image(
            painter = painterResource(
                id = when (currentAct) {
                    1 -> R.drawable.act1townbackground
                    2 -> R.drawable.act2town
                    3 -> R.drawable.act3town
                    4 -> R.drawable.act4town
                    else -> R.drawable.act5background
                }
            ),
            contentDescription = "Town background",
            modifier = Modifier
                .fillMaxSize()
                .scale(backgroundScale.value),
            contentScale = ContentScale.Crop
        )

        // LIGHT DARK OVERLAY
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.15f)
                )
        )

        // MAIN CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // TITLE
            Text(
                text = "HORRORS AWOKEN",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Town title
            Text(
                text = when (currentAct) {
                    1 -> "OVERGROWN TOWN"
                    2 -> "FROZEN TOWN"
                    3 -> "THE LOST SEA"
                    4 -> "THE FORSAKEN TOWN"
                    else -> "PLACES BEYOND"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // COMPASS AT TOP
            Spacer(
                modifier = Modifier.height(12.dp)
            )

            TownCompass(
                onNorth = {
                    state.lastDirectionChosenByPlayer = "NORTH"
                    viewModel.whereToGoBasedOnTheDirection(state.lastDirectionChosenByPlayer)
                },
                onSouth = {
                    state.lastDirectionChosenByPlayer = "SOUTH"
                    viewModel.whereToGoBasedOnTheDirection(state.lastDirectionChosenByPlayer)
                },
                onEast = {
                    state.lastDirectionChosenByPlayer = "EAST"
                    viewModel.whereToGoBasedOnTheDirection(state.lastDirectionChosenByPlayer)
                },
                onWest = {
                    state.lastDirectionChosenByPlayer = "WEST"
                    viewModel.whereToGoBasedOnTheDirection(state.lastDirectionChosenByPlayer)
                }
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            // NPC AREA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {

                // LEFT NPC
                if (currentAct == 1 || currentAct == 2 || currentAct == 4) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = (-50).dp)
                    ) {
                        LeftNPCBox(
                            imageRes = when (currentAct) {
                                1 -> R.drawable.healer
                                2 -> R.drawable.act2healer
                                4 -> R.drawable.act4healer
                                else -> null
                            },
                            label = getNpcName(
                                act = currentAct,
                                npcNumber = 1
                            ),
                            onClick = {
                                viewModel.playerIsHealedByNPC()
                            }
                        )

                        GoldPriceLabel(
                            amount = state.player.priceToHeal,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = 20.dp)
                        )
                    }
                }

                // RIGHT NPC
                if (currentAct == 1 || currentAct == 2 || currentAct == 4) { // NPC images are only shown if the current act is 1, 2 or 4
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 110.dp)
                    ) {
                        RightNPCBox(
                            imageRes = when (currentAct) {
                                1 -> R.drawable.act1artsteacher
                                2 -> if (
                                    !state.player.hasDragonRuby &&
                                    state.act4QuestIsFinished
                                ) {
                                    R.drawable.act2smithupgraded
                                } else {
                                    R.drawable.act2smith
                                }
                                4 -> R.drawable.act4mage
                                else -> null
                            },
                            label = getNpcName(
                                act = currentAct,
                                npcNumber = 2
                            ),
                            onClick = {
                                when (currentAct) {
                                    1 -> viewModel.playerLearnTechniques()

                                    2 -> {
                                        if (state.player.hasDragonRuby) {
                                            viewModel.giveSmithDragonRuby()
                                        } else {
                                            viewModel.openAct2SmithOverlay()
                                        }
                                    }

                                    4 -> viewModel.talkToDragonMage()
                                }
                            }
                        )

                        if (currentAct == 1 && !state.player.techniqueGuardIsLearned) {
                            GoldPriceLabel(
                                amount = state.player.priceToLearnTechnique,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .offset(y = (-40).dp)
                            )
                        }
                    }
                }
            }

            // ----------------------------------------------------
            // BOTTOM AREA
            // ----------------------------------------------------
            Spacer(
                modifier = Modifier.height(18.dp)
            )

        }

        // Act 2 Smith overlay (modified inventory overlay)
        if (state.inventoryOpen) {
            InventoryOverlay(
                player = state.player,
                onClose = {
                    viewModel.closeAct2SmithOverlay()
                },
                onEquipItem = viewModel::equipItem,
                onUnequipItem = viewModel::unEquipItem
            )
        }

        // Act 1 screen
        if (state.act1Quest1ScreenOpen) {
            Act1Quest1Screen(
                viewModel = viewModel,
                onClose = { viewModel.act1Quest1(false) }
            )
        }

        // Act 3 reforge frog screen
        if (state.reforgeFrogScreenOpen) {
            ReforgeFrogScreen(
                viewModel = viewModel,
                onClose = { viewModel.reforgeFrogScreenOpen(false) }
            )
        }

        // Act 2 opentional boss area screen
        if (state.act2OptionalAreaScreenOpen) {
            Act2OptionalAreaScreen(
                viewModel = viewModel,
                onClose = { viewModel.openAct2OptionalAreaScreen(false) }
            )
        }

        TownTypewriterText(
            currentAct = state.currentAct,
            numberOfTechniquesLearned = state.player.numberOfTechniquesLearned,
            act1TownTextShown = state.act1TownTextShown,
            act3TownTextShown = state.act3TownTextShown,
            shownTechniqueTexts = state.shownTechniqueTexts,
            adHocText = state.adHocTownText,
            onAct1TextShown = { viewModel.markAct1TextShown() },
            onAct3TextShown = { viewModel.markAct3TextShown() },
            onTechniqueTextShown = { n -> viewModel.markTechniqueTextShown(n) },
            onAdHocTextShown = { viewModel.clearAdHocTownText() }
        )
    }

}

// COMPASS
@Composable
private fun TownCompass(
    onNorth: () -> Unit,
    onSouth: () -> Unit,
    onEast: () -> Unit,
    onWest: () -> Unit
) {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        // COMPASS IMAGE
        Image(
            painter = painterResource(
                id = R.drawable.compass
            ),
            contentDescription = "Town compass",
            modifier = Modifier.size(170.dp),
            contentScale = ContentScale.Fit
        )

        // NORTH
        Box(
            modifier = Modifier
                .size(
                    width = 90.dp,
                    height = 75.dp
                )
                .align(Alignment.TopCenter)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onNorth()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "N",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }

        // SOUTH
        Box(
            modifier = Modifier
                .size(
                    width = 90.dp,
                    height = 75.dp
                )
                .align(Alignment.BottomCenter)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onSouth()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "S",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }

        // WEST
        Box(
            modifier = Modifier
                .size(
                    width = 75.dp,
                    height = 90.dp
                )
                .align(Alignment.CenterStart)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onWest()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "W",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }

        // EAST
        Box(
            modifier = Modifier
                .size(
                    width = 75.dp,
                    height = 90.dp
                )
                .align(Alignment.CenterEnd)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onEast()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "E",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}


// ================================================================
// NPC BOX
// ================================================================
@Composable
private fun LeftNPCBox(
    imageRes: Int?,
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(
                width = 210.dp,
                height = 310.dp
            )
            .clickable (
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun RightNPCBox(
    imageRes: Int?,
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(
                width = 430.dp,
                height = 500.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// NPC IMAGES
private fun getNpcImage(
    act: Int,
    npcNumber: Int,
    smithHasDragonRuby: Boolean = false,
): Int? {

    return when (act) {

        1 -> when (npcNumber) {
            1 -> R.drawable.healer
            2 -> R.drawable.act1artsteacher
            else -> null
        }

        2 -> when (npcNumber) {
            1 -> R.drawable.act2healer
            2 -> if (smithHasDragonRuby) R.drawable.act2smithupgraded else R.drawable.act2smith
            else -> null
        }

        3 -> when (npcNumber) {
            1 -> null
            2 -> null
            else -> null
        }

        4 -> when (npcNumber) {
            1 -> R.drawable.act4healer
            2 -> R.drawable.act4mage
            else -> null
        }

        5 -> when (npcNumber) {
            1 -> null
            2 -> null
            else -> null
        }

        else -> null
    }
}


// ================================================================
// NPC NAMES
// ================================================================
private fun getNpcName(
    act: Int,
    npcNumber: Int
): String {

    return when (act) {

        1 -> when (npcNumber) {
            1 -> ""
            2 -> ""
            else -> ""
        }

        2 -> when (npcNumber) {
            1 -> ""
            2 -> ""
            else -> ""
        }

        3 -> when (npcNumber) {
            1 -> ""
            2 -> ""
            else -> ""
        }

        4 -> when (npcNumber) {
            1 -> ""
            2 -> ""
            else -> ""
        }

        5 -> when (npcNumber) {
            1 -> ""
            2 -> ""
            else -> ""
        }

        else -> ""
    }
}


