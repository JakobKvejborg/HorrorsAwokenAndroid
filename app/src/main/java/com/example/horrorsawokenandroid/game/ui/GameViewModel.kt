package com.example.horrorsawokenandroid.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horrorsawokenandroid.game.model.EncounterBattle
import com.example.horrorsawokenandroid.game.model.ItemUpgrader
import com.example.horrorsawokenandroid.game.model.Items
import com.example.horrorsawokenandroid.game.model.Monster
import com.example.horrorsawokenandroid.game.model.MonsterContainer
import com.example.horrorsawokenandroid.game.model.NoOpSoundPlayer
import com.example.horrorsawokenandroid.game.model.Player
import com.example.horrorsawokenandroid.game.model.SoundPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

/*
FORMAT: CTRL + ALT + L
COMMENT CODE: CTRL + / (on the numpad)
SEARCH ENTIRE PROJECT: CTRL + SHIFT + F
*/

/*
GameViewModel contains all the logic that is going on in the game.
Which enemies are encountered etc.
 */

class GameViewModel(
    startingPlayer: Player = Player.newHero(),
    private val sounds: SoundPlayer = NoOpSoundPlayer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameUiState(player = startingPlayer)
    )

    val uiState: StateFlow<GameUiState> = _uiState
    internal val monsterContainer = MonsterContainer()
    private val items = Items()
    private var droppedItem: Items.Item? = null
    private var lastMusicAct: Int = -1

    // Encounter
    internal val encounterBattle = EncounterBattle(
        getState = { _uiState.value },
        updateState = { transform -> _uiState.update(transform) },
        sounds = sounds,
        onMonsterDefeated = { monster ->
            generateItemFoundOnMonster(monster)
            setBossDefeatedFlags(monster)
        },
        onPlayerDefeated = {
            setCurrentScreen(GameScreen.GameOver)
        }
    )

    fun startEncounter(monsterPool: List<Monster>) {
        encounterBattle.startEncounter(
            monsterPool = monsterPool,
            onEncounterStarted = { droppedItem = null }
        )
    }

    // ATTACK MOVES
    private val attackMoves = AttackMoves(
        getState = {
            _uiState.value
        },

        updateState = { transform ->
            _uiState.update(transform)
        },

        attackResolver = { rawDamage, noLifeSteal, noCrit ->
            encounterBattle.executeAttack(
                rawDamage = rawDamage,
                noLifeSteal = noLifeSteal,
                noCrit = noCrit
            )
        },

        sounds = sounds,
        scope = viewModelScope
    )

    // ITEM UPGRADES
    val itemUpgrader = ItemUpgrader(
        getState = { _uiState.value },
        updateState = { transform -> _uiState.update(transform) },
        sounds = sounds,
    )

    fun upgradeItem(
        item: Items.Item?,
        onEquipItem: (Items.Item) -> Unit,
        onUpgradeApplied: () -> Unit
    ) {
        itemUpgrader.upgradeItem(item, onEquipItem, onUpgradeApplied)
    }

    // INITIAL SETUP / first encounter
    init {
        encounterBattle.startEncounter(
            monsterPool = monsterContainer.listOfMonsters1,
            onEncounterStarted = { droppedItem = null }
        )
    }

    // ATTACK BUTTONS
    fun normalAttack() {
        attackMoves.normalAttack()
    }

    fun bloodLustAttack() {
        _uiState.update {
            it.copy(
                bloodlustAnimation = it.bloodlustAnimation + 1
            )
        }
        attackMoves.bloodLustAttack()
    }

    fun swiftAttack() {
        attackMoves.swiftAttack()
    }

    fun roarAttack() {
        attackMoves.roarAttack()
    }

    fun divineAttack() {
        _uiState.update {
            it.copy(
                divineAnimation = it.divineAnimation + 1
            )
        }
        attackMoves.divineAttack()
    }

    fun guardAttack() {
        attackMoves.guardAttack()
    }

    private fun setBossDefeatedFlags(monster: Monster) {
        val monsterName = monster.name

        when (monsterName) {
            "Aldrus Thornfell" -> uiState.value.isAct1BossDefeated = true
            "Wintermaw" -> uiState.value.isAct2BossDefeated = true
            "The Devouring Abyss" -> uiState.value.isAct3BossDefeated = true
            "Awoken Horror" -> uiState.value.isAct5BossDefeated = true
        }

    }

    private fun checkIfPlayerDefeated() {

        if (
            _uiState.value.player.currentHealth <= 0
        ) {
            sounds.playDeathGameOverSound()
            setCurrentScreen(GameScreen.GameOver)
        }
    }

    internal fun playerIsHealedByNPC() {
        val player = _uiState.value.player
        val currentAct = uiState.value.currentAct

        // Guard clauses if player doesn't have enough gold
        if (player.PriceToHeal > player.goldInPocket && currentAct == 1) {
            sounds.playAct1HealingNoGold()
            return
        }
        if (player.PriceToHeal > player.goldInPocket) {
            return
        }

        player.currentHealth = player.maxHealth
        player.goldInPocket -= player.PriceToHeal
        player.PriceToHeal += (player.PriceToHeal * 0.1 + 7).toInt() // TODO / ModifierProcessor.HealPriceReducedModifier

        when (currentAct) {
            1 -> sounds.playAct1HealingMusic()
            2 -> sounds.playAct2HealingSound()
            4 -> sounds.playAct4HealingSound()
        }

    }

    private fun checkIfPlayerLevelsUp(updatedPlayer: Player) {
        val didLevelUp =
            updatedPlayer.levelUp()

        if (didLevelUp) {
            sounds.playLevelUp()
        }
    }

    fun restartGame() {
        sounds.muteAllMusic()

        _uiState.value = GameUiState(
            player = Player.newHero(),
            currentScreen = GameScreen.Menu
        )
    }

    private fun playerGainsXPandGold(
        monster: Monster,
        player: Player
    ): Triple<Int, Int, Player> {
        val expGained =
            monster.monsterExperience

        val goldGained =
            monster.monsterGold * player.goldFind
        if (goldGained > 0) {
            sounds.playCoin()
        }

        val updatedPlayer =
            player.copy(
                experience =
                    player.experience + expGained,

                goldInPocket =
                    player.goldInPocket + goldGained
            )

        return Triple(expGained, goldGained, updatedPlayer)
    }

    // Function to tech the player the different techniques based on which ones he already knows
    internal fun playerLearnTechniques() {
        val player = _uiState.value.player

        if (player.PriceToLearnTechnique > player.goldInPocket || player.TechniqueGuardIsLearned) { // Function does nothing if player already knows Guard
            sounds.playAct1ArtsTeacherNo()
            return
        }

        player.goldInPocket -= player.PriceToLearnTechnique
        player.PriceToLearnTechnique *= 3;
        sounds.playAct1ArtsTeacher()

        if (!player.techniqueBloodLustIsLearned) {
            player.techniqueBloodLustIsLearned = true

        } else if (!player.TechniqueSwiftIsLearned) {
            player.TechniqueSwiftIsLearned = true

        } else if (!player.TechniqueRoarIsLearned) {
            player.TechniqueRoarIsLearned = true

        } else if (!player.TechniqueDivineIsLearned) {
            player.TechniqueDivineIsLearned = true

        } else if (!player.TechniqueGuardIsLearned) {
            player.TechniqueGuardIsLearned = true
        }
    }

    // Inventory
    fun openInventory() {
        _uiState.update {
            it.copy(inventoryOpen = true)
        }
        sounds.playInventorySound()
    }

    fun closeInventory() {
        _uiState.update {
            it.copy(inventoryOpen = false)
        }
    }

    fun openAct2SmithOverlay() {
        _uiState.update {
            it.copy(inventoryOpen = true)
        }
        uiState.value.act2SmithOverlayOpen = true
        sounds.playAct2SmithOffer()
    }

    fun closeAct2SmithOverlay() {
        _uiState.update {
            it.copy(inventoryOpen = false)
        }
        uiState.value.act2SmithOverlayOpen = false
    }

    fun equipItem(item: Items.Item) {
        val player = _uiState.value.player

        soundsForEquippingItems()

        val updatedPlayer = player.copy()
        updatedPlayer.equipItem(item) // player stats are updated after equipping an item

        _uiState.update {
            it.copy(player = updatedPlayer)
        }
    }

    fun unEquipItem(item: Items.Item) {
        val player = _uiState.value.player

        sounds.playLootItemsSound()

        val updatedPlayer = player.copy()
        updatedPlayer.unEquipItem(item) // player stats are updated after unequipping an item

        _uiState.update {
            it.copy(player = updatedPlayer)
        }
    }

    fun whereToGoBasedOnTheDirection(direction: String) {
        val currentAct = uiState.value.currentAct
        if (uiState.value.introMonstersAreCompleted) {
            uiState.value.firstTimeTownVisitedMusic = false
        }

        // Player goes South
        if (direction == "SOUTH") {
            if (uiState.value.act1Quest1Started) {
                // TODO needs act 1 quest
            }
            when (currentAct) {
                2 -> setCurrentScreen(GameScreen.TownAct1)
                3 -> setCurrentScreen(GameScreen.TownAct2)
                4 -> setCurrentScreen(GameScreen.TownAct3)
                5 -> setCurrentScreen(GameScreen.TownAct4)
            }
            return
        }

        // Player goes North
        when (currentAct) {
            1 -> when (direction) {
                "NORTH" -> if (uiState.value.isAct1BossDefeated == true) {
                    goToNextAct()
                    return
                }
            }

            2 -> when (direction) {
                "NORTH" -> if (uiState.value.isAct2BossDefeated == true) {
                    goToNextAct()
                    return
                }
            }

            3 -> when (direction) {
                "NORTH" -> if (uiState.value.isAct3BossDefeated == true) {
                    goToNextAct()
                    return
                }
            }

            5 -> when (direction) {
                "NORTH" -> if (uiState.value.isAct5BossDefeated == true && uiState.value.sophiaIsDead) {
                    // TODO make a "Sophia" game view
                    return
                }
            }
        }

        var monsterPoolBasedOnDirection =
            whichListOfMonstersToFightBasedOnDirection(direction, currentAct) ?: return


        val combatScreen = when (currentAct) {
            1 -> GameScreen.CombatAct1
            2 -> GameScreen.CombatAct2
            3 -> GameScreen.CombatAct3
            4 -> GameScreen.CombatAct4
            5 -> GameScreen.CombatAct5
            else -> GameScreen.CombatAct1
        }

        setCurrentScreen(combatScreen)

        encounterBattle.startEncounter(
            monsterPool = monsterPoolBasedOnDirection,
            onEncounterStarted = { droppedItem = null }
        )
    }

    private fun whichListOfMonstersToFightBasedOnDirection(
        direction: String,
        currentAct: Int
    ): List<Monster>? {

        val monsterPoolBasedOnDirection = when (currentAct) {
            // Act 1
            1 -> when (direction) {
                "WEST" -> monsterContainer.listOfMonsters1
                "EAST" -> monsterContainer.listOfMonsters2
                "NORTH" -> {
                    sounds.playAct1BossSound()
                    monsterContainer.listOfMonstersBossAct1
                }

                else -> {
                    monsterContainer.listOfMonsters1
                }
            }

            // Act 2
            2 -> when (direction) {
                "WEST" -> monsterContainer.listOfMonstersSnowGoldGoblin
                "EAST" -> monsterContainer.listOfSnowMonsters1
                "NORTH" -> {
                    if (uiState.value.isAct2BossDefeated == true) {
                        goToNextAct()
                        return null
                    }
                    sounds.playAct2BossSound()
                    monsterContainer.listOfMonstersBossAct2
                }

                else -> {
                    monsterContainer.listOfSnowMonsters1
                }
            }

            // Act 3
            3 -> when (direction) {
                "WEST" -> monsterContainer.listOfMonstersAct3
                "EAST" -> monsterContainer.listOfMonstersAct3
                "NORTH" -> {
                    if (uiState.value.isAct3BossDefeated == true) {
                        goToNextAct()
                        return null
                    }
                    sounds.playAct3Boss()
                    monsterContainer.listOfMonstersBossAct3
                }

                else -> {
                    monsterContainer.listOfMonstersAct3
                }
            }

            // Act 4
            4 -> when (direction) {
                "WEST" -> monsterContainer.listOfMonstersAct4West
                "EAST" -> monsterContainer.listOfDragonsAct4East
                "NORTH" ->
                    if (uiState.value.player.numberOfDragonEggsInInventory < 4) {
                        monsterContainer.listOfDragonEggAct4North
                    } else {
                        goToNextAct()
                        return null
                    }

                else -> {
                    monsterContainer.listOfMonstersAct4West
                }
            }

            // Act 5
            5 -> when (direction) {
                "WEST" -> monsterContainer.listOfAct5Monsters
                "EAST" -> monsterContainer.listOfAct5Monsters
                "NORTH" -> {
                    if (!uiState.value.isAct5BossDefeated == true) {
                        monsterContainer.listOfMonstersBossAct5
                    } else {
                        monsterContainer.listOfOptionalBossAct5
                    }
                }

                else -> {
                    monsterContainer.listOfAct5Monsters
                }
            }

            else -> {
                monsterContainer.listOfMonsters1
            }
        }

        return monsterPoolBasedOnDirection
    }

    fun setCurrentScreen(screen: GameScreen) {
        _uiState.update {
            it.copy(currentScreen = screen)
        }
    }

    // Item drop
    private fun generateItemFoundOnMonster(monster: Monster) {
        if (specialMonsterDrops(monster)) {
            return
        }

        val currentAct =
            uiState.value.currentAct // This method finds out which act the play currently is in
        val found = items.generateLoot(currentAct)

        droppedItem = found

        _uiState.update {
            it.copy(
                lootAvailable = found != null, // found != null means "found contains something"
                droppedItem = found
            )
        }
    }

    private fun specialMonsterDrops(monster: Monster): Boolean {
        if (monster.name == "Egg-Watcher Dragon") {
            _uiState.update { state ->
                state.copy(
                    player = state.player.copy(
                        numberOfDragonEggsInInventory =
                            state.player.numberOfDragonEggsInInventory + 1
                    )
                )
            }

            return true
        }

        if (monster.name == "Gold Goblin" || monster.name == "Nest-Watcher Dragon") {
            return true
        }

        return false
    }

    // This function adds the dropped item to the player's inventory
    fun playerCollectsLoot() {

        val loot = _uiState.value.droppedItem ?: return
        val player = _uiState.value.player

        player.inventory.add(loot)
        sounds.playLootItemsSound() // plays a sound when the player loots an item

        droppedItem = null

        _uiState.update {
            it.copy(
                lootAvailable = false,
                droppedItem = null,
                encounterLog = "You find the item: ${loot.name}! "
                        + "Player inventory now contains: " + player.inventory.joinToString(", ") { it.name } + ". Player dragonegg number: ${player.numberOfDragonEggsInInventory}", // for debugging
            )
        }

    }

    // This function allows the player to continue in combat
    fun continueAfterMonsterDefeated() {

        _uiState.update {
            it.copy(
                monsterDefeated = false,
                lootAvailable = false
            )
        }

        whereToGoBasedOnTheDirection(uiState.value.lastDirectionChosenByPlayer)
    }

    private fun goToNextAct() {
        val currentAct = uiState.value.currentAct
        uiState.value.firstTimeTownVisitedMusic = true

        when (currentAct) {
            1 -> {
                uiState.value.currentAct = 2
                setCurrentScreen(GameScreen.TownAct2)
                sounds.act2TownMixer()
                uiState.value.isAct1BossDefeated = false // Makes act 1 boss repeatable
                uiState.value.hasAct2BeenVisited == true
            }

            2 -> {
                uiState.value.currentAct = 3
                setCurrentScreen(GameScreen.TownAct3)
                sounds.act3TownMixer()
                uiState.value.hasAct3BeenVisited == true
            }

            3 -> {
                uiState.value.currentAct = 4
                setCurrentScreen(GameScreen.TownAct4)
                sounds.act4TownMixer()
                uiState.value.hasAct4BeenVisited == true
            }

            4 -> {
                uiState.value.currentAct = 5
                setCurrentScreen(GameScreen.TownAct5)
                sounds.act5TownMixer()
                uiState.value.hasAct5BeenVisited
            }
        }

        // sample code
        /*
        when {
            currentAct == 2 && uiState.value.isAct2BossDefeated -> GameScreen.TownAct3
        }
        */

    }

    // This function allows the player to return to town
    fun goToTown() {

        val currentAct = uiState.value.currentAct

        val gameScreenBasedOnAct: GameScreen = when (currentAct) {
            1 -> GameScreen.TownAct1
            2 -> GameScreen.TownAct2
            3 -> GameScreen.TownAct3
            4 -> GameScreen.TownAct4
            5 -> GameScreen.TownAct5
            else -> GameScreen.TownAct1 // Fallback
        }

        _uiState.update {
            it.copy(
                currentScreen = gameScreenBasedOnAct,
                monsterDefeated = false,
                monster = null,
            )
        }
        playTownMusicBasedOnAct()
    }

    // This function returns the player to the previous act town AKA south
    fun goToPreviousTown() {
        val previousAct = (uiState.value.currentAct - 1).coerceAtLeast(1)
        uiState.value.firstTimeTownVisitedMusic = true

        val previousTown = when (previousAct) {
            1 -> GameScreen.TownAct1
            2 -> GameScreen.TownAct2
            3 -> GameScreen.TownAct3
            4 -> GameScreen.TownAct4
            5 -> GameScreen.TownAct5
            else -> GameScreen.TownAct1
        }

        _uiState.update {
            it.copy(
                currentAct = previousAct,
                currentScreen = previousTown,
                monsterDefeated = false,
                monster = null
            )
        }

        playTownMusicBasedOnAct()
    }

    fun playTownMusicBasedOnAct() {
        val currentAct = uiState.value.currentAct
        val playMusicOrNot = uiState.value.firstTimeTownVisitedMusic

        if (!playMusicOrNot) {
            return
        }

        when (currentAct) {
            1 -> sounds.act1TownMixer()
            2 -> sounds.act2TownMixer()
            3 -> sounds.act3TownMixer()
            4 -> sounds.act4TownMixer()
            5 -> sounds.act5TownMixer()
        }

        uiState.value.firstTimeTownVisitedMusic = false
    }


    fun playAct4MusicAfterIntro() {
        sounds.playAct4Music()
    }

    // TODO maybe a sound for each different item type
    fun soundsForEquippingItems() {
        sounds.playEquipSound()
    }

    fun playTrashSound() {
        sounds.playTrashSound()
    }

    // ------------------------------------------------------------
    // POPUPS
    // ------------------------------------------------------------
    fun dismissGoldPopup() {
        _uiState.update {
            it.copy(
                goldPopupText = null
            )
        }
    }

    fun dismissHpPopup() {
        _uiState.update {
            it.copy(
                hpPopupText = null
            )
        }
    }
}
