package com.kneeremover.randomcrap.multiblocks;

import com.google.common.base.Suppliers;
import com.kneeremover.randomcrap.registers.blockRegister;
import com.kneeremover.randomcrap.util.tags;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.Supplier;

import static net.minecraft.util.Hand.MAIN_HAND;

public class taterGenerator {
    public static final Supplier<IMultiblock> TATER_CAULDRON = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
            new String[][]{
                    {
                            "_____________",
                            "_B_________B_",
                            "_____________",
                            "_____________",
                            "_____________",
                            "_____________",
                            "______C______",
                            "_____________",
                            "_____________",
                            "_____________",
                            "_____________",
                            "_B_________B_",
                            "_____________",

                    },
                    {
                            "EEE_______EEE",
                            "EEE___B___EEE",
                            "EEE_______EEE",
                            "_____________",
                            "_____RSR_____",
                            "____RRRRR____",
                            "_B__SRFRS__B_",
                            "____RRRRR____",
                            "_____RSR_____",
                            "_____________",
                            "EEE_______EEE",
                            "EEE___B___EEE",
                            "EEE_______EEE",
                    },
                    {
                            "_____III_____",
                            "_____III_____",
                            "_____III_____",
                            "_____________",
                            "_____________",
                            "III_______III",
                            "III___0___III",
                            "III_______III",
                            "_____________",
                            "_____________",
                            "_____III_____",
                            "_____III_____",
                            "_____III_____",
                    }
            },
            'I', Blocks.IRON_BLOCK,
            'E', Blocks.EMERALD_BLOCK,
            'B', Blocks.BEACON,
            'R', blockRegister.RUBY_BLOCK.get(),
            '0', Blocks.NETHERRACK,
            'F', Blocks.FIRE,
            'S', blockRegister.ENERGISED_STONE_BLOCK.get(),
            'C', PatchouliAPI.get().predicateMatcher(Blocks.CAULDRON, blockState -> {
                if (blockState.getBlock().getTags().contains(tags.Blocks.TATER_CAULDRON.getName())) {
                    return true;
                }
                return false;
            })
    ));

    public static void click(PlayerInteractEvent.RightClickBlock event) {
        if (test(event.getWorld(), event.getPos(), event.getHand())) {
            event.getPlayer().sendStatusMessage(new TranslationTextComponent("event.randomcrap.multiblock.valid"), true);
        }
    }
    public static boolean test(World world, BlockPos pos, Hand hand) {
        // Make sure it's server side and only returns once
        if (!world.isRemote && hand == MAIN_HAND) {
            ServerWorld sworld = (ServerWorld) world;
            pos = pos.add(0, -2, 0);
            Rotation rot = TATER_CAULDRON.get().validate(world, pos);
            return rot != null;
        }
        return false;
    }
}
