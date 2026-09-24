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

    var goldFind: Int = 1,
    var playerIsOnLowHealth: Int = 20, // hp threshold for glow/guard
    var techniqueBloodLustIsLearned: Boolean = false,
    var TechniqueSwiftIsLearned: Boolean = false,
    var TechniqueRoarIsLearned: Boolean = false,
    var TechniqueDivineIsLearned: Boolean = false,
    var TechniqueGuardIsLearned: Boolean = false,
    var PriceToHeal: Int = 2,
    var PriceToLearnTechnique: Int = 10,
    var numberOfDragonEggsInInventory: Int = 0,

    // Roar buff
    var isRoarActive: Boolean = false,
    var roarBuffDodge: Int = 10,
    var roarBuffCrit: Int = 5,
    var roarBuffCountdown: Int = 0,

    // Guard buff
    var guardBuffIsActive: Boolean = false,
    var guardBuffArmor: Int = 0,
) {

    val xpNeededToLevelUp: Int get() = (10 * (level + level)) + (level * level) - 1

    companion object {
        fun newHero() = Player(
            name = "Hero", maxHealth = 40, currentHealth = 40, damage = 1000, strength = 3,
            lifesteal = 0, armor = 0, dodgeChance = 0, goldInPocket = 0, experience = 0,
            level = 1, critChance = 0, regeneration = 0, critDamage = 150,
        )
    }

    fun calculateTotalDamage(): Int {
        return 1 + (damage / 2) + (strength / 6) * (level / 3)
    }

    fun equipItem(item: Items.Item) {
        // Remove old item of the same type and subtract its bonuses
        val oldItem = equippedItems[item.type]
        val wasAtOneHealth = currentHealth < 2  // If the player has 1 HP, keep them at 1 HP after equipment changes

        if (oldItem != null) {
            damage -= oldItem.damage
            armor -= oldItem.armor
            maxHealth -= oldItem.health
            currentHealth -= oldItem.health
            dodgeChance -= oldItem.dodgeChance
            strength -= oldItem.strength
            level -= oldItem.skillLevel
            regeneration -= oldItem.regeneration
            critChance -= oldItem.critChance
            lifesteal -= oldItem.lifesteal
            critDamage -= oldItem.critDamage
        }

        // Apply new item bonuses
        damage += item.damage
        armor += item.armor
        maxHealth += item.health
        currentHealth += item.health
        dodgeChance += item.dodgeChance
        strength += item.strength
        level += item.skillLevel
        regeneration += item.regeneration
        critChance += item.critChance
        lifesteal += item.lifesteal
        critDamage += item.critDamage

        if (wasAtOneHealth) { currentHealth = 1 } // If the player was at 1 HP, they remain at exactly 1 HP
    }

    fun unequipItem(item: Items.Item) {
        if (equippedItems[item.type] != item) return

        val wasAtOneHealth = currentHealth < 2

        damage -= item.damage
        armor -= item.armor
        maxHealth -= item.health
        currentHealth -= item.health
        dodgeChance -= item.dodgeChance
        strength -= item.strength
        level -= item.skillLevel
        regeneration -= item.regeneration
        critChance -= item.critChance
        lifesteal -= item.lifesteal
        critDamage -= item.critDamage

        equippedItems.remove(item.type)

        if (currentHealth < 1) { currentHealth = 1 }
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
}
