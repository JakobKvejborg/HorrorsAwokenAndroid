package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horrorsawokenandroid.R
import com.example.horrorsawokenandroid.game.model.Items
import com.example.horrorsawokenandroid.game.model.ReforgeFrogStat
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun ReforgeFrogScreen(
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val player = state.player

    var selectedItem by remember { mutableStateOf<Items.Item?>(null) }
    var selectedStat by remember {
        mutableStateOf<ReforgeFrogStat.ReforgeableStat?>(null)
    }

    fun getStatValue(
        item: Items.Item,
        stat: ReforgeFrogStat.ReforgeableStat
    ): Int {
        return when (stat) {
            ReforgeFrogStat.ReforgeableStat.Health -> item.health
            ReforgeFrogStat.ReforgeableStat.Damage -> item.damage
            ReforgeFrogStat.ReforgeableStat.DodgeChance -> item.dodgeChance
            ReforgeFrogStat.ReforgeableStat.Strength -> item.strength
            ReforgeFrogStat.ReforgeableStat.Armor -> item.armor
            ReforgeFrogStat.ReforgeableStat.Lifesteal -> item.lifesteal
            ReforgeFrogStat.ReforgeableStat.Regeneration -> item.regeneration
            ReforgeFrogStat.ReforgeableStat.CritChance -> item.critChance
            ReforgeFrogStat.ReforgeableStat.CritDamage -> item.critDamage
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { }
            .pointerInput(Unit) {
                detectTapGestures {
                    selectedItem = null
                }
            },
    ) {

        // BACKGROUND
        Image(
            painter = painterResource(
                if (state.act2OptionalBossDefeated) {
                    R.drawable.act3frogwithlily
                } else {
                    R.drawable.act3froggy
                }
            ),
            contentDescription = "Frog",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .fillMaxHeight(0.92f)
                .align(Alignment.Center)
                .background(
                    Color(0xFF16181C).copy(alpha = 0.15f),
                    RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        ) {

            Spacer(Modifier.height(22.dp)) // top spacer

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FROG'S REFORGE",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

            }

            Spacer(Modifier.height(8.dp))

            // EQUIPPED
            Text(
                text = "EQUIPPED",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(262.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // TOP ROW
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Helmet],
                            iconRes = R.drawable.helmeticon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Helmet] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Helmet],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Helmet]
                                selectedStat = null
                            }
                        )

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Amulet],
                            iconRes = R.drawable.amuleticon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Amulet] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Amulet],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Amulet]
                                selectedStat = null
                            }
                        )

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Shoulders],
                            iconRes = R.drawable.shouldersicon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Shoulders] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Shoulders],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Shoulders]
                                selectedStat = null
                            }
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // MIDDLE ROW
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Hook],
                            iconRes = R.drawable.hookicon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Hook] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Hook],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Hook]
                                selectedStat = null
                            }
                        )

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Armor],
                            iconRes = R.drawable.armoricon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Armor] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Armor],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Armor]
                                selectedStat = null
                            }
                        )

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Weapon],
                            iconRes = R.drawable.swordicon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Weapon] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Weapon],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Weapon]
                                selectedStat = null
                            }
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // BOTTOM ROW
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Boots],
                            iconRes = R.drawable.bootsicon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Boots] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Boots],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Boots]
                                selectedStat = null
                            }
                        )

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Leggings],
                            iconRes = R.drawable.leggingsicon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Leggings] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Leggings],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Leggings]
                                selectedStat = null
                            }
                        )

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Gloves],
                            iconRes = R.drawable.glovesicon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Gloves] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Gloves],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Gloves]
                                selectedStat = null
                            }
                        )

                        FrogEquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Belt],
                            iconRes = R.drawable.belticon,
                            isSelected =
                                player.equippedItems[Items.ItemType.Belt] != null &&
                                        selectedItem == player.equippedItems[Items.ItemType.Belt],
                            onClick = {
                                selectedItem =
                                    player.equippedItems[Items.ItemType.Belt]
                                selectedStat = null
                            }
                        )
                    }
                }

                // ITEM INFO
                if (selectedItem != null) {
                    ItemInfoPanel(
                        item = selectedItem!!,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // SELECTED ITEM TO BE REFORGED 3x3 GRID
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // the item stats to be reforged box
            ) {
                if (selectedItem != null) {

                    val item = selectedItem!!

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // ITEM + STAT GRID
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.Black.copy(alpha = 0.42f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(9.dp)
                        ) {

                            Column {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.name,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (item.isItemReforged) {
                                        Text(
                                            text = "REFORGED",
                                            color = Color(0xFFFFD700),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(Modifier.height(7.dp))

                                // 3 x 3 STAT GRID
                                ReforgeFrogStat.ReforgeableStat.entries
                                    .chunked(3)
                                    .forEach { rowStats ->

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {

                                            rowStats.forEach { stat ->

                                                val value = getStatValue(item, stat)
                                                val canSelect = value > 0
                                                val isSelected = selectedStat == stat

                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .background(
                                                            when {
                                                                isSelected ->
                                                                    Color(0xFFFFD700)
                                                                        .copy(alpha = 0.18f)

                                                                canSelect ->
                                                                    Color.White
                                                                        .copy(alpha = 0.05f)

                                                                else ->
                                                                    Color.Black
                                                                        .copy(alpha = 0.2f)
                                                            },
                                                            RoundedCornerShape(6.dp)
                                                        )
                                                        .border(
                                                            width = if (isSelected) 1.dp else 0.dp,
                                                            color = if (isSelected)
                                                                Color(0xFFFFD700)
                                                            else
                                                                Color.Transparent,
                                                            shape = RoundedCornerShape(6.dp)
                                                        )
                                                        .clickable(enabled = canSelect) {
                                                            selectedStat = stat
                                                        }
                                                        .padding(
                                                            horizontal = 5.dp,
                                                            vertical = 6.dp
                                                        )
                                                ) {
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text(
                                                            text = stat.name,
                                                            color = if (canSelect)
                                                                Color.White
                                                            else
                                                                Color.Gray,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )

                                                        Text(
                                                            text = value.toString(),
                                                            color = if (canSelect)
                                                                Color(0xFFFFD700)
                                                            else
                                                                Color.Gray,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }

                                            repeat(3 - rowStats.size) {
                                                Spacer(Modifier.weight(1f))
                                            }
                                        }

                                        Spacer(Modifier.height(4.dp))
                                    }
                            }
                        }

                        Spacer(Modifier.height(25.dp))

                        Text(
                            text = when {
                                item.isItemReforged ->
                                    "This item has already been reforged."

                                else ->
                                    "Select a stat to reforge."
                            },
                            color = Color.Gray,
                            fontSize = 11.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Select an equipped item",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // REFORGE BUTTON
            Button(
                onClick = {
                    val item = selectedItem ?: return@Button
                    val stat = selectedStat ?: return@Button

                    val reforgedItem = viewModel.reforgeFrogStat.reforgeItem(
                        item = item,
                        stat = stat
                    )

                    if (reforgedItem != null) {
                        selectedItem = reforgedItem
                        selectedStat = null
                    }
                },
                enabled =
                    selectedItem != null &&
                            selectedStat != null &&
                            !selectedItem!!.isItemReforged &&
                            player.goldInPocket >=
                            viewModel.reforgeFrogStat.priceToReforgeFrog,
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("REFORGE ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFFFD700)
                            )
                        ) {
                            append("${viewModel.reforgeFrogStat.priceToReforgeFrog}G")
                        }
                    }
                )
            }

            Spacer(Modifier.weight(1f))

            // CLOSE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Text(
                    text = "BACK TO OPEN WATERS ->",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.White.copy(alpha = 0.60f),
                            offset = Offset(0f, 0f),
                            blurRadius = 8f
                        )
                    ),
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clickable {
                            onClose()
                        }
                )
            }
        }
    }
}

@Composable
private fun FrogEquipmentSlot(
    item: Items.Item?,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(76.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected)
                    Color(0xFFFFD700)
                else
                    Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = item != null) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = item?.name ?: "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alpha = if (item == null) 0.35f else 1f
        )
    }
}