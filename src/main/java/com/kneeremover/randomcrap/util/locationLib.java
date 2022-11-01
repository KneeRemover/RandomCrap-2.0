package com.kneeremover.randomcrap.util;

import net.minecraft.util.ResourceLocation;

import static com.kneeremover.randomcrap.RandomCrap.modid;

public class locationLib {
    public static ResourceLocation append(String string) {
        return new ResourceLocation(modid, string);
    }
}
