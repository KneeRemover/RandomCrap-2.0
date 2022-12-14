package com.kneeremover.randomcrap.multiblocks;

import com.google.common.base.Suppliers;
import com.kneeremover.randomcrap.registers.blockRegister;
import com.kneeremover.randomcrap.util.tags;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.Supplier;

import static net.minecraft.util.Hand.MAIN_HAND;

public class taterGenerator {
	public static final Supplier<IMultiblock> TATER_CAULDRON = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			new String[][]{
					{
							"_____________",
							"_B_________B_",
							"_____________",
							"_____________",
							"_____________",
							"_____________",
							"______C______",
							"_____________",
							"_____________",
							"_____________",
							"_____________",
							"_B_________B_",
							"_____________",

					},
					{
							"EEE_______EEE",
							"EEE___B___EEE",
							"EEE_______EEE",
							"_____________",
							"_____RSR_____",
							"____RRRRR____",
							"_B__SRFRS__B_",
							"____RRRRR____",
							"_____RSR_____",
							"_____________",
							"EEE_______EEE",
							"EEE___B___EEE",
							"EEE_______EEE",
					},
					{
							"_____III_____",
							"_____III_____",
							"_____III_____",
							"_____________",
							"_____________",
							"III_______III",
							"III___0___III",
							"III_______III",
							"_____________",
							"_____________",
							"_____III_____",
							"_____III_____",
							"_____III_____",
					}
			},
			'I', Blocks.IRON_BLOCK,
			'E', Blocks.EMERALD_BLOCK,
			'B', Blocks.BEACON,
			'R', blockRegister.RUBY_BLOCK.get(),
			'0', Blocks.NETHERRACK,
			'F', Blocks.FIRE,
			'S', blockRegister.ENERGISED_STONE_BLOCK.get(),
			'C', PatchouliAPI.get().predicateMatcher(Blocks.CAULDRON, blockState -> blockState.getBlock().getTags().contains(tags.Blocks.TATER_CAULDRON.getName()))
	));

	@SubscribeEvent
	public static void click(PlayerInteractEvent.RightClickBlock event) {
		kateBucket.click(event);
		if (test(event.getWorld(), event.getPos(), event.getHand())) {
			event.getPlayer().displayClientMessage(new TranslationTextComponent("event.randomcrap.multiblock.valid"), true);
		}
	}

	public static boolean test(World world, BlockPos pos, Hand hand) {
		// Make sure it's server side and only returns once
		if (!world.isClientSide && hand == MAIN_HAND) {
			pos = pos.offset(0, -2, 0);
			Rotation rot = TATER_CAULDRON.get().validate(world, pos);
			return rot != null;
		}
		return false;
	}
}
