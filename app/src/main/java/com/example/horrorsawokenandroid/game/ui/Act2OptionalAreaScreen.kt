package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horrorsawokenandroid.R

@Composable
fun Act2OptionalAreaScreen(
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val bossText = if (state.act2OptionalBossDefeated) {
        "The tomb of the King has been magically encased in ice once more. Maybe it's best left this way."
    } else {
        "After a long search in the cold mountains, you stand before the frozen cave you have been seeking. The entrance is blocked by a thick layer of ice.\nDo you truly dare break the seal?"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                viewModel.act2OptionalBossFight()
            },
    ) {
        // BACKGROUND
        Image(
            painter = painterResource(R.drawable.act2icytomb),
            contentDescription = "Act 2 optional boss",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // OPTIONAL AREA DIALOGUE TEXT (Bottom Center)
        Text(
            text = bossText,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.9f), // Strong dark shadow for atmosphere
                    offset = Offset(0f, 2f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 24.dp, end = 120.dp, bottom = 48.dp)
        )

        // BACK TO TOWN
        Text(
            text = "BACK TO TOWN ->",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.White.copy(alpha = 0.60f),
                    offset = Offset(0f, 0f),
                    blurRadius = 8f
                )
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable {
                    onClose()
                }
        )
    }
}