package com.example.horrorsawokenandroid.game.model

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import kotlin.random.Random

interface SoundPlayer {
    fun playNormalAttack()
    fun playBloodLustAttack()
    fun playSwiftAttack()
    fun playRoarAttack()
    fun playDivineAttack()
    fun playGuard()
    fun playCrit()
    fun playDodge()
    fun playCoin()
    fun playLevelUp()

    fun playHealingSound()
    fun playAct2HealingSound()
    fun playAct4HealingSound()
    fun playAct1HealingNoGold()

    fun playAct1TownMusic()
    fun playAct2TownMusic()
    fun playAct2WindMusic()
    fun playAct1BossSound()
    fun playAct2BossSound()
    fun playAct3Boss()
    fun playAct2FrostfallenKing()

    fun playAct3Music()
    fun playAct4Music()
    fun playAct5Music()
    fun playAct3Waves()

    fun playAct1HealingMusic()
    fun playResurrectionMusic()

    fun playAct1ArtsTeacher()
    fun playAct1ArtsTeacherNo()
    fun playAct1WomanCrying()
    fun playAct4Q1Voice()

    fun playAct2SmithOffer()
    fun playAct2SmithNo()

    fun playAct3Frog()
    fun playAct3ReforgeFroggy()
    fun playAct3FrozenLilyFrogSound()
    fun playAct4MageSound()

    fun playSmithingSound()
    fun playInventorySound()
    fun playSmithUpgradeRubySound()
    fun playDeathGameOverSound()
    fun playLootItemsSound()
    fun playEquipSound()

    fun stopAct1TownMusic()
    fun stopAct2TownMusic()
    fun stopAct2WindSound()
    fun stopAct1ThunderSound()
    fun stopAct3Waves()
    fun stopAct3Music()
    fun stopAct4Music()
    fun stopAct5Music()

    fun muteAllMusic()

    fun release()
}


class AndroidSoundPlayer(
    private val context: Context
) : SoundPlayer {

    private val random = Random.Default

    private val soundPool: SoundPool

    private val soundEffects = mutableMapOf<String, Int>()

    private var act1TownPlayer: MediaPlayer? = null
    private var act2TownPlayer: MediaPlayer? = null
    private var act1ThunderPlayer: MediaPlayer? = null
    private var act2WindPlayer: MediaPlayer? = null
    private var bossPlayer: MediaPlayer? = null
    private var soundEffectPlayer: MediaPlayer? = null
    private var critSoundPlayer: MediaPlayer? = null
    private var healingMusicPlayer: MediaPlayer? = null
    private var npcSpeechPlayer: MediaPlayer? = null
    private var act3WavesPlayer: MediaPlayer? = null
    private var act3MusicPlayer: MediaPlayer? = null
    private var act4MusicPlayer: MediaPlayer? = null
    private var act5MusicPlayer: MediaPlayer? = null

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(8)
            .setAudioAttributes(audioAttributes)
            .build()

        loadSounds()
    }

    private fun loadSounds() {

        loadSound("act1healer")
        loadSound("sword1")
        loadSound("sword2")
        loadSound("sword3")
        loadSound("sword4")
        loadSound("sword5")
        loadSound("sword6")
        loadSound("sword7")
        loadSound("act1boss")
        loadSound("act2healer")
        loadSound("smithing")
        loadSound("inventory")
        loadSound("deathgameover")
        loadSound("lootitems")
        loadSound("roarattack")
        loadSound("divineattack")
        loadSound("act4healer")
        loadSound("act1healernogold")
        loadSound("equip")
        loadSound("smithupgrade")
    }

    private fun loadSound(name: String) {
        val resourceId = context.resources.getIdentifier(
            name,
            "raw",
            context.packageName
        )

        if (resourceId != 0) {
            soundEffects[name] = soundPool.load(context, resourceId, 1)
        }
    }

    private fun playSound(name: String) {
        val soundId = soundEffects[name] ?: return

        soundPool.play(
            soundId,
            1f,
            1f,
            1,
            0,
            1f
        )
    }

    private fun playMusic(
        fileName: String,
        currentPlayer: MediaPlayer?,
        looping: Boolean = false
    ): MediaPlayer? {

        currentPlayer?.release()

        val resourceName = fileName
            .substringBeforeLast(".")
            .lowercase()

        val resourceId = context.resources.getIdentifier(
            resourceName,
            "raw",
            context.packageName
        )

        if (resourceId == 0) {
            return null
        }

        return try {
            MediaPlayer.create(context, resourceId)?.apply {
                isLooping = looping
                start()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun playEffectMusic(
        fileName: String,
        currentPlayer: MediaPlayer?
    ): MediaPlayer? {
        return playMusic(
            fileName = fileName,
            currentPlayer = currentPlayer,
            looping = false
        )
    }

    // ---------------------------------------------------------
    // Combat sounds
    // ---------------------------------------------------------

    override fun playNormalAttack() {
        val choice = random.nextInt(1, 8)

        playSound("sword$choice")
    }

    override fun playBloodLustAttack() {
        soundEffectPlayer = playEffectMusic(
            "bloodlust.wav",
            soundEffectPlayer
        )

        playNormalAttack()
    }

    override fun playSwiftAttack() {
        soundEffectPlayer = playEffectMusic(
            "dodgejab.wav",
            soundEffectPlayer
        )
    }

    override fun playRoarAttack() {
        playSound("roarattack")
    }

    override fun playDivineAttack() {
        playSound("divineattack")
    }

    override fun playGuard() {
        soundEffectPlayer = playEffectMusic(
            "guardattack.wav",
            soundEffectPlayer
        )
    }

    override fun playCrit() {
        critSoundPlayer = playEffectMusic(
            "crit.wav",
            critSoundPlayer
        )
    }

    override fun playDodge() {
        soundEffectPlayer = playEffectMusic(
            "dodge.wav",
            soundEffectPlayer
        )
    }

    // ---------------------------------------------------------
    // General sounds
    // ---------------------------------------------------------

    override fun playCoin() {
        soundEffectPlayer = playEffectMusic(
            "coin.wav",
            soundEffectPlayer
        )
    }

    override fun playLevelUp() {
        soundEffectPlayer = playEffectMusic(
            "levelup.wav",
            soundEffectPlayer
        )
    }

    override fun playHealingSound() {
        playSound("act1healer")
    }

    override fun playAct2HealingSound() {
        playSound("act2healer")
    }

    override fun playAct4HealingSound() {
        playSound("act4healer")
    }

    override fun playAct1HealingNoGold() {
        playSound("act1healernogold")
    }

    override fun playSmithingSound() {
        playSound("smithing")
    }

    override fun playInventorySound() {
        playSound("inventory")
    }

    override fun playSmithUpgradeRubySound() {
        playSound("smithupgrade")
    }

    override fun playDeathGameOverSound() {
        playSound("deathgameover")
    }

    override fun playLootItemsSound() {
        playSound("lootitems")
    }

    override fun playEquipSound() {
        playSound("equip")
    }

    // ---------------------------------------------------------
    // Town / area music
    // ---------------------------------------------------------
    fun playThunder() {
        act1ThunderPlayer = playMusic(
            "thunder.wav",
            act1ThunderPlayer,
            true
        )
    }

    override fun playAct1TownMusic() {
        act1TownPlayer = playMusic(
            "act1town.wav",
            act1TownPlayer,
            true
        )
    }

    override fun playAct2TownMusic() {
        act2TownPlayer = playMusic(
            "act2town.wav",
            act2TownPlayer,
            true
        )
    }

    override fun playAct2WindMusic() {
        act2WindPlayer = playMusic(
            "act2wind.wav",
            act2WindPlayer,
            true
        )
    }

    override fun playAct3Music() {
        act3MusicPlayer = playMusic(
            "act3.wav",
            act3MusicPlayer,
            true
        )
    }

    override fun playAct4Music() {
        act4MusicPlayer = playMusic(
            "act4.wav",
            act4MusicPlayer,
            true
        )
    }

    override fun playAct5Music() {
        act5MusicPlayer = playMusic(
            "act5.wav",
            act5MusicPlayer,
            true
        )
    }

    override fun playAct3Waves() {
        act3WavesPlayer = playMusic(
            "act3waves.wav",
            act3WavesPlayer,
            true
        )
    }

    // ---------------------------------------------------------
    // Boss music
    // ---------------------------------------------------------

    override fun playAct1BossSound() {
        bossPlayer = playMusic(
            "act1boss.wav",
            bossPlayer,
            true
        )
    }

    override fun playAct2BossSound() {
        bossPlayer = playMusic(
            "act2boss.wav",
            bossPlayer,
            true
        )
    }

    override fun playAct3Boss() {
        bossPlayer = playMusic(
            "act3boss.wav",
            bossPlayer,
            true
        )
    }

    override fun playAct2FrostfallenKing() {
        bossPlayer = playMusic(
            "act2frostfallenking.wav",
            bossPlayer,
            true
        )
    }

    // ---------------------------------------------------------
    // Healing / resurrection
    // ---------------------------------------------------------

    override fun playAct1HealingMusic() {
        healingMusicPlayer = playMusic(
            "act1healer.wav",
            healingMusicPlayer
        )
    }

    override fun playResurrectionMusic() {
        healingMusicPlayer = playMusic(
            "resurrection.wav",
            healingMusicPlayer
        )
    }

    // ---------------------------------------------------------
    // NPC voices
    // ---------------------------------------------------------

    override fun playAct1ArtsTeacher() {
        npcSpeechPlayer = playMusic(
            "act1artsteacher.wav",
            npcSpeechPlayer
        )
    }

    override fun playAct1ArtsTeacherNo() {
        npcSpeechPlayer = playMusic(
            "act1artsteacherno.wav",
            npcSpeechPlayer
        )
    }

    override fun playAct1WomanCrying() {
        npcSpeechPlayer = playMusic(
            "act1quest1womancrying.wav",
            npcSpeechPlayer
        )
    }

    override fun playAct4Q1Voice() {
        npcSpeechPlayer = playMusic(
            "act4q1voice.wav",
            npcSpeechPlayer
        )
    }

    override fun playAct2SmithOffer() {
        val sound = if (random.nextInt(2) == 0) {
            "act2smithoffer.wav"
        } else {
            "act2smithoffer2.wav"
        }

        npcSpeechPlayer = playMusic(
            sound,
            npcSpeechPlayer
        )
    }

    override fun playAct2SmithNo() {
        npcSpeechPlayer = playMusic(
            "act2smithno.wav",
            npcSpeechPlayer
        )
    }

    // ---------------------------------------------------------
    // Act 3 / Act 4 voices
    // ---------------------------------------------------------

    override fun playAct3Frog() {
        when (random.nextInt(4)) {
            0 -> {
                npcSpeechPlayer = playMusic(
                    "frog1.wav",
                    npcSpeechPlayer
                )
            }

            1 -> {
                npcSpeechPlayer = playMusic(
                    "frog1.wav",
                    npcSpeechPlayer
                )
            }

            2 -> {
                npcSpeechPlayer = playMusic(
                    "frog2.wav",
                    npcSpeechPlayer
                )
            }

            3 -> {
                npcSpeechPlayer = playMusic(
                    "frog3.wav",
                    npcSpeechPlayer
                )
            }
        }
    }

    override fun playAct3ReforgeFroggy() {
        when (random.nextInt(3)) {
            0 -> {
                npcSpeechPlayer = playMusic(
                    "frog3.wav",
                    npcSpeechPlayer
                )
            }

            1 -> {
                npcSpeechPlayer = playMusic(
                    "frog4.wav",
                    npcSpeechPlayer
                )
            }

            2 -> {
                // No sound
            }
        }
    }

    override fun playAct3FrozenLilyFrogSound() {
        npcSpeechPlayer = playMusic(
            "act3frozenlilyfrogsound.wav",
            npcSpeechPlayer
        )
    }

    override fun playAct4MageSound() {
        val sound = when (random.nextInt(3)) {
            0 -> "act4mage1.wav"
            1 -> "act4mage2.wav"
            else -> "act4mage3.wav"
        }

        npcSpeechPlayer = playMusic(
            sound,
            npcSpeechPlayer
        )
    }

    // ---------------------------------------------------------
    // Stop music
    // ---------------------------------------------------------

    override fun stopAct1TownMusic() {
        stopPlayer(act1TownPlayer)
        act1TownPlayer = null
    }

    override fun stopAct2TownMusic() {
        stopPlayer(act2TownPlayer)
        act2TownPlayer = null
    }

    override fun stopAct2WindSound() {
        stopPlayer(act2WindPlayer)
        act2WindPlayer = null
    }

    override fun stopAct1ThunderSound() {
        stopPlayer(act1ThunderPlayer)
        act1ThunderPlayer = null
    }

    override fun stopAct3Waves() {
        stopPlayer(act3WavesPlayer)
        act3WavesPlayer = null
    }

    override fun stopAct3Music() {
        stopPlayer(act3MusicPlayer)
        act3MusicPlayer = null
    }

    override fun stopAct4Music() {
        stopPlayer(act4MusicPlayer)
        act4MusicPlayer = null
    }

    override fun stopAct5Music() {
        stopPlayer(act5MusicPlayer)
        act5MusicPlayer = null
    }

    override fun muteAllMusic() {
        stopAct1TownMusic()
        stopAct2TownMusic()
        stopAct2WindSound()
        stopAct4Music()
        stopAct3Music()
        stopAct5Music()
    }

    private fun stopPlayer(player: MediaPlayer?) {
        try {
            player?.stop()
        } catch (_: Exception) {
        }

        player?.release()
    }

    override fun release() {
        stopPlayer(act1TownPlayer)
        stopPlayer(act2TownPlayer)
        stopPlayer(act1ThunderPlayer)
        stopPlayer(act2WindPlayer)
        stopPlayer(bossPlayer)
        stopPlayer(soundEffectPlayer)
        stopPlayer(critSoundPlayer)
        stopPlayer(healingMusicPlayer)
        stopPlayer(npcSpeechPlayer)
        stopPlayer(act3WavesPlayer)
        stopPlayer(act3MusicPlayer)
        stopPlayer(act4MusicPlayer)
        stopPlayer(act5MusicPlayer)

        soundPool.release()
    }
}


object NoOpSoundPlayer : SoundPlayer {

    override fun playNormalAttack() {}
    override fun playBloodLustAttack() {}
    override fun playSwiftAttack() {}
    override fun playRoarAttack() {}
    override fun playDivineAttack() {}
    override fun playGuard() {}
    override fun playCrit() {}
    override fun playDodge() {}
    override fun playCoin() {}
    override fun playLevelUp() {}

    override fun playHealingSound() {}
    override fun playAct2HealingSound() {}
    override fun playAct4HealingSound() {}
    override fun playAct1HealingNoGold() {}

    override fun playAct1TownMusic() {}
    override fun playAct2TownMusic() {}
    override fun playAct2WindMusic() {}
    override fun playAct1BossSound() {}
    override fun playAct2BossSound() {}
    override fun playAct3Boss() {}
    override fun playAct2FrostfallenKing() {}

    override fun playAct3Music() {}
    override fun playAct4Music() {}
    override fun playAct5Music() {}
    override fun playAct3Waves() {}

    override fun playAct1HealingMusic() {}
    override fun playResurrectionMusic() {}

    override fun playAct1ArtsTeacher() {}
    override fun playAct1ArtsTeacherNo() {}
    override fun playAct1WomanCrying() {}
    override fun playAct4Q1Voice() {}

    override fun playAct2SmithOffer() {}
    override fun playAct2SmithNo() {}

    override fun playAct3Frog() {}
    override fun playAct3ReforgeFroggy() {}
    override fun playAct3FrozenLilyFrogSound() {}
    override fun playAct4MageSound() {}

    override fun playSmithingSound() {}
    override fun playInventorySound() {}
    override fun playSmithUpgradeRubySound() {}
    override fun playDeathGameOverSound() {}
    override fun playLootItemsSound() {}
    override fun playEquipSound() {}

    override fun stopAct1TownMusic() {}
    override fun stopAct2TownMusic() {}
    override fun stopAct2WindSound() {}
    override fun stopAct1ThunderSound() {}
    override fun stopAct3Waves() {}
    override fun stopAct3Music() {}
    override fun stopAct4Music() {}
    override fun stopAct5Music() {}

    override fun muteAllMusic() {}
    override fun release() {}
}