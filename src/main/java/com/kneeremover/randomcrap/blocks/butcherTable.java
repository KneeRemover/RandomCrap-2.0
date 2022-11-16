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
import org.jetbrains.annotations.NotNull;

import static com.kneeremover.randomcrap.util.crapLib.itemInstance;
import static com.kneeremover.randomcrap.util.crapLib.takeOne;

@SuppressWarnings("deprecation")
public class butcherTable extends Block {
    public butcherTable(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ActionResultType use(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand handIn, @NotNull BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && player.getMainHandItem().getItem() == Items.BEEF) {
                player.addItem(itemRegister.ANIMAL_FAT.get().getDefaultInstance());
                takeOne((ServerPlayerEntity) player);
        }
        return ActionResultType.SUCCESS;
    }


    @Override
    public void attack(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull PlayerEntity player) {
        if (!worldIn.isClientSide && player.getMainHandItem().getItem() == Items.BEEF) {
            ItemStack animalFat = itemInstance(itemRegister.ENERGISED_STONE);
            animalFat.setCount(player.getMainHandItem().getCount());
            player.addItem(animalFat);
            player.getMainHandItem().setCount(0);
        }
    }
}
