package com.kneeremover.randomcrap.items.kateBucket;


import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

import static com.kneeremover.randomcrap.RandomCrap.tools;

/**
 * Created by TGG on 7/06/2020.
 * <p>
 * Used to store Flower ItemStacks.  Will only accept SMALL_FLOWERS and TALL_FLOWERS
 */
public class itemStackHandler extends ItemStackHandler {

	public static final int MIN_SLOTS = 1;
	public static final int MAX_SLOTS = 54;

	public itemStackHandler(int numberOfSlots) {
		super(MathHelper.clamp(numberOfSlots, MIN_SLOTS, MAX_SLOTS));
		if (numberOfSlots < MIN_SLOTS || numberOfSlots > MAX_SLOTS) {
			throw new IllegalArgumentException("Invalid number of slots:" + numberOfSlots);
		}
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		if (slot < 0 || slot >= MAX_SLOTS) {
			throw new IllegalArgumentException("Invalid slot number:" + slot);
		}
		if (stack.isEmpty()) return false;
		return tools.contains(stack.getItem());
	}

	/**
	 * Count how many empty slots are in the bag
	 *
	 * @return the number of empty slots
	 */
	public int getNumberOfEmptySlots() {
		final int NUMBER_OF_SLOTS = getSlots();

		int emptySlotCount = 0;
		for (int i = 0; i < NUMBER_OF_SLOTS; ++i) {
			if (getStackInSlot(i) == ItemStack.EMPTY) {
				++emptySlotCount;
			}
		}
		return emptySlotCount;
	}

	/**
	 * returns true if the contents have changed since the last call.
	 * Resets to false after each call.
	 *
	 * @return true if changed since the last call
	 */
	public boolean isDirty() {
		boolean currentState = isDirty;
		isDirty = false;
		return currentState;
	}

	/**
	 * Called whenever the contents of the bag have changed.
	 * We need to do this manually in order to make sure that the server sends a synchronisation packet to the client for the parent ItemStack
	 * The reason is because capability information is not stored in the ItemStack nbt tag, so vanilla does not notice when
	 * the flowerbag's capability has changed.
	 */
	protected void onContentsChanged(int slot) {
		// A problem - the ItemStack and the ItemStackHandler don't know which player is holding the flower bag.  Or in fact whether
		//   the bag is being held by any player at all.
		// We have a few choices -
		// * we can search all the players on the server to see which one is holding the bag; or
		// * we can try to store the owner of the ItemStack in the ItemStackHandler, ItemStack, or ContainerFlowerBag,
		//   (which becomes problematic if the owner drops the ItemStack, or if there is no container); or
		// * we can mark the bag as dirty and let the containerFlowerBag detect that.
		// I've used the third method because it is easier to code, produces less coupling between classes, and probably more efficient
		// Fortunately, we only need to manually force an update when the player has the container open.  If changes could occur while the
		//   item was discarded (inside an ItemEntity) it would be much trickier.
		isDirty = true;
	}

	private boolean isDirty = true;

}