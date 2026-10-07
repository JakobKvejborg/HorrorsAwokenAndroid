package com.example.horrorsawokenandroid.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horrorsawokenandroid.game.model.EncounterBattle
import com.example.horrorsawokenandroid.game.model.ItemUpgrader
import com.example.horrorsawokenandroid.game.model.Items
import com.example.horrorsawokenandroid.game.model.ModifierStorage
import com.example.horrorsawokenandroid.game.model.Modifiers
import com.example.horrorsawokenandroid.game.model.Monster
import com.example.horrorsawokenandroid.game.model.MonsterContainer
import com.example.horrorsawokenandroid.game.model.NoOpSoundPlayer
import com.example.horrorsawokenandroid.game.model.Player
import com.example.horrorsawokenandroid.game.model.ReforgeFrogStat
import com.example.horrorsawokenandroid.game.model.SoundPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/*
FORMAT: CTRL + ALT + L
COMMENT CODE: CTRL + / (on the numpad)
SEARCH ENTIRE PROJECT: CTRL + SHIFT + F
IF ERROR ANDROID SDK, CTRL+SHIFT+O TO SYNC GRADLE FILES
*/

/*
GameViewModel contains all the logic that is going on in the game.
Which enemies are encountered etc.
 */

class GameViewModel(
    startingPlayer: Player = Player.newHero(),
    private val sounds: SoundPlayer = NoOpSoundPlayer,
    private val modifierStorage: ModifierStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameUiState(player = startingPlayer)
    )

    val uiState: StateFlow<GameUiState> = _uiState
    internal val monsterContainer = MonsterContainer()
    private val items = Items()
    private var droppedItem: Items.Item? = null
    private var lastMusicAct: Int = -1
    val reforgeFrogStat = ReforgeFrogStat(
        getState = { _uiState.value },
        updateState = { transform -> _uiState.update(transform) },
        sounds = sounds,
    )
    val itemUpgrader = ItemUpgrader(
        getState = { _uiState.value },
        updateState = { transform -> _uiState.update(transform) },
        sounds = sounds,
    )
    val modifiers = Modifiers(
        getState = { _uiState.value },
        updateState = { transform -> _uiState.update(transform) },
        storage = modifierStorage,
        itemUpgrader = itemUpgrader,
        reforgeFrogStat = reforgeFrogStat,
    )

    fun reforgeFrogScreenOpen(open: Boolean) {
        _uiState.update { it.copy(reforgeFrogScreenOpen = open) }
    }

    fun openAct2OptionalAreaScreen(open: Boolean) {
        _uiState.update { it.copy(act2OptionalAreaScreenOpen = open) }
    }

    fun checkIfSophiaIsAlive() {
        val state = uiState.value

        if (state.player.numberOfUnlockedModifiers >= 3) {
            _uiState.update {
                it.copy(sophiaIsDead = false) // Sets sophia to be alive
            }
        }
    }

    fun isSophiaDeadDebug(): Boolean { // TODO delete
        val state = uiState.value

        return state.sophiaIsDead
    }

    fun act3TalkToFrogSound() {
        sounds.playAct3Frog()
    }

    fun womanCryingSoundAct1Q1() {
        sounds.playAct1WomanCrying()
    }

    fun act1Quest1(open: Boolean) {
        val state = uiState.value
        state.act1Quest1Started = true

        if (state.act1Quest1BoyIsSaved && !state.act1Quest1IsFinished) {
            val helmet = Items.Item(
                name = "Father's Helmet",
                type = Items.ItemType.Helmet,
                dodgeChance = (0..1).random(),
                strength = (0..1).random(),
                armor = (0..2).random(),
                health = (0..10).random(),
                levelRequirement = 4,
                strengthRequirement = 5,
            )

            state.player.inventory.add(helmet)

            _uiState.update {
                it.copy(
                    act1Quest1ScreenOpen = open,
                    act1Quest1Started = true,
                    act1Quest1IsFinished = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    act1Quest1ScreenOpen = open,
                    act1Quest1Started = true
                )
            }
        }
    }

    fun markAct1Quest1ThankYouShown() {
        _uiState.update { it.copy(act1Quest1ThankYouShown = true) }
    }

    // Encounter
    internal val encounterBattle = EncounterBattle(
        getState = { _uiState.value },
        updateState = { transform -> _uiState.update(transform) },
        sounds = sounds,
        onMonsterDefeated = { monster ->
            generateItemFoundOnMonster(monster)
            setBossDefeatedFlags(monster)
            if (monster.name == "Hungry Beast") {
                _uiState.update { it.copy(act1Quest1BoyIsSaved = true) }
            }
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

    fun upgradeItem(
        item: Items.Item?,
        onEquipItem: (Items.Item) -> Unit,
        onUpgradeApplied: () -> Unit
    ) {
        itemUpgrader.upgradeItem(item, onEquipItem, onUpgradeApplied)
    }

    // INITIAL SETUP / first encounter
    init {
        _uiState.update { it.copy(player = modifiers.loadUnlockedModifiers(it.player)) } // apply persisted modifiers to the starting player
        encounterBattle.startEncounter(
            monsterPool = monsterContainer.listOfMonstersAct1West,
            onEncounterStarted = { droppedItem = null }
        )
    }

    // ATTACK BUTTONS
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

    private fun setBossDefeatedFlags(monster: Monster) {
        val monsterName = monster.name

        when (monsterName) {
            "Aldrus Thornfell" -> uiState.value.isAct1BossDefeated = true
            "Wintermaw" -> uiState.value.isAct2BossDefeated = true
            "The Devouring Abyss" -> uiState.value.isAct3BossDefeated = true
            "Awoken Horror" -> uiState.value.isAct5BossDefeated = true
        }

    }

    internal fun playerIsHealedByNPC() {
        val player = _uiState.value.player
        val currentAct = uiState.value.currentAct

        // Guard clauses if player doesn't have enough gold
        if (player.priceToHeal > player.goldInPocket && currentAct == 1) {
            sounds.playAct1HealingNoGold()
            return
        }
        if (player.priceToHeal > player.goldInPocket) {
            return
        }

        val newPriceToHeal =
            player.priceToHeal + ((player.priceToHeal * 0.1 + 7).toInt() / player.healingCostModifier)

        _uiState.update {
            it.copy(
                player = it.player.copy(
                    currentHealth = it.player.maxHealth,
                    goldInPocket = it.player.goldInPocket - player.priceToHeal,
                    priceToHeal = newPriceToHeal
                )
            )
        }

        when (currentAct) {
            1 -> sounds.playAct1HealingMusic()
            2 -> sounds.playAct2HealingSound()
            4 -> sounds.playAct4HealingSound()
        }

    }

    // Reset game
    fun restartGame() {
        sounds.muteAllMusic()

        _uiState.value = GameUiState(
            player = modifiers.loadUnlockedModifiers(Player.newHero()),
            currentScreen = GameScreen.Menu,
        )
    }

    // Function to teach the player the different techniques based on which ones he already knows
    internal fun playerLearnTechniques() {
        val player = _uiState.value.player
        val isSkilled = player.hasSkilledModifier

        if (player.priceToLearnTechnique > player.goldInPocket || player.techniqueGuardIsLearned) {
            sounds.playAct1ArtsTeacherNo()
            return
        }

        val learnedPlayer = when {
            !player.techniqueBloodLustIsLearned ->
                player.copy(techniqueBloodLustIsLearned = true)

            !player.techniqueSwiftIsLearned && (isSkilled || uiState.value.hasAct2BeenVisited) ->
                player.copy(techniqueSwiftIsLearned = true)

            !player.techniqueRoarIsLearned && ((isSkilled && uiState.value.hasAct2BeenVisited) || uiState.value.hasAct3BeenVisited) ->
                player.copy(techniqueRoarIsLearned = true)

            !player.techniqueDivineIsLearned && ((isSkilled && uiState.value.hasAct3BeenVisited) || uiState.value.hasAct4BeenVisited) ->
                player.copy(techniqueDivineIsLearned = true)

            !player.techniqueGuardIsLearned && ((isSkilled && uiState.value.hasAct4BeenVisited) || uiState.value.hasAct5BeenVisited) ->
                player.copy(techniqueGuardIsLearned = true)

            else -> null
        } ?: run {
            sounds.playAct1ArtsTeacherNo()
            return
        }

        _uiState.update {
            it.copy(
                player = learnedPlayer.copy(
                    goldInPocket = learnedPlayer.goldInPocket - player.priceToLearnTechnique,
                    priceToLearnTechnique = player.priceToLearnTechnique * 3,
                    numberOfTechniquesLearned = player.numberOfTechniquesLearned + 1,
                )
            )
        }

        sounds.playAct1ArtsTeacher()
    }

    internal fun talkToDragonMage() {
        sounds.playAct4MageSound()
        val player = uiState.value.player
        val questIsCompletingRightNow = player.numberOfDragonEggsInInventory >= 3

        // If the quest is already finished, give them a generic "go away" text and stop!
        if (uiState.value.act4QuestIsFinished) {
            showTownText("Leave me alone, I said! Hahahahaha...")
            return
        }

        _uiState.update { state ->

            state.copy(
                act4Quest1Started = true,
                act4QuestIsFinished = if (questIsCompletingRightNow) true else state.act4QuestIsFinished,
                player = if (questIsCompletingRightNow) {
                    player.copy(numberOfDragonEggsInInventory = 0, hasDragonRuby = true)
                } else {
                    player
                }
            )
        }

        showTownText(
            if (questIsCompletingRightNow) "Goooood... The eggs. Here, take this. Now leave me alone! Hahahahaha..."
            else "What do you want... Oh yes, the eggs! I need 3 Dragon eggs. Didn't I already tell you? They're up north somewhere. Go get them!"
        )

        if (uiState.value.act4QuestIsFinished == true) {
            sounds.dragonRubySound()
        }

    }

    fun showTownText(text: String) {
        _uiState.update { it.copy(adHocTownText = text) }
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
            it.copy(inventoryOpen = true, act2SmithOverlayOpen = true)
        }
        sounds.playAct2SmithOffer()
    }

    fun closeAct2SmithOverlay() {
        _uiState.update {
            it.copy(inventoryOpen = false, act2SmithOverlayOpen = false)
        }
    }

    fun giveSmithDragonRuby() {
        showTownText("What is this now? Where did you find this?! I can sense powerful magic surrounding this item. Perhaps I can make some use of it...")

        _uiState.update {
            it.copy(
                player = it.player.copy(hasDragonRuby = false),
                act4DragonRubyHasBeenGivenToSmith = true,
                smithUpgradeMultiplication = it.smithUpgradeMultiplication + 1
            )
        }
        sounds.playSmithUpgradeRubySound()
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
        val state = uiState.value
        val currentAct = state.currentAct
        if (state.introMonstersAreCompleted) {
            state.firstTimeTownVisitedMusic = false
        }

        // Player goes South
        if (direction == "SOUTH") {
            if (currentAct == 1 && state.act1Quest1Started && !state.act1HungryBeastDefeated) {
                setCurrentScreen(GameScreen.CombatAct1)
                _uiState.update {
                    it.copy(
                        act1HungryBeastDefeated = true, justDefeatedHungryBeast = true
                    )
                }
                encounterBattle.startEncounter(
                    monsterPool = monsterContainer.listOfMonstersAct1Quest1,
                    onEncounterStarted = { droppedItem = null }
                )
                return
            }
            goToPreviousTown()
            return
        }

        // Player goes North
        when (currentAct) {
            1 -> when (direction) {
                "NORTH" -> if (state.isAct1BossDefeated == true) {
                    goToNextAct()
                    return
                }
            }

            2 -> when (direction) {
                "NORTH" -> if (state.isAct2BossDefeated == true) {
                    goToNextAct()
                    return
                }
            }

            3 -> when (direction) {
                "NORTH" -> if (state.isAct3BossDefeated == true) {
                    goToNextAct()
                    return
                }
            }

            5 -> when (direction) {
                "NORTH" -> {
                    if (state.isAct5BossDefeated == true && !state.sophiaIsSaved) {
                        goToNextAct()
                        return
                    }
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
                "WEST" -> monsterContainer.listOfMonstersAct1West
                "EAST" -> monsterContainer.listOfMonstersAct1East
                "NORTH" -> {
                    sounds.playAct1BossSound()
                    monsterContainer.listOfMonstersBossAct1
                }

                else -> {
                    monsterContainer.listOfMonstersAct1West
                }
            }

            // Act 2
            2 -> when (direction) {
                "WEST" -> monsterContainer.listOfMonstersSnowGoldGoblin
                "EAST" -> monsterContainer.listOfSnowMonstersAct2East
                "NORTH" -> {
                    if (uiState.value.isAct2BossDefeated == true) {
                        goToNextAct()
                        return null
                    }
                    sounds.playAct2BossSound()
                    monsterContainer.listOfMonstersBossAct2
                }

                else -> {
                    monsterContainer.listOfSnowMonstersAct2East
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
                    if (uiState.value.act4Quest1Started && !uiState.value.act4QuestIsFinished) {
                        monsterContainer.listOfDragonEggAct4North
                    } else if (uiState.value.act4QuestIsFinished) {
                        goToNextAct()
                        return null
                    } else {
                        monsterContainer.listOfDragonNestWatchersAct4North // Player can't collect eggs unless he has spoken to act4 mage
                    }

                else -> {
                    return null
                }
            }

            // Act 5
            5 -> when (direction) {
                "WEST" -> monsterContainer.listOfAct5MonstersWest
                "EAST" -> monsterContainer.listOfAct5MonstersEast
                "NORTH" -> {
                    if (!uiState.value.isAct5BossDefeated) {
                        monsterContainer.listOfMonstersBossAct5
                    } else {
                        monsterContainer.listOfOptionalBossAct5
                    }
                }

                else -> {
                    monsterContainer.listOfAct5MonstersEast
                }
            }

            else -> {
                monsterContainer.listOfMonstersAct1West
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

        val currentAct = uiState.value.currentAct // which act the player currently is in
        val direction = uiState.value.lastDirectionChosenByPlayer
        val found = items.generateLoot(currentAct, direction)

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
            return true // Egg-Watcher Dragons don't drop any loot
        }

        if (monster.name == "The Frostfallen King") {
            _uiState.update {
                it.copy(player = it.player.copy(hasFrozenLily = true))
            }
        }

        if ( // 100% drop rate and better loot on these bosses
            monster.name == "The Frostfallen King" ||
            monster.name == "Awoken Horror" ||
            monster.name == "Ultimate Darkness"
        ) {
            val found = items.generateBossItem(
                act = uiState.value.currentAct,
                direction = uiState.value.lastDirectionChosenByPlayer
            )
            droppedItem = found
            _uiState.update {
                it.copy(
                    lootAvailable = true,
                    droppedItem = found
                )
            }
            return true
        }

        if (monster.name == "Gold Goblin" || monster.name == "Nest-Watcher Dragon") {
            return true // These monsters doesn't drop items
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
                normalAttackAnimation = 0,
                encounterLog = "You find the item: ${loot.name}."
//                        + "Player inventory now contains: " + player.inventory.joinToString(", ") { it.name } + ". Player dragonegg number: ${player.numberOfDragonEggsInInventory}", // for debugging
            )
        }

    }

    fun act2OptionalBossFight() {
        val state = uiState.value
        if (state.act2OptionalBossDefeated) return
        sounds.playAct2FrostfallenKing()

        // Needed for Continue to not appear right after the fight
        _uiState.update {
            it.copy(
                act2OptionalBossDefeated = true, justDefeatedOptionalBoss = true
            )
        }
        setCurrentScreen(GameScreen.CombatAct2)
        encounterBattle.startEncounter(
            monsterPool = monsterContainer.listOfOptionalBossAct2,
            onEncounterStarted = { droppedItem = null }
        )
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
                uiState.value.hasAct2BeenVisited = true
            }

            2 -> {
                uiState.value.currentAct = 3
                setCurrentScreen(GameScreen.TownAct3)
                sounds.act3TownMixer()
                uiState.value.hasAct3BeenVisited = true
            }

            3 -> {
                uiState.value.currentAct = 4
                setCurrentScreen(GameScreen.TownAct4)
                sounds.act4TownMixer()
                uiState.value.hasAct4BeenVisited = true
            }

            4 -> {
                uiState.value.currentAct = 5
                setCurrentScreen(GameScreen.TownAct5)
                sounds.act5TownMixer()
                uiState.value.hasAct5BeenVisited = true
            }

            5 -> {
                if (uiState.value.sophiaIsDead) {
                    setCurrentScreen(GameScreen.Sophia)
                    sounds.sophiaDeadMixer()
                    modifiers.giveRandomModifier()
                } else if (!uiState.value.sophiaIsSaved) {
                    setCurrentScreen(GameScreen.Sophia)
                    modifiers.giveRandomModifier()
                }
            }
        }

    }

    fun markSophiaSaved() {
        _uiState.update {
            it.copy(sophiaIsSaved = true)
        }
    }

    // This function allows the player to return to town
    fun goToTown() {
        _uiState.update {
            it.copy(
                justDefeatedOptionalBoss = false,
                justDefeatedHungryBeast = false
            )
        }

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
        if (uiState.value.currentAct == 1) {
            return
        }

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

    fun trashAllItemsInInventory() {
        _uiState.update {
            it.player.inventory.clear()
            it
        }
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

    fun markAct1TextShown() {
        _uiState.update { it.copy(act1TownTextShown = true) }
    }

    fun markAct3TextShown() {
        _uiState.update { it.copy(act3TownTextShown = true) }
    }

    fun markTechniqueTextShown(n: Int) {
        _uiState.update { it.copy(shownTechniqueTexts = it.shownTechniqueTexts + n) }
    }

    fun clearAdHocTownText() {
        _uiState.update { it.copy(adHocTownText = null) }
    }

}
