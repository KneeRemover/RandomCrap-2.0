package com.kneeremover.randomcrap.util.network.message;

import com.kneeremover.randomcrap.items.handheldWaystone;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static com.kneeremover.randomcrap.RandomCrap.LOGGER;

public class leftClick {
    public ItemStack stack;

    //public leftClick () {}

    public leftClick (ItemStack stack) {
        this.stack = stack;
    }
    public static void encode(leftClick message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
    }

    public static leftClick decode(PacketBuffer buffer) {
        return new leftClick(buffer.readItemStack());
    }

    public static void handle(leftClick message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            /*handheldWaystone.leftClickPacket(player, message.stack, Objects.requireNonNull(context.getSender()).world.isRemote);*/
            ItemStack stack = message.stack;
            if (stack.getItem() instanceof handheldWaystone) {
                CompoundNBT nbt = stack.getOrCreateTag();
                int slot = nbt.getInt("slot");

                if (slot == 0) {
                    slot = 1;
                    nbt.putInt("slot", 1);
                }
                if (nbt.getInt("maxSlots") == 0) {
                    nbt.putInt("maxSlots", 1);
                }
                stack.write(nbt); // if 0 make 1

                if (slot >= nbt.getInt("maxSlots")) {
                    LOGGER.info("Looping around because slot is " + slot + " and maxSlots is " + nbt.getInt("maxSlots"));
                    slot = 1;
                    nbt.putInt("slot", 1);
                } else {
                    LOGGER.info("Incrementing slots from " + slot + " to " + (slot + 1));
                    slot++;
                    nbt.putInt("slot", slot);
                }
                stack.write(nbt);
                assert player != null;
                ((PlayerEntity) player).sendStatusMessage(new StringTextComponent(new TranslationTextComponent("item.randomcrap.handheldWaystone.slot").getString() + slot), true);
                player.setHeldItem(Hand.MAIN_HAND, stack);
            }
        });
        context.setPacketHandled(true);
    }
}
