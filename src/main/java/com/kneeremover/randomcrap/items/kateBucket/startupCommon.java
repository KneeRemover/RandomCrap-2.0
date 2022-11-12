package com.kneeremover.randomcrap.items.kateBucket;


import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 *
 * The methods for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class startupCommon
{
    public static item item;  // this holds the unique instance of your block
    public static ContainerType<container> containerType;

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        containerType = IForgeContainerType.create(container::createContainerClientSide);
        containerType.setRegistryName("katebucket_registry_name");
        event.getRegistry().register(containerType);
    }
}
