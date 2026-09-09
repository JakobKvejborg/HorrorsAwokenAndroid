package com.example.horrorsawokenandroid.game.model

import kotlin.random.Random

data class Monster(
    val name: String,
    val maxHealth: Int,
    var currentHealth: Int,
    val minDamage: Int,
    val maxDamage: Int,
    val randomDamageModifier: Int,
    val monsterExperience: Int,
    val monsterGold: Int,
    val imageRes: Int? = null
) {

    fun cloneMonster(): Monster {
        return copy(currentHealth = maxHealth)
    }

    fun calculateMonsterDamage(): Int {
        return minDamage + Random.nextInt(randomDamageModifier + 1)
    }
}