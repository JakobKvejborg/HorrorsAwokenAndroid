package com.example.horrorsawokenandroid.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horrorsawokenandroid.R
import com.example.horrorsawokenandroid.game.model.Items
import com.example.horrorsawokenandroid.game.model.Player
import kotlinx.coroutines.withTimeoutOrNull
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@Composable
fun InventoryOverlay(
    player: Player,
    onClose: () -> Unit,
    onEquipItem: (Items.Item) -> Unit,
    onUnequipItem: (Items.Item) -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var heldItem by remember { mutableStateOf<Items.Item?>(null) }
    var draggedItem by remember { mutableStateOf<Items.Item?>(null) }
    var draggedFromEquipment by remember { mutableStateOf(false) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    var inventoryBounds by remember { mutableStateOf<Rect?>(null) }
    val equipmentBounds = remember { mutableStateMapOf<Items.ItemType, Rect>() }
    var trashBounds by remember { mutableStateOf<Rect?>(null) }
    val magneticDropZone = 47f
    val isOverTrash = trashBounds?.inflate(magneticDropZone)?.contains(dragPosition) == true
    var upgradeItem by remember { mutableStateOf<Items.Item?>(null) } // TODO remove unused code
    var upgradeCount by remember { mutableStateOf(0) }
    var upgradeBoxBounds by remember { mutableStateOf<Rect?>(null) }

    fun openInventoryInfoBox(item: Items.Item) {
        heldItem = item
    }

    fun closeInventoryInfoBox() {
        heldItem = null
    }

    fun startDrag(
        item: Items.Item,
        fromEquipment: Boolean,
        position: Offset
    ) {
        // 1. Hide the item info card instantly when the drag starts
        heldItem = null

        // 2. Lock in the item data
        draggedItem = item
        draggedFromEquipment = fromEquipment

        // 3. Set the initial draw location using the slot's absolute center position
        dragPosition = position

    }

    fun updateDrag(delta: Offset) {
        // Accumulate the finger movement changes to glide smoothly across the layout coordinate plane
        dragPosition = Offset(
            x = dragPosition.x + delta.x,
            y = dragPosition.y + delta.y
        )
    }

    fun finishDrag() {
        val item = draggedItem ?: return

        // Trash can. If the dragged item is positioned over the trash can, deletes/removes the item from inventory
        if (trashBounds?.inflate(magneticDropZone)?.contains(dragPosition) == true) {
            player.inventory.remove(item) // Remove the item from the inventory.

            // Stop dragging.
            draggedItem = null
            heldItem = null
            draggedFromEquipment = false

            return
        }

        val equipmentTarget = equipmentBounds.entries
            .firstOrNull {
                it.value
                    .inflate(magneticDropZone)
                    .contains(dragPosition)
            }
            ?.key

        when { // Inventory -> correct equipment slot - this also checks player strength and level requirement
            !draggedFromEquipment && equipmentTarget == item.type && item.levelRequirement <= player.level && item.strengthRequirement <= player.strength
                -> {
                onEquipItem(item)
            }
            // Equipment -> inventory
            draggedFromEquipment && inventoryBounds?.contains(dragPosition) == true
                -> {
                onUnequipItem(item)
            }
        }

        draggedItem = null
        draggedFromEquipment = false
        heldItem = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.Transparent)
            .padding(10.dp)
            .pointerInput(Unit) {
                detectTapGestures { // What happens when there's pressed outside the inventory screen
//                    // when this is empty it prevents the player from pressing buttons behind the inventory screen
//                    onClose() // Insert this onClose() method to allow the player to close the inventory by pressing almost anywhere
                    closeInventoryInfoBox()
                }
            },
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.98f) // How wide the entire inventory screen is
                .fillMaxHeight(0.90f) // How tall the entire inventory screen is
                .background(
                    color = Color(0xFF16181C).copy(alpha = 0.85f), // This sets the transparency of the inventory background (higher = more transparent)
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        ) {

            // HEADER use this code if you want a header for the inventory
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Text(
//                    text = "", // This is where a header text like "INVENTORY" could be
//                    modifier = Modifier.weight(1f),
//                    color = Color.White,
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Black
//                )
//
//                TextButton(
//                    onClick = onClose
//                ) {
//                    Text(
//                        text = "CLOSE",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//
            Spacer(
                modifier = Modifier.height(19.dp)
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

            // The equipped area has a FIXED height. This prevents the inventory section from moving down when the player looks at an item
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

                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Helmet],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Helmet],
                            iconRes = R.drawable.helmeticon,
                            label = "",
                            slotType = Items.ItemType.Helmet,
                            isDropTarget = draggedItem?.type == Items.ItemType.Helmet,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Helmet] = it
                            },
                            onItemHeld = {
                                it?.let { item ->
                                    openInventoryInfoBox(item)
                                }
                            },
                            onItemReleased = {
                                closeInventoryInfoBox()
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )


                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Amulet],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Amulet],
                            iconRes = R.drawable.amuleticon,
                            label = "",
                            slotType = Items.ItemType.Amulet,
                            isDropTarget = draggedItem?.type == Items.ItemType.Amulet,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Amulet] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )

                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Shoulders],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Shoulders],
                            iconRes = R.drawable.shouldersicon,
                            label = "",
                            slotType = Items.ItemType.Shoulders,
                            isDropTarget = draggedItem?.type == Items.ItemType.Shoulders,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Shoulders] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
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
                            item = player.equippedItems[Items.ItemType.Hook],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Hook],
                            iconRes = R.drawable.hookicon,
                            label = "",
                            slotType = Items.ItemType.Hook,
                            isDropTarget = draggedItem?.type == Items.ItemType.Hook,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Hook] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )

                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Armor],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Armor],
                            iconRes = R.drawable.armoricon,
                            label = "",
                            slotType = Items.ItemType.Armor,
                            isDropTarget = draggedItem?.type == Items.ItemType.Armor,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Armor] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )

                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Weapon],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Weapon],
                            iconRes = R.drawable.swordicon,
                            label = "",
                            slotType = Items.ItemType.Weapon,
                            isDropTarget = draggedItem?.type == Items.ItemType.Weapon,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Weapon] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
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
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Boots],
                            iconRes = R.drawable.bootsicon,
                            label = "",
                            slotType = Items.ItemType.Boots,
                            isDropTarget = draggedItem?.type == Items.ItemType.Boots,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Boots] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )

                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Leggings],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Leggings],
                            iconRes = R.drawable.leggingsicon,
                            label = "",
                            slotType = Items.ItemType.Leggings,
                            isDropTarget = draggedItem?.type == Items.ItemType.Leggings,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Leggings] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )

                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Gloves],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Gloves],
                            iconRes = R.drawable.glovesicon,
                            label = "",
                            slotType = Items.ItemType.Gloves,
                            isDropTarget = draggedItem?.type == Items.ItemType.Gloves,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Gloves] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )

                        EquipmentSlot(
                            item = player.equippedItems[Items.ItemType.Belt],
                            isHeld = heldItem != null && heldItem == player.equippedItems[Items.ItemType.Belt],
                            iconRes = R.drawable.belticon,
                            label = "",
                            slotType = Items.ItemType.Belt,
                            isDropTarget = draggedItem?.type == Items.ItemType.Belt,
                            onBoundsChanged = {
                                equipmentBounds[Items.ItemType.Belt] = it
                            },
                            onItemHeld = {
                                heldItem = it
                            },
                            onItemReleased = {
                                heldItem = null
                            },
                            onDragStart = ::startDrag,
                            onDrag = ::updateDrag,
                            onDragEnd = ::finishDrag,
                            draggedItem = draggedItem,
                        )
                    }
                }

                // This panel overlays the equipped pictures.
                if (heldItem != null) {
                    ItemInfoPanel(
                        item = heldItem!!,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

//            Spacer(
//                modifier = Modifier.height(1.dp)
//            )

            // =====================================================
            // INVENTORY
            // =====================================================
            if (!state.act2SmithOverlayOpen) {
                Text(
                    text = "INVENTORY",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Box( // Inventory box
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .onGloballyPositioned {
                            inventoryBounds = it.boundsInRoot()
                        }
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
                                    isHeld = heldItem == item,
                                    onItemHeld = {
                                        heldItem = it
                                    },
                                    onItemReleased = {
                                        heldItem = null
                                    },
                                    onDragStart = ::startDrag,
                                    onDrag = ::updateDrag,
                                    onDragEnd = ::finishDrag,
                                    isBeingDragged = draggedItem == item
                                )
                            }
                        }
                    }
                }
            }

            // =====================================================
            // UPGRADE AREA (Only while talking to act 2 smith)
            // =====================================================
            if (state.act2SmithOverlayOpen) {

                Spacer(
                    modifier = Modifier.height(40.dp)
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BLACKSMITH",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(36.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally // Centers both the text and the box horizontally
                ) {
                    Text(
                        text = "Drag an equipped item here",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp) // Adds a small gap before the box
                    )

                    Box( // Upgrade item box
                        modifier = Modifier
                            .size(86.dp)
                            .onGloballyPositioned {
                                upgradeBoxBounds = it.boundsInRoot()
                            }
                            .background(
                                Color.Black.copy(alpha = 0.45f),
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color.Gray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Render the item icon if one is placed inside
                        if (upgradeItem != null) {
                            val icon = getItemTypeIcon(upgradeItem!!.type)

                            if (icon != null) {
                                Image(
                                    painter = painterResource(id = icon),
                                    contentDescription = upgradeItem!!.name,
                                    modifier = Modifier.size(120.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // UPGRADE BUTTON

                    Button(
                        onClick = {
                            upgradeCount++ // Each upgrade increases the next upgrade cost by 25 gold.
                        },
                        enabled = upgradeItem != null,
                        shape = androidx.compose.ui.graphics.RectangleShape
                    ) {
                        Text(
                            text = if (upgradeItem == null) {
                                "UPGRADE"
                            } else {
                                "UPGRADE ${upgradeItem!!.costToUpgradeItem + (upgradeCount * 25)}G"
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

            }

            // =====================================================
            // FOOTER / CLOSE BUTTON + TRASH CAN
            // =====================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp), // Sets the total height of the footer
                verticalAlignment = Alignment.CenterVertically
            ) {

                // TRASH CAN - BOTTOM LEFT
                if (!state.act2SmithOverlayOpen) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .offset(x = 10.dp)
                            .onGloballyPositioned {
                                trashBounds = it.boundsInRoot()
                            }
                            .pointerInput(Unit) {
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image( // trash can image
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Trash",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Pushes CLOSE all the way to the right
                Spacer(
                    modifier = Modifier.weight(1f)
                )

                // CLOSE BUTTON - BOTTOM RIGHT
                Text(
                    text = "",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )

                TextButton(
                    onClick = onClose,
                    contentPadding = PaddingValues(vertical = 2.dp) // Shrinks the button height

                ) {
                    Text(
                        text = "CLOSE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color.White.copy(alpha = 0.60f), // Soft, light white opacity
                                offset = Offset(0f, 0f), // Keeps the glow centered around the text
                                blurRadius = 8f // Higher number = softer, wider glow spreading outward
                            )
                        )
                    )
                }
            }

        }

        val itemBeingDragged = draggedItem
        if (itemBeingDragged != null) {
            val icon = getItemTypeIcon(draggedItem!!.type)

            if (icon != null) {
                // Force this wrapper box to fill the entire screen layout area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(99f) // Keep it pushed completely above all screen text/panels
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .offset {
                                IntOffset(
                                    // 2. Subtract 36.dp to pull the exact center point right under your finger
                                    x = dragPosition.x.roundToInt() - 36.dp.roundToPx(),
                                    y = dragPosition.y.roundToInt() - 36.dp.roundToPx()
                                )
                            }
                    ) {
                        Image( // The image of the dragged item
                            painter = painterResource(id = icon),
                            contentDescription = draggedItem!!.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        // Red overlay on the item if the item is held over the trash item image box, or the level/strength requirement of an item is higher than the player level
                        if (player.level < itemBeingDragged.levelRequirement || isOverTrash && !draggedFromEquipment || player.strength < itemBeingDragged.strengthRequirement) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Red.copy(alpha = 0.25f))
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
internal fun EquipmentSlot(
    item: Items.Item?,
    iconRes: Int,
    label: String,
    isHeld: Boolean,
    slotType: Items.ItemType,
    isDropTarget: Boolean,
    onBoundsChanged: (Rect) -> Unit,
    onItemHeld: (Items.Item?) -> Unit,
    onItemReleased: () -> Unit,
    draggedItem: Items.Item?,
    onDragStart: (Items.Item, Boolean, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {

    var slotBounds by remember {
        mutableStateOf<Rect?>(null)
    }

    Box(
        modifier = Modifier
            .size(76.dp)
            .onGloballyPositioned {
                val bounds = it.boundsInRoot()
                slotBounds = bounds
                onBoundsChanged(bounds)
            }
            .background(
                color = if (isDropTarget) Color(0xFF4CAF50).copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            // Bright glowing green border around the box when the item box is a valid drop target
            .border(
                width = if (isDropTarget) 2.dp else 1.dp,
                color = if (isDropTarget) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (isHeld) 2.dp else 0.dp,
                color = if (isHeld) Color(0xFF4CAF50) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(item) {
                if (item != null) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            slotBounds?.let { bounds ->
                                onDragStart(
                                    item,
                                    true,
                                    bounds.center
                                ) // 'true' because it IS equipment
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            onDrag(dragAmount)
                        },
                        onDragEnd = { onDragEnd() },
                        onDragCancel = { onDragEnd() }
                    )
                }
            }
            .clickable(
                enabled = item != null,
                onClick = { // This opens and closes the item info box when an equipped item is clicked
                    if (isHeld) {
                        onItemReleased()
                    } else {
                        onItemHeld(item)
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alpha = when { // This sets the equipped item image to be transparent when dragged, fully visible when equipped, and transparent when no item is equipped
                item == null -> 0.35f
                item == draggedItem -> 0.35f
                else -> 1f
            }
        )
    }
}


// ================================================================
// ITEM INFO
// ================================================================
@Composable
internal fun ItemInfoPanel(
    item: Items.Item,
    modifier: Modifier = Modifier
) {
    val itemTypeIcon = getItemTypeIcon(item.type)

    Box(
        modifier = modifier
            .background(
                Color.Black.copy(alpha = 0.58f), // This sets the transparency of the item info box
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Item type icon
            if (itemTypeIcon != null) {
                Image(
                    painter = painterResource(id = itemTypeIcon),
                    contentDescription = item.type.name,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = item.name,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
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
    }
}


// ================================================================
// ITEM TYPE ICONS
// ================================================================
internal fun getItemTypeIcon(
    type: Items.ItemType
): Int? {
    return when (type) {
        Items.ItemType.Helmet -> R.drawable.helmeticon
        Items.ItemType.Amulet -> R.drawable.amuleticon
        Items.ItemType.Shoulders -> R.drawable.shouldersicon
        Items.ItemType.Hook -> R.drawable.hookicon
        Items.ItemType.Armor -> R.drawable.armoricon
        Items.ItemType.Weapon -> R.drawable.swordicon
        Items.ItemType.Boots -> R.drawable.bootsicon
        Items.ItemType.Leggings -> R.drawable.leggingsicon
        Items.ItemType.Gloves -> R.drawable.glovesicon
        Items.ItemType.Belt -> R.drawable.belticon
        else -> null
    }
}


// ================================================================
// INVENTORY ROW
// ================================================================
@Composable
internal fun InventoryItemRow(
    item: Items.Item,
    isHeld: Boolean,
    onItemHeld: (Items.Item?) -> Unit,
    onItemReleased: () -> Unit,
    isBeingDragged: Boolean,
    onDragStart: (Items.Item, Boolean, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    var rowBounds by remember {
        mutableStateOf<Rect?>(null)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                rowBounds = it.boundsInRoot()
            }
            .background(
                Color.White.copy(alpha = 0.06f),
                RoundedCornerShape(6.dp)
            )
            .border(
                width = if (isHeld) 2.dp else 0.dp,
                color = if (isHeld) Color(0xFF4CAF50) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(item) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        onItemReleased() // Hide info panel instantly
                        rowBounds?.let { bounds ->
                            onDragStart(
                                item,
                                false,
                                bounds.center
                            ) // false because it is NOT equipment
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() }
                )
            }
            .clickable(
                onClick = {
                    if (isHeld) onItemReleased() else onItemHeld(item) // Open/close the inventory info box by clicking the item
                }
            )
            .padding(
                horizontal = 8.dp,
                vertical = 7.dp
            )
    ) {
        if (!isBeingDragged) {
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
}