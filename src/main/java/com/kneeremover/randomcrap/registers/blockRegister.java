package com.kneeremover.randomcrap.registers;

import com.kneeremover.randomcrap.RandomCrap;
import com.kneeremover.randomcrap.blocks.butcherTable;
import com.kneeremover.randomcrap.blocks.oilCauldron;
import com.kneeremover.randomcrap.blocks.rubyBookshelf;
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

import static com.kneeremover.randomcrap.util.crapLib.MODID;

public class blockRegister {
	static final AbstractBlock.IPositionPredicate returnFalse = (p_test_1_, p_test_2_, p_test_3_) -> false;

	public static final DeferredRegister<Block> BLOCKS
			= DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

	public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block", () -> new rubyBookshelf(AbstractBlock.Properties.of(Material.STONE).harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(5f)));
	public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore", () -> new Block(AbstractBlock.Properties.of(Material.METAL).harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(2f)));
	public static final RegistryObject<Block> RUBY_BOOKSHELF = registerBlock("ruby_bookshelf", () -> new Block(AbstractBlock.Properties.of(Material.WOOD).harvestLevel(2).harvestTool(ToolType.AXE).strength(1f)));
	public static final RegistryObject<Block> OIL_CAULDRON = registerBlock("oil_cauldron", () -> new oilCauldron(AbstractBlock.Properties.of(Material.METAL).harvestLevel(0).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(2f).noOcclusion()));
	public static final RegistryObject<Block> BUTCHER_TABLE = registerBlock("butcher_table", () -> new butcherTable(AbstractBlock.Properties.of(Material.WOOD).harvestLevel(1).harvestTool(ToolType.AXE).strength(2f)));
	public static final RegistryObject<Block> ENERGISED_STONE_BLOCK = registerBlock("energised_stone_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL).harvestLevel(1).harvestTool(ToolType.AXE).strength(2f)));
	public static final RegistryObject<Block> BLUE_HOPPER = registerBlock("blue_hopper", () -> new Block(AbstractBlock.Properties.of(Material.METAL).harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(2f).noOcclusion().isViewBlocking(returnFalse)));
	public static final RegistryObject<Block> BUCKET_UPGRADE = registerBlock("bucket_upgrade_station", () -> new Block(AbstractBlock.Properties.of(Material.WOOD).harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(2f)));

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
		itemRegister.ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().tab(RandomCrap.TAB)));
	}

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}