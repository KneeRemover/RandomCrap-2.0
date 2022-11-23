package com.kneeremover.randomcrap.util.network;

import com.kneeremover.randomcrap.util.network.message.leftClick;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class main {
	public static final String NETWORK_VERSION = "0.1.0";

	public static final SimpleChannel CHANNEL = NetworkRegistry
			.newSimpleChannel(new ResourceLocation("randomcrap", "network"), () -> NETWORK_VERSION, version -> version.equals(NETWORK_VERSION),
					version -> version.equals(NETWORK_VERSION));

	public static void init() {
		CHANNEL.registerMessage(0, leftClick.class, leftClick::encode, leftClick::decode, leftClick::handle);
	}
}
