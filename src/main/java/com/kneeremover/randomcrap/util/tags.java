package com.kneeremover.randomcrap.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import static com.kneeremover.randomcrap.util.crapLib.modid;

public class tags {

	public static class Blocks {

		public static final Tags.IOptionalNamedTag<Block> TATER_CAULDRON =
				createTag();

		private static Tags.IOptionalNamedTag<Block> createTag() {
			return BlockTags.createOptional(new ResourceLocation(modid, "tater_cauldron"));
		}

		private static Tags.IOptionalNamedTag<Block> createForgeTag(String name) {
			return BlockTags.createOptional(new ResourceLocation("forge", name));
		}
	}

	public static class Items {

		public static final Tags.IOptionalNamedTag<Item> AMETHYST = createForgeTag();

		private static Tags.IOptionalNamedTag<Item> createTag(String name) {
			return ItemTags.createOptional(new ResourceLocation(modid, name));
		}

		private static Tags.IOptionalNamedTag<Item> createForgeTag() {
			return ItemTags.createOptional(new ResourceLocation("forge", "gems/amethyst"));
		}
	}
}
