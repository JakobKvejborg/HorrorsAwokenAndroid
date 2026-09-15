package com.example.horrorsawokenandroid.game.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.horrorsawokenandroid.R
import androidx.core.net.toUri
import com.example.horrorsawokenandroid.game.model.AndroidSoundPlayer

@Composable
fun IntroMovieScreen(
    viewModel: GameViewModel
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {
                // Skip intro
                viewModel.startEncounter(
                    monsterPool = viewModel.monsterContainer.listOfMonsters1,
                )
                viewModel.setCurrentScreen(GameScreen.CombatAct1)
                viewModel.playAct4MusicAfterIntro()
            }
    ) {

        AndroidView(
            factory = { context ->

                VideoView(context).apply {

                    setVideoURI(
                        "android.resource://${context.packageName}/${R.raw.intromoviehorror}".toUri()
                    )

                    setOnCompletionListener {

                        // Movie finished
                        viewModel.startEncounter(
                            monsterPool = viewModel.monsterContainer.listOfMonsters1
                        )

                        viewModel.setCurrentScreen(GameScreen.CombatAct1)
                    }

                    start()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}