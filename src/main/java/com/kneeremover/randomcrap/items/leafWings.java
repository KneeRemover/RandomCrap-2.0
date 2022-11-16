package com.kneeremover.randomcrap.items;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class leafWings extends Item {
    public leafWings(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            ServerPlayerEntity splayer = (ServerPlayerEntity) player;
            splayer.setDeltaMovement(Vector3d.directionFromRotation(player.getRotationVector()).normalize());
            splayer.getMainHandItem().shrink(1);
        }
        return ActionResult.success(stack);
    }
}