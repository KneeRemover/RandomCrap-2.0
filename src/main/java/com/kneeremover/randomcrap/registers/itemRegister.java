package com.kneeremover.randomcrap.registers;

import com.kneeremover.randomcrap.RandomCrap;
import com.kneeremover.randomcrap.items.*;
import com.kneeremover.randomcrap.items.kateBucket.item;
import com.kneeremover.randomcrap.items.secondMouth;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.kneeremover.randomcrap.util.crapLib.MODID;


public class itemRegister {
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	//  TOOLS   \\
	public static final RegistryObject<Item> ENERGISED_PICKAXE = ITEMS.register("energised_pickaxe", () -> new PickaxeItem(itemTiers.ENERGISED, 0, 0, new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> ENERGISED_AXE = ITEMS.register("energised_axe", () -> new AxeItem(itemTiers.ENERGISED, 0, 0, new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> ENERGISED_SHOVEL = ITEMS.register("energised_shovel", () -> new ShovelItem(itemTiers.ENERGISED, 0, 0, new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> ENERGISED_HOE = ITEMS.register("energised_hoe", () -> new HoeItem(itemTiers.ENERGISED, 0, 0, new Item.Properties().tab(RandomCrap.TAB)));

	//  ARMR    \\
	public static final RegistryObject<Item> ENERGISED_HELMET = ITEMS.register("energised_helmet", () -> new ArmorItem(armorTiers.ENERGISED, EquipmentSlotType.HEAD, new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> ENERGISED_CHESTPLATE = ITEMS.register("energised_chestplate", () -> new ArmorItem(armorTiers.ENERGISED, EquipmentSlotType.CHEST, new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> ENERGISED_LEGGINGS = ITEMS.register("energised_leggings", () -> new ArmorItem(armorTiers.ENERGISED, EquipmentSlotType.LEGS, new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> ENERGISED_BOOTS = ITEMS.register("energised_boots", () -> new ArmorItem(armorTiers.ENERGISED, EquipmentSlotType.FEET, new Item.Properties().tab(RandomCrap.TAB)));

	//  WPNS    \\
	public static final RegistryObject<Item> ENERGISED_SWORD = ITEMS.register("energised_sword", () -> new SwordItem(itemTiers.ENERGISED, 10, 6, new Item.Properties().tab(RandomCrap.TAB)));

	//  ITEMS   \\
	public static final RegistryObject<Item> ENERGISED_STONE = ITEMS.register("energised_stone", () -> new Item(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> COMPACT_ENERGISED_STONE = ITEMS.register("compact_energised_stone", () -> new Item(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> SIGIL = ITEMS.register("sigil", () -> new Item(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> TELEPORTATION_CORE = ITEMS.register("teleportation_core", () -> new Item(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new Item(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> HANDHELD_WAYSTONE = ITEMS.register("handheld_waystone", () -> new handheldWaystone(new Item.Properties().tab(RandomCrap.TAB)));
	//public static final RegistryObject<Item> ROCKET_SHIP = ITEMS.register("rocket_ship", () -> new rocket(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> ANIMAL_FAT = ITEMS.register("animal_fat", () -> new animalFat(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> STOPWATCH = ITEMS.register("stopwatch", () -> new clock(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> KATE_BUCKET = ITEMS.register("kate_bucket", () -> new item(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> LEAF_WINGS = ITEMS.register("leaf_wings", () -> new leafWings(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> RUBY_COMPOUND = ITEMS.register("ruby_compound", () -> new rubyCompound(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> BASIL = ITEMS.register("basil", () -> new basil(new Item.Properties().tab(RandomCrap.TAB).stacksTo(143)));
	public static final RegistryObject<Item> PLAYER_CONTAINER = ITEMS.register("player_container", () -> new playerContainer(new Item.Properties().tab(RandomCrap.TAB)));
	public static final RegistryObject<Item> SECOND_MOUTH = ITEMS.register("second_mouth", () -> new secondMouth(new Item.Properties().tab(RandomCrap.TAB)));

	//   FOOD   \\
	public static final RegistryObject<Item> TATER_TOTS = ITEMS.register("tater_tots",
			() -> new Item(new Item.Properties().tab(RandomCrap.TAB).food(new Food.Builder().nutrition(20).fast().saturationMod(20)
					.effect(() -> new EffectInstance(Effects.ABSORPTION, 600, 10), 100).effect(() -> new EffectInstance(Effects.DAMAGE_BOOST, 600, 10), 100).effect(() -> new EffectInstance(Effects.DAMAGE_RESISTANCE, 600, 10), 100)
					.effect(() -> new EffectInstance(Effects.MOVEMENT_SPEED, 600, 2), 100).alwaysEat().build()).rarity(Rarity.EPIC)));


	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
