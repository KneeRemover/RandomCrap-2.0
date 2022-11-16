package com.kneeremover.randomcrap.util.network.message;

import com.kneeremover.randomcrap.items.handheldWaystone;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class leftClick {

    public leftClick () {

    }
    @SuppressWarnings("EmptyMethod")
    public static void encode(leftClick message, PacketBuffer buffer) {
    }

    public static leftClick decode(PacketBuffer buffer) {
        return new leftClick();
    }

    public static void handle(leftClick message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            /*handheldWaystone.leftClickPacket(player, message.stack, Objects.requireNonNull(context.getSender()).world.isRemote);*/
            ItemStack stack = player.getMainHandItem();
            handheldWaystone.leftClickPacket(stack, player);
        });
        context.setPacketHandled(true);
    }
}
