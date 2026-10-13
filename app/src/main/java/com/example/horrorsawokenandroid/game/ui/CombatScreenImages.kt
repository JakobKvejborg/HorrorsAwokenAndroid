package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.horrorsawokenandroid.R


@Composable
fun CombatScreenImages(
    heroDodgeShake: Float,
    guardAttackEffect: Float,
    divineAttackEffect: Float,
    swiftAttackEffect: Float,
    bloodSplatterCritEffect: Float,
    numberOfDragonEggsInInventory: Int,
    act4QuestIsFinished: Boolean,
) {

    // DRAGON EGGS
    if (numberOfDragonEggsInInventory > 0 && !act4QuestIsFinished) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                contentAlignment = Alignment.Center // Centers the text over the image
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dragonegg),
                    contentDescription = "Dragon Egg Button Background",
                    modifier = Modifier.size(100.dp) // Adjust sizing to fit your text comfortably
                )

                Text( // Number of dragon eggs in inventory
                    text = "$numberOfDragonEggsInInventory",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // Dragon Wings
    if (false) {
        Image(
            painter = painterResource(R.drawable.dragonwings1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                //.fillMaxSize()
                .offset(y = 70.dp, x = 2.dp + heroDodgeShake.dp)
                .size(480.dp)
        )
    }

    // Sheleton Dragon Wings
    if (false) {
        Image(
            painter = painterResource(R.drawable.skeletonwings2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                //.fillMaxSize()
                .offset(y = 160.dp, x = 40.dp + heroDodgeShake.dp)
                .size(320.dp)
        )
    }

    // Guard attack animation image — fullscreen, top of z-order, independent of all other layout
    Image(
        painter = painterResource(R.drawable.guardshield3),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .alpha(guardAttackEffect * 0.7f) // 0.5f = 50% as visible
    )

    // Divine light image — fullscreen, top of z-order, independent of all other layout
    Image(
        painter = painterResource(R.drawable.divinelight),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .alpha(divineAttackEffect * 0.4f) // 0.5f = 50% as visible
    )

    // Swift attack image on Dodge — fullscreen, top of z-order, independent of all other layout
    Image(
        painter = painterResource(R.drawable.swiftattacksword4),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .alpha(swiftAttackEffect * 0.7f) // 0.5f = 50% as visible
    )

    // Blood splatter on critical hit crit image — fullscreen, top of z-order, independent of all other layout
    Image(
        painter = painterResource(R.drawable.bloodsplatter2),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .alpha(bloodSplatterCritEffect * 0.6f) // 0.5f = 50% as visible
    )
}