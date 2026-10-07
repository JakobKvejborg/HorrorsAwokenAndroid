package com.example.horrorsawokenandroid.game.model

import com.example.horrorsawokenandroid.game.ui.GameUiState
import kotlin.random.Random

class ItemUpgrader(
    private val getState: () -> GameUiState,
    private val updateState: ((GameUiState) -> GameUiState) -> Unit,
    private val sounds: SoundPlayer,
    ) {

    fun upgradeItem(
        itemToBeUpgraded: Items.Item?,
        onEquipItem: (Items.Item) -> Unit,
        onUpgradeApplied: () -> Unit,
    ) {
        val currentState = getState()
        val item = itemToBeUpgraded ?: return
        val player = getState().player

        if (player.goldInPocket < currentState.costToUpgradeItem || item.name.contains("Upg.") || item.type == Items.ItemType.Amulet ) {
            sounds.playAct2SmithNo()
            return
        }

        sounds.playSmithingSound()

        val upgradedItem = applySmithUpgrade(item).copy(name = "Upg. " + item.name,)

        updateState {
            it.copy(
                player = it.player.copy(
                    goldInPocket = it.player.goldInPocket - currentState.costToUpgradeItem
                ),
                costToUpgradeItem = it.costToUpgradeItem + 25
            )
        }

        onUpgradeApplied()
        onEquipItem(upgradedItem)
    }

    private fun applySmithUpgrade(item: Items.Item): Items.Item {
        val m = getState().smithUpgradeMultiplication

        return when (item.type) {
            Items.ItemType.Weapon -> when (Random.nextInt(1, 5)) {
                1 -> item.copy(damage = item.damage + Random.nextInt(1, 3) + m - 1, upgradedStatName = "damage")
                2 -> item.copy(strength = item.strength + Random.nextInt(1, 4) * m, upgradedStatName = "strength")
                3 -> item.copy(lifesteal = item.lifesteal + Random.nextInt(1, 10) * m, upgradedStatName = "lifesteal")
                else -> item.copy(critDamage = item.critDamage + Random.nextInt(3, 16) * m, upgradedStatName = "critDamage")
            }

            Items.ItemType.Armor -> when (Random.nextInt(1, 3)) {
                1 -> item.copy(health = item.health + Random.nextInt(5, 51) * m + m, upgradedStatName = "health")
                else -> item.copy(armor = item.armor + Random.nextInt(1, 3) * m, upgradedStatName = "armor")
            }

            Items.ItemType.Boots -> when (Random.nextInt(1, 3)) {
                1 -> item.copy(health = item.health + Random.nextInt(1, 25) * m + m, upgradedStatName = "health")
                else -> item.copy(dodgeChance = item.dodgeChance + Random.nextInt(1, 6) * m, upgradedStatName = "dodgeChance")
            }

            Items.ItemType.Gloves -> when (Random.nextInt(1, 5)) {
                1 -> item.copy(armor = item.armor + Random.nextInt(1, 3) * m, upgradedStatName = "armor")
                2 -> item.copy(strength = item.strength + Random.nextInt(1, 3) * m, upgradedStatName = "strength")
                3 -> item.copy(regeneration = item.regeneration + Random.nextInt(1, 8) * m * m, upgradedStatName = "regeneration")
                else -> item.copy(critDamage = item.critDamage + Random.nextInt(3, 20) + m + m + m, upgradedStatName = "critDamage")
            }

            Items.ItemType.Leggings -> when (Random.nextInt(1, 5)) {
                1 -> item.copy(dodgeChance = item.dodgeChance + Random.nextInt(2, 7) * m, upgradedStatName = "dodgeChance")
                2 -> item.copy(strength = item.strength + Random.nextInt(1, 4) * m, upgradedStatName = "strength")
                3 -> item.copy(critChance = item.critChance + Random.nextInt(1, 11) * m, upgradedStatName = "critChance")
                else -> item.copy(health = item.health + Random.nextInt(1, 46) * m, upgradedStatName = "health")
            }

            Items.ItemType.Helmet -> when (Random.nextInt(1, 5)) {
                1 -> item.copy(armor = item.armor + Random.nextInt(1, 2) * m, upgradedStatName = "armor")
                2 -> item.copy(strength = item.strength + Random.nextInt(1, 3) * m, upgradedStatName = "strength")
                3 -> item.copy(health = item.health + Random.nextInt(1, 35) * m, upgradedStatName = "health")
                else -> item.copy(regeneration = item.regeneration + Random.nextInt(1, 15) * m * m, upgradedStatName = "regeneration")
            }

            Items.ItemType.Belt -> when (Random.nextInt(1, 4)) {
                1 -> item.copy(armor = item.armor + Random.nextInt(1, 3) * m, upgradedStatName = "armor")
                2 -> item.copy(health = item.health + Random.nextInt(1, 23) * m + m, upgradedStatName = "health")
                else -> item.copy(critDamage = item.critDamage + Random.nextInt(3, 15) + m + m + m, upgradedStatName = "critDamage")
            }

            else -> item.copy(lifesteal = item.lifesteal + Random.nextInt(1, 10) + m + m, upgradedStatName = "lifesteal")
        }
    }

}