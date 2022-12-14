package com.kneeremover.randomcrap.items.kateBucket;


import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class provides all the capabilities that the FlowerBag possesses.
 * In this case it only provides one capability, the ITEM_HANDLER_CAPABILITY i.e. ItemStackHandlerFlowerBag, which
 * is used to store flower ItemStacks.
 */

@SuppressWarnings("unchecked")
public class capabilityProvider implements ICapabilitySerializable<INBT> {

	private final Direction NO_SPECIFIC_SIDE = null;

	/**
	 * Asks the Provider if it has the given capability
	 *
	 * @param capability<T> capability to be checked for
	 * @param facing        the side of the provider being checked (null = no particular side)
	 * @param <T>           The interface instance that is used
	 * @return a lazy-initialisation supplier of the interface instance that is used to access this capability
	 */
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability)
			//noinspection unchecked
			return (LazyOptional<T>) (lazyInitialisionSupplier);
		return LazyOptional.empty();
		//  I have written these two lines out in long hand to make it clearer what is going on, but if your Provider only has
		//    one Capability, you can also use the following instead, which does the same thing:
		//    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, lazyInitialisionSupplier);
		// Note that if you are implementing getCapability in a derived class, eg MyEntity, then you should call
		//     return super.getCapability(capability, facing);
		//   instead of returning empty.
	}

	@Override
	public INBT serializeNBT() {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(getCachedInventory(), null);
	}


	@Override
	public void deserializeNBT(INBT nbt) {
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(getCachedInventory(), null, nbt);
	}

	/**
	 * Return a lazily-initialised inventory
	 * i.e. After the class instance has been created, but before the first call to this function, the inventory hasn't been created yet.
	 * At the time of the first call, we create the inventory
	 * For all subsequent calls, we return the previously-created instance.
	 * To be honest, unless your initialisation is very expensive in memory or time, it's probably not worth the effort, i.e. you
	 * could just allocate the itemStackHandlerFlowerBag in your constructor and your lazyInitialisationSupplier could just
	 * return that without a dedicated method to perform a cache check.
	 *
	 * @return the ItemStackHandlerFlowerBag which stores the flowers.
	 */
	private IItemHandler getCachedInventory() {
		if (ItemStackHandler == null) {
			ItemStackHandler = new itemStackHandler(MAX_NUMBER_ITEMS);
		}
		return ItemStackHandler;
	}

	private static final int MAX_NUMBER_ITEMS = 54;

	private itemStackHandler ItemStackHandler;  // initially null until our first call to getCachedInventory

	//  a supplier: when called, returns the result of getCachedInventory()
	private final LazyOptional<IItemHandler> lazyInitialisionSupplier = LazyOptional.of(this::getCachedInventory);
}
