package com.kneeremover.randomcrap.items;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class leafWings extends Item {
    public leafWings(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ActionResult<ItemStack> use(World world, PlayerEntity player, @NotNull Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            if (!player.abilities.instabuild) {
                stack.shrink(1);
            }
            player.fallDistance = 0;
            player.setDeltaMovement(Vector3d.directionFromRotation(player.getRotationVector()).normalize().multiply(3, 3, 3));
            player.hurtMarked = true;
        }
        return ActionResult.sidedSuccess(stack, world.isClientSide());
    }
}