package com.kneeremover.randomcrap.items;


import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class basil extends Item {
	public basil(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent("\u00A77It's SOMETHING alright..."));
		// For those of you digging through the code as to what this means, it's a reference... to SOMETHING...

		// Why is this here? \_(:|)_/
	}
}