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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha

@Composable
fun ModifierButtonsScreen(
    onClose: () -> Unit,
    viewModel: GameViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val unlocked = state.player.unlockedModifiers
    val active = state.player.activeModifiers
    var selectedModifier by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
            .padding(16.dp)
    ) {

        // TOP LABEL (info)
        Text(
            text = selectedModifier?.let { viewModel.modifiers.getDescription(it) } ?: "Complete the game to unlock a Modifier.",
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CombatButtonLayout(
                    text = "CRIT",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("crit" in unlocked) 1f else 0.4f),
                    isGlowing = "crit" in active,
                    glowColor = Color(0xFFE53935),
                    onClick = {
                        if ("crit" in unlocked) {
                            selectedModifier = "crit"
                            viewModel.modifiers.applyModifier("crit")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "HEALTHY",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("healthy" in unlocked) 1f else 0.4f),
                    isGlowing = "healthy" in active,
                    glowColor = Color(0xFF43A047),
                    onClick = {
                        if ("healthy" in unlocked) {
                            selectedModifier = "healthy"
                            viewModel.modifiers.applyModifier("healthy")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "JUGGERNAUT",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("juggernaut" in unlocked) 1f else 0.4f),
                    isGlowing = "juggernaut" in active,
                    glowColor = Color(0xFF78909C),
                    onClick = {
                        if ("juggernaut" in unlocked) {
                            selectedModifier = "juggernaut"
                            viewModel.modifiers.applyModifier("juggernaut")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "INTUITIVE",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("intuitive" in unlocked) 1f else 0.4f),
                    isGlowing = "intuitive" in active,
                    glowColor = Color(0xFF29B6F6),
                    onClick = {
                        if ("intuitive" in unlocked) {
                            selectedModifier = "intuitive"
                            viewModel.modifiers.applyModifier("intuitive")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "UPGRADER",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("upgrader" in unlocked) 1f else 0.4f),
                    isGlowing = "upgrader" in active,
                    glowColor = Color(0xFFAB47BC),
                    onClick = {
                        if ("upgrader" in unlocked) {
                            selectedModifier = "upgrader"
                            viewModel.modifiers.applyModifier("upgrader")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "HEALER",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("healer" in unlocked) 1f else 0.4f),
                    isGlowing = "healer" in active,
                    glowColor = Color(0xFF00C853),
                    onClick = {
                        if ("healer" in unlocked) {
                            selectedModifier = "healer"
                            viewModel.modifiers.applyModifier("healer")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "VAMPIRE",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("vampire" in unlocked) 1f else 0.4f),
                    isGlowing = "vampire" in active,
                    glowColor = Color(0xFFB71C1C),
                    onClick = {
                        if ("vampire" in unlocked) {
                            selectedModifier = "vampire"
                            viewModel.modifiers.applyModifier("vampire")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "THIEF",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("thief" in unlocked) 1f else 0.4f),
                    isGlowing = "thief" in active,
                    glowColor = Color(0xFFFFD54F),
                    onClick = {
                        if ("thief" in unlocked) {
                            selectedModifier = "thief"
                            viewModel.modifiers.applyModifier("thief")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "PIRATE",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("pirate" in unlocked) 1f else 0.4f),
                    isGlowing = "pirate" in active,
                    glowColor = Color(0xFF26A69A),
                    onClick = {
                        if ("pirate" in unlocked) {
                            selectedModifier = "pirate"
                            viewModel.modifiers.applyModifier("pirate")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "CHEAP SMITH",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("cheapsmith" in unlocked) 1f else 0.4f),
                    isGlowing = "cheapsmith" in active,
                    glowColor = Color(0xFFFF8F00),
                    onClick = {
                        if ("cheapsmith" in unlocked) {
                            selectedModifier = "cheapsmith"
                            viewModel.modifiers.applyModifier("cheapsmith")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "SKILLED",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("skilled" in unlocked) 1f else 0.4f),
                    isGlowing = "skilled" in active,
                    glowColor = Color(0xFF42A5F5),
                    onClick = {
                        if ("skilled" in unlocked) {
                            selectedModifier = "skilled"
                            viewModel.modifiers.applyModifier("skilled")
                        }
                    }
                )
            }

            // COLUMN 2
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CombatButtonLayout(
                    text = "RICH",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("rich" in unlocked) 1f else 0.4f),
                    isGlowing = "rich" in active,
                    glowColor = Color(0xFFFFC107),
                    onClick = {
                        if ("rich" in unlocked) {
                            selectedModifier = "rich"
                            viewModel.modifiers.applyModifier("rich")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "IMMORTAL",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("immortal" in unlocked) 1f else 0.4f),
                    isGlowing = "immortal" in active,
                    glowColor = Color(0xFFB3E5FC),
                    onClick = {
                        if ("immortal" in unlocked) {
                            selectedModifier = "immortal"
                            viewModel.modifiers.applyModifier("immortal")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "DANGEROUS",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("dangerous" in unlocked) 1f else 0.4f),
                    isGlowing = "dangerous" in active,
                    glowColor = Color(0xFFD50000),
                    onClick = {
                        if ("dangerous" in unlocked) {
                            selectedModifier = "dangerous"
                            viewModel.modifiers.applyModifier("dangerous")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "STRONG",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("strong" in unlocked) 1f else 0.4f),
                    isGlowing = "strong" in active,
                    glowColor = Color(0xFFFF5722),
                    onClick = {
                        if ("strong" in unlocked) {
                            selectedModifier = "strong"
                            viewModel.modifiers.applyModifier("strong")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "SOLDIER",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("soldier" in unlocked) 1f else 0.4f),
                    isGlowing = "soldier" in active,
                    glowColor = Color(0xFF42A5F5),
                    onClick = {
                        if ("soldier" in unlocked) {
                            selectedModifier = "soldier"
                            viewModel.modifiers.applyModifier("soldier")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "VETERAN",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("veteran" in unlocked) 1f else 0.4f),
                    isGlowing = "veteran" in active,
                    glowColor = Color(0xFFCD7F32),
                    onClick = {
                        if ("veteran" in unlocked) {
                            selectedModifier = "veteran"
                            viewModel.modifiers.applyModifier("veteran")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "REFLEXES",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("reflexes" in unlocked) 1f else 0.4f),
                    isGlowing = "reflexes" in active,
                    glowColor = Color(0xFF00E5FF),
                    onClick = {
                        if ("reflexes" in unlocked) {
                            selectedModifier = "reflexes"
                            viewModel.modifiers.applyModifier("reflexes")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "SMITHING",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("smithing" in unlocked) 1f else 0.4f),
                    isGlowing = "smithing" in active,
                    glowColor = Color(0xFFFF6D00),
                    onClick = {
                        if ("smithing" in unlocked) {
                            selectedModifier = "smithing"
                            viewModel.modifiers.applyModifier("smithing")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "FRIENDLY",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("friendly" in unlocked) 1f else 0.4f),
                    isGlowing = "friendly" in active,
                    glowColor = Color(0xFF26C6DA),
                    onClick = {
                        if ("friendly" in unlocked) {
                            selectedModifier = "friendly"
                            viewModel.modifiers.applyModifier("friendly")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "LOOTER",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("looter" in unlocked) 1f else 0.4f),
                    isGlowing = "looter" in active,
                    glowColor = Color(0xFFFFD700),
                    onClick = {
                        if ("looter" in unlocked) {
                            selectedModifier = "looter"
                            viewModel.modifiers.applyModifier("looter")
                        }
                    }
                )

                CombatButtonLayout(
                    text = "PHOENIX",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(if ("phoenix" in unlocked) 1f else 0.4f),
                    isGlowing = "phoenix" in active,
                    glowColor = Color(0xFFFF3D00),
                    onClick = {
                        if ("phoenix" in unlocked) {
                            selectedModifier = "phoenix"
                            viewModel.modifiers.applyModifier("phoenix")
                        }
                    }
                )
            }
        }

        // GO BACK -> BUTTON — BOTTOM RIGHT
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "BACK TO MENU ->",
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