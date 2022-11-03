package com.kneeremover.randomcrap.multiblocks;

import com.google.common.base.Suppliers;
import com.kneeremover.randomcrap.registers.blockRegister;
import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.Supplier;

import static com.kneeremover.randomcrap.util.crapLib.*;
import static net.minecraft.util.Hand.MAIN_HAND;

@Mod.EventBusSubscriber(modid = modid)
public class kateBucket {
    public static final Supplier<IMultiblock> BUCKET_UPGRADER = Suppliers.memoize(() -> {
        return PatchouliAPI.get().makeMultiblock(
                new String[][]{
                        {
                                "_____",
                                "_____",
                                "__I__",
                                "_____",
                                "_____"
                        },
                        {
                                "_____",
                                "__R__",
                                "_RRR_",
                                "__R__",
                                "_____"
                        },
                        {
                                "__I__",
                                "_____",
                                "I_B_I",
                                "_____",
                                "__I__"
                        },
                        {
                                "__F__",
                                "_____",
                                "F___F",
                                "_____",
                                "__F__"
                        },
                        {
                                "__F__",
                                "_____",
                                "F_U_F",
                                "_____",
                                "__F__"
                        },
                        {
                                "_RIR_",
                                "RIEIR",
                                "IE0EI",
                                "RIEIR",
                                "_RIR_"
                        }
                },
                'R', blockRegister.RUBY_BLOCK.get(),
                '0', blockRegister.RUBY_BLOCK.get(),
                'I', blockRegister.ENERGISED_STONE_BLOCK.get(),
                'E', Blocks.END_STONE_BRICKS.getBlock(),
                'F', Blocks.CRIMSON_FENCE.getBlock(),
                'U', blockRegister.BUCKET_UPGRADE.get(),
                'B', blockRegister.BLUE_HOPPER.get()
        );
    });

    public static void click(PlayerInteractEvent.RightClickBlock event) {
        if (test(event.getWorld(), event.getPos(), event.getHand()) && !event.getWorld().isRemote) {
            event.getPlayer().sendStatusMessage(new TranslationTextComponent("event.randomcrap.multiblock.valid"), true);
            if (event.getPlayer().getHeldItemMainhand().getItem() == Items.BUCKET) {
                takeOne((ServerPlayerEntity) event.getPlayer());
                event.getPlayer().addItemStackToInventory(itemInstance(itemRegister.KATE_BUCKET));
            }
        }
    }

    public static boolean test(World world, BlockPos pos, Hand hand) {
        // Make sure it's server side and only returns once
        if (!world.isRemote && hand == MAIN_HAND) {
            ServerWorld sworld = (ServerWorld) world;
            pos = pos.add(0, -1, 0);
            Rotation rot = BUCKET_UPGRADER.get().validate(world, pos);
            if (rot != null) {
                return true;
            }
        }
        return false;
    }
}
