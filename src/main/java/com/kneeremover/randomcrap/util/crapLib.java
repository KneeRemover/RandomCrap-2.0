package com.kneeremover.randomcrap.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class crapLib {
	public static final String MODID = "randomcrap";

	public static @NotNull ResourceLocation append(String string) {
		return new ResourceLocation(MODID, string);
	}

	public static @NotNull ItemStack itemInstance(@NotNull RegistryObject<Item> rItem) {
		return rItem.get().getDefaultInstance();
	}

	public static @NotNull BlockState blockInstance(@NotNull RegistryObject<Block> rBlock) {
		return rBlock.get().defaultBlockState();
	}

	public static void takeOne(@NotNull ServerPlayerEntity player) {
		player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
	}

	public static void takeOneOff(@NotNull ServerPlayerEntity player) {
		player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
	}

	public static Vector3d getFacingDir(@NotNull PlayerEntity player) {
		return Vector3d.directionFromRotation(player.getRotationVector()).normalize();
	}
	public static int cyclePlus(int current, int min, int max) {
		if (current >= max) {
			return min;
		} else {
			return current + 1;
		}
	}
	public static int cycleMinus(int current, int min, int max) {
		if (current <= min) {
			return max;
		} else {
			return current - 1;
		}
	}
}
