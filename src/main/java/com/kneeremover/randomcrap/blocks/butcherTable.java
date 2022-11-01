package com.kneeremover.randomcrap.blocks;

import com.kneeremover.randomcrap.RandomCrap;
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

public class butcherTable extends Block {
    public butcherTable(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && player.getHeldItemMainhand().getItem() == Items.BEEF) {
                player.addItemStackToInventory(itemRegister.ANIMAL_FAT.get().getDefaultInstance());
                player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        if (!worldIn.isRemote && player.getHeldItemMainhand().getItem() == Items.BEEF) {
            ItemStack animalFat = itemRegister.ANIMAL_FAT.get().getDefaultInstance();
            animalFat.setCount(player.getHeldItemMainhand().getCount());
            player.addItemStackToInventory(animalFat);
            player.getHeldItemMainhand().setCount(0);
        }
    }
}
