package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horrorsawokenandroid.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale

@Composable
fun TownScreen(
    viewModel: GameViewModel
) { val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
    ) {

        // TOWN BACKGROUND
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

        Image(
            painter = painterResource(id = R.drawable.act1townbackground),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .scale(backgroundScale.value),
            contentScale = ContentScale.Crop
        )

        // --------------------------------------------------------
        // CONTENT
        // --------------------------------------------------------

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

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Overgrown Town",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))


            Spacer(modifier = Modifier.weight(1f))

            // --------------------------------------------------------
            // TOWN BUTTONS
            // --------------------------------------------------------

            TownButton(
                text = "SHOP",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Shop later
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            TownButton(
                text = "INVENTORY",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Inventory later
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            TownButton(
                text = "REST",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Rest later
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            TownButton(
                text = "RETURN TO COMBAT",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Combat later
                }
            )
        }
    }
}

@Composable
private fun TownButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .height(48.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF3E3E3E),
                        Color(0xFF151515)
                    )
                ),
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            letterSpacing = 1.5.sp,
            color = Color.White
        )
    }
}