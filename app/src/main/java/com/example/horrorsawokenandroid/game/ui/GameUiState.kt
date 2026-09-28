package com.example.horrorsawokenandroid.game.ui

import com.example.horrorsawokenandroid.game.model.Items
import com.example.horrorsawokenandroid.game.model.Monster
import com.example.horrorsawokenandroid.game.model.Player

data class GameUiState(
    val player: Player,
    val monster: Monster? = null,
    val encounterLog: String = "",
    val playerDodgedFlag: Boolean = false,
    val goldPopupText: String? = null,     // null = hidden
    val hpPopupText: String? = null,       // null = hidden
    val lootAvailable: Boolean = false,
    val droppedItem: Items.Item? = null,
    val monsterDefeated: Boolean = false,
    val introMonstersDefeated: Int = 0,
    var introMonstersAreCompleted: Boolean = false,
    var totalMonstersDefeated: Int = 0,
    val monsterImageShake: Int = 0,
    val heroDodgeShake: Int = 0,
    val inventoryOpen: Boolean = false,
    var act2SmithOverlayOpen: Boolean = false,
    val currentScreen: GameScreen = GameScreen.Menu, // This decides where the game begins (in menu, in combat e.g.)
    var isAct1BossDefeated: Boolean = false,
    var isAct2BossDefeated: Boolean = false,
    var isAct3BossDefeated: Boolean = false,
    var isAct5BossDefeated: Boolean = false,
    var lastDirectionChosenByPlayer: String = "",
    var firstTimeTownVisitedMusic: Boolean = true,
    var currentAct: Int = 1,
    var hasAct2BeenVisited: Boolean = false,
    var hasAct3BeenVisited: Boolean = false,
    var hasAct4BeenVisited: Boolean = false,
    var hasAct5BeenVisited: Boolean = false,

    // Attack animations
    var bloodLustAnimation: Int = 0,
    var divineAnimation: Int = 0,
    var bloodSplatterAnimation: Int = 0,
    var guardAnimation: Int = 0,
    var swiftAnimation: Int = 0,
    var normalAttackAnimation: Int = 0,

    // Quests
    var act1Quest1Started: Boolean = false,
    var act1Quest1IsFinished: Boolean = false,
    var act4QuestIsFinished: Boolean = false,
    var act4QuestRubyHasBeenGivenToSmith: Boolean = false,
    var sophiaIsDead: Boolean = true,
    var act1TownTextShown: Boolean = false,
    var act3TownTextShown: Boolean = false,

    )