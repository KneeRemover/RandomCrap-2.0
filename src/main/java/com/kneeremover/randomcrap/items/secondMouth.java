package com.kneeremover.randomcrap.items;

import com.kneeremover.randomcrap.registers.itemRegister;
import com.kneeremover.randomcrap.util.crapLib;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class secondMouth extends Item {
	public secondMouth(Item.Properties properties) {
		super(properties);
	}

	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event) {
		PlayerInventory inventory = event.player.inventory;
		if (inventory.contains(crapLib.itemInstance(itemRegister.SECOND_MOUTH)) && inventory.getItem(8).getItem().isEdible() && event.player.canEat(false)) {
			event.player.eat(event.player.level, event.player.inventory.getItem(8));
		}
	}
}
