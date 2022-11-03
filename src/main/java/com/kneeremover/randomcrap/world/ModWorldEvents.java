package com.kneeremover.randomcrap.world;

import com.kneeremover.randomcrap.world.gen.ModOreGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.kneeremover.randomcrap.util.crapLib.modid;

@Mod.EventBusSubscriber(modid = modid)
public class ModWorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModOreGeneration.generatoeOres(event);
    }
}
