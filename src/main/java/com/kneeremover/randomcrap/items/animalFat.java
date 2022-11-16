package com.kneeremover.randomcrap.items;


import com.kneeremover.randomcrap.registers.blockRegister;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import static com.kneeremover.randomcrap.util.crapLib.blockInstance;

public class animalFat extends Item {
    public animalFat(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos pos = context.getClickedPos();
            if (serverWorld.getBlockState(pos).getBlock() == Blocks.CAULDRON.getBlock()) {
                serverWorld.setBlock(pos, blockInstance(blockRegister.OIL_CAULDRON), 1);
                stack.setCount(stack.getCount() - 1);
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}