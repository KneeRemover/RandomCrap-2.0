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
import net.minecraftforge.fml.common.Mod;

import static com.kneeremover.randomcrap.util.crapLib.blockInstance;

public class animalFat extends Item {
    public animalFat(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos pos = context.getPos();
            if (serverWorld.getBlockState(pos).getBlock() == Blocks.CAULDRON.getBlock()) {
                serverWorld.setBlockState(pos, blockInstance(blockRegister.OIL_CAULDRON));
                stack.setCount(stack.getCount() - 1);
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}