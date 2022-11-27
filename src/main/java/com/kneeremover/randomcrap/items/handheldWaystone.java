package com.kneeremover.randomcrap.items;


import com.kneeremover.randomcrap.registers.itemRegister;
import com.kneeremover.randomcrap.util.network.main;
import com.kneeremover.randomcrap.util.network.message.leftClick;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.kneeremover.randomcrap.RandomCrap.LOGGER;

public class handheldWaystone extends Item {
	public handheldWaystone(Properties properties) {
		super(properties);
	}

	public static HashMap<RegistryKey<World>, ServerWorld> dimensions = new HashMap<>();

	@SubscribeEvent
	public static void onWorldSave(WorldEvent.Save evt) {
		dimensions = null;
	}


	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
		int slots = stack.getOrCreateTag().getInt("randomcrap.maxSlots");
		if (slots == 0) {
			slots = 1;
		}
		tooltip.add(new StringTextComponent("\u00A77Max slots: " + slots));
	}

	@SubscribeEvent
	public static ActionResult<ItemStack> leftClick(PlayerInteractEvent.LeftClickEmpty event) {
		if (event.getItemStack().getItem() instanceof handheldWaystone) {
			main.CHANNEL.sendToServer(new leftClick());
		}
		return ActionResult.success(event.getItemStack());
	}

	public static void leftClickPacket(ItemStack stack, ServerPlayerEntity player) {
		if (stack.getItem() instanceof handheldWaystone && !player.getCooldowns().isOnCooldown(itemRegister.HANDHELD_WAYSTONE.get())) {
			player.getCooldowns().addCooldown(itemRegister.HANDHELD_WAYSTONE.get(), 20);
			CompoundNBT nbt = stack.getOrCreateTag();
			int slot = nbt.getInt("randomcrap.slot");

			if (slot == 0) {
				slot = 1;
				nbt.putInt("randomcrap.slot", 1);
			}
			if (nbt.getInt("randomcrap.maxSlots") == 0) {
				nbt.putInt("randomcrap.maxSlots", 1);
			}
			stack.setTag(nbt); // if 0 make 1

			if (slot >= nbt.getInt("randomcrap.maxSlots")) {
				LOGGER.info("Looping around because slot is " + slot + " and maxSlots is " + nbt.getInt("randomcrap.maxSlots"));
				slot = 1;
				nbt.putInt("randomcrap.slot", 1);
			} else {
				LOGGER.info("Incrementing slots from " + slot + " to " + (slot + 1));
				slot++;
				nbt.putInt("randomcrap.slot", slot);
			}
			stack.setTag(nbt);
			assert player != null;
			((PlayerEntity) player).displayClientMessage(new StringTextComponent(new TranslationTextComponent("item.randomcrap.handheldWaystone.slot").getString() + slot), true);
			player.setItemInHand(Hand.MAIN_HAND, stack);
		}
	}

	@Override
	public @NotNull ActionResult<ItemStack> use(@NotNull World world, PlayerEntity
			player, @Nonnull Hand hand) {
		ItemStack stack = player.getMainHandItem();
		CompoundNBT nbt = stack.getOrCreateTag();
		int slot = nbt.getInt("randomcrap.slot");
		if (dimensions == null || dimensions.isEmpty()) {
			if (world instanceof ServerWorld) {
				dimensions = new HashMap<>();
				Iterable<ServerWorld> worlds = Objects.requireNonNull(world.getServer()).getAllLevels();
				worlds.forEach((Consumer<Object>) o -> {
					ServerWorld world1 = (ServerWorld) o;
					dimensions.put(world1.dimension(), world1);
				});
			}
		}
		if (!world.isClientSide) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			@SuppressWarnings("ConstantConditions") ServerWorld serverWorld = (ServerWorld) world;
			if (nbt.getInt("randomcrap.slot") == 0) {
				nbt.putInt("randomcrap.slot", 1);
				nbt.putInt("randomcrap.maxSlots", 1);
				stack.setTag(nbt);
			}
			if (player.isCrouching() && !(player.getOffhandItem().getItem() instanceof handheldWaystone)) {
				nbt.putDouble("randomcrap.xpos" + slot, player.position().x());
				nbt.putDouble("randomcrap.ypos" + slot, player.position().y());
				nbt.putDouble("randomcrap.zpos" + slot, player.position().z());
				nbt.putString("randomcrap.dim" + slot, serverWorld.dimension().location().toString());
				nbt.putString("randomcrap.dimName" + slot, serverWorld.dimension().location().getNamespace());
				player.displayClientMessage(new TranslationTextComponent("item.randomcrap.handheldWaystone.setPos"), true);
				stack.setTag(nbt);
			} else if (nbt.getFloat("randomcrap.xpos" + slot) == 0) {
				player.displayClientMessage(new TranslationTextComponent("item.randomcrap.handheldWaystone.error.unsetPos"), true);
			} else {
				double x = nbt.getDouble("randomcrap.xpos" + slot);
				double y = nbt.getDouble("randomcrap.ypos" + slot);
				double z = nbt.getDouble("randomcrap.zpos" + slot);

				// TODO This isn't actually a todo, but IDEs will highlight it, so you people will see it. Below is a way to TRANSFORM A STRING INTO A REGISTRY KEY.
				// TODO Use this for storing dimensions in NBT.
				ResourceLocation dimLoc = new ResourceLocation(nbt.getString("randomcrap.dim" + slot));
				RegistryKey<World> rk = RegistryKey.create(Registry.DIMENSION_REGISTRY, dimLoc);
				ServerWorld dim = dimensions.get(rk);
				serverPlayer.teleportTo(dim, x, y, z, 0, 0);
			}
		}
		return ActionResult.success(stack);
	}

	@SubscribeEvent
	public static void AnvilUpdateEvent(AnvilUpdateEvent evt) {
		if (evt.getLeft().getItem() instanceof handheldWaystone && evt.getRight().getItem() == itemRegister.SIGIL.get()) {
			ItemStack input = evt.getLeft();
			ItemStack mod = evt.getRight();
			ItemStack output = input.copy();
			CompoundNBT nbt = output.getOrCreateTag();
			if (nbt.getInt("randomcrap.maxSlots") == 0) {
				nbt.putInt("randomcrap.maxSlots", 1 + mod.getCount());
			} else {
				nbt.putInt("randomcrap.maxSlots", nbt.getInt("randomcrap.maxSlots") + mod.getCount());
			}
			evt.setOutput(output);
			evt.setCost(1);
			evt.setResult(Event.Result.ALLOW);
		}
	}
}

// Below is a way to save & load. I'm not using it now, but it mihgt be useful later!
/*
    public static void Save (World world) {

        RandomCrap.LOGGER.atInfo().log("Save event triggered");
        if (world instanceof ServerWorld) {
            String path ="saves\\test\\waystone.txt";
            File file = new File("saves\\test\\waystone.txt");
            try {
                if (file.createNewFile()) {
                    RandomCrap.LOGGER.atInfo().log("No waystone data file found. Creating one now...");
                } else {
                    System.out.println("Waystone file found!");
                    if (dimensions == null || dimensions.isEmpty()) {
                        RandomCrap.LOGGER.atInfo().log("Loading from file...");
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                        dimensions = (HashMap<Integer, ServerWorld>) ois.readObject();
                    } else {
                        RandomCrap.LOGGER.atInfo().log("Writing data to " + file.getAbsolutePath());
                        try{
                            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                            oos.writeObject(dimensions);
                            oos.flush();
                            oos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
*/
