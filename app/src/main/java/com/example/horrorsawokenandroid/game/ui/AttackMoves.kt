package com.example.horrorsawokenandroid.game.ui

import com.example.horrorsawokenandroid.game.model.NoOpSoundPlayer
import com.example.horrorsawokenandroid.game.model.SoundPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.random.Random

class AttackMoves(
    private val getState: () -> GameUiState,
    private val updateState: ((GameUiState) -> GameUiState) -> Unit,
    private val attackResolver: suspend (
        rawDamage: Int,
        noLifeSteal: Boolean,
        noCrit: Boolean
    ) -> Unit,
    private val sounds: SoundPlayer = NoOpSoundPlayer,
    private val scope: CoroutineScope
) {

    fun normalAttack() = scope.launch {
        val state = getState()
        val monster = state.monster ?: return@launch

        if (monster.currentHealth <= 0) return@launch

        sounds.playNormalAttack()

        val damage = state.player.calculateTotalDamage()

        attackResolver(
            damage,
            false,
            false
        )
    }

    fun bloodLustAttack() = scope.launch {
        val state = getState()
        val monster = state.monster ?: return@launch

        if (monster.currentHealth <= 0) return@launch

        sounds.playBloodLustAttack()

        val damage = (state.player.calculateTotalDamage() * 1.6).toInt()
        val healthCost = (state.player.maxHealth * 0.08).toInt()

        updateState {
            val player = it.player.copy(
                currentHealth = (
                        it.player.currentHealth - healthCost
                        ).coerceAtLeast(0)
            )

            it.copy(player = player)
        }

        attackResolver(
            damage,
            false,
            false
        )
    }

    fun swiftAttack() = scope.launch {
        val state = getState()
        val monster = state.monster ?: return@launch

        if (monster.currentHealth <= 0) return@launch

        sounds.playSwiftAttack()

        val baseDamage = state.player.calculateTotalDamage()

        val damage = if (!state.playerDodgedFlag) {
            (baseDamage * 0.4).toInt()
        } else {
            (baseDamage * 1.4).toInt()
        }

        updateState {
            it.copy(playerDodgedFlag = false)
        }

        attackResolver(
            damage,
            false,
            false
        )
    }

    fun roarAttack() = scope.launch {
        updateState {
            it.copy(
                player = it.player.copy(
                    roarBuffCountdown = Random.nextInt(4, 8)
                )
            )
        }

        val state = getState()
        val monster = state.monster ?: return@launch

        if (monster.currentHealth <= 0) return@launch

        sounds.playRoarAttack()

        val damage = (state.player.calculateTotalDamage() * 0.1).toInt()

        if (!state.player.isRoarActive) {
            updateState {
                val player = it.player.copy()
                player.turnOnRoarBuff()

                it.copy(player = player)
            }
        }

        attackResolver(
            damage,
            true,
            true
        )
    }

    fun divineAttack() = scope.launch {
        val state = getState()
        val monster = state.monster ?: return@launch

        if (monster.currentHealth <= 0) return@launch

        sounds.playDivineAttack()

        val player = state.player

        val missingHpPct =
            (player.maxHealth - player.currentHealth).toDouble() /
                    player.maxHealth

        val damageAtFullHp = 0.2
        val damageAtLowHp = 3.1

        val multiplier =
            damageAtFullHp +
                    (damageAtLowHp - damageAtFullHp) *
                    missingHpPct.pow(1.5)

        val damage =
            (player.calculateTotalDamage() * multiplier).toInt()

        attackResolver(
            damage,
            true,
            true
        )
    }

    fun guardAttack() {
        val state = getState()
        val monster = state.monster ?: return

        if (monster.currentHealth <= 0) return

        val player = state.player

        if (
            player.guardBuffIsActive ||
            player.currentHealth > player.playerIsOnLowHealth
        ) {
            return
        }

        val healAmount = player.maxHealth / 15
        val buffArmor = (player.armor * 0.15).toInt()

        sounds.playGuard()

        updateState {
            val updatedPlayer = player.copy(
                currentHealth = (
                        player.currentHealth + healAmount
                        ).coerceAtMost(player.maxHealth),

                armor = player.armor + buffArmor,

                guardBuffArmor = buffArmor,

                guardBuffIsActive = true
            )

            it.copy(
                player = updatedPlayer,
                encounterLog =
                    it.encounterLog +
                            "\nYou stand your ground, boosting your defense!"
            )
        }
    }
}