package com.kneeremover.randomcrap.blocks;

import com.kneeremover.randomcrap.multiblocks.taterGenerator;
import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.kneeremover.randomcrap.util.crapLib.itemInstance;
import static com.kneeremover.randomcrap.util.crapLib.takeOne;

@SuppressWarnings("deprecation")
public class oilCauldron extends Block {
	public oilCauldron(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull ActionResultType use(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand handIn, @NotNull BlockRayTraceResult hit) {
		if (!worldIn.isClientSide) {
			if (player.getMainHandItem().getItem().asItem() == Items.POTATO && taterGenerator.test(worldIn, pos, handIn)) {
				takeOne((ServerPlayerEntity) player);
				player.addItem(itemInstance(itemRegister.TATER_TOTS));
				worldIn.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
			}
		}
		return ActionResultType.SUCCESS;
	}
}
