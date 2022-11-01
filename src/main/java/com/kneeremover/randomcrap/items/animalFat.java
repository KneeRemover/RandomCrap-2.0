package com.kneeremover.randomcrap.items;


import com.kneeremover.randomcrap.registers.blockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = "randomcrap", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class animalFat extends Item {
    public animalFat(Properties properties) {
        super(properties);
    }

    public static HashMap<RegistryKey, ServerWorld> dimensions = new HashMap<>();

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos pos = context.getPos();
            if (serverWorld.getBlockState(pos).getBlock() == Blocks.CAULDRON.getBlock()) {
                serverWorld.setBlockState(pos, blockRegister.OIL_CAULDRON.get().getDefaultState());
                stack.setCount(stack.getCount() - 1);
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}