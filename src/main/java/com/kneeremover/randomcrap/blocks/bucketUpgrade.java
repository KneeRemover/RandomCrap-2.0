package com.kneeremover.randomcrap.blocks;

import com.kneeremover.randomcrap.multiblocks.kateBucket;
import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

public class bucketUpgrade extends Block {
    public bucketUpgrade(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ActionResultType onBlockActivated(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand handIn, @NotNull BlockRayTraceResult hit) {
        if (!worldIn.isRemote && player.getHeldItemMainhand().getItem() == Items.BUCKET && kateBucket.test(worldIn, pos, handIn)) {
                player.addItemStackToInventory(itemInstance(itemRegister.ENERGISED_STONE));
                takeOne((ServerPlayerEntity) player);
        }
        return ActionResultType.SUCCESS;
    }
}
