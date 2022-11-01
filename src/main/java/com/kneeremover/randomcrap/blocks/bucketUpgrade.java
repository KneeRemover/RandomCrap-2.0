package com.kneeremover.randomcrap.blocks;

import com.kneeremover.randomcrap.multiblocks.kateBucket;
import com.kneeremover.randomcrap.multiblocks.taterGenerator;
import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class bucketUpgrade extends Block {
    public bucketUpgrade(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && player.getHeldItemMainhand().getItem() == Items.BUCKET && kateBucket.test(worldIn, pos, handIn)) {
                player.addItemStackToInventory(itemRegister.ANIMAL_FAT.get().getDefaultInstance());
                player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
        }
        return ActionResultType.SUCCESS;
    }
}
