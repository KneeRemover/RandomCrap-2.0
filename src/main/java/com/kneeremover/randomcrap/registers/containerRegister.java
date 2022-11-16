package com.kneeremover.randomcrap.registers;


import com.kneeremover.randomcrap.items.kateBucket.container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class containerRegister
{
    public static final ContainerType<container> kateBucketContainer = IForgeContainerType.create(container::createContainerClientSide);

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        kateBucketContainer.setRegistryName("katebucket_registry_name");
        event.getRegistry().register(kateBucketContainer);
    }
}
