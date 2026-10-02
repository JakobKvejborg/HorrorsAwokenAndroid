package com.example.horrorsawokenandroid.game.model

import com.example.horrorsawokenandroid.game.ui.GameUiState
import kotlin.random.Random

class ReforgeFrogStat(
    private val getState: () -> GameUiState,
    private val updateState: ((GameUiState) -> GameUiState) -> Unit,
    private val sounds: SoundPlayer,
) {
    var priceToReforgeFrog: Int = 100
    var reforgeModifier: Double = 0.0
    var lilyReforgeModifier: Double = 0.0

    enum class ReforgeableStat {
        Health, Damage, DodgeChance, Strength, Armor,
        Lifesteal, Regeneration, CritChance, CritDamage
    }

    private fun rollMultiplier(): Double {
        // Random value between 0.90 and 1.50, plus modifiers
        return Random.nextDouble() * (1.50 - 0.90) + 0.90 + reforgeModifier + lilyReforgeModifier
    }

    private fun reforgeStat(statValue: Int): Int {
        return (statValue * rollMultiplier()).toInt()
    }

    private fun applyReforge(item: Items.Item, stat: ReforgeableStat): Items.Item {
        val multiplier = rollMultiplier()
        return when (stat) {
            ReforgeableStat.Health -> item.copy(health = (item.health * multiplier).toInt())
            ReforgeableStat.Damage -> item.copy(damage = (item.damage * multiplier).toInt())
            ReforgeableStat.DodgeChance -> item.copy(dodgeChance = (item.dodgeChance * multiplier).toInt())
            ReforgeableStat.Strength -> item.copy(strength = (item.strength * multiplier).toInt())
            ReforgeableStat.Armor -> item.copy(armor = (item.armor * multiplier).toInt())
            ReforgeableStat.Lifesteal -> item.copy(lifesteal = (item.lifesteal * multiplier).toInt())
            ReforgeableStat.Regeneration -> item.copy(regeneration = (item.regeneration * multiplier).toInt())
            ReforgeableStat.CritChance -> item.copy(critChance = (item.critChance * multiplier).toInt())
            ReforgeableStat.CritDamage -> item.copy(critDamage = (item.critDamage * multiplier).toInt())
        }.copy(isItemReforged = true) // Each item can only be reforged once
    }

    /** Reforges one stat on the given item. Returns the reforged item, or null if the player can't afford it / item already reforged. */
    fun reforgeItem(
        item: Items.Item,
        stat: ReforgeableStat,
    ): Items.Item? {

        val player = getState().player

        if (player.hasFrozenLily) {
            updateState {
                it.copy(
                    player = it.player.copy(hasFrozenLily = false)
                )
            }
            sounds.playAct3FrozenLilyFrogSound()
            lilyReforgeModifier += 0.15
            return null
        }

        if (
            player.goldInPocket < priceToReforgeFrog || item.isItemReforged
        ) {
            sounds.playAct3ReforgeFroggy()
            return null
        }

        val reforged = applyReforge(item, stat)

        updateState {
            val player = it.player

            // Remove old item's stats
            player.unEquipItem(item)

            // unEquipItem() puts the old item into inventory.
            // Remove it again because we are replacing it with the reforged item.
            player.inventory.remove(item)

            // Apply reforged item's stats and equip it
            player.equipItem(reforged)

            it.copy(
                player = player.copy(
                    goldInPocket =
                        player.goldInPocket - priceToReforgeFrog
                )
            )
        }

        sounds.playCoin()
        sounds.playAct3ReforgeFroggy()

        return reforged
    }

}