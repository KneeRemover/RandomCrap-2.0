package com.kneeremover.randomcrap.items;


import com.kneeremover.randomcrap.registers.itemRegister;
import com.kneeremover.randomcrap.util.crapLib;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class playerContainer extends Item {
	public playerContainer(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
		if (worldIn != null && stack.getOrCreateTag().getBoolean("randomcrap.containsPlayer")) {
			tooltip.add(new StringTextComponent("\u00A77Contains: " + Objects.requireNonNull(worldIn.getPlayerByUUID(stack.getOrCreateTag().getUUID("randomcrap.storedPlayer"))).getName().getString()));
		} else {
			tooltip.add(new StringTextComponent("\u00A77Contains: NONE"));
		}
	}

	@Override
	public @NotNull ActionResult<ItemStack> use(@NotNull World world, PlayerEntity player, @NotNull Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundNBT nbt = stack.getOrCreateTag();
		CompoundNBT nbt2 = new CompoundNBT();
		if (!world.isClientSide) {
			Boolean containsPlayer = nbt.getBoolean("randomcrap.containsPlayer");
			int timeSinceLastSave = Math.abs(nbt.getInt("randomcrap.confirmTime") - Objects.requireNonNull(world.getServer()).getTickCount());


			if (!containsPlayer && (nbt.getInt("randomcrap.confirmTime") == 0 || timeSinceLastSave > 100)) {
				// Player has not been stored, and the confirm timer has expired or has not been set, warn them
				nbt.putInt("randomcrap.confirmTime", Objects.requireNonNull(world.getServer()).getTickCount());
				player.displayClientMessage(new TranslationTextComponent("item.randomcrap.player_container.info.saveWarning"), true);
			} else if (!containsPlayer && timeSinceLastSave < 100) {
				// Player has not been stored, and the confirm timer has not expired, store them
				nbt2.putUUID("randomcrap.storedPlayer", player.getUUID());
				nbt2.putBoolean("randomcrap.containsPlayer", true);
				player.displayClientMessage(new TranslationTextComponent("item.randomcrap.player_container.info.saved"), true);

				ItemStack newStack = crapLib.itemInstance(itemRegister.PLAYER_CONTAINER);
				newStack.setTag(nbt2);
				player.addItem(newStack);
				stack.shrink(1);
				return ActionResult.consume(stack);
			} else if (containsPlayer) {
				// Player has been stored, teleport them
				ServerPlayerEntity containedPlayer = Objects.requireNonNull(player.getServer()).getPlayerList().getPlayer(nbt.getUUID("randomcrap.storedPlayer"));
				if (containedPlayer != null) {
					containedPlayer.teleportTo((ServerWorld) world, player.getX(), player.getY(), player.getZ(), 0, 0);
				}

				stack.shrink(1);
				return ActionResult.consume(stack);
			}
		}
		return ActionResult.success(stack);
	}
}