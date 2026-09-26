package com.example.horrorsawokenandroid.game.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf

data class Player(
    var name: String,
    var maxHealth: Int,
    var currentHealth: Int,
    var damage: Int,
    var strength: Int,
    var lifesteal: Int = 0,       // percent
    var armor: Int = 0,
    var dodgeChance: Int = 0,     // 0-100
    var goldInPocket: Int = 0,
    var experience: Int = 0,
    var level: Int = 1,
    var critChance: Int = 0,      // 0-100
    var regeneration: Int = 0,
    var critDamage: Int = 150,    // percent, e.g. 150 = 1.5x

    // Inventory
    val inventory: SnapshotStateList<Items.Item> = mutableStateListOf(),
    val equippedItems: SnapshotStateMap<Items.ItemType, Items.Item> = mutableStateMapOf(),
    var numberOfDragonEggsInInventory: Int = 0,

    var goldFind: Int = 1,
    var techniqueBloodLustIsLearned: Boolean = false,
    var TechniqueSwiftIsLearned: Boolean = false,
    var TechniqueRoarIsLearned: Boolean = false,
    var TechniqueDivineIsLearned: Boolean = false,
    var TechniqueGuardIsLearned: Boolean = false,
    var PriceToHeal: Int = 2,
    var PriceToLearnTechnique: Int = 10,

    // Roar buff
    var isRoarActive: Boolean = false,
    var roarBuffDodge: Int = 10,
    var roarBuffCrit: Int = 5,
    var roarBuffCountdown: Int = 0,

    // Guard buff
    var guardBuffIsActive: Boolean = false,
    var guardBuffArmor: Int = 0,
) {

    val playerIsOnLowHealth: Double get() = maxHealth * 0.35
    val xpNeededToLevelUp: Int get() = (10 * (level + level)) + (level * level) - 1

    companion object {
        fun newHero() = Player( // TODO important, set dmg to 1, level to 1, and str to 3, gold to 0, and maxhealth 40, currenthealth 40
            name = "Hero", maxHealth = 400, currentHealth = 400, damage = 1, strength = 10,
            lifesteal = 0, armor = 0, dodgeChance = 0, goldInPocket = 11000, experience = 0,
            level = 15, critChance = 0, regeneration = 0, critDamage = 150,
        )
    }

    fun calculateTotalDamage(): Int {
        return 1 + (damage / 2) + (strength / 6) * (level / 3)
    }

    fun turnOnRoarBuff() {
        isRoarActive = true
        dodgeChance += roarBuffDodge
        critChance += roarBuffCrit
    }

    fun turnOffRoarBuff() {
        isRoarActive = false
        roarBuffCountdown = 0
        dodgeChance -= roarBuffDodge
        critChance -= roarBuffCrit
    }

    fun resetRoarBuff() {
        if (isRoarActive) turnOffRoarBuff()
    }

    fun resetGuardBuff() {
        if (guardBuffIsActive) {
            armor -= guardBuffArmor
            guardBuffArmor = 0
            guardBuffIsActive = false
        }
    }

    fun levelUp(): Boolean {
        if (experience < xpNeededToLevelUp) {
            return false
        }
        level++
        experience = 0
        maxHealth += level + (level / 2)
        currentHealth = maxHealth // this heals the player to full hp on level-up

        return true
    }

    fun equipItem(item: Items.Item) {
        val oldItem = equippedItems[item.type]

        damage = damage - (oldItem?.damage ?: 0) + item.damage
        armor = armor - (oldItem?.armor ?: 0) + item.armor
        maxHealth = maxHealth - (oldItem?.health ?: 0) + item.health
        dodgeChance = dodgeChance - (oldItem?.dodgeChance ?: 0) + item.dodgeChance
        strength = strength - (oldItem?.strength ?: 0) + item.strength
        level = level - (oldItem?.skillLevel ?: 0) + item.skillLevel
        regeneration = regeneration - (oldItem?.regeneration ?: 0) + item.regeneration
        critChance = critChance - (oldItem?.critChance ?: 0) + item.critChance
        lifesteal = lifesteal - (oldItem?.lifesteal ?: 0) + item.lifesteal
        critDamage = critDamage - (oldItem?.critDamage ?: 0) + item.critDamage

        inventory.remove(item)

        if (oldItem != null) { inventory.add(oldItem) } // If there already was an equipped item of the same type, add the item to inventory

        equippedItems[item.type] = item
    }

    fun unEquipItem(item: Items.Item) {
        if (equippedItems[item.type] != item) return

        damage -= item.damage
        armor -= item.armor
        maxHealth -= item.health
        dodgeChance -= item.dodgeChance
        strength -= item.strength
        level -= item.skillLevel
        regeneration -= item.regeneration
        critChance -= item.critChance
        lifesteal -= item.lifesteal
        critDamage -= item.critDamage

        equippedItems.remove(item.type)
        inventory.add(item)
    }
}
