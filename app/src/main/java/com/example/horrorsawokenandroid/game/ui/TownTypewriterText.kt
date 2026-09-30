package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle

private val techniqueTexts = mapOf(
    1 to "Hours of training teaches you the Blood Lust technique. Use it with care - it deals huge damage, but comes at the cost of blood.",
    2 to "After hours of grueling training, you have learned the Swift technique. Unleash a powerful strike after dodging an attack.",
    3 to "You have now mastered the Roar technique, a battle cry that boosts your reflexes for a period.",
    4 to "You have learned a Divine technique - use it in a time of need.",
    5 to "The once mighty warrior teaches you the Guard technique - a way to boost your defenses. When near death, use this stance to stay alive."
)

private val TECHNIQUE_TEXT_COLOR = Color.White
private const val DEFAULT_TEXT_COLOR = 0xFF4E4E4E

@Composable
fun TownTypewriterText(
    currentAct: Int,
    numberOfTechniquesLearned: Int,
    act1TownTextShown: Boolean,
    act3TownTextShown: Boolean,
    shownTechniqueTexts: Set<Int>,
    adHocText: String?,
    onAct1TextShown: () -> Unit,
    onAct3TextShown: () -> Unit,
    onTechniqueTextShown: (Int) -> Unit,
    onAdHocTextShown: () -> Unit,
) {
    val techniqueText = techniqueTexts[numberOfTechniquesLearned]

    val announcement = remember(currentAct, numberOfTechniquesLearned, adHocText) {
        when {
            adHocText != null -> adHocText to false
            currentAct == 1 && !act1TownTextShown -> "Go west..." to false
            currentAct == 3 && !act3TownTextShown -> "Save me..." to false
            techniqueText != null && numberOfTechniquesLearned !in shownTechniqueTexts ->
                techniqueText to true
            else -> null to false
        }
    }

    val (text, isTechniqueText) = announcement

    DisposableEffect(adHocText) {
        onDispose {
            if (adHocText != null) {
                onAdHocTextShown()
            }
        }
    }

    LaunchedEffect(currentAct, numberOfTechniquesLearned, adHocText) {
        if (currentAct == 1 && !act1TownTextShown) onAct1TextShown()
        if (currentAct == 3 && !act3TownTextShown) onAct3TextShown()
        if (techniqueText != null && numberOfTechniquesLearned !in shownTechniqueTexts) {
            onTechniqueTextShown(numberOfTechniquesLearned)
        }
    }

    if (text != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TypewriterText(
                color = when {
                    adHocText != null -> Color.White
                    isTechniqueText -> TECHNIQUE_TEXT_COLOR
                    else -> Color(DEFAULT_TEXT_COLOR)
                },
                text = text,
                holdMs = when {
                    adHocText != null -> 2500 // How long text should be displayed before disappearing
                    isTechniqueText -> 5000
                    else -> 2900
                },
                letterDelayMs = when {
                    adHocText != null -> 1
                    isTechniqueText -> 10
                    else -> 30
                },
                modifier = Modifier.padding(horizontal = 32.dp),
                onFinished = {
//                if (adHocText != null) {
                    onAdHocTextShown()

                }
            )
        }
    }
}

@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 18.sp,
    letterDelayMs: Long = 30, // lower = faster
    holdMs: Long = 2900,
    onFinished: () -> Unit = {}
) {
    var visibleStart by remember { mutableIntStateOf(0) }
    var visibleEnd by remember { mutableIntStateOf(0) }

    LaunchedEffect(text) {
        visibleStart = 0
        visibleEnd = 0

        for (i in 1..text.length) {
            visibleEnd = i
            delay(letterDelayMs)
        }

        delay(holdMs)

        for (i in 1..text.length) {
            visibleStart = i
            delay(letterDelayMs)
        }

        onFinished()
    }

    Text(
        text = buildAnnotatedString {
            text.forEachIndexed { index, char ->
                val visible = index >= visibleStart && index < visibleEnd

                withStyle(
                    SpanStyle(
                        color = if (visible) color else Color.Transparent
                    )
                ) {
                    append(char)
                }
            }
        },
        fontSize = fontSize,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun GoldPriceLabel(
    amount: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp
) {
    val text = "${amount}G"
    val goldGradient = verticalGradient(
        colors = listOf(
            Color(0xFFFFF6C0), // near-white highlight at top
            Color(0xFFFFD54F), // gold
            Color(0xFFB8860B)  // dark bronze at bottom
        )
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Blurred glow behind the text
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFFD54F).copy(alpha = 0.6f),
            modifier = Modifier.blur(14.dp)
        )

        // Dark outline — 4 offset copies behind the main text
        val outlineOffsets = listOf(
            Offset(-1.5f, -1.5f), Offset(1.5f, -1.5f),
            Offset(-1.5f, 1.5f), Offset(1.5f, 1.5f)
        )
        outlineOffsets.forEach { offset ->
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.offset(x = offset.x.dp, y = offset.y.dp)
            )
        }

        // Main gradient-filled text with drop shadow
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                brush = goldGradient,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    offset = Offset(2f, 3f),
                    blurRadius = 4f
                )
            )
        )
    }
}