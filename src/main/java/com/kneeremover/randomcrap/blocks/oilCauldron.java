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

import static com.kneeremover.randomcrap.util.crapLib.itemInstance;
import static com.kneeremover.randomcrap.util.crapLib.takeOne;

public class oilCauldron extends Block {
    public oilCauldron(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            if (player.getHeldItemMainhand().getItem().asItem() == Items.POTATO && taterGenerator.test(worldIn, pos, handIn)) {
                takeOne((ServerPlayerEntity) player);
                player.addItemStackToInventory(itemInstance(itemRegister.TATER_TOTS));
                worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
            }
        }
        return ActionResultType.SUCCESS;
    }
}
