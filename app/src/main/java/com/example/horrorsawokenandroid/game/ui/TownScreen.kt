package com.example.horrorsawokenandroid.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.graphicsLayer
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

    val currentAct = viewModel.getCurrentAct()


    // BACKGROUND ZOOM
    val backgroundScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        backgroundScale.animateTo(
            targetValue = 1.15f,
            animationSpec = tween(
                durationMillis = 60000,
                easing = LinearEasing
            )
        )
    }

    // TOWN
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
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

        // --------------------------------------------------------
        // MAIN CONTENT
        // --------------------------------------------------------

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
                    viewModel.whereToGoBasedOnTheDirection("NORTH")
                },
                onSouth = {
                    // SOUTH TODO return to previous Act/start quest 1
                },
                onEast = {
                    viewModel.whereToGoBasedOnTheDirection("EAST")
                },
                onWest = {
                    viewModel.whereToGoBasedOnTheDirection("WEST")
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
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-50).dp) // This decides how far left the image is pushed off screen (-10 is a bit left, -200 is far left)
                ) {
                    LeftNPCBox(
                        imageRes = getNpcImage(
                            act = currentAct,
                            npcNumber = 1
                        ),
                        label = getNpcName(
                            act = currentAct,
                            npcNumber = 1
                        ),
                        onClick = {
                            when (currentAct) {
                                1, 2, 3, 4 -> {
                                    viewModel.playerIsHealedByNPC()
                                }
                            }
                        }
                    )
                }

                // RIGHT NPC
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 110.dp) // This decides how far right the image is pushed off screen (10 is a bit right, 200 is far right)
                ) {
                    RightNPCBox(
                        imageRes = getNpcImage(
                            act = currentAct,
                            npcNumber = 2
                        ),
                        label = getNpcName(
                            act = currentAct,
                            npcNumber = 2
                        ),
                        onClick = {
                            when (currentAct) {
                                1 -> {
                                    viewModel.playerLearnTechniques()
                                }
                                2 -> {
                                    // ACT 2 NPC 2
                                }
                                3 -> {
                                    // ACT 3 NPC 2
                                }
                                4 -> {
                                    // ACT 4 NPC 2
                                }
                                5 -> {
                                    // ACT 5 NPC 2
                                }
                            }
                        }
                    )
                }
            }

            // ----------------------------------------------------
            // BOTTOM AREA
            // ----------------------------------------------------

            Spacer(
                modifier = Modifier.height(18.dp)
            )


        }
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
                    width = 75.dp,
                    height = 60.dp
                )
                .align(Alignment.TopCenter)
                .clickable {
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
                    width = 75.dp,
                    height = 60.dp
                )
                .align(Alignment.BottomCenter)
                .clickable {
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
                    width = 60.dp,
                    height = 75.dp
                )
                .align(Alignment.CenterStart)
                .clickable {
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
                    width = 60.dp,
                    height = 75.dp
                )
                .align(Alignment.CenterEnd)
                .clickable {
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
    Box(
        modifier = Modifier
            .size(
                width = 210.dp,
                height = 310.dp
            )
            .clickable {
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
    Box(
        modifier = Modifier
            .size(
                width = 430.dp,
                height = 500.dp
            )
            .clickable {
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
    npcNumber: Int
): Int? {

    return when (act) {

        1 -> when (npcNumber) {
            1 -> R.drawable.healer
            2 -> R.drawable.act1artsteacher
            else -> null
        }

        2 -> when (npcNumber) {
            1 -> R.drawable.act2healer
            2 -> R.drawable.act2smith
            else -> null
        }

        3 -> when (npcNumber) {
            1 -> null
            2 -> null
            else -> null
        }

        4 -> when (npcNumber) {
            1 -> null
            2 -> null
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


