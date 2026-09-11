package com.example.horrorsawokenandroid.game.ui

import com.example.horrorsawokenandroid.game.model.Items
import com.example.horrorsawokenandroid.game.model.Monster
import com.example.horrorsawokenandroid.game.model.Player

data class GameUiState(
    val player: Player,
    val monster: Monster? = null,
    val encounterLog: String = "",
    val playerDodgedFlag: Boolean = false, // drives SwiftAttack button glow
    val goldPopupText: String? = null,     // null = hidden
    val hpPopupText: String? = null,       // null = hidden
    val lootAvailable: Boolean = false,
    val droppedItem: Items.Item? = null,
    val monsterDefeated: Boolean = false,
    val currentScreen: GameScreen = GameScreen.CombatAct1, // TODO important this makes the game start in combat

)