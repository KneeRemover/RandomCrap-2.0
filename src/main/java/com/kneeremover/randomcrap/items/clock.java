package com.kneeremover.randomcrap.items;


import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class clock extends Item {
	public clock(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull ActionResult<ItemStack> use(@NotNull World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getMainHandItem();
		if (!player.getCooldowns().isOnCooldown(itemRegister.STOPWATCH.get())) {
			try {
				Thread.sleep(5000);
				player.getCooldowns().addCooldown(itemRegister.STOPWATCH.get(), 12000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return ActionResult.success(stack);
	}
}