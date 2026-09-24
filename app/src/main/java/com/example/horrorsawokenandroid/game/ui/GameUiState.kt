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
    val introMonstersDefeated: Int = 0,
    var introMonstersAreCompleted: Boolean = false,
    var totalMonstersDefeated: Int = 0,
    val monsterImageShake: Int = 0,
    val inventoryOpen: Boolean = false,
    val currentScreen: GameScreen = GameScreen.Menu, // This decides where the game begins (in menu, in combat e.g.)
    var isAct1BossDefeated: Boolean = false,
    var isAct2BossDefeated: Boolean = false,
    var isAct3BossDefeated: Boolean = false,
    var isAct5BossDefeated: Boolean = false,
    var lastDirectionChosenByPlayer: String = "",
    var firstTimeTownVisitedMusic: Boolean = true,
    var currentAct: Int = 1,

    // Quests
    var act1Quest1Started: Boolean = false,
    var act1Quest1IsFinished: Boolean = false,
    var act4QuestIsFinished: Boolean = false,
    var act4QuestRubyHasBeenGivenToSmith: Boolean = false,
    var sophiaIsDead: Boolean = true,
)