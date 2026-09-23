package com.example.horrorsawokenandroid.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // ------------------------------------------------------------
    // ATTACK MOVES
    // ------------------------------------------------------------
    private val attackMoves = AttackMoves(
        getState = {
            _uiState.value
        },

        updateState = { transform ->
            _uiState.update(transform)
        },

        attackResolver = { rawDamage, noLifeSteal, noCrit ->
            executeAttack(
                rawDamage = rawDamage,
                noLifeSteal = noLifeSteal,
                noCrit = noCrit
            )
        },

        sounds = sounds,
        scope = viewModelScope
    )


    // INITIAL SETUP / first encounter
    init {
        startEncounter(
            monsterPool = monsterContainer.listOfMonsters1
        )
    }

    // ------------------------------------------------------------
    // ATTACK BUTTONS
    // ------------------------------------------------------------
    fun normalAttack() {
        attackMoves.normalAttack()
    }

    fun bloodLustAttack() {
        attackMoves.bloodLustAttack()
    }

    fun swiftAttack() {
        attackMoves.swiftAttack()
    }

    fun roarAttack() {
        attackMoves.roarAttack()
    }

    fun divineAttack() {
        attackMoves.divineAttack()
    }

    fun guardAttack() {
        attackMoves.guardAttack()
    }

    // ------------------------------------------------------------
    // ENCOUNTER SETUP
    // ------------------------------------------------------------
    fun startEncounter(
        monsterPool: List<Monster>
    ) {
        if (monsterPool.isEmpty()) return

        checkIfPlayerDefeated() // Start by checking if player is defeated (blood loss or damage over time effects etc)

        droppedItem = null

        val encountered =
            monsterPool[
                Random.nextInt(monsterPool.size)
            ].cloneMonster()

        _uiState.update {
            it.copy(
                monster = encountered,
                encounterLog = initialEncounterText(encountered),
                lootAvailable = false,
                monsterDefeated = false,
                playerDodgedFlag = false,
                goldPopupText = null,
                hpPopupText = null,
                monsterImageShake = 0, // This resets the monster image shake on a new encounter
            )
        }
    }

    private fun initialEncounterText(monster: Monster): String =
        when (monster.name) {

            "Orc" ->
                "You have encountered an ${monster.name}! Execute it."

            "Watchers" ->
                "You have encountered some ${monster.name}! Kill them."

            "Aldrus Thornfell",
            "Wintermaw",
            "The Devouring Abyss" ->
                "You have awakened ${monster.name}! Your end is near."

            "The Frostfallen King" ->
                "You have shattered the ice tomb of ${monster.name}! A colossal figure rises from its crystallized prison."

            "Ultimate Darkness" ->
                "The Hero now faces the ${monster.name} - a final challenge."

            "Awoken Horror" ->
                "You stand before the ${monster.name} itself. This is where you die."

            else ->
                "You have encountered a ${monster.name}! Kill it."
        }

    // ------------------------------------------------------------
    // SHARED ATTACK RESOLUTION
    // ------------------------------------------------------------

    private suspend fun executeAttack(
        rawDamage: Int,
        noLifeSteal: Boolean,
        noCrit: Boolean
    ) {
        var monster = _uiState.value.monster ?: return
        var player = _uiState.value.player
        var damage = rawDamage
        checkIfPlayerDefeated()

        _uiState.update {
            it.copy(
                playerDodgedFlag = false
            )
        }

        // Roar buff countdown
        player = countdownRoarBuff(player)

        // LIFESTEAL
        player = playerLifesteals(noLifeSteal, player)

        // CRITICAL HIT
        val isCrit =
            player.critChance >= Random.nextInt(1, 101)

        if (
            isCrit &&
            !noCrit
        ) {
            damage =
                (
                        damage *
                                (player.critDamage / 100.0)
                        ).toInt()

            sounds.playCrit()
        }

        // --------------------------------------------------------
        // DAMAGE MONSTER
        // --------------------------------------------------------

        monster = monster.copy(
            currentHealth = (
                    monster.currentHealth - damage
                    ).coerceAtLeast(0)
        )

        val attackText =
            attackText(
                monsterName = monster.name,
                damage = damage
            )

        _uiState.update {
            it.copy(
                player = player,
                monster = monster,
                encounterLog = attackText,
                monsterImageShake = it.monsterImageShake + 1,
            )
        }

        // --------------------------------------------------------
        // MONSTER TURN
        // --------------------------------------------------------

        if (monster.currentHealth > 0) {
            delay(175)
            monsterAttacks()
        } else {
            checkIfMonsterDefeated()
        }
    }

    // ------------------------------------------------------------
    // ATTACK TEXT
    // ------------------------------------------------------------

    private fun attackText(
        monsterName: String,
        damage: Int
    ): String {

        val bossNames = setOf(
            "Aldrus Thornfell",
            "Wintermaw",
            "The Devouring Abyss",
            "The Frostfallen King"
        )

        return if (monsterName in bossNames) {
            "You attack $monsterName, and deal $damage damage."
        } else {
            "You attack the $monsterName, and deal $damage damage."
        }
    }

    // This function handles when a monster attacks the player
    private suspend fun monsterAttacks() {

        val monster =
            _uiState.value.monster ?: return

        val player =
            _uiState.value.player

        if (player.currentHealth <= 0) {
            checkIfPlayerDefeated()
            return
        }

        // --------------------------------------------------------
        // DODGE
        // --------------------------------------------------------

        val dodgeRoll =
            Random.nextInt(1, 101)

        if (dodgeRoll <= player.dodgeChance) {

            sounds.playDodge()

            _uiState.update {
                it.copy(
                    playerDodgedFlag = true,
                    encounterLog =
                        it.encounterLog +
                                "\nYou dodged the horror's attack!"
                )
            }

            checkIfPlayerDefeated()
            return
        }

        // --------------------------------------------------------
        // MONSTER DAMAGE
        // --------------------------------------------------------

        val monsterDamage =
            monster.calculateMonsterDamage()

        val armorBlocked =
            minOf(
                player.armor,
                monsterDamage
            )

        val damageTaken =
            (
                    monsterDamage - armorBlocked
                    ).coerceAtLeast(0)

        _uiState.update {

            val updatedPlayer =
                it.player.copy(
                    currentHealth =
                        (
                                it.player.currentHealth -
                                        damageTaken
                                ).coerceAtLeast(0)
                )

            it.copy(
                player = updatedPlayer,
                encounterLog =
                    it.encounterLog +
                            "\nThe horror attacks you back and deals $monsterDamage damage."
            )
        }

        checkIfPlayerDefeated()
    }

    // Function to check if the player is dead
    private fun checkIfPlayerDefeated() {

        if (
            _uiState.value.player.currentHealth <= 0
        ) {
            sounds.playDeathGameOverSound()
            setCurrentScreen(GameScreen.GameOver)
        }
    }

    // Function to check if the monster fought is dead
    private fun checkIfMonsterDefeated() {

        val monster =
            _uiState.value.monster ?: return

        if (monster.currentHealth > 0) return // Functions stops if the monster has more than 0 health, e.g. is still alive

        val player =
            _uiState.value.player

        // Reset temporary combat buffs
        player.resetRoarBuff()
        player.resetGuardBuff()

        val (expGained, goldGained, updatedPlayer) = playerGainsXPandGold(
            monster,
            player
        ) // player gains xp and gold after a battle

        checkIfPlayerLevelsUp(updatedPlayer) // check if player levels up after a battle
        playerRegeneratesHealthBasedOnRegen(player, updatedPlayer)
        generateItemFoundOnMonster(monster) // This function is always called when a monster is defeated. Items.kt handles the drop chance, and CombatScreen handles if the loot image should be shown or not

        // --------------------------------------------------------
        // UPDATE STATE
        // --------------------------------------------------------
        _uiState.update {
            it.copy(
                player = updatedPlayer,

                encounterLog =
                    it.encounterLog +
                            "\nYou have defeated the horror. You gain $expGained xp.",

                goldPopupText =
                    if (goldGained > 0) {
                        "+${goldGained}G"
                    } else {
                        null
                    },

                monster = null,

                monsterDefeated = true,

                playerDodgedFlag = false,
                totalMonstersDefeated = it.totalMonstersDefeated + 1
            )
        }

        setBossDefeatedFlags(monster)
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

    // Function to regenerate some of the player's health after a monster is defeated
    private fun playerRegeneratesHealthBasedOnRegen(
        player: Player,
        updatedPlayer: Player
    ) {
        if (
            player.regeneration > 0 &&
            updatedPlayer.currentHealth < updatedPlayer.maxHealth
        ) {
            updatedPlayer.currentHealth =
                (
                        updatedPlayer.currentHealth +
                                player.regeneration
                        ).coerceAtMost(
                        updatedPlayer.maxHealth
                    )
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
        if (player.PriceToHeal > player.goldInPocket) { return }

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

        if (player.PriceToLearnTechnique > player.goldInPocket) {
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
    }

    fun closeInventory() {
        _uiState.update {
            it.copy(inventoryOpen = false)
        }
    }

    fun equipItem(item: Items.Item) {
        val player = _uiState.value.player

        val newInventory = player.inventory.toMutableList()
        val newEquipped = player.equippedItems.toMutableMap()

        newInventory.remove(item)

        // If something is already equipped there, put it back in inventory
        val oldItem = newEquipped[item.type]
        if (oldItem != null && oldItem != item) {
            newInventory.add(oldItem)
        }

        newEquipped[item.type] = item

        _uiState.update {
            it.copy(
                player = player.copy(
                    inventory = newInventory,
                    equippedItems = newEquipped
                )
            )
        }
    }

    fun unequipItem(item: Items.Item) {
        val player = _uiState.value.player

        val newInventory = player.inventory.toMutableList()
        val newEquipped = player.equippedItems.toMutableMap()

        if (newEquipped[item.type] != item) return

        newEquipped.remove(item.type)
        newInventory.add(item)

        _uiState.update {
            it.copy(
                player = player.copy(
                    inventory = newInventory,
                    equippedItems = newEquipped
                )
            )
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

        startEncounter(monsterPool = monsterPoolBasedOnDirection)
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
                "NORTH" -> monsterContainer.listOfDragonEggAct4North
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
//                        + "Player inventory now contains: " + player.inventory.joinToString(", ") { it.name } + ".", // for debugging
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
            }

            2 -> {
                uiState.value.currentAct = 3
                setCurrentScreen(GameScreen.TownAct3)
                sounds.act3TownMixer()
            }

            3 -> {
                uiState.value.currentAct = 4
                setCurrentScreen(GameScreen.TownAct4)
                sounds.act4TownMixer()
            }

            4 -> {
                uiState.value.currentAct = 5
                setCurrentScreen(GameScreen.TownAct5)
                sounds.act5TownMixer()
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

private fun GameViewModel.playerLifesteals(
    noLifeSteal: Boolean,
    player: Player
): Player {
    var player1 = player
    if (
        !noLifeSteal &&
        player1.lifesteal > 0
    ) {
        val healed =
            (player1.calculateTotalDamage() * player1.lifesteal) / 100

        player1 = player1.copy(
            currentHealth = (
                    player1.currentHealth + healed
                    ).coerceAtMost(player1.maxHealth)
        )
    }
    // TODO maybe make a popup of the amount healed from lifesteal
    return player1
}

private fun countdownRoarBuff(player: Player): Player {
    var player1 = player
    if (player1.roarBuffCountdown > 0) {
        player1 = player1.copy(
            roarBuffCountdown = player1.roarBuffCountdown - 1
        )
    }

    if (
        player1.isRoarActive &&
        player1.roarBuffCountdown == 0
    ) {
        player1.turnOffRoarBuff()
    }
    return player1
}