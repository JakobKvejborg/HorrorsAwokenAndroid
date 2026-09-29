package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight

@Composable
fun ModifierButtonsScreen(
    onClose: () -> Unit,
    viewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
            .padding(16.dp)
    ) {

        // TOP LABEL (info)
        Text(
            text = "", // fill in info text here
            color = Color.White,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BUTTON GRID
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // COLUMN 1
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceEvenly // Use this to space the buttons evenly down the screen
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CombatButtonLayout(
                    text = "CRIT",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "HPUP",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "JUGGERNAUT",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "LEARNER",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "UPGRADER",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "HEALER",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "VAMPIRE",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "THIEF",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = " CHEAP PIRATE",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "CHEAP SMITH",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "SKILLED",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
            }

            // COLUMN 2
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                //                verticalArrangement = Arrangement.SpaceEvenly // Use this to space the buttons evenly down the screen
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CombatButtonLayout(
                    text = "RICH",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "IMMORTAL",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "DANGEROUS",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "STRONG",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "SOLDIER",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "VETERAN",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "REFLEXES",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "SMITHING",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "FRIENDLY",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "LOOTER",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                CombatButtonLayout(
                    text = "PHOENIX",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
            }
        }

        // GO BACK -> BUTTON — BOTTOM RIGHT
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "GO BACK ->",
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
                    .padding(vertical = 8.dp)
                    .clickable { onClose() }
            )
        }
    }
}