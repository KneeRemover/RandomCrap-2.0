package com.kneeremover.randomcrap.multiblocks;

import com.google.common.base.Suppliers;
import com.kneeremover.randomcrap.registers.blockRegister;
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

public class enchanter {
	public static final Supplier<IMultiblock> STONE_ENERGISER = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			new String[][]{
					{
							"_____",
							"__S__",
							"_SSS_",
							"__S__",
							"_____",
					},
					{
							"__R__",
							"_____",
							"R_S_R",
							"_____",
							"__R__",
					},
					{
							"__F__",
							"_____",
							"F_H_F",
							"_____",
							"__F__",
					},
					{
							"__B__",
							"_____",
							"B_0_B",
							"_____",
							"__B__",
					},
					{
							"__R__",
							"_____",
							"R_E_R",
							"_____",
							"__R__",
					}
			},
			'R', blockRegister.RUBY_BLOCK.get(),
			'E', Blocks.ENCHANTING_TABLE.getBlock(),
			'B', blockRegister.RUBY_BOOKSHELF.get(),
			'F', Blocks.OAK_FENCE.getBlock(),
			'H', blockRegister.BLUE_HOPPER.get(),
			'S', Blocks.STONE.getBlock()
	));

	@SubscribeEvent
	public static void click(PlayerInteractEvent.RightClickBlock event) {
		if (test(event.getWorld(), event.getPos(), event.getHand())) {
			event.getPlayer().displayClientMessage(new TranslationTextComponent("event.randomcrap.multiblock.valid"), true);
		}
	}

	public static boolean test(World world, BlockPos pos, Hand hand) {
		// Make sure it's server side and only returns once
		if (!world.isClientSide && hand == MAIN_HAND) {
			pos = pos.offset(0, 1, 0);
			Rotation rot = STONE_ENERGISER.get().validate(world, pos);
			return rot != null;
		}
		return false;
	}
}
