package com.kneeremover.randomcrap.registers;

import com.kneeremover.randomcrap.RandomCrap;
import com.kneeremover.randomcrap.blocks.butcherTable;
import com.kneeremover.randomcrap.blocks.oilCauldron;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.kneeremover.randomcrap.util.crapLib.modid;

public class blockRegister {
    static final AbstractBlock.IPositionPredicate returnFalse = (p_test_1_, p_test_2_, p_test_3_) -> false;

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);

    public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                    .harvestLevel(2).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(5f)));

    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                    .harvestLevel(2).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> OIL_CAULDRON = registerBlock("oil_cauldron",
            () -> new oilCauldron(AbstractBlock.Properties.create(Material.IRON)
                    .harvestLevel(0).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(2f).notSolid()));

    public static final RegistryObject<Block> BUTCHER_TABLE = registerBlock("butcher_table",
            () -> new butcherTable(AbstractBlock.Properties.create(Material.WOOD)
                    .harvestLevel(1).harvestTool(ToolType.AXE).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> ENERGISED_STONE_BLOCK = registerBlock("energised_stone_block",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                    .harvestLevel(1).harvestTool(ToolType.AXE).hardnessAndResistance(2f)));

    public static final RegistryObject<Block> BLUE_HOPPER = registerBlock("blue_hopper",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                    .harvestLevel(2).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(2f).hardnessAndResistance(2f).notSolid().setOpaque(returnFalse)));

    public static final RegistryObject<Block> BUCKET_UPGRADE = registerBlock("bucket_upgrade_station",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                    .harvestLevel(2).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(2f)));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        itemRegister.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().group(RandomCrap.TAB)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}