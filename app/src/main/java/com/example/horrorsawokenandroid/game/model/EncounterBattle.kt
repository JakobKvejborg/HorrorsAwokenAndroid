package com.example.horrorsawokenandroid.game.model

import com.example.horrorsawokenandroid.game.ui.GameUiState
import kotlin.random.Random
import kotlinx.coroutines.delay

class EncounterBattle(
    private val getState: () -> GameUiState, // unresolved reference
    private val updateState: ((GameUiState) -> GameUiState) -> Unit,
    private val sounds: SoundPlayer,
    private val onMonsterDefeated: (Monster) -> Unit, // GameViewModel hooks in generateItemFoundOnMonster + setBossDefeatedFlags
    private val onPlayerDefeated: () -> Unit,          // GameViewModel hooks in setCurrentScreen(GameOver)
) {

    suspend fun executeAttack(
        rawDamage: Int,
        noLifeSteal: Boolean,
        noCrit: Boolean
    ) {
        var monster = getState().monster ?: return
        var player = getState().player
        var damage = rawDamage
        checkIfPlayerDefeated()

        updateState {
            it.copy(
                playerDodgedFlag = false
            )
        }

        player = countdownRoarBuff(player) // Roar buff countdown
        player = playerLifesteals(noLifeSteal, player) // LIFESTEAL

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

        // DAMAGE MONSTER
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

        updateState {
            it.copy(
                player = player,
                monster = monster,
                encounterLog = attackText,
                monsterImageShake = it.monsterImageShake + 1,
            )
        }

        // MONSTER TURN
        if (monster.currentHealth > 0) {
            delay(175)
            monsterAttacks()
        } else {
            checkIfMonsterDefeated()
        }
    }

    // ATTACK TEXT
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
            getState().monster ?: return

        val player =
            getState().player

        if (player.currentHealth <= 0) {
            checkIfPlayerDefeated()
            return
        }

        // Player dodges
        val dodgeRoll =
            Random.nextInt(1, 101)

        if (dodgeRoll <= player.dodgeChance) {

            sounds.playDodge()

            updateState {
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

        // MONSTER DAMAGE
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

        updateState {

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
            getState().player.currentHealth <= 0
        ) {
            sounds.playDeathGameOverSound()
            onPlayerDefeated()
        }
    }

    // Function to check if the monster fought is dead
    private fun checkIfMonsterDefeated() {

        val monster =
            getState().monster ?: return

        if (monster.currentHealth > 0) return

        val player =
            getState().player

        // Reset temporary combat buffs
        player.resetRoarBuff()
        player.resetGuardBuff()

        val (expGained, goldGained, updatedPlayer) = playerGainsXPandGold(
            monster,
            player
        )

        checkIfPlayerLevelsUp(updatedPlayer)
        playerRegeneratesHealthBasedOnRegen(player, updatedPlayer)

        // UPDATE STATE
        updateState {
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

        onMonsterDefeated(monster) // GameViewModel handles generateItemFoundOnMonster + setBossDefeatedFlags
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

    // Function to check if player levels up
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

    private fun playerLifesteals(
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

    fun startEncounter(
        monsterPool: List<Monster>,
        onEncounterStarted: () -> Unit, // lets GameViewModel clear droppedItem
    ) {
        if (monsterPool.isEmpty()) return

        checkIfPlayerDefeated()

        onEncounterStarted()

        val encountered =
            monsterPool[
                Random.nextInt(monsterPool.size)
            ].cloneMonster()

        updateState {
            it.copy(
                monster = encountered,
                encounterLog = initialEncounterText(encountered),
                lootAvailable = false,
                monsterDefeated = false,
                playerDodgedFlag = false,
                goldPopupText = null,
                hpPopupText = null,
                monsterImageShake = 0,
            )
        }
    }

}