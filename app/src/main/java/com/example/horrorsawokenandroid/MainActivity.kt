package com.example.horrorsawokenandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.horrorsawokenandroid.game.model.AndroidSoundPlayer
import com.example.horrorsawokenandroid.game.ui.CombatScreen
import com.example.horrorsawokenandroid.game.ui.GameViewModel
import com.example.horrorsawokenandroid.ui.theme.HorrorsAwokenAndroidTheme

class MainActivity : ComponentActivity() {

    private lateinit var soundPlayer: AndroidSoundPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        soundPlayer = AndroidSoundPlayer(this)

        enableEdgeToEdge()

        setContent {
            HorrorsAwokenAndroidTheme {

                val gameViewModel: GameViewModel = viewModel(
                    factory = GameViewModelFactory(soundPlayer)
                )

                CombatScreen(
                    viewModel = gameViewModel
                )
            }
        }
    }

    override fun onDestroy() {
        soundPlayer.release()
        super.onDestroy()
    }
}

class GameViewModelFactory(
    private val sounds: AndroidSoundPlayer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(
                sounds = sounds
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
