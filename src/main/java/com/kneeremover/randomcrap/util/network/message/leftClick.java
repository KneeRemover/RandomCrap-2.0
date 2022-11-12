package com.kneeremover.randomcrap.util.network.message;

import com.kneeremover.randomcrap.items.handheldWaystone;
import com.mojang.brigadier.Message;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class leftClick {
    public ItemStack stack;

    public leftClick () {}

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
            handheldWaystone.leftClick(player, message.stack);
        });
        context.setPacketHandled(true);
    }
}
