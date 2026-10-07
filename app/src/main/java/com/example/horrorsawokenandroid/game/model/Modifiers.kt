package com.example.horrorsawokenandroid.game.model

import com.example.horrorsawokenandroid.game.ui.GameUiState

class Modifiers(
    private val getState: () -> GameUiState,
    private val updateState: ((GameUiState) -> GameUiState) -> Unit,
    private val storage: ModifierStorage = NoOpModifierStorage,
    private val itemUpgrader: ItemUpgrader,
    private val reforgeFrogStat: ReforgeFrogStat,
) {
    companion object {
        val allIds = listOf(
            "crit", "healthy", "juggernaut", "intuitive", "vampire", "thief", "rich",
            "immortal", "dangerous", "strong", "soldier", "veteran", "reflexes",
            "looter", "skilled",
            "upgrader", "healer", "smuggler", "smithing", "pirate",
            "cheapsmith", "friendly", "phoenix"
        )
    }

    fun loadUnlocked(): Set<String> = storage.load() // unlocked is never used

    /** Re-applies every already-unlocked modifier's effect to a freshly-created Player. Call this on new-game/restart. */
    fun loadUnlockedModifiers(player: Player): Player {
        val unlocked = storage.load()

        return player.copy(
            unlockedModifiers = unlocked,
            activeModifiers = emptySet()
        )
    }

    /** Applies ONE specific modifier by id (used by manual/debug buttons). No-ops if already unlocked. */
    fun applyModifier(id: String): String {
        val state = getState()
        val player = state.player

        if (id !in player.unlockedModifiers) {
            return "Modifier not unlocked"
        }

        val isActive = id in player.activeModifiers

        val updatedPlayer = if (isActive) {
            removeEffect(id, player) // Disable modifier
        } else {
            applyEffect(id, player) // Enable modifier
        }

        val newActive = if (isActive) {
            player.activeModifiers - id
        } else {
            player.activeModifiers + id
        }

        updateState {
            it.copy(
                player = updatedPlayer.copy(
                    activeModifiers = newActive,
                    numberOfUnlockedModifiers = state.player.numberOfUnlockedModifiers + 1
                )
            )
        }

        // Only unlocked modifiers are saved
        storage.save(player.unlockedModifiers)

        return if (isActive) {
            "$id disabled"
        } else {
            "$id enabled"
        }
    }

    /** Picks ONE random modifier the player doesn't have yet and applies it. Returns null if all are unlocked. */
    fun giveRandomModifier(): String? {
        val state = getState()

        val available = allIds.filter {
            it !in state.player.unlockedModifiers
        }

        if (available.isEmpty()) return null

        val id = available.random()

        val newUnlocked = state.player.unlockedModifiers + id

        updateState {
            it.copy(
                player = state.player.copy(
                    unlockedModifiers = newUnlocked,
                )
            )
        }

        storage.save(newUnlocked)

        return id
    }

    fun getDescription(id: String): String = descriptionFor(id)

    private fun descriptionFor(id: String): String = when (id) {
        "crit" -> "Increases Crit Chance."
        "healthy" -> "Increases Max Health."
        "juggernaut" -> "Increases Armor."
        "intuitive" -> "Reduces the cost to learn new attacks."
        "vampire" -> "Increases Lifesteal."
        "thief" -> "Increases starting gold slightly."
        "rich" -> "Increases starting gold."
        "immortal" -> "Increases Health Regeneration."
        "dangerous" -> "Increases Critical Damage."
        "strong" -> "Increases Strength."
        "soldier" -> "Increases Damage slightly."
        "veteran" -> "Increases Damage."
        "reflexes" -> "Increases Dodge Chance."
        "looter" -> "Increases gold found."
        "skilled" -> "Unlocks the first technique, Blood Lust."
        "upgrader" -> "Reduces smith upgrade costs greatly."
        "healer" -> "Reduces healers' healing costs."
        "smuggler" -> "Start with a Dagger in inventory."
        "smithing" -> "Improves smithing upgrades."
        "pirate" -> "Reduces reforge costs (Frog)."
        "cheapsmith" -> "Reduces smithing costs slightly."
        else -> "Unknown modifier."
    }

    private fun applyEffect(id: String, player: Player): Player =
        when (id) {
            "crit" -> player.copy(critChance = player.critChance + 5)
            "healthy" -> player.copy(maxHealth = player.maxHealth + 20)
            "juggernaut" -> player.copy(armor = player.armor + 1)
            "intuitive" -> player.copy(priceToLearnTechnique = player.priceToLearnTechnique / 2)
            "vampire" -> player.copy(lifesteal = player.lifesteal + 5)
            "thief" -> player.copy(goldInPocket = player.goldInPocket + 50)
            "rich" -> player.copy(goldInPocket = player.goldInPocket + 250)
            "immortal" -> player.copy(regeneration = player.regeneration + 4)
            "dangerous" -> player.copy(critDamage = player.critDamage + 20)
            "strong" -> player.copy(strength = player.strength + 2)
            "soldier" -> player.copy(damage = player.damage + 1)
            "veteran" -> player.copy(damage = player.damage + 2)
            "reflexes" -> player.copy(dodgeChance = player.dodgeChance + 3)
            "looter" -> player.copy(goldFind = player.goldFind + 1)
            "skilled" -> player.copy(
                techniqueBloodLustIsLearned = true,
                numberOfTechniquesLearned = 1,
                hasSkilledModifier = true
            )

            "upgrader" -> {
                itemUpgrader.costToUpgradeItem /= 2
                player
            }

            "healer" -> player.copy(healingCostModifier = 2)
            "smuggler" -> {
                val dagger = Items.Item(
                    name = "Soldier's Dagger",
                    type = Items.ItemType.Weapon,
                    damage = 1,
                    strengthRequirement = 1,
                    levelRequirement = 1
                )
                player.inventory.add(dagger)
                player
            }

            "smithing" -> {
                itemUpgrader.smithUpgradeMultiplication = 2
                player
            }

            "pirate" -> {
                reforgeFrogStat.priceToReforgeFrog /= 2
                player
            }

            "cheapsmith" -> {
                itemUpgrader.costToUpgradeItem -= 10
                player
            }

            else -> player
        }

    private fun removeEffect(id: String, player: Player): Player =
        when (id) {
            "crit" -> player.copy(critChance = player.critChance - 5)
            "healthy" -> player.copy(maxHealth = player.maxHealth - 20)
            "juggernaut" -> player.copy(armor = player.armor - 1)
            "intuitive" -> player.copy(priceToLearnTechnique = player.priceToLearnTechnique * 2)
            "vampire" -> player.copy(lifesteal = player.lifesteal - 5)
            "thief" -> player.copy(goldInPocket = player.goldInPocket - 50)
            "rich" -> player.copy(goldInPocket = player.goldInPocket - 250)
            "immortal" -> player.copy(regeneration = player.regeneration - 4)
            "dangerous" -> player.copy(critDamage = player.critDamage - 20)
            "strong" -> player.copy(strength = player.strength - 2)
            "soldier" -> player.copy(damage = player.damage - 1)
            "veteran" -> player.copy(damage = player.damage - 2)
            "reflexes" -> player.copy(dodgeChance = player.dodgeChance - 3)
            "looter" -> player.copy(goldFind = player.goldFind - 1)
            "skilled" -> player.copy(
                techniqueBloodLustIsLearned = false,
                numberOfTechniquesLearned = 0,
                hasSkilledModifier = false
            )

            "upgrader" -> {
                itemUpgrader.costToUpgradeItem *= 2
                player
            }

            "healer" -> player.copy(healingCostModifier = 1)

            "smuggler" -> {
                player.inventory.removeIf { it.name == "Soldier's Dagger" }
                player
            }

            "smithing" -> {
                itemUpgrader.smithUpgradeMultiplication = 1
                player
            }

            "pirate" -> {
                reforgeFrogStat.priceToReforgeFrog *= 2
                player
            }

            "cheapsmith" -> {
                itemUpgrader.costToUpgradeItem += 10
                player
            }

            "friendly" -> {
                reforgeFrogStat.reforgeModifier -= 0.15
                player
            }

            "phoenix" -> player.copy(hasPhoenixModifier = false)

            else -> player
        }

    // This resets all modifiers, only to be used for testing. Can be called via viewModel.modifiers.resetModifiers()
    fun resetModifiers() {
        updateState {
            it.copy(
                player = it.player.copy(
                    unlockedModifiers = emptySet(),
                    activeModifiers = emptySet()
                )
            )
        }

        storage.save(emptySet())
    }
}