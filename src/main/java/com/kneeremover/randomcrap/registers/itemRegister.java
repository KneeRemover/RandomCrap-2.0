package com.kneeremover.randomcrap.registers;

import com.kneeremover.randomcrap.RandomCrap;
import com.kneeremover.randomcrap.items.animalFat;
import com.kneeremover.randomcrap.items.handheldWaystone;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;


public class itemRegister {


    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RandomCrap.modid);


    public static final RegistryObject<Item> ENERGISED_STONE = ITEMS.register("energised_stone",
            () -> new Item(new Item.Properties().group(RandomCrap.TAB)));

    public static final RegistryObject<Item> COMPACT_ENERGISED_STONE = ITEMS.register("compact_energised_stone",
            () -> new Item(new Item.Properties().group(RandomCrap.TAB)));

    public static final RegistryObject<Item> SIGIL = ITEMS.register("sigil",
            () -> new Item(new Item.Properties().group(RandomCrap.TAB)));

    public static final RegistryObject<Item> TELEPORTATION_CORE = ITEMS.register("teleportation_core",
            () -> new Item(new Item.Properties().group(RandomCrap.TAB)));

    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby",
            () -> new Item(new Item.Properties().group(RandomCrap.TAB)));

    public static final RegistryObject<Item> HANDHELD_WAYSTONE = ITEMS.register("handheld_waystone",
            () -> new handheldWaystone(new Item.Properties().group(RandomCrap.TAB)));

    public static final RegistryObject<Item> ANIMAL_FAT = ITEMS.register("animal_fat",
            () -> new animalFat(new Item.Properties().group(RandomCrap.TAB)));

    public static final RegistryObject<Item> TATER_TOTS = ITEMS.register("tater_tots",
            () -> new Item(new Item.Properties().group(RandomCrap.TAB).food(new Food.Builder().hunger(20).fastToEat().saturation(20)
                    .effect(() -> new EffectInstance(Effects.ABSORPTION, 600, 10), 100).effect(() -> new EffectInstance(Effects.STRENGTH, 600, 10), 100).effect(() -> new EffectInstance(Effects.RESISTANCE, 600, 10), 100)
                    .effect(() -> new EffectInstance(Effects.SPEED, 600, 2), 100).setAlwaysEdible().build()).rarity(Rarity.EPIC)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
