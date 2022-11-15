package com.kneeremover.randomcrap.items;

import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static java.lang.Math.abs;

@Mod.EventBusSubscriber
public class rocket extends Item {
    public rocket(Properties properties) {
        super(properties);
    }

    // going on a trip
    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItemMainhand();
        if (!world.isRemote) {
            if (player.getCooldownTracker().getCooldown(itemRegister.ROCKET_SHIP.get(), 1) == 0) {
                player.getCooldownTracker().setCooldown(itemRegister.ROCKET_SHIP.get(), 200);
                CompoundNBT nbt = stack.getOrCreateTag();
                nbt.putBoolean("isBoosting", true);
                nbt.putInt("age", player.ticksExisted);
                stack.write(nbt);
            }
        }
        return ActionResult.resultSuccess(stack);
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.getHeldItemMainhand().getItem() == itemRegister.ROCKET_SHIP.get()) {
            CompoundNBT nbt = event.player.getHeldItemMainhand().getOrCreateTag();
            if (nbt.getBoolean("isBoosting")) {
                if (!event.player.collidedHorizontally && !event.player.collidedVertically && abs(nbt.getInt("age") - event.player.ticksExisted) <= 3) {
                    event.player.setMotion(Vector3d.fromPitchYaw(event.player.getPitchYaw()).normalize());
                    nbt.putInt("age", event.player.ticksExisted);
                } else {
                    event.player.getHeldItemMainhand().setCount(event.player.getHeldItemMainhand().getCount() - 1);
                    nbt.putBoolean("isBoosting", false);
                    event.player.getHeldItemMainhand().write(nbt);
                }
            }
        }
    }
}