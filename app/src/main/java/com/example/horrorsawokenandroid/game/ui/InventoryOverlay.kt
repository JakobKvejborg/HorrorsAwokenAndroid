package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horrorsawokenandroid.R
import com.example.horrorsawokenandroid.game.model.Items
import com.example.horrorsawokenandroid.game.model.Player

@Composable
fun InventoryOverlay(
    player: Player,
    onClose: () -> Unit
) {
    var heldItem by remember {
        mutableStateOf<Items.Item?>(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF16181C).copy(alpha = 0.82f))
            .padding(10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFF16181C).copy(alpha = 0.65f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        ) {

            // =====================================================
            // HEADER
            // =====================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "INVENTORY",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )

                TextButton(
                    onClick = onClose
                ) {
                    Text(
                        text = "CLOSE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // =====================================================
            // EQUIPPED ITEMS
            // =====================================================

            Text(
                text = "EQUIPPED",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            // TOP ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Helmet],
                    iconRes = R.drawable.helmeticon,
                    label = "Helmet",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Amulet],
                    iconRes = R.drawable.amuleticon,
                    label = "Amulet",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Shoulders],
                    iconRes = R.drawable.shouldersicon,
                    label = "Shoulders",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // MIDDLE ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.WeaponLeftHand],
                    iconRes = R.drawable.hookicon,
                    label = "Left Hand",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Armor],
                    iconRes = R.drawable.armoricon,
                    label = "Armor",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.WeaponRightHand],
                    iconRes = R.drawable.swordicon, // TODO sword icon
                    label = "Right Hand",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // BOTTOM ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Boots],
                    iconRes = R.drawable.bootsicon,
                    label = "Boots",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Leggings],
                    iconRes = R.drawable.leggingsicon,
                    label = "Leggings",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Gloves],
                    iconRes = R.drawable.glovesicon,
                    label = "Gloves",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )

                EquipmentSlot(
                    item = player.equippedItems[Items.ItemType.Belt],
                    iconRes = R.drawable.belticon,
                    label = "Belt",
                    onItemHeld = {
                        heldItem = it
                    },
                    onItemReleased = {
                        heldItem = null
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =====================================================
            // HELD ITEM INFORMATION
            // =====================================================
            if (heldItem != null) {

                ItemInfoPanel(
                    item = heldItem!!
                )

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            Color.Black.copy(alpha = 0.45f),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hold an equipped item to inspect it",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =====================================================
            // INVENTORY
            // =====================================================
            Text(
                text = "INVENTORY",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        Color.Black.copy(alpha = 0.45f),
                        RoundedCornerShape(8.dp)
                    )
            ) {

                if (player.inventory.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Inventory is empty",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        items(
                            items = player.inventory
                        ) { item ->

                            InventoryItemRow(
                                item = item,
                                onItemHeld = {
                                    heldItem = it
                                },
                                onItemReleased = {
                                    heldItem = null
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


// ================================================================
// EQUIPMENT SLOT
// ================================================================

@Composable
private fun EquipmentSlot(
    item: Items.Item?,
    iconRes: Int,
    label: String,
    onItemHeld: (Items.Item?) -> Unit,
    onItemReleased: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .pointerInput(item) {

                detectTapGestures(
                    onPress = {

                        onItemHeld(item)

                        try {
                            awaitRelease()
                        } finally {
                            onItemReleased()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alpha = if (item != null) 1f else 0.35f
        )
    }
}


// ================================================================
// ITEM INFO
// ================================================================

@Composable
private fun ItemInfoPanel(
    item: Items.Item
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Black.copy(alpha = 0.65f),
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {

        Text(
            text = item.name,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = item.type.name,
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = item.statText(),
            color = Color.LightGray,
            fontSize = 13.sp,
            lineHeight = 17.sp
        )
    }
}


// ================================================================
// INVENTORY ROW
// ================================================================

@Composable
private fun InventoryItemRow(
    item: Items.Item,
    onItemHeld: (Items.Item?) -> Unit,
    onItemReleased: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White.copy(alpha = 0.06f),
                RoundedCornerShape(6.dp)
            )
            .pointerInput(item) {

                detectTapGestures(
                    onPress = {

                        onItemHeld(item)

                        try {
                            awaitRelease()
                        } finally {
                            onItemReleased()
                        }
                    }
                )
            }
            .padding(
                horizontal = 8.dp,
                vertical = 7.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = item.type.name,
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}