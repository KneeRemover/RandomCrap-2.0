package com.kneeremover.randomcrap;


import com.kneeremover.randomcrap.items.handheldWaystone;
import com.kneeremover.randomcrap.items.kateBucket.screen;
import com.kneeremover.randomcrap.items.kateBucket.startupCommon;
import com.kneeremover.randomcrap.multiblocks.kateBucket;
import com.kneeremover.randomcrap.multiblocks.taterGenerator;
import com.kneeremover.randomcrap.registers.blockRegister;
import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.patchouli.api.PatchouliAPI;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static com.kneeremover.randomcrap.multiblocks.kateBucket.BUCKET_UPGRADER;
import static com.kneeremover.randomcrap.multiblocks.taterGenerator.TATER_CAULDRON;
import static com.kneeremover.randomcrap.util.crapLib.append;
import static com.kneeremover.randomcrap.util.crapLib.modid;


@Mod(modid)
public class RandomCrap {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String NAME = "Bucket Summoning";
    public static List<Item> tools = new ArrayList<Item>();

    public RandomCrap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        blockRegister.register(eventBus);
        itemRegister.register(eventBus);
        eventBus.register(startupCommon.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PatchouliAPI.get().registerMultiblock(append("tater_cauldron"), TATER_CAULDRON.get());
        PatchouliAPI.get().registerMultiblock(append("kate_bucket"), BUCKET_UPGRADER.get());

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (!item.getToolTypes(item.getItem().getDefaultInstance()).isEmpty()) {
                tools.add(item);
            }
        }
    }

    private void serverSetup(final FMLServerAboutToStartEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(startupCommon.containerType, screen::new);
    }

    public static final ItemGroup TAB = new ItemGroup("crapTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(itemRegister.RUBY.get());
        }
    };

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save evt) {
        handheldWaystone.dimensions = null;
    }

    @SubscribeEvent
    public void RightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        taterGenerator.click(event);
        kateBucket.click(event);
    }
    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        startupCommon.registerContainers(event);
    }

    @SubscribeEvent
    void AnvilUpdateEvent(AnvilUpdateEvent evt) {
        if (evt.getLeft().getItem() instanceof handheldWaystone && evt.getRight().getItem() == itemRegister.SIGIL.get()) {
            // LOGGER.info("Detected recipe handheld waystone upgrade");
            ItemStack input = evt.getLeft();
            ItemStack mod = evt.getRight();
            ItemStack output = input.copy();
            CompoundNBT nbt = output.getOrCreateTag();
            if (nbt.getInt("maxSlots") == 0) {
                nbt.putInt("maxSlots", 1 + mod.getCount());
            } else {
                nbt.putInt("maxSlots", nbt.getInt("maxSlots") + mod.getCount());
            }
            evt.setOutput(output);
            evt.setCost(1);
            evt.setResult(Event.Result.ALLOW);
            LOGGER.info("New number of slots: " + nbt.getInt("maxSlots"));
        }
    }
}
