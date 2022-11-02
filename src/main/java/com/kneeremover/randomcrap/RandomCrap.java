package com.kneeremover.randomcrap;

import com.kneeremover.randomcrap.items.handheldWaystone;
import com.kneeremover.randomcrap.multiblocks.kateBucket;
import com.kneeremover.randomcrap.multiblocks.taterGenerator;
import com.kneeremover.randomcrap.registers.blockRegister;
import com.kneeremover.randomcrap.registers.itemRegister;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.patchouli.api.PatchouliAPI;

import static com.kneeremover.randomcrap.multiblocks.kateBucket.BUCKET_UPGRADER;
import static com.kneeremover.randomcrap.multiblocks.taterGenerator.TATER_CAULDRON;
import static com.kneeremover.randomcrap.util.crapLib.append;


@Mod("randomcrap")
public class RandomCrap {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String modid = "randomcrap";

    public RandomCrap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        blockRegister.register(eventBus);
        itemRegister.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PatchouliAPI.get().registerMultiblock(append("tater_cauldron"), TATER_CAULDRON.get());
        PatchouliAPI.get().registerMultiblock(append("kate_bucket"), BUCKET_UPGRADER.get());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    public static final ItemGroup TAB = new ItemGroup("crapTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(itemRegister.RUBY.get());
        }
    };

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save evt) {
        //handheldWaystone.Save((World) evt.getWorld());
        handheldWaystone.dimensions = null;
    }

    @SubscribeEvent
    public void RightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        taterGenerator.click(event);
        kateBucket.click(event);
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
