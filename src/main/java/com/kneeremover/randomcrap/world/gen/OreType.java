package com.kneeremover.randomcrap.world.gen;

import com.kneeremover.randomcrap.registers.blockRegister;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum OreType {

	RUBY(Lazy.of(blockRegister.RUBY_ORE));


	private final Lazy<Block> block;
	private final int maxVeinSize;
	private final int minHeight;
	private final int maxHeight;

	OreType(Lazy<Block> block) {
		this.block = block;
		this.maxVeinSize = 4;
		this.minHeight = 20;
		this.maxHeight = 50;
	}

	public Lazy<Block> getBlock() {
		return block;
	}

	public int getMaxVeinSize() {
		return maxVeinSize;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public static OreType get(Block block) {
		for (OreType ore : values()) {
			if (block == ore.block) {
				return ore;
			}
		}
		return null;
	}
}
