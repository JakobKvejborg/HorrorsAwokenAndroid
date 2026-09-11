
package com.example.horrorsawokenandroid.game.model

import kotlin.math.roundToInt
import kotlin.random.Random

class Items {

    private val random = Random.Default

    // ============================================================
    // ITEM
    // ============================================================

    data class Item(
        var name: String,
        var type: ItemType,

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

        fun cloneItem(): Item {
            return copy()
        }

        fun statText(): String {

            val stats = mutableListOf<String>()

            if (damage > 0) stats += "Damage: $damage"
            if (health > 0) stats += "Health: $health"
            if (lifesteal > 0) stats += "Lifesteal: $lifesteal"
            if (critChance > 0) stats += "Crit chance: $critChance"
            if (critDamage > 0) stats += "Crit damage: $critDamage"
            if (armor > 0) stats += "Armor: $armor"
            if (dodgeChance > 0) stats += "Dodge: $dodgeChance"
            if (strength > 0) stats += "Strength: $strength"
            if (regeneration > 0) stats += "Regen: $regeneration"
            if (skillLevel > 0) stats += "Skilllevel: $skillLevel"
            if (strengthRequirement > 0) stats += "Str req: $strengthRequirement"
            if (levelRequirement > 0) stats += "Level req: $levelRequirement"

            return stats.joinToString("\n")
        }

        override fun toString(): String {
            return statText()
        }
    }

    // ============================================================
    // ITEM TYPE
    // ============================================================

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

    // ============================================================
    // QUALITY
    // ============================================================

    private enum class Quality(
        val multiplier: Double
    ) {
        Damaged(0.5),
        Normal(1.0),
        Magic(1.8),
        Strong(2.0),
        Rare(2.6),
        Unique(3.0),
        Epic(4.5),
        Legendary(5.0),
        Godly(6.0)
    }

    // ============================================================
    // PUBLIC LOOT GENERATION
    // ============================================================

    /**
     * Generates one random item based on the current act.
     * Also rolls a % chance for the loot to even drop
     * Act 1 = early game
     * Act 2 = early/mid game
     * Act 3 = mid game
     * Act 4 = late game
     * Act 5 = end game
     */
    fun generateLoot(act: Int): Item? {

        val lootRoll = Random.nextInt(100)

        if (lootRoll >= 32) {
            return null
        }

        val safeAct = act.coerceIn(1, 5)

        val type = generateItemType(safeAct)
        val quality = generateQuality()

        return generateItem(
            act = safeAct,
            type = type,
            quality = quality
        )
    }

    /**
     * Useful when a specific equipment slot is needed.
     */
    fun generateLoot(
        act: Int,
        type: ItemType
    ): Item {

        val safeAct = act.coerceIn(1, 5)
        val quality = generateQuality()

        return generateItem(
            act = safeAct,
            type = type,
            quality = quality
        )
    }

    // ============================================================
    // QUALITY
    // ============================================================

    /**
     * Exact same rarity chances as the old ImprovedRandomItem.
     */
    private fun generateQuality(): Quality {

        val roll = random.nextDouble(0.0, 100.0)

        var total = 0.0

        val chances = listOf(
            Quality.Damaged to 24.0,
            Quality.Normal to 51.0,
            Quality.Magic to 8.0,
            Quality.Strong to 6.0,
            Quality.Rare to 5.0,
            Quality.Unique to 4.0,
            Quality.Epic to 1.4,
            Quality.Legendary to 0.5,
            Quality.Godly to 0.1
        )

        for ((quality, chance) in chances) {

            total += chance

            if (roll < total) {
                return quality
            }
        }

        return Quality.Normal
    }

    // ============================================================
    // ITEM TYPE DISTRIBUTION
    // ============================================================

    private fun generateItemType(act: Int): ItemType {

        /*
         * The weapon pool is intentionally not dominant.
         * Armor pieces make up a large part of the loot pool,
         * just like the old game.
         */

        val pool = when (act) {

            1 -> listOf(
                ItemType.WeaponRightHand,
                ItemType.WeaponRightHand,
                ItemType.Armor,
                ItemType.Boots,
                ItemType.Gloves,
                ItemType.Leggings,
                ItemType.Belt,
                ItemType.Helmet,
                ItemType.Shoulders,
                ItemType.WeaponLeftHand
            )

            2 -> listOf(
                ItemType.WeaponRightHand,
                ItemType.WeaponRightHand,
                ItemType.WeaponLeftHand,
                ItemType.Armor,
                ItemType.Boots,
                ItemType.Gloves,
                ItemType.Leggings,
                ItemType.Belt,
                ItemType.Helmet,
                ItemType.Shoulders
            )

            3 -> listOf(
                ItemType.WeaponRightHand,
                ItemType.WeaponRightHand,
                ItemType.WeaponLeftHand,
                ItemType.Armor,
                ItemType.Boots,
                ItemType.Gloves,
                ItemType.Leggings,
                ItemType.Belt,
                ItemType.Helmet,
                ItemType.Shoulders,
                ItemType.Amulet
            )

            4 -> listOf(
                ItemType.WeaponRightHand,
                ItemType.WeaponRightHand,
                ItemType.WeaponLeftHand,
                ItemType.Armor,
                ItemType.Boots,
                ItemType.Gloves,
                ItemType.Leggings,
                ItemType.Belt,
                ItemType.Helmet,
                ItemType.Shoulders,
                ItemType.Amulet
            )

            else -> listOf(
                ItemType.WeaponRightHand,
                ItemType.WeaponRightHand,
                ItemType.WeaponLeftHand,
                ItemType.Armor,
                ItemType.Boots,
                ItemType.Gloves,
                ItemType.Leggings,
                ItemType.Belt,
                ItemType.Helmet,
                ItemType.Shoulders,
                ItemType.Amulet
            )
        }

        return pool.random(random)
    }

    // ============================================================
    // ACT SCALING
    // ============================================================

    /*
     * Deliberately conservative.
     *
     * The old item system already had strong quality multipliers.
     * Act scaling therefore only provides a modest progression.
     *
     * This prevents:
     *
     * Act 1 Legendary
     *       <
     * Act 5 Normal
     *
     * from becoming ridiculously exaggerated.
     */

    private fun actMultiplier(act: Int): Double {
        return when (act) {
            1 -> 1.00
            2 -> 1.15
            3 -> 1.30
            4 -> 1.45
            5 -> 1.60
            else -> 1.00
        }
    }

    private fun requirementBonus(act: Int): Int {
        return when (act) {
            1 -> 0
            2 -> 2
            3 -> 4
            4 -> 6
            5 -> 8
            else -> 0
        }
    }

    // ============================================================
    // GENERATE ITEM
    // ============================================================

    private fun generateItem(
        act: Int,
        type: ItemType,
        quality: Quality
    ): Item {

        val item = Item(
            name = "",
            type = type
        )

        val actMultiplier = actMultiplier(act)

        when (type) {

            ItemType.WeaponRightHand ->
                generateWeapon(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.WeaponLeftHand ->
                generateLeftHandWeapon(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Armor ->
                generateArmor(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Boots ->
                generateBoots(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Gloves ->
                generateGloves(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Leggings ->
                generateLeggings(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Helmet ->
                generateHelmet(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Belt ->
                generateBelt(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Shoulders ->
                generateShoulders(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )

            ItemType.Amulet ->
                generateAmulet(
                    item,
                    quality.multiplier,
                    actMultiplier,
                    act
                )
        }

        item.name = generateName(
            type = type,
            quality = quality,
            act = act
        )

        return item
    }

    // ============================================================
    // WEAPON
    // ============================================================

    private fun generateWeapon(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        /*
         * Based directly on the old weapon formula:
         *
         * Damage:
         * (quality + 0.25) * 2.5
         * to
         * (quality + 0.25) * 4
         */

        val minDamage = (
                (quality + 0.25) *
                        2.5 *
                        act
                ).roundToInt()
            .coerceAtLeast(1)

        val maxDamage = (
                (quality + 0.25) *
                        4.0 *
                        act
                ).roundToInt()
            .coerceAtLeast(minDamage + 1)

        item.damage = random.nextInt(
            minDamage,
            maxDamage + 1
        )

        item.critChance = (
                random.nextInt(0, 5) *
                        quality *
                        act
                ).roundToInt()

        item.strength = (
                random.nextInt(1, 3) *
                        quality *
                        act
                ).roundToInt()

        /*
         * Lifesteal and crit damage are intentionally uncommon.
         * This keeps them valuable.
         */
        if (random.nextInt(100) < 25) {
            item.lifesteal = (
                    random.nextInt(1, 4) *
                            quality *
                            act
                    ).roundToInt()
        }

        if (random.nextInt(100) < 20) {
            item.critDamage = (
                    random.nextInt(2, 8) *
                            quality *
                            act
                    ).roundToInt()
        }

        item.strengthRequirement = (
                random.nextInt(1, 6) *
                        quality +
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(2, 7) +
                        quality * 4 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // LEFT HAND / HOOK
    // ============================================================

    private fun generateLeftHandWeapon(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        item.damage = (
                random.nextInt(0, 5) *
                        act
                ).roundToInt()

        item.strength = (
                random.nextInt(0, 2) *
                        quality *
                        act
                ).roundToInt()

        item.regeneration = (
                random.nextInt(0, 4) *
                        quality *
                        quality *
                        act
                ).roundToInt()

        item.lifesteal = (
                random.nextInt(0, 5) *
                        quality *
                        act
                ).roundToInt()

        item.critDamage = (
                random.nextInt(0, 9) +
                        (quality - 1) * quality -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.strengthRequirement = (
                random.nextInt(1, 4) *
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(4, 14) +
                        quality * 2 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // ARMOR
    // ============================================================

    private fun generateArmor(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        item.strength = (
                random.nextInt(0, 3) +
                        quality -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.health = (
                random.nextInt(2, 32) *
                        quality *
                        2.0 *
                        act
                ).roundToInt()

        item.armor = (
                quality *
                        act
                ).roundToInt()
            .coerceAtLeast(1)

        item.strengthRequirement = (
                random.nextInt(0, 6) *
                        quality +
                        quality -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.levelRequirement = (
                random.nextInt(1, 4) +
                        quality * 4 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // BOOTS
    // ============================================================

    private fun generateBoots(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        item.dodgeChance = (
                random.nextInt(0, 7) +
                        quality * act
                ).roundToInt()

        item.armor = (
                random.nextInt(0, 3) +
                        quality * act -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.health = (
                random.nextInt(1, 21) *
                        quality *
                        act
                ).roundToInt()

        item.strengthRequirement = (
                random.nextInt(1, 6) *
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(1, 5) +
                        quality * 3 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // GLOVES
    // ============================================================

    private fun generateGloves(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        val critDamageValues =
            intArrayOf(0, 3, 8)

        item.critDamage = (
                critDamageValues[
                    random.nextInt(
                        critDamageValues.size
                    )
                ] *
                        quality *
                        act
                ).roundToInt()

        item.strength = (
                random.nextInt(0, 3) +
                        quality * act
                ).roundToInt()

        item.critChance = (
                random.nextInt(1, 9) +
                        quality *
                        act
                ).roundToInt()

        item.armor = (
                random.nextInt(0, 4) +
                        quality * act -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.strengthRequirement = (
                random.nextInt(0, 6) *
                        quality +
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(1, 5) +
                        quality * 3 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // LEGGINGS
    // ============================================================

    private fun generateLeggings(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        item.armor = (
                random.nextInt(0, 3) +
                        quality * act -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.strength = (
                random.nextInt(0, 3) *
                        quality *
                        act
                ).roundToInt()

        item.regeneration = (
                random.nextInt(0, 3) *
                        quality *
                        act
                ).roundToInt()

        item.health = (
                random.nextInt(0, 49) *
                        quality *
                        act
                ).roundToInt()

        /*
         * Small chance of additional defensive utility.
         */
        if (random.nextInt(100) < 35) {
            item.dodgeChance = (
                    random.nextInt(1, 5) *
                            quality *
                            act
                    ).roundToInt()
        }

        if (random.nextInt(100) < 30) {
            item.critChance = (
                    random.nextInt(1, 4) *
                            quality *
                            act
                    ).roundToInt()
        }

        item.strengthRequirement = (
                random.nextInt(1, 6) *
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(1, 6) +
                        quality * 2 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // HELMET
    // ============================================================

    private fun generateHelmet(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        val healthValues =
            intArrayOf(
                0,
                0,
                20,
                70
            )

        item.health = (
                healthValues[
                    random.nextInt(
                        healthValues.size
                    )
                ] *
                        quality *
                        act
                ).roundToInt()

        item.armor = (
                random.nextInt(0, 4) +
                        quality * act -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.strength = (
                random.nextInt(0, 3) +
                        quality * act
                ).roundToInt()

        item.regeneration = (
                random.nextInt(0, 4) *
                        quality *
                        act
                ).roundToInt()

        item.critDamage = (
                random.nextInt(0, 10) +
                        (quality - 1) *
                        quality
                ).roundToInt()

        item.strengthRequirement = (
                random.nextInt(1, 6) *
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(3, 10) +
                        quality * 2 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // BELT
    // ============================================================

    private fun generateBelt(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        item.armor =
            random.nextInt(0, 3)

        item.strength = (
                random.nextInt(0, 2) *
                        quality *
                        act
                ).roundToInt()

        item.regeneration = (
                random.nextInt(1, 4) *
                        quality *
                        act
                ).roundToInt()

        item.lifesteal = (
                random.nextInt(0, 9) *
                        quality *
                        act
                ).roundToInt()

        item.strengthRequirement = (
                random.nextInt(0, 6) *
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(1, 6) +
                        quality * 2 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // SHOULDERS
    // ============================================================

    private fun generateShoulders(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        item.regeneration = (
                random.nextInt(0, 2) *
                        quality *
                        quality +
                        quality / 3
                ).roundToInt()

        item.health = (
                random.nextInt(0, 46) *
                        quality *
                        act
                ).roundToInt()

        item.armor = (
                random.nextInt(0, 3) +
                        quality * act -
                        1
                ).roundToInt()
            .coerceAtLeast(0)

        item.strengthRequirement = (
                random.nextInt(0, 4) +
                        quality * 2
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(2, 8) +
                        quality * 3 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // AMULET
    // ============================================================

    private fun generateAmulet(
        item: Item,
        quality: Double,
        act: Double,
        actNumber: Int
    ) {

        /*
         * Amulets are intentionally utility-heavy rather than
         * becoming huge stat sticks.
         */

        item.regeneration = (
                random.nextInt(0, 6) *
                        quality *
                        quality *
                        act
                ).roundToInt()

        item.health = (
                random.nextInt(0, 16) *
                        quality *
                        2.0 *
                        act
                ).roundToInt()

        item.critChance = (
                random.nextInt(0, 3) *
                        quality *
                        act
                ).roundToInt()

        item.dodgeChance = (
                random.nextInt(1, 6) +
                        quality
                ).roundToInt()

        item.levelRequirement = (
                random.nextInt(10, 16) +
                        quality * 2 +
                        requirementBonus(actNumber)
                ).roundToInt()
    }

    // ============================================================
    // NAME GENERATION
    // ============================================================

    private fun generateName(
        type: ItemType,
        quality: Quality,
        act: Int
    ): String {

        val baseName =
            getBaseName(type, act)

        val prefix =
            when (quality) {

                Quality.Damaged ->
                    "Damaged"

                Quality.Unique ->
                    uniquePrefixes.random(random)

                Quality.Epic ->
                    epicPrefixes.random(random)

                Quality.Legendary ->
                    legendaryPrefixes.random(random)

                Quality.Godly ->
                    godlyPrefixes.random(random)

                else ->
                    ""
            }

        val suffix =
            when (quality) {

                Quality.Damaged ->
                    ""

                Quality.Normal ->
                    normalSuffixes.random(random)

                Quality.Magic ->
                    magicSuffixes.random(random)

                Quality.Strong ->
                    strongSuffixes.random(random)

                Quality.Rare ->
                    rareSuffixes.random(random)

                Quality.Unique ->
                    uniqueSuffixes.random(random)

                Quality.Epic ->
                    epicSuffixes.random(random)

                Quality.Legendary ->
                    legendarySuffixes.random(random)

                Quality.Godly ->
                    godlySuffixes.random(random)
            }

        return listOf(
            prefix,
            baseName,
            suffix
        )
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    // ============================================================
    // BASE ITEM NAMES
    // ============================================================

    private fun getBaseName(
        type: ItemType,
        act: Int
    ): String {

        return when (type) {

            ItemType.WeaponRightHand -> {

                when (act) {

                    1 -> listOf(
                        "Axe",
                        "Sword",
                        "Knife",
                        "Small Sword",
                        "Small Hammer",
                        "Dagger",
                        "Small Dagger",
                        "Spear",
                        "Pike",
                        "Whip",
                        "Rusty Dagger"
                    ).random(random)

                    2 -> listOf(
                        "Scythe",
                        "Hammer",
                        "Blood Knife",
                        "Blood Sword",
                        "Vampire Slayer",
                        "Battle Axe",
                        "Shiny Axe",
                        "Magic Sword",
                        "Magic Hammer",
                        "Magic Halberd"
                    ).random(random)

                    3 -> listOf(
                        "Blood Hammer",
                        "Rusty Greataxe",
                        "Shiny Sword",
                        "Night's Edge",
                        "Rapier",
                        "Great Sword",
                        "Great Halberd",
                        "Greataxe"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Axe",
                        "Dragon Sword",
                        "Dread Hammer",
                        "Blood Halberd",
                        "Demon Blade",
                        "War Scythe",
                        "Blacksteel Sword"
                    ).random(random)

                    else -> listOf(
                        "Hellforged Axe",
                        "Soul Reaper",
                        "Doomblade",
                        "Apocalypse Sword",
                        "Abyssal Hammer",
                        "King's Executioner",
                        "Worldbreaker",
                        "Nightmare Greataxe"
                    ).random(random)
                }
            }

            ItemType.WeaponLeftHand -> {

                when (act) {

                    1 -> listOf(
                        "Hook",
                        "Bloody Hook",
                        "Small Hook"
                    ).random(random)

                    2 -> listOf(
                        "Hook",
                        "Bloody Hook",
                        "Iron Hook",
                        "Barbed Hook"
                    ).random(random)

                    3 -> listOf(
                        "Blood Hook",
                        "War Hook",
                        "Cruel Hook",
                        "Magic Hook"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Hook",
                        "Dread Hook",
                        "Infernal Hook",
                        "Molten Hook"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Hook",
                        "Soul Hook",
                        "Doom Hook",
                        "Worldbreaker Hook"
                    ).random(random)
                }
            }

            ItemType.Armor -> {

                when (act) {

                    1 -> listOf(
                        "Rusty Armor",
                        "Cloak",
                        "Rags",
                        "Heavy Armor",
                        "Damaged Armor",
                        "Rusty Mail",
                        "Leather Armor"
                    ).random(random)

                    2 -> listOf(
                        "Plate",
                        "Bronze Plate",
                        "Rusty Plate",
                        "Iron Armor",
                        "Leather Armor"
                    ).random(random)

                    3 -> listOf(
                        "Iron Armor",
                        "Plated Jacket",
                        "Steel Armor",
                        "Battle Plate",
                        "Knight Armor"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Armor",
                        "Infernal Plate",
                        "Dread Armor",
                        "Blacksteel Armor",
                        "Wyrmplate"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Armor",
                        "Doomplate",
                        "Armor of the Fallen",
                        "Apocalyptic Plate",
                        "Soulforged Armor"
                    ).random(random)
                }
            }

            ItemType.Boots -> {

                when (act) {

                    1 -> listOf(
                        "Boots",
                        "Sandals",
                        "Health Boots",
                        "Rusty Boots",
                        "Strength Boots",
                        "Leather Boots",
                        "Normal Shoes"
                    ).random(random)

                    2 -> listOf(
                        "Bronze Boots",
                        "Swift Boots",
                        "Rusty Boots",
                        "Iron Boots",
                        "Leather Boots"
                    ).random(random)

                    3 -> listOf(
                        "Iron Boots",
                        "Steel Boots",
                        "War Boots",
                        "Reinforced Boots"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Boots",
                        "Infernal Boots",
                        "Flamewalker Boots",
                        "Wyrmhide Boots"
                    ).random(random)

                    else -> listOf(
                        "Doom Boots",
                        "Abyss Walker",
                        "Soulforged Boots",
                        "Boots of the End"
                    ).random(random)
                }
            }

            ItemType.Gloves -> {

                when (act) {

                    1 -> listOf(
                        "Gloves",
                        "Leather Gloves",
                        "Damaged Gloves",
                        "Worn Gloves"
                    ).random(random)

                    2 -> listOf(
                        "Bronze Gloves",
                        "Steel Gauntlets",
                        "Iron Gauntlets",
                        "Chance Guards"
                    ).random(random)

                    3 -> listOf(
                        "Iron Gauntlets",
                        "Battle Gloves",
                        "War Gauntlets",
                        "Steel Mitts"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Gauntlets",
                        "Infernal Gauntlets",
                        "Dread Gloves",
                        "Molten Gauntlets"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Gauntlets",
                        "Doom Gauntlets",
                        "Soulforged Gloves",
                        "Endbringer Gauntlets"
                    ).random(random)
                }
            }

            ItemType.Leggings -> {

                when (act) {

                    1 -> listOf(
                        "Worn Pants",
                        "Torn Pants",
                        "Leggings",
                        "Pants",
                        "Leather Leggings"
                    ).random(random)

                    2 -> listOf(
                        "Bronze Leggings",
                        "Vampire Leggings",
                        "Iron Leggings",
                        "Reinforced Leggings"
                    ).random(random)

                    3 -> listOf(
                        "Iron Leggings",
                        "Steel Leggings",
                        "War Leggings",
                        "Knight Leggings"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Leggings",
                        "Infernal Leggings",
                        "Flameforged Leggings",
                        "Dread Leggings"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Leggings",
                        "Doom Leggings",
                        "Soulforged Leggings",
                        "Leggings of the End"
                    ).random(random)
                }
            }

            ItemType.Helmet -> {

                when (act) {

                    1 -> listOf(
                        "Leather Helmet",
                        "Worn Helmet",
                        "Rusty Helmet"
                    ).random(random)

                    2 -> listOf(
                        "Bronze Helmet",
                        "Iron Helmet",
                        "War Helmet"
                    ).random(random)

                    3 -> listOf(
                        "Steel Helmet",
                        "Knight Helmet",
                        "Battle Helm"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Helm",
                        "Infernal Helm",
                        "Wyrm Helm"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Helm",
                        "Doom Helm",
                        "Helm of the Fallen"
                    ).random(random)
                }
            }

            ItemType.Belt -> {

                when (act) {

                    1 -> listOf(
                        "Belt",
                        "Rusty Belt",
                        "Leather Belt",
                        "Strong Belt",
                        "Belt of Life"
                    ).random(random)

                    2 -> listOf(
                        "Bronze Belt",
                        "Vampire Belt",
                        "Iron Belt",
                        "War Belt"
                    ).random(random)

                    3 -> listOf(
                        "Steel Belt",
                        "Battle Belt",
                        "Champion's Belt"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Belt",
                        "Infernal Belt",
                        "Dread Belt"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Belt",
                        "Doom Belt",
                        "Belt of the End"
                    ).random(random)
                }
            }

            ItemType.Shoulders -> {

                when (act) {

                    1 -> listOf(
                        "Pads",
                        "Strong Shoulders",
                        "Strong Pads",
                        "Shoulders"
                    ).random(random)

                    2 -> listOf(
                        "Bronze Shoulders",
                        "Rusty Shoulders",
                        "Iron Shoulders"
                    ).random(random)

                    3 -> listOf(
                        "Steel Shoulders",
                        "War Shoulders",
                        "Knight Shoulders"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Shoulders",
                        "Infernal Shoulders",
                        "Wyrm Shoulders"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Shoulders",
                        "Doom Shoulders",
                        "Soulforged Shoulders"
                    ).random(random)
                }
            }

            ItemType.Amulet -> {

                when (act) {

                    1 -> listOf(
                        "Amulet",
                        "Old Amulet"
                    ).random(random)

                    2 -> listOf(
                        "Bronze Amulet",
                        "Mystic Amulet"
                    ).random(random)

                    3 -> listOf(
                        "Blood Amulet",
                        "Warrior's Amulet",
                        "Arcane Amulet"
                    ).random(random)

                    4 -> listOf(
                        "Dragon Amulet",
                        "Infernal Amulet",
                        "Dread Amulet"
                    ).random(random)

                    else -> listOf(
                        "Abyssal Amulet",
                        "Doom Amulet",
                        "Amulet of the End"
                    ).random(random)
                }
            }
        }
    }

    // ============================================================
    // PREFIXES
    // ============================================================

    private val uniquePrefixes = listOf(
        "Fabled",
        "Mythic",
        "Elder",
        "Heroic",
        "Grand",
        "Warlord's",
        "Knight's",
        "Unique",
        "Cunning"
    )

    private val epicPrefixes = listOf(
        "Ancient",
        "Cursed",
        "Enchanted",
        "Epic",
        "Golden",
        "Silver",
        "Hero's"
    )

    private val legendaryPrefixes = listOf(
        "Legendary",
        "Immortal",
        "Timeless"
    )

    private val godlyPrefixes = listOf(
        "Divine",
        "Godly"
    )

    // ============================================================
    // SUFFIXES
    // ============================================================

    private val normalSuffixes = listOf(
        "of Health",
        "of Stamina",
        "of Iron",
        "of Bronze",
        "of Leather",
        "of Defense",
        "of Protection",
        "of Stone",
        "of Steel"
    )

    private val magicSuffixes = listOf(
        "of Magic",
        "of Mysticism",
        "of Arcana",
        "of Sorcery",
        "of Enchantment",
        "of Mana",
        "of Illusion",
        "of Mystic",
        "of Hexes",
        "of Confusion"
    )

    private val strongSuffixes = listOf(
        "of Fury",
        "of Skill",
        "of Flames",
        "of Frost",
        "of Storms",
        "of the Stars",
        "of Oracle",
        "of Strength"
    )

    private val rareSuffixes = listOf(
        "of Champions",
        "of Valor",
        "of Fortitude",
        "of Precision",
        "of Berserk",
        "of Titans",
        "of Might",
        "of Blood"
    )

    private val uniqueSuffixes = listOf(
        "of Gold",
        "of Power",
        "of Glory",
        "of Wisdom"
    )

    private val epicSuffixes = listOf(
        "of Shadows",
        "of Light",
        "of the Void",
        "of Eternity",
        "of Oblivion"
    )

    private val legendarySuffixes = listOf(
        "of Dragons",
        "of Angels",
        "of Chaos",
        "of Demons",
        "of Doom"
    )

    private val godlySuffixes = listOf(
        "of God"
    )
}
