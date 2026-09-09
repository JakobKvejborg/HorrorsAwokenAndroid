package com.example.horrorsawokenandroid.game.model

import kotlin.random.Random

data class Item(
    var name: String = "",
    var type: ItemType = ItemType.Amulet,

    var health: Int = 0,
    var damage: Int = 0,
    var dodgeChance: Int = 0,
    var strength: Int = 0,
    var armor: Int = 0,
    var skillLevel: Int = 0,
    var lifesteal: Int = 0,
    var fireDamage: Int = 0,
    var poisonDamage: Int = 0,
    var regeneration: Int = 0,
    var strengthRequirement: Int = 0,
    var levelRequirement: Int = 0,
    var critChance: Int = 0,
    var critDamage: Int = 0,

    var isItemUpgraded: Boolean = false,
    var isItemReforged: Boolean = false
) {

    override fun toString(): String {
        val stats = linkedMapOf(
            "Damage" to damage,
            "Health" to health,
            "Lifesteal" to lifesteal,
            "Crit chance" to critChance,
            "Crit damage" to critDamage,
            "Armor" to armor,
            "Dodge" to dodgeChance,
            "Strength" to strength,
            "Regen" to regeneration,
            "Skilllevel" to skillLevel,
            "Str req" to strengthRequirement,
            "Level req" to levelRequirement
        )

        return stats
            .filter { it.value > 0 }
            .entries
            .joinToString("\n") { "${it.key}: ${it.value}" }
    }

    fun cloneItem(): Item {
        return copy()
    }

    fun upgradeItem() {
        if (isItemUpgraded || type == ItemType.Amulet) {
            return
        }

        name = "Upg. $name"
        costToUpgrade += 25
        levelRequirement += 1
        isItemUpgraded = true

        when (type) {

            ItemType.WeaponRightHand -> {
                when (Random.nextInt(1, 5)) {

                    1 -> {
                        damage += Random.nextInt(1, 3) +
                                smithUpgradeMultiplication - 1
                    }

                    2 -> {
                        strength += Random.nextInt(1, 4) *
                                smithUpgradeMultiplication
                    }

                    3 -> {
                        lifesteal += Random.nextInt(1, 10) *
                                smithUpgradeMultiplication
                    }

                    4 -> {
                        critDamage += Random.nextInt(3, 16) *
                                smithUpgradeMultiplication
                    }
                }
            }

            ItemType.Armor -> {
                when (Random.nextInt(1, 3)) {

                    1 -> {
                        health += Random.nextInt(5, 51) *
                                smithUpgradeMultiplication +
                                smithUpgradeMultiplication
                    }

                    2 -> {
                        armor += Random.nextInt(1, 3) *
                                smithUpgradeMultiplication
                    }
                }
            }

            ItemType.Boots -> {
                when (Random.nextInt(1, 3)) {

                    1 -> {
                        health += Random.nextInt(1, 25) *
                                smithUpgradeMultiplication +
                                smithUpgradeMultiplication
                    }

                    2 -> {
                        dodgeChance += Random.nextInt(1, 6) *
                                smithUpgradeMultiplication
                    }
                }
            }

            ItemType.Gloves -> {
                when (Random.nextInt(1, 5)) {

                    1 -> {
                        armor += Random.nextInt(1, 3) *
                                smithUpgradeMultiplication
                    }

                    2 -> {
                        strength += Random.nextInt(1, 3) *
                                smithUpgradeMultiplication
                    }

                    3 -> {
                        regeneration += Random.nextInt(1, 8) *
                                smithUpgradeMultiplication *
                                smithUpgradeMultiplication
                    }

                    4 -> {
                        critDamage += Random.nextInt(3, 20) +
                                smithUpgradeMultiplication +
                                smithUpgradeMultiplication +
                                smithUpgradeMultiplication
                    }
                }
            }

            ItemType.Leggings -> {
                when (Random.nextInt(1, 5)) {

                    1 -> {
                        dodgeChance += Random.nextInt(2, 7) *
                                smithUpgradeMultiplication
                    }

                    2 -> {
                        strength += Random.nextInt(1, 4) *
                                smithUpgradeMultiplication
                    }

                    3 -> {
                        critChance += Random.nextInt(1, 11) *
                                smithUpgradeMultiplication
                    }

                    4 -> {
                        health += Random.nextInt(1, 46) *
                                smithUpgradeMultiplication
                    }
                }
            }

            ItemType.Helmet -> {
                when (Random.nextInt(1, 5)) {

                    1 -> {
                        armor += Random.nextInt(1, 2) *
                                smithUpgradeMultiplication
                    }

                    2 -> {
                        strength += Random.nextInt(1, 3) *
                                smithUpgradeMultiplication
                    }

                    3 -> {
                        health += Random.nextInt(1, 35) *
                                smithUpgradeMultiplication
                    }

                    4 -> {
                        regeneration += Random.nextInt(1, 15) *
                                smithUpgradeMultiplication *
                                smithUpgradeMultiplication
                    }
                }
            }

            ItemType.Belt -> {
                when (Random.nextInt(1, 4)) {

                    1 -> {
                        armor += Random.nextInt(1, 3) *
                                smithUpgradeMultiplication
                    }

                    2 -> {
                        health += Random.nextInt(1, 23) *
                                smithUpgradeMultiplication +
                                smithUpgradeMultiplication
                    }

                    3 -> {
                        critDamage += Random.nextInt(3, 15) +
                                smithUpgradeMultiplication +
                                smithUpgradeMultiplication +
                                smithUpgradeMultiplication
                    }
                }
            }

            else -> {
                lifesteal += Random.nextInt(1, 10) +
                        smithUpgradeMultiplication +
                        smithUpgradeMultiplication
            }
        }
    }

    companion object {
        var costToUpgrade: Int = 40
        var smithUpgradeMultiplication: Int = 1
    }
}

enum class ItemType {
    WeaponLeftHand,
    WeaponRightHand,
    Armor,
    Boots,
    Helmet,
    Amulet,
    Leggings,
    Shoulders,
    Gloves,
    Belt
}