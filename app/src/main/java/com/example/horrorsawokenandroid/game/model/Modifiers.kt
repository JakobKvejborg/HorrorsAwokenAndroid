package com.example.horrorsawokenandroid.game.model

import com.example.horrorsawokenandroid.game.ui.GameUiState

class Modifiers(
    private val getState: () -> GameUiState,
    private val updateState: ((GameUiState) -> GameUiState) -> Unit,
    private val storage: ModifierStorage = NoOpModifierStorage,
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

    fun loadUnlocked(): Set<String> = storage.load() // unlucked is never used

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
            // Disable modifier
            removeEffect(id, player)
        } else {
            // Enable modifier
            applyEffect(id, player)
        }

        val newActive = if (isActive) {
            player.activeModifiers - id
        } else {
            player.activeModifiers + id
        }

        updateState {
            it.copy(
                player = updatedPlayer.copy(
                    activeModifiers = newActive
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
                    unlockedModifiers = newUnlocked
                )
            )
        }

        storage.save(newUnlocked)

        return id
    }

    fun getDescription(id: String): String =
        applyEffectWithMessage(id, getState().player).second

    private fun applyEffect(id: String, player: Player): Player =
        applyEffectWithMessage(id, player).first

    private fun applyEffectWithMessage(id: String, player: Player): Pair<Player, String> =
        when (id) {
            "crit" -> player.copy(critChance = player.critChance + 5) to "Increases Crit Chance."
            "healthy" -> player.copy(maxHealth = player.maxHealth + 15) to "Increases Max Health."
            "juggernaut" -> player.copy(armor = player.armor + 1) to "Increases Armor."
            "intuitive" -> player.copy(PriceToLearnTechnique = player.PriceToLearnTechnique / 2) to "Reduces the cost to learn new attacks."
            "vampire" -> player.copy(lifesteal = player.lifesteal + 5) to "Increases Lifesteal."
            "thief" -> player.copy(goldInPocket = player.goldInPocket + 50) to "Increases starting gold slightly."
            "rich" -> player.copy(goldInPocket = player.goldInPocket + 250) to "Increases starting gold."
            "immortal" -> player.copy(regeneration = player.regeneration + 5) to "Increases Health Regeneration."
            "dangerous" -> player.copy(critDamage = player.critDamage + 20) to "Increases Critical Damage."
            "strong" -> player.copy(strength = player.strength + 3) to "Increases Strength."
            "soldier" -> player.copy(damage = player.damage + 1) to "Increases Damage slightly."
            "veteran" -> player.copy(damage = player.damage + 2) to "Increases Damage."
            "reflexes" -> player.copy(dodgeChance = player.dodgeChance + 3) to "Increases Dodge Chance."
            "looter" -> player.copy(goldFind = player.goldFind + 1) to "Increases Gold Find."
            "skilled" -> player.copy(techniqueBloodLustIsLearned = true) to "Unlocks the first technique, Blood Lust." // TODO fix this,
            "upgrader" -> {
                // TODO: Reduce Item.CostToUpgrade by half
                player to "Reduces item upgrade costs greatly (Smith)."
            }
            "healer" -> {
                // TODO: Set healing cost reduction to 2
                player to "Reduces healers' healing costs."
            }
            "smuggler" -> {
                // TODO: Give the player a Soldier's Dagger
                player to "Start with a Soldier's Dagger in inventory."
            }
            "smithing" -> {
                // TODO: Increase Item.SmithUpgradeMultiplication by 1
                player to "Improves smithing upgrades."
            }
            "pirate" -> {
                // TODO: Reduce ReforgeItemStat.PriceToReforgeFrog by half
                player to "Reduces reforge costs (Frog)."
            }
            "cheapsmith" -> {
                // TODO: Reduce Item.CostToUpgrade by 10
                player to "Reduces smithing costs slightly."
            }
            "friendly" -> {
                // TODO: Increase ReforgeItemStat.ReforgeModifier by 0.15
                player to "Improves reforge effects (Frog)."
            }
            "phoenix" -> {
                // TODO: Set resurrection buff to true
                player to "Death can wait..."
            }
            else -> player to "Unknown modifier"
        }

    private fun removeEffect(id: String, player: Player): Player =
        when (id) {
            "crit" -> player.copy(critChance = player.critChance - 5)
            "healthy" -> player.copy(maxHealth = player.maxHealth - 15)
            "juggernaut" -> player.copy(armor = player.armor - 1)
            "intuitive" -> player.copy(PriceToLearnTechnique = player.PriceToLearnTechnique * 2)
            "vampire" -> player.copy(lifesteal = player.lifesteal - 5)
            "thief" -> player.copy(goldInPocket = player.goldInPocket - 50)
            "rich" -> player.copy(goldInPocket = player.goldInPocket - 250)
            "immortal" -> player.copy(regeneration = player.regeneration - 5)
            "dangerous" -> player.copy(critDamage = player.critDamage - 20)
            "strong" -> player.copy(strength = player.strength - 3)
            "soldier" -> player.copy(damage = player.damage - 1)
            "veteran" -> player.copy(damage = player.damage - 2)
            "reflexes" -> player.copy(dodgeChance = player.dodgeChance - 3)
            "looter" -> player.copy(goldFind = player.goldFind - 1)
            "skilled" -> player.copy(techniqueBloodLustIsLearned = false)
            "upgrader" -> {
                // TODO: Restore Item.CostToUpgrade
                player
            }

            "healer" -> {
                // TODO: Remove healing cost reduction
                player
            }

            "smuggler" -> {
                // TODO: Remove the Soldier's Dagger if appropriate
                player
            }

            "smithing" -> {
                // TODO: Decrease Item.SmithUpgradeMultiplication by 1
                player
            }

            "pirate" -> {
                // TODO: Restore ReforgeItemStat.PriceToReforgeFrog
                player
            }

            "cheapsmith" -> {
                // TODO: Increase Item.CostToUpgrade by 10
                player
            }

            "friendly" -> {
                // TODO: Decrease ReforgeItemStat.ReforgeModifier by 0.15
                player
            }

            "phoenix" -> {
                // TODO: Set resurrection buff to false
                player
            }
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