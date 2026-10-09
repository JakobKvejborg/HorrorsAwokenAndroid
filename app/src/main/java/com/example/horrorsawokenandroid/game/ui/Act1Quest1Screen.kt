package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.horrorsawokenandroid.R

@Composable
fun Act1Quest1Screen(
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { // blocks clicks from reaching anything underneath
                if (state.act1Quest1BoyIsSaved && !state.act1Quest1ThankYouShown) {
                viewModel.markAct1Quest1ThankYouShown()
            }
                onClose() },
    ) {
        // BACKGROUND
        Image(
            painter = painterResource(
                if (state.act1Quest1BoyIsSaved) {
                    R.drawable.act1quest1backgroundboyfound
                } else {
                    R.drawable.act1quest1background
                }
            ),
            contentDescription = "Quest background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // QUEST TEXT
        Text(
            text = when {
                state.act1Quest1BoyIsSaved && !state.act1Quest1ThankYouShown ->
                    "Thank you so much for returning my boy to me! As a token of my gratitude, here, take this. It belonged to my father. Farewell, Hero.\n\n[Father's Helmet added to inventory]"

                state.act1Quest1IsFinished ->
                    "Thanks again, Hero."

                !state.act1Quest1InitialTextShown ->
                    "Hey, you! You must help me! I... I’ve lost my little boy. I turned away for a moment, and now he’s gone. I’m so worried - I can’t bear the thought of him being out there alone. Please, if you could help me find him, I’d be forever grateful."

                else ->
                    "Have you found him yet? It's not safe out there. I hope nothing's happened to him."
            },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    offset = Offset(0f, 2f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 80.dp)
                .padding(horizontal = 24.dp)
        )

        // BACK
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
                .padding(
                    end = 16.dp,   // Distance from right
                    bottom = 46.dp // Distance from bottom
                )
                .clickable { // Back to town "CLOSE" button
                    if (state.act1Quest1BoyIsSaved && !state.act1Quest1ThankYouShown) {
                        viewModel.markAct1Quest1ThankYouShown()
                    }
                    onClose()
                }
        )
    }
}