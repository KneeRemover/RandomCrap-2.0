package com.kneeremover.randomcrap.items.kateBucket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.kneeremover.randomcrap.RandomCrap.tools;

public class item extends Item {

    private static final int STACK_SIZE = 1;

    public item(Properties properties) {
        super(properties.maxStackSize(STACK_SIZE) // the item will appear on the Miscellaneous tab in creative
        );
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@NotNull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getOrCreateTag().getBoolean("used")) {
            int current = stack.getOrCreateTag().getInt("current");
            if (player.isSneaking()) {
                if (current < 3) {
                    current++;
                } else {
                    current = 0;
                }
                stack.getOrCreateTag().putInt("current", current);
            } else {
                if (current == 0) refresh(stack, ToolType.PICKAXE, world);
                if (current == 1) refresh(stack, ToolType.AXE, world);
                if (current == 2) refresh(stack, ToolType.SHOVEL, world);
                if (current == 3) refresh(stack, ToolType.HOE, world);

                CompoundNBT nbt = stack.getOrCreateTag();
                int dirtyCounter = nbt.getInt("dirtyCounter");
                nbt.putInt("dirtyCounter", dirtyCounter + 1);
                stack.setTag(nbt);

                if (!world.isRemote) {  // server only!
                    INamedContainerProvider containerProviderBucket = new ContainerProviderBucket(this, stack);
                    final int NUMBER_OF_FLOWER_SLOTS = 16;
                    NetworkHooks.openGui((ServerPlayerEntity) player,
                            containerProviderBucket,
                            (packetBuffer) -> packetBuffer.writeInt(NUMBER_OF_FLOWER_SLOTS));
                }
            }
        } else {
            stack.getOrCreateTag().putBoolean("used", true);
        }
        return ActionResult.resultSuccess(stack);
    }

    private static class ContainerProviderBucket implements INamedContainerProvider {
        public ContainerProviderBucket(item item, ItemStack itemStackBucket) {
            this.itemStackBucket = itemStackBucket;
            this.item = item;
        }

        @Override
        public @NotNull ITextComponent getDisplayName() {
            return itemStackBucket.getDisplayName();
        }

        @Override
        public container createMenu(int windowID, @NotNull PlayerInventory playerInventory, @NotNull PlayerEntity playerEntity) {
            return container.createContainerServerSide(windowID, playerInventory,
                    getItemStackHandler(itemStackBucket),
                    itemStackBucket);
        }

        private final item item;
        private final ItemStack itemStackBucket;
    }

    // ---------------- Code related to Capabilities
    //

    // The CapabilityProvider returned from this method is used to specify which capabilities the ItemBucket possesses
    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {

        return new capabilityProvider();
    }

    private static itemStackHandler getItemStackHandler(ItemStack itemStack) {
        @SuppressWarnings("ConstantConditions") IItemHandler itemStackHandler = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (!(itemStackHandler instanceof itemStackHandler)) {
            LOGGER.error("ItemBucket did not have the expected ITEM_HANDLER_CAPABILITY");
            return new itemStackHandler(1);
        }
        return (itemStackHandler) itemStackHandler;
    }

    private final String BASE_NBT_TAG = "base";
    private final String CAPABILITY_NBT_TAG = "cap";

    // Refresh the inventory with whatever item type it needs
    public static List<Item> bucketTools;

    public static void refresh(ItemStack stack, ToolType toolType, World world) {
        if (!world.isRemote) {     // Only server side, of course.
            bucketTools = new ArrayList<>();    // Reset the tools container
            for (Item item : tools) {           // Cycle through all the tools
                if (item.getToolTypes(item.getItem().getDefaultInstance()).contains(toolType))
                    bucketTools.add(item);   // Match to tooltype
            }

            itemStackHandler itemStackHandler = item.getItemStackHandler(stack);
            for (int i = 0; i < 54; i++) {
                itemStackHandler.setStackInSlot(i, Items.AIR.getDefaultInstance());
            } // Reset the inventory

            for (int i = 0; i < bucketTools.size(); i++) {
                if (i < 54) {
                    itemStackHandler.setStackInSlot(i, bucketTools.get(i).getDefaultInstance());
                }
            } // Add all the tools
        }
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();
        itemStackHandler itemStackHandler = getItemStackHandler(stack);
        CompoundNBT capabilityTag = itemStackHandler.serializeNBT();
        CompoundNBT combinedTag = new CompoundNBT();
        if (baseTag != null) {
            combinedTag.put(BASE_NBT_TAG, baseTag);
        }
        if (capabilityTag != null) {
            combinedTag.put(CAPABILITY_NBT_TAG, capabilityTag);
        }
        return combinedTag;
    }

    /**
     * Retrieve our capability information from the transmitted NBT information
     *
     * @param stack The stack that received NBT
     * @param nbt   Received NBT, can be null
     */
    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundNBT baseTag = nbt.getCompound(BASE_NBT_TAG);              // empty if not found
        CompoundNBT capabilityTag = nbt.getCompound(CAPABILITY_NBT_TAG); // empty if not found
        stack.setTag(baseTag);
        itemStackHandler itemStackHandler = getItemStackHandler(stack);
        itemStackHandler.deserializeNBT(capabilityTag);
    }

    private static final Logger LOGGER = LogManager.getLogger();
}