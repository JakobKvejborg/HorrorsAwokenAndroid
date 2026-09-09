package com.example.horrorsawokenandroid.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horrorsawokenandroid.game.model.Item
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

class GameViewModel(
    startingPlayer: Player = Player.newHero(),
    private val sounds: SoundPlayer = NoOpSoundPlayer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameUiState(player = startingPlayer)
    )

    val uiState: StateFlow<GameUiState> = _uiState

    private val monsterContainer = MonsterContainer()

    private var encounterItems: List<Item> = emptyList()
    private var droppedItem: Item? = null

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
            monsterPool = monsterContainer.listOfMonsters1,
            itemPool = emptyList()
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
        monsterPool: List<Monster>,
        itemPool: List<Item>
    ) {
        if (monsterPool.isEmpty()) return

        droppedItem = null
        encounterItems = itemPool

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
                    it.encounterLog +
                            "\n" +
                            attackText
            )
        }

        // --------------------------------------------------------
        // MONSTER TURN
        // --------------------------------------------------------

        if (monster.currentHealth > 0) {
            delay(200)
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

    // ------------------------------------------------------------
    // MONSTER ATTACK
    // ------------------------------------------------------------

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

    // ------------------------------------------------------------
    // PLAYER DEFEATED
    // ------------------------------------------------------------

    private fun checkIfPlayerDefeated() {

        if (
            _uiState.value.player.currentHealth <= 0
        ) {
            // TODO:
            // Port CheckIfPlayerIsDefeated from C#
            // and implement the game-over screen.
        }
    }

    // ------------------------------------------------------------
    // MONSTER DEFEATED
    // ------------------------------------------------------------

    private fun checkIfMonsterDefeated() {

        val monster =
            _uiState.value.monster ?: return

        if (monster.currentHealth > 0) return

        val player =
            _uiState.value.player

        // Reset temporary combat buffs.
        player.resetRoarBuff()
        player.resetGuardBuff()

        // --------------------------------------------------------
        // EXPERIENCE / GOLD
        // --------------------------------------------------------

        val expGained =
            monster.monsterExperience

        val goldGained =
            monster.monsterGold * player.goldFind

        val updatedPlayer =
            player.copy(
                experience =
                    player.experience + expGained,

                goldInPocket =
                    player.goldInPocket + goldGained
            )

        // --------------------------------------------------------
        // LEVEL UP
        // --------------------------------------------------------

        updatedPlayer.levelUp()

        // --------------------------------------------------------
        // REGENERATION
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // GOLD SOUND
        // --------------------------------------------------------

        if (goldGained > 0) {
            sounds.playCoin()
        }

        // --------------------------------------------------------
        // ITEM DROP
        // --------------------------------------------------------

        generateItemFoundOnMonster(monster)

        totalMonstersDefeated++

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

                playerDodgedFlag = false
            )
        }
    }

    // ------------------------------------------------------------
    // ITEM DROP
    // ------------------------------------------------------------

    private fun generateItemFoundOnMonster(
        monster: Monster
    ) {

        // TODO:
        // Port GetDragonEgg / GetFrozenLily
        // special-case drops once Item/Player fields are final.

        if (encounterItems.isEmpty()) {
            return
        }

        val found =
            encounterItems[
                Random.nextInt(
                    encounterItems.size
                )
            ].cloneItem()

        droppedItem = found

        _uiState.update {
            it.copy(
                lootAvailable = true
            )
        }
    }

    // ------------------------------------------------------------
    // LOOT
    // ------------------------------------------------------------

    fun lootItem() {

        val item =
            droppedItem ?: return

        _uiState.update {
            it.copy(
                encounterLog =
                    it.encounterLog +
                            "\nYou find an item on the horror's corpse: ${item.name}.",

                lootAvailable = false
            )

            // TODO:
            // Add item to the real inventory once
            // ItemContainer has been ported.
        }

        droppedItem = null
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