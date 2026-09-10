package com.example.horrorsawokenandroid.game.model

/**
 * Ported from Player.cs. Constructor order per your original:
 * Player(name, maxHealth, currentHealth, damage, strength, lifesteal, armor, dodge,
 *        gold, experience, level, crit, regen, critDamage)
 * e.g. Player("Hero", 40, 40, 1, 3, 0, 0, 0, 0, 0, 1, 0, 0, 150)
 */
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

    var goldFind: Int = 1,
    var playerIsOnLowHealth: Int = 20, // hp threshold for glow/guard
    var techniqueBloodLustIsLearned: Boolean = false,
    var numberOfDragonEggsInInventory: Int = 0,

    // Roar buff
    var isRoarActive: Boolean = false,
    var roarBuffCountdown: Int = 0,

    // Guard buff
    var guardBuffIsActive: Boolean = false,
    var guardBuffArmor: Int = 0,
) {

    val xpNeededToLevelUp: Int get() = (10 * (level + level)) + (level * level) - 1

    companion object {
        fun newHero() = Player(
            name = "Hero", maxHealth = 40, currentHealth = 40, damage = 1, strength = 3,
            lifesteal = 0, armor = 0, dodgeChance = 0, goldInPocket = 0, experience = 0,
            level = 1, critChance = 0, regeneration = 0, critDamage = 150,
        )
    }

    fun calculateTotalDamage(): Int {
        return 1 + (damage / 2) + (strength / 6) * (level / 3)
    }

    fun turnOnRoarBuff() {
        isRoarActive = true
        // TODO: apply roar's stat bonus (original likely buffed damage or crit here)
    }

    fun turnOffRoarBuff() {
        isRoarActive = false
        roarBuffCountdown = 0
        // TODO: remove roar's stat bonus
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

    fun levelUp() {
        while (experience >= xpNeededToLevelUp) {
            level++
            experience = 0
            maxHealth += level + (level / 2)
            currentHealth = maxHealth // this heals the player to full hp on level-up
        }
    }
}