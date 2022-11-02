package com.kneeremover.randomcrap.blocks;

import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import static com.kneeremover.randomcrap.util.crapLib.itemInstance;
import static com.kneeremover.randomcrap.util.crapLib.takeOne;

public class butcherTable extends Block {
    public butcherTable(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && player.getHeldItemMainhand().getItem() == Items.BEEF) {
                player.addItemStackToInventory(itemRegister.ANIMAL_FAT.get().getDefaultInstance());
                takeOne((ServerPlayerEntity) player);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        if (!worldIn.isRemote && player.getHeldItemMainhand().getItem() == Items.BEEF) {
            ItemStack animalFat = itemInstance(itemRegister.ENERGISED_STONE);
            animalFat.setCount(player.getHeldItemMainhand().getCount());
            player.addItemStackToInventory(animalFat);
            player.getHeldItemMainhand().setCount(0);
        }
    }
}
