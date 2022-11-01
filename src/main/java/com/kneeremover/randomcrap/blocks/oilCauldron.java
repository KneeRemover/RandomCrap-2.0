package com.kneeremover.randomcrap.blocks;

import com.kneeremover.randomcrap.multiblocks.taterGenerator;
import com.kneeremover.randomcrap.registers.blockRegister;
import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class oilCauldron extends Block {
    public oilCauldron(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            if (player.getHeldItemMainhand().getItem().asItem() == Items.POTATO && taterGenerator.test(worldIn, pos, handIn)) {
                player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
                player.addItemStackToInventory(itemRegister.TATER_TOTS.get().getDefaultInstance());
                worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
            }
        }
        return ActionResultType.SUCCESS;
    }
}
