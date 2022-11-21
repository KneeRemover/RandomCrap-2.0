package com.kneeremover.randomcrap.items;


import com.kneeremover.randomcrap.multiblocks.enchanter;
import com.kneeremover.randomcrap.registers.itemRegister;
import com.kneeremover.randomcrap.util.crapLib;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class rubyCompound extends Item {
    public rubyCompound(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos pos = context.getClickedPos();
            if (serverWorld.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE.getBlock() && enchanter.test(context.getLevel(), context.getClickedPos(), context.getHand())) {
                PlayerEntity player = context.getPlayer();
                ItemStack stack2 = crapLib.itemInstance(itemRegister.ENERGISED_STONE);
                stack2.setCount(stack.getCount());
                player.addItem(stack2);
                stack.setCount(0);
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}