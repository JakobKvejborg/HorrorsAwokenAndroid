package com.example.horrorsawokenandroid.game.model

import com.example.horrorsawokenandroid.R

class MonsterContainer {

// ------------------------------------------------------------
// ACT 1 - NORMAL MONSTERS
// ------------------------------------------------------------

    val listOfMonsters1 = listOf(
        Monster(
            name = "Goblin",
            maxHealth = 10,
            currentHealth = 10,
            minDamage = 0,
            maxDamage = 1,
            randomDamageModifier = 3,
            monsterExperience = 7,
            monsterGold = 3,
            imageRes = R.drawable.goblin
        ),
        Monster(
            name = "Orc",
            maxHealth = 7,
            currentHealth = 7,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 3,
            monsterExperience = 9,
            monsterGold = 0,
            imageRes = R.drawable.orc
        ),
        Monster(
            name = "Skeleton",
            maxHealth = 8,
            currentHealth = 8,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 1,
            monsterExperience = 8,
            monsterGold = 0,
            imageRes = R.drawable.skeleton
        ),
        Monster(
            name = "Ghast",
            maxHealth = 7,
            currentHealth = 7,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 4,
            monsterExperience = 8,
            monsterGold = 1,
            imageRes = R.drawable.ghast
        ),
        Monster(
            name = "Corpse",
            maxHealth = 8,
            currentHealth = 4,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 2,
            monsterExperience = 6,
            monsterGold = 0,
            imageRes = R.drawable.corpse
        ),
        Monster(
            name = "Demon",
            maxHealth = 10,
            currentHealth = 10,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 2,
            monsterExperience = 10,
            monsterGold = 2,
            imageRes = R.drawable.demon
        ),
        Monster(
            name = "Ghost",
            maxHealth = 8,
            currentHealth = 8,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 3,
            monsterExperience = 10,
            monsterGold = 1,
            imageRes = R.drawable.ghost
        ),
        Monster(
            name = "Watchers",
            maxHealth = 9,
            currentHealth = 9,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 3,
            monsterExperience = 7,
            monsterGold = 1,
            imageRes = R.drawable.watchers
        ),
        Monster(
            name = "Weak Bandit",
            maxHealth = 6,
            currentHealth = 6,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 1,
            monsterExperience = 7,
            monsterGold = 0,
            imageRes = R.drawable.weakbandit
        )
    )

// ------------------------------------------------------------
// ACT 1/2 - STRONGER NORMAL MONSTERS
// ------------------------------------------------------------

    val listOfMonsters2 = listOf(
        Monster(
            name = "Ghost",
            maxHealth = 8,
            currentHealth = 8,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 3,
            monsterExperience = 10,
            monsterGold = 1
        ),
        Monster(
            name = "Ghast",
            maxHealth = 7,
            currentHealth = 7,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 4,
            monsterExperience = 8,
            monsterGold = 1
        ),
        Monster(
            name = "Golem",
            maxHealth = 28,
            currentHealth = 28,
            minDamage = 2,
            maxDamage = 0,
            randomDamageModifier = 5,
            monsterExperience = 15,
            monsterGold = 0
        ),
        Monster(
            name = "Knight",
            maxHealth = 27,
            currentHealth = 27,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 7,
            monsterExperience = 17,
            monsterGold = 3
        ),
        Monster(
            name = "Horror",
            maxHealth = 21,
            currentHealth = 21,
            minDamage = 3,
            maxDamage = 0,
            randomDamageModifier = 5,
            monsterExperience = 13,
            monsterGold = 0
        ),
        Monster(
            name = "Starved",
            maxHealth = 17,
            currentHealth = 17,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 7,
            monsterExperience = 19,
            monsterGold = 0
        ),
        Monster(
            name = "Bat",
            maxHealth = 20,
            currentHealth = 20,
            minDamage = 4,
            maxDamage = 0,
            randomDamageModifier = 2,
            monsterExperience = 12,
            monsterGold = 0
        ),
        Monster(
            name = "Wood Horror",
            maxHealth = 16,
            currentHealth = 16,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 8,
            monsterExperience = 22,
            monsterGold = 4
        ),
        Monster(
            name = "Dead Troll",
            maxHealth = 19,
            currentHealth = 19,
            minDamage = 3,
            maxDamage = 0,
            randomDamageModifier = 5,
            monsterExperience = 20,
            monsterGold = 2
        ),
        Monster(
            name = "Lost",
            maxHealth = 27,
            currentHealth = 27,
            minDamage = 4,
            maxDamage = 0,
            randomDamageModifier = 1,
            monsterExperience = 2,
            monsterGold = 4
        ),
        Monster(
            name = "Lizard",
            maxHealth = 33,
            currentHealth = 33,
            minDamage = 1,
            maxDamage = 0,
            randomDamageModifier = 6,
            monsterExperience = 30,
            monsterGold = 0
        )
    )

// ------------------------------------------------------------
// ACT 1 BOSS
// ------------------------------------------------------------

    val listOfMonstersBossAct1 = listOf(
        Monster(
            name = "Aldrus Thornfell",
            maxHealth = 100,
            currentHealth = 100,
            minDamage = 5,
            maxDamage = 0,
            randomDamageModifier = 7,
            monsterExperience = 40,
            monsterGold = 5
        )
    )

// ------------------------------------------------------------
// ACT 1 QUEST
// ------------------------------------------------------------

    val listOfMonstersAct1Quest1 = listOf(
        Monster(
            name = "Hungry Beast",
            maxHealth = 42,
            currentHealth = 42,
            minDamage = 3,
            maxDamage = 0,
            randomDamageModifier = 11,
            monsterExperience = 30,
            monsterGold = 0
        )
    )

// ------------------------------------------------------------
// SNOW AREA
// ------------------------------------------------------------

    val listOfSnowMonsters1 = listOf(
        Monster(
            name = "Snow Demon",
            maxHealth = 35,
            currentHealth = 35,
            minDamage = 11,
            maxDamage = 0,
            randomDamageModifier = 5,
            monsterExperience = 22,
            monsterGold = 0
        ),
        Monster(
            name = "Snow Antler",
            maxHealth = 33,
            currentHealth = 33,
            minDamage = 10,
            maxDamage = 0,
            randomDamageModifier = 5,
            monsterExperience = 18,
            monsterGold = 3
        ),
        Monster(
            name = "Snow Crazed",
            maxHealth = 30,
            currentHealth = 30,
            minDamage = 9,
            maxDamage = 0,
            randomDamageModifier = 5,
            monsterExperience = 15,
            monsterGold = 1
        ),
        Monster(
            name = "Snow Moose",
            maxHealth = 43,
            currentHealth = 43,
            minDamage = 12,
            maxDamage = 0,
            randomDamageModifier = 3,
            monsterExperience = 20,
            monsterGold = 3
        ),
        Monster(
            name = "Snow Horror",
            maxHealth = 45,
            currentHealth = 45,
            minDamage = 8,
            maxDamage = 0,
            randomDamageModifier = 6,
            monsterExperience = 25,
            monsterGold = 5
        ),
        Monster(
            name = "Snow Angel",
            maxHealth = 39,
            currentHealth = 39,
            minDamage = 6,
            maxDamage = 0,
            randomDamageModifier = 11,
            monsterExperience = 21,
            monsterGold = 0
        )
    )

// ------------------------------------------------------------
// SNOW GOLD GOBLIN
// ------------------------------------------------------------

    val listOfMonstersSnowGoldGoblin = listOf(
        Monster(
            name = "Gold Goblin",
            maxHealth = 30,
            currentHealth = 30,
            minDamage = 6,
            maxDamage = 0,
            randomDamageModifier = 10,
            monsterExperience = 0,
            monsterGold = 9
        )
    )

// ------------------------------------------------------------
// ACT 2 BOSS
// ------------------------------------------------------------

    val listOfMonstersBossAct2 = listOf(
        Monster(
            name = "Wintermaw",
            maxHealth = 270,
            currentHealth = 270,
            minDamage = 8,
            maxDamage = 0,
            randomDamageModifier = 17,
            monsterExperience = 50,
            monsterGold = 0
        )
    )

// ------------------------------------------------------------
// ACT 2 OPTIONAL BOSS
// ------------------------------------------------------------

    val listOfOptionalBossAct2 = listOf(
        Monster(
            name = "The Frostfallen King",
            maxHealth = 1100,
            currentHealth = 1100,
            minDamage = 48,
            maxDamage = 0,
            randomDamageModifier = 25,
            monsterExperience = 500,
            monsterGold = 0
        )
    )

// ------------------------------------------------------------
// ACT 3 - SEA
// ------------------------------------------------------------

    val listOfMonstersAct3 = listOf(
        Monster(
            name = "Sea Horror",
            maxHealth = 140,
            currentHealth = 140,
            minDamage = 10,
            maxDamage = 0,
            randomDamageModifier = 15,
            monsterExperience = 40,
            monsterGold = 12
        ),
        Monster(
            name = "Sea Terror",
            maxHealth = 150,
            currentHealth = 150,
            minDamage = 8,
            maxDamage = 0,
            randomDamageModifier = 20,
            monsterExperience = 50,
            monsterGold = 0
        ),
        Monster(
            name = "Ghost of the Sea",
            maxHealth = 170,
            currentHealth = 170,
            minDamage = 3,
            maxDamage = 0,
            randomDamageModifier = 29,
            monsterExperience = 30,
            monsterGold = 17
        ),
        Monster(
            name = "Monster of the Sea",
            maxHealth = 115,
            currentHealth = 115,
            minDamage = 24,
            maxDamage = 0,
            randomDamageModifier = 3,
            monsterExperience = 38,
            monsterGold = 20
        ),
        Monster(
            name = "Kraken",
            maxHealth = 120,
            currentHealth = 120,
            minDamage = 24,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 50,
            monsterGold = 0
        ),
        Monster(
            name = "Lost Pirate",
            maxHealth = 153,
            currentHealth = 153,
            minDamage = 5,
            maxDamage = 0,
            randomDamageModifier = 21,
            monsterExperience = 66,
            monsterGold = 24
        )
    )

// ------------------------------------------------------------
// ACT 3 BOSS
// ------------------------------------------------------------

    val listOfMonstersBossAct3 = listOf(
        Monster(
            name = "The Devouring Abyss",
            maxHealth = 400,
            currentHealth = 400,
            minDamage = 15,
            maxDamage = 0,
            randomDamageModifier = 30,
            monsterExperience = 100,
            monsterGold = 30
        )
    )

// ------------------------------------------------------------
// ACT 4 WEST
// ------------------------------------------------------------

    val listOfMonstersAct4West = listOf(
        Monster(
            name = "Burning Skeleton",
            maxHealth = 220,
            currentHealth = 210,
            minDamage = 35,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 70,
            monsterGold = 30
        ),
        Monster(
            name = "Burning Lizard",
            maxHealth = 240,
            currentHealth = 230,
            minDamage = 24,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 70,
            monsterGold = 30
        ),
        Monster(
            name = "Magma Frog",
            maxHealth = 210,
            currentHealth = 210,
            minDamage = 12,
            maxDamage = 0,
            randomDamageModifier = 2,
            monsterExperience = 88,
            monsterGold = 30
        ),
        Monster(
            name = "Magma Horror",
            maxHealth = 180,
            currentHealth = 180,
            minDamage = 40,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 20,
            monsterGold = 30
        ),
        Monster(
            name = "Forgotten Prince",
            maxHealth = 195,
            currentHealth = 195,
            minDamage = 20,
            maxDamage = 0,
            randomDamageModifier = 12,
            monsterExperience = 77,
            monsterGold = 30
        ),
        Monster(
            name = "Fire Knight",
            maxHealth = 200,
            currentHealth = 180,
            minDamage = 34,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 66,
            monsterGold = 30
        )
    )

// ------------------------------------------------------------
// ACT 4 DRAGONS
// ------------------------------------------------------------

    val listOfDragonsAct4East = listOf(
        Monster(
            name = "Dragon Mage",
            maxHealth = 240,
            currentHealth = 240,
            minDamage = 36,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 90,
            monsterGold = 44
        ),
        Monster(
            name = "Dragon King",
            maxHealth = 320,
            currentHealth = 320,
            minDamage = 23,
            maxDamage = 0,
            randomDamageModifier = 14,
            monsterExperience = 100,
            monsterGold = 30
        ),
        Monster(
            name = "Silver Dragon",
            maxHealth = 260,
            currentHealth = 260,
            minDamage = 43,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 20,
            monsterGold = 60
        ),
        Monster(
            name = "Dragon Hydra",
            maxHealth = 276,
            currentHealth = 276,
            minDamage = 29,
            maxDamage = 0,
            randomDamageModifier = 8,
            monsterExperience = 79,
            monsterGold = 0
        ),
        Monster(
            name = "Golden Dragon",
            maxHealth = 350,
            currentHealth = 350,
            minDamage = 24,
            maxDamage = 0,
            randomDamageModifier = 5,
            monsterExperience = 20,
            monsterGold = 60
        )
    )

// ------------------------------------------------------------
// ACT 4 DRAGON EGG AREA
// ------------------------------------------------------------

    val listOfDragonEggAct4North = listOf(
        Monster(
            name = "Nest-Watcher Dragon",
            maxHealth = 310,
            currentHealth = 310,
            minDamage = 13,
            maxDamage = 0,
            randomDamageModifier = 50,
            monsterExperience = 60,
            monsterGold = 0
        ),
        Monster(
            name = "Egg-Watcher Dragon",
            maxHealth = 310,
            currentHealth = 310,
            minDamage = 14,
            maxDamage = 0,
            randomDamageModifier = 50,
            monsterExperience = 60,
            monsterGold = 0
        )
    )

// ------------------------------------------------------------
// ACT 5
// ------------------------------------------------------------

    val listOfAct5Monsters = listOf(
        Monster(
            name = "Shadow",
            maxHealth = 470,
            currentHealth = 470,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 66,
            monsterExperience = 80,
            monsterGold = 120
        ),
        Monster(
            name = "Jester",
            maxHealth = 299,
            currentHealth = 299,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 93,
            monsterExperience = 90,
            monsterGold = 20
        ),
        Monster(
            name = "False Light Entity",
            maxHealth = 510,
            currentHealth = 510,
            minDamage = 53,
            maxDamage = 0,
            randomDamageModifier = 0,
            monsterExperience = 150,
            monsterGold = 0
        ),
        Monster(
            name = "Black Angel",
            maxHealth = 360,
            currentHealth = 360,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 39,
            monsterExperience = 120,
            monsterGold = 120
        ),
        Monster(
            name = "Priest",
            maxHealth = 415,
            currentHealth = 415,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 67,
            monsterExperience = 50,
            monsterGold = 180
        ),
        Monster(
            name = "Crow",
            maxHealth = 320,
            currentHealth = 320,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 89,
            monsterExperience = 77,
            monsterGold = 0
        ),
        Monster(
            name = "Void",
            maxHealth = 470,
            currentHealth = 470,
            minDamage = 32,
            maxDamage = 0,
            randomDamageModifier = 40,
            monsterExperience = 99,
            monsterGold = 160
        ),
        Monster(
            name = "Blood Void",
            maxHealth = 373,
            currentHealth = 340,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 58,
            monsterExperience = 77,
            monsterGold = 0
        ),
        Monster(
            name = "Death Angel",
            maxHealth = 364,
            currentHealth = 364,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 74,
            monsterExperience = 120,
            monsterGold = 133
        ),
        Monster(
            name = "Dark Mage",
            maxHealth = 394,
            currentHealth = 394,
            minDamage = 15,
            maxDamage = 0,
            randomDamageModifier = 45,
            monsterExperience = 150,
            monsterGold = 60
        ),
        Monster(
            name = "Blood Horror",
            maxHealth = 340,
            currentHealth = 310,
            minDamage = 0,
            maxDamage = 0,
            randomDamageModifier = 56,
            monsterExperience = 50,
            monsterGold = 0
        )
    )

// ------------------------------------------------------------
// ACT 5 BOSS
// ------------------------------------------------------------

    val listOfMonstersBossAct5 = listOf(
        Monster(
            name = "Awoken Horror",
            maxHealth = 1346,
            currentHealth = 1346,
            minDamage = 40,
            maxDamage = 0,
            randomDamageModifier = 80,
            monsterExperience = 1000,
            monsterGold = 500
        )
    )

// ------------------------------------------------------------
// ACT 5 OPTIONAL BOSS
// ------------------------------------------------------------

    val listOfOptionalBossAct5 = listOf(
        Monster(
            name = "Ultimate Darkness",
            maxHealth = 9999,
            currentHealth = 9999,
            minDamage = 80,
            maxDamage = 0,
            randomDamageModifier = 85,
            monsterExperience = 9999,
            monsterGold = 999
        )
    )

}
