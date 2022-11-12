package com.kneeremover.randomcrap.items.kateBucket;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * The ContainerBucket is used to manipulate the contents of the Bucket (ItemStackHandlerBucket).
 * The master copy is on the server side, with a "dummy" copy stored on the client side
 * The GUI (ContainerScreen) on the client side interacts with the dummy copy.
 * Vanilla ensures that the server and client copies remain synchronised.
 */

public class container extends Container {

    public static container createContainerServerSide(int windowID, PlayerInventory playerInventory, itemStackHandler bucketContents,
                                                      ItemStack Bucket) {
        return new container(windowID, playerInventory, bucketContents, Bucket);
    }

    public static container createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        // for this example we use extraData for the server to tell the client how many slots for  itemstacks the  bucket contains.
        int numberOfSlots = 54;

        try {
            itemStackHandler itemStackHandler = new itemStackHandler(numberOfSlots);

            // on the client side there is no parent ItemStack to communicate with - we use a dummy inventory
            return new container(windowID, playerInventory, itemStackHandler, ItemStack.EMPTY);
        } catch (IllegalArgumentException iae) {
            LOGGER.warn(iae);
        }
        return null;
    }

    private final itemStackHandler itemStackHandler;
    private final ItemStack itemStackBeingHeld;
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 51 = TileInventory slots, which map to our bucket slot numbers 0 - 15)

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int BUCKET_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int MAX_EXPECTED_BUCKET_SLOT_COUNT = 54;

    public static final int BUCKET_INVENTORY_YPOS = -38;  // the ContainerScreenBucket needs to know these so it can tell where to draw the Titles
    public static final int PLAYER_INVENTORY_YPOS = 84;

    /**
     * Creates a container suitable for server side or client side
     * @param windowId ID of the container
     * @param playerInv the inventory of the player
     * @param itemStackHandler the inventory stored in the bucket
     */
    private container(int windowId, PlayerInventory playerInv,
                      itemStackHandler itemStackHandler,
                      ItemStack itemStackBeingHeld) {
        super(startupCommon.containerType, windowId);
        this.itemStackHandler = itemStackHandler;
        this.itemStackBeingHeld = itemStackBeingHeld;
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int HOTBAR_XPOS = 8;
        final int HOTBAR_YPOS = 142;
        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new Slot(playerInv, x, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        final int PLAYER_INVENTORY_XPOS = 8;
        // Add the rest of the player's inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new Slot(playerInv, slotNumber, xpos, ypos));
            }
        }

        int bucketSlotCount = itemStackHandler.getSlots();
        if (bucketSlotCount < 1 || bucketSlotCount > MAX_EXPECTED_BUCKET_SLOT_COUNT) {
            LOGGER.warn("Unexpected invalid slot count in ItemStackHandlerBucket(" + bucketSlotCount + ")");
            bucketSlotCount = MathHelper.clamp(bucketSlotCount, 1, MAX_EXPECTED_BUCKET_SLOT_COUNT);
        }

        final int BUCKET_SLOTS_PER_ROW = 9;
        final int BUCKET_INVENTORY_XPOS = 8;
        // Add the tile inventory container to the gui
        for (int bucketSlot = 0; bucketSlot < bucketSlotCount; ++bucketSlot) {
            int bucketRow = bucketSlot / BUCKET_SLOTS_PER_ROW;
            int bucketCol = bucketSlot % BUCKET_SLOTS_PER_ROW;
            int xpos = BUCKET_INVENTORY_XPOS + SLOT_X_SPACING * bucketCol;
            int ypos = BUCKET_INVENTORY_YPOS + SLOT_Y_SPACING * bucketRow;
            addSlot(new SlotItemHandler(itemStackHandler, bucketSlot, xpos, ypos));
        }
    }

    // Check if the player is still able to access the container
    // In this case - if the player stops holding the bucket, return false
    // Called on the server side only.
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {

        ItemStack main = player.getHeldItemMainhand();
        ItemStack off = player.getHeldItemOffhand();
        return (!main.isEmpty() && main == itemStackBeingHeld) ||
                (!off.isEmpty() && off == itemStackBeingHeld);
    }

    // This is where you specify what happens when a player shift clicks a slot in the gui
    //  (when you shift click a slot in the Bucket Inventory, it moves it to the first available position in the hotbar and/or
    //    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
    //    position in the Bucket inventory)
    // At the very least you must override this and return ItemStack.EMPTY or the game will crash when the player shift clicks a slot
    // returns ItemStack.EMPTY if the source slot is empty, or if none of the the source slot item could be moved
    //   otherwise, returns a copy of the source stack
    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@NotNull PlayerEntity player, int sourceSlotIndex) {
        Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();
        final int BUCKET_SLOT_COUNT = itemStackHandler.getSlots();

        // Check if the slot clicked is one of the vanilla container slots
        if (sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the bucket inventory
            if (!mergeItemStack(sourceStack, BUCKET_INVENTORY_FIRST_SLOT_INDEX, BUCKET_INVENTORY_FIRST_SLOT_INDEX + BUCKET_SLOT_COUNT, false)){
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (sourceSlotIndex < BUCKET_INVENTORY_FIRST_SLOT_INDEX + BUCKET_SLOT_COUNT) {
            // This is a bucket slot so merge the stack into the players inventory
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            LOGGER.warn("Invalid slotIndex:" + sourceSlotIndex);
            return ItemStack.EMPTY;
        }

        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    /**
     * Because capability nbt is not actually stored in the ItemStack nbt (it is created fresh each time we need to transmit or save an nbt), detectAndSendChanges
     *   does not work for our ItemBucket ItemStack.  i.e. when the contents of ItemStackHandlerBucket are changed, the nbt of ItemBucket ItemStack don't change,
     *   so it is not sent to the client.
     * For this reason, we need to manually detect when it has changed and mark it dirty.
     * The easiest way is just to set a counter in the nbt tag and let the vanilla code notice that the itemstack has changed.
     * The side effect is that the player's hand moves down and up (because the client thinks it is a new ItemStack) but that's not objectionable.
     * Alternatively you could copy the code from vanilla detectAndSendChanges and tweak it to find the slot for itemStackBeingHeld and send it manually.
     * <p>
     * Of course, if your ItemStack's capability doesn't affect the rendering of the ItemStack, i.e. the Capability is not needed on the client at all, then
     *   you don't need to bother with marking it dirty.
     */
    @Override
    public void detectAndSendChanges() {
        if (itemStackHandler.isDirty()) {
            CompoundNBT nbt = itemStackBeingHeld.getOrCreateTag();
            int dirtyCounter = nbt.getInt("dirtyCounter");
            nbt.putInt("dirtyCounter", dirtyCounter + 1);
            itemStackBeingHeld.setTag(nbt);
        }
        super.detectAndSendChanges();
    }

    private static final Logger LOGGER = LogManager.getLogger();

}
