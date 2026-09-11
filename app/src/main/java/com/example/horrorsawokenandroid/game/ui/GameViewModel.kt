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
    private val monsterContainer = MonsterContainer()
    private val items = Items()
    private var droppedItem: Items.Item? = null

    var totalMonstersDefeated = 0
        private set


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

    // ------------------------------------------------------------
    // INITIAL SETUP
    // ------------------------------------------------------------

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
                hpPopupText = null
            )
        }
    }

    private fun initialEncounterText(monster: Monster): String =
        when (monster.name) {

            "Watchers" ->
                "You have encountered some ${monster.name}! Kill them."

            "Aldrus Thornfell",
            "Wintermaw",
            "The Devouring Abyss" ->
                "You have awakened ${monster.name}! Your end is near."

            "The Frostfallen King" ->
                "You have shattered the ice tomb of ${monster.name}! A colossal figure rises from its crystalized prison."

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

        _uiState.update {
            it.copy(
                playerDodgedFlag = false
            )
        }

        // --------------------------------------------------------
        // ROAR BUFF COUNTDOWN
        // --------------------------------------------------------

        if (player.roarBuffCountdown > 0) {
            player = player.copy(
                roarBuffCountdown = player.roarBuffCountdown - 1
            )
        }

        if (
            player.isRoarActive &&
            player.roarBuffCountdown == 0
        ) {
            player.turnOffRoarBuff()
        }

        // --------------------------------------------------------
        // LIFESTEAL
        // --------------------------------------------------------

        if (
            !noLifeSteal &&
            player.lifesteal > 0
        ) {
            val healed =
                (player.calculateTotalDamage() * player.lifesteal) / 100

            player = player.copy(
                currentHealth = (
                        player.currentHealth + healed
                        ).coerceAtMost(player.maxHealth)
            )

            if (healed > 0) {
                _uiState.update {
                    it.copy(
                        hpPopupText = "+$healed"
                    )
                }
            }
        }

        // --------------------------------------------------------
        // CRITICAL HIT
        // --------------------------------------------------------

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
                encounterLog =
                    attackText
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
            // TODO:
            // Port CheckIfPlayerIsDefeated from C#
            // and implement the game-over screen.
        }
    }


    // Function to check if the monster fought is dead
    private fun checkIfMonsterDefeated() {

        val monster =
            _uiState.value.monster ?: return

        if (monster.currentHealth > 0) return // Functions stops if the monster has more than 0 health, e.g. is still alive

        val player =
            _uiState.value.player

        // Reset temporary combat buffs.
        player.resetRoarBuff()
        player.resetGuardBuff()

        val (expGained, goldGained, updatedPlayer) = playerGainsXPandGold(monster, player) // player gains xp and gold after a battle

        checkIfPlayerLevelsUp(updatedPlayer) // check if player levels up after a battle

        playerRegeneratesHealthBasedOnRegen(player, updatedPlayer)

        generateItemFoundOnMonster(monster) // This function is always called when a monster is defeated. Items.kt handles the drop chance, and CombatScreen handles if the loot image should be shown or not

        // --------------------------------------------------------
        // UPDATE STATE
        // --------------------------------------------------------
        totalMonstersDefeated++

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

                playerDodgedFlag = false
            )
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

    private fun checkIfPlayerLevelsUp(updatedPlayer: Player) {
        val didLevelUp =
            updatedPlayer.levelUp()

        if (didLevelUp) {
            sounds.playLevelUp()
        }
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

    // ------------------------------------------------------------
    // ITEM DROP
    // ------------------------------------------------------------
    private fun generateItemFoundOnMonster(monster: Monster) {

        val currentAct = getCurrentAct() // This method finds out which act the play currently is in
        val found = items.generateLoot(currentAct)

        droppedItem = found

        _uiState.update {
            it.copy(
                lootAvailable = found != null, // TODO i don't understand this code
                droppedItem = found
            )
        }
    }

    // This function adds the dropped item to the player's inventory
    fun playerCollectsLoot() {

        val loot = _uiState.value.droppedItem ?: return
        val player = _uiState.value.player

        player.inventory.add(loot)

        droppedItem = null

        _uiState.update {
            it.copy(
                lootAvailable = false,
                droppedItem = null,
                encounterLog = "You find the item: ${loot.name}! " +
                        "Player inventory now contains: " + player.inventory.joinToString(", ") {it.name} + ".", // TODO delete this, it's just for debugging
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

        startEncounter(
            // TODO super important, monsters not working
            monsterPool = monsterContainer.listOfMonsters1,
        )
    }

    // This functions allows the player to return to town
    fun goToTown() {
        _uiState.update {
            it.copy(
                currentScreen = GameScreen.TownAct1,
                monsterDefeated = false,
                monster = null,
            )
        }
        sounds.playAct1TownMusic()
        sounds.stopAct4Music() // TODO remove
    }

    // This function finds out which act the play currently is in and returns it as an Int // TODO add more if the game expands
    private fun getCurrentAct(): Int {
        return when (_uiState.value.currentScreen) {
            GameScreen.Menu -> 1

            GameScreen.CombatAct1,
            GameScreen.TownAct1 -> 1

            GameScreen.CombatAct2,
            GameScreen.TownAct2 -> 2

            GameScreen.CombatAct3,
            GameScreen.TownAct3 -> 3

            GameScreen.CombatAct4,
            GameScreen.TownAct4 -> 4

            GameScreen.CombatAct5,
            GameScreen.TownAct5 -> 5
        }
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