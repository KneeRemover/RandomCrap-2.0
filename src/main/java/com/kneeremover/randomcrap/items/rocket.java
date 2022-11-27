/*
package com.kneeremover.randomcrap.items;

import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static java.lang.Math.abs;

@Mod.EventBusSubscriber
public class rocket extends Item {
	public rocket(Properties properties) {
		super(properties);
	}

	// going on a trip
	@Override
	public @NotNull ActionResult<ItemStack> use(@NotNull World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getMainHandItem();
		if (!world.isClientSide) {
			if (!player.getCooldowns().isOnCooldown(itemRegister.ROCKET_SHIP.get())) {
				player.getCooldowns().addCooldown(itemRegister.ROCKET_SHIP.get(), 200);
				CompoundNBT nbt = stack.getOrCreateTag();
				nbt.putBoolean("isBoosting", true);
				nbt.putLong("age", world.getGameTime());
				stack.setTag(nbt);
			}
		}
		return ActionResult.success(stack);
	}

	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event) {
		if (event.player.getMainHandItem().getItem() == itemRegister.ROCKET_SHIP.get()) {
			CompoundNBT nbt = event.player.getMainHandItem().getOrCreateTag();
			if (nbt.getBoolean("isBoosting")) {
				if (!event.player.horizontalCollision && !event.player.verticalCollision && abs(nbt.getLong("age") - event.player.level.getGameTime()) <= 3) {
					event.player.setDeltaMovement(Vector3d.directionFromRotation(event.player.getRotationVector()).normalize());
					nbt.putLong("age", event.player.level.getGameTime());
				} else {
					event.player.getMainHandItem().shrink(1);
					nbt.putBoolean("isBoosting", false);
					event.player.getMainHandItem().setTag(nbt);
				}
			}
		}
	}
}*/
