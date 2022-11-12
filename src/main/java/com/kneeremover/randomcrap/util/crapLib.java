package com.kneeremover.randomcrap.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class crapLib {
    public static final String modid = "randomcrap";

    public static @NotNull ResourceLocation append(String string) {
        return new ResourceLocation(modid, string);
    }
    public static @NotNull ItemStack itemInstance(@NotNull RegistryObject<Item> rItem) {
        return rItem.get().getDefaultInstance();
    }
    public static @NotNull BlockState blockInstance(@NotNull RegistryObject<Block> rBlock) {
        return rBlock.get().getDefaultState();
    }
    public static void takeOne(@NotNull ServerPlayerEntity player) {
        player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
    }
    public static void takeOneOff(@NotNull ServerPlayerEntity player) {
        player.getHeldItemOffhand().setCount(player.getHeldItemOffhand().getCount() - 1);
    }
}