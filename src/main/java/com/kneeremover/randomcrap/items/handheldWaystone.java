package com.kneeremover.randomcrap.items;


import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = "randomcrap", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class handheldWaystone extends Item {
    public handheldWaystone(Properties properties) {
        super(properties);
    }

    public static HashMap<RegistryKey, ServerWorld> dimensions = new HashMap<>();

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Integer slots = stack.getOrCreateTag().getInt("maxSlots");
        if (slots == 0) {
            slots = 1;
        }
        tooltip.add(new StringTextComponent("\u00A77Max slots: " + slots.toString()));
    }
/*    @SubscribeEvent
    public void leftClickItemEvent(PlayerInteractEvent.LeftClickEmpty event)
    {
        PlayerEntity player = event.getPlayer();
        if (!player.getEntityWorld().isRemote) {
            // then is server side
            ItemStack hitem = player.getHeldItemMainhand();
            if (hitem.getItem() instanceof handheldWaystone) {
                CompoundNBT nbt = hitem.getOrCreateTag();
                if (nbt.getInt("maxSlots") == 0 || nbt.getInt("slot") == nbt.getInt("maxSlots")) {
                    nbt.putInt("slot", 1);
                    hitem.write(nbt);
                    Integer slot = nbt.getInt("slot");
                    player.sendStatusMessage(new StringTextComponent(slot.toString()), true);
                }
            } else {
                RandomCrap.LOGGER.info("you aren't even holding the right item genius");
            }
        }
    }*/
 // TODO figure out how the heck to use event busses
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        CompoundNBT nbt = stack.getOrCreateTag();
        Integer slot = nbt.getInt("slot");
        if (dimensions == null || dimensions.isEmpty()) {
            if (world instanceof ServerWorld) {
                dimensions = new HashMap<>();
                Iterable worlds = world.getServer().getWorlds();
                worlds.forEach((Consumer<Object>) o -> {
                    ServerWorld world1 = (ServerWorld) o;
                    dimensions.put(world1.getDimensionKey(), world1);
                });
            }
        }
        if (!world.isRemote) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ServerWorld serverWorld = (ServerWorld) world;
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
                player.sendStatusMessage(new TranslationTextComponent("kneeremover.handheldWaystone.setPos"), true);
                stack.write(nbt);
            } else if (player.getHeldItemOffhand().getItem() instanceof handheldWaystone) {
                if (slot == nbt.getInt("maxSlots")) {
                    nbt.putInt("slot", 1);
                    stack.write(nbt);
                } else {
                    nbt.putInt("slot", slot + 1);
                    stack.write(nbt);
                }
                player.sendStatusMessage(new StringTextComponent("Selected slot: " + slot.toString()), true);
            } else if (nbt.getFloat("xpos" + slot.toString()) == 0) {
                player.sendStatusMessage(new TranslationTextComponent("kneeremover.handheldWaystone.error.unsetPos"), true);
            } else {
                Double x = nbt.getDouble("xpos" + slot);
                Double y = nbt.getDouble("ypos" + slot);
                Double z = nbt.getDouble("zpos" + slot);

                String dimension = nbt.getString("dim" + slot);
                Boolean success = false;

                // TODO This isn't actually a todo, but IDEs will highlight it, so you people will see it. Below is a way to TRANSFORM A STRING INTO A REGISTRY KEY.
                // TODO Use this for storing dimensions in NBT.
                ResourceLocation dimLoc = new ResourceLocation(nbt.getString("dim" + slot));
                RegistryKey rk = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimLoc);
                ServerWorld dim = dimensions.get(rk);
                serverPlayer.teleport(dim, x, y, z, 0, 0);
            }
        }
        return super.onItemUseFirst(stack, context);
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
