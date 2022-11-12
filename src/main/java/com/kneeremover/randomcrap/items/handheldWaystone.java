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
    public void addInformation(ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        int slots = stack.getOrCreateTag().getInt("maxSlots");
        if (slots == 0) {
            slots = 1;
        }
        tooltip.add(new StringTextComponent("\u00A77Max slots: " + slots));
    }

    @SubscribeEvent
    public static ActionResult<ItemStack> leftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getItemStack().getItem() instanceof handheldWaystone) {
            main.CHANNEL.sendToServer(new leftClick(event.getItemStack()));
        }
        return ActionResult.resultSuccess(event.getItemStack());
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        CompoundNBT nbt = stack.getOrCreateTag();
        int slot = nbt.getInt("slot");
        if (dimensions == null || dimensions.isEmpty()) {
            if (world instanceof ServerWorld) {
                dimensions = new HashMap<>();
                Iterable<ServerWorld> worlds = Objects.requireNonNull(world.getServer()).getWorlds();
                worlds.forEach((Consumer<Object>) o -> {
                    ServerWorld world1 = (ServerWorld) o;
                    dimensions.put(world1.getDimensionKey(), world1);
                });
            }
        }
        if (!world.isRemote) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            @SuppressWarnings("ConstantConditions") ServerWorld serverWorld = (ServerWorld) world;
            if (nbt.getInt("slot") == 0) {
                nbt.putInt("slot", 1);
                nbt.putInt("maxSlots", 1);
                stack.write(nbt);
            }
            if (player.isSneaking() && !(player.getHeldItemOffhand().getItem() instanceof handheldWaystone)) {
                nbt.putDouble("xpos" + slot, player.getPosition().getX());
                nbt.putDouble("ypos" + slot, player.getPosition().getY());
                nbt.putDouble("zpos" + slot, player.getPosition().getZ());
                nbt.putString("dim" + slot, serverWorld.getDimensionKey().getLocation().toString());
                nbt.putString("dimName" + slot, serverWorld.getDimensionKey().getLocation().getNamespace());
                player.sendStatusMessage(new TranslationTextComponent("item.randomcrap.handheldWaystone.setPos"), true);
                stack.write(nbt);
            } else if (nbt.getFloat("xpos" + slot) == 0) {
                player.sendStatusMessage(new TranslationTextComponent("item.randomcrap.handheldWaystone.error.unsetPos"), true);
            } else {
                double x = nbt.getDouble("xpos" + slot);
                double y = nbt.getDouble("ypos" + slot);
                double z = nbt.getDouble("zpos" + slot);

                // TODO This isn't actually a todo, but IDEs will highlight it, so you people will see it. Below is a way to TRANSFORM A STRING INTO A REGISTRY KEY.
                // TODO Use this for storing dimensions in NBT.
                ResourceLocation dimLoc = new ResourceLocation(nbt.getString("dim" + slot));
                RegistryKey<World> rk = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimLoc);
                ServerWorld dim = dimensions.get(rk);
                serverPlayer.teleport(dim, x, y, z, 0, 0);
            }
        }
        return ActionResult.resultSuccess(stack);
    }

    @SubscribeEvent
    public static void AnvilUpdateEvent(AnvilUpdateEvent evt) {
        if (evt.getLeft().getItem() instanceof handheldWaystone && evt.getRight().getItem() == itemRegister.SIGIL.get()) {
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
