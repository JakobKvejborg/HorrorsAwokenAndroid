package com.example.horrorsawokenandroid.game.model

import android.content.Context

interface ModifierStorage {
    fun load(): Set<String>
    fun save(ids: Set<String>)
}

object NoOpModifierStorage : ModifierStorage {
    override fun load(): Set<String> = emptySet()
    override fun save(ids: Set<String>) {}
}

class AndroidModifierStorage(context: Context) : ModifierStorage {
    private val prefs = context.getSharedPreferences("modifiers_prefs", Context.MODE_PRIVATE)

    override fun load(): Set<String> =
        prefs.getStringSet("unlocked_modifiers", emptySet()) ?: emptySet()

    override fun save(ids: Set<String>) {
        prefs.edit().putStringSet("unlocked_modifiers", ids).apply()
    }
}