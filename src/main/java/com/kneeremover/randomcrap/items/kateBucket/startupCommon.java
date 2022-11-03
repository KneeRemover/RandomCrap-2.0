package com.kneeremover.randomcrap.items.kateBucket;


import com.kneeremover.randomcrap.RandomCrap;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 *
 * The methods for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class startupCommon
{
    public static item item;  // this holds the unique instance of your block
    public static ContainerType<container> containerType;

    public static void registerItems(final RegistryEvent.Register<Item> itemRegisterEvent) {
        item = new item(new Item.Properties().group(RandomCrap.TAB));
        item.setRegistryName("mbe32_flower_bag_registry_name");
        itemRegisterEvent.getRegistry().register(item);
    }

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        // not actually required for this example....
    }

    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        containerType = IForgeContainerType.create(container::createContainerClientSide);
        containerType.setRegistryName("mbe32_container_registry_name");
        event.getRegistry().register(containerType);
    }
}
