package com.chesy.unstriplog.item;

import com.chesy.unstriplog.UnstripLog;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item BARK = register("bark", new BarkItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, UnstripLog.id("bark")))));


    public static void registerFuels() {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(ModItems.BARK, 150);
        });
    }

    public static <T extends Item> T register(String id, T item){
        Identifier itemID = Identifier.fromNamespaceAndPath(UnstripLog.MODID, id);
        return Registry.register(BuiltInRegistries.ITEM, itemID, item); //returns Registered Item
    }

    public static void initialize() {
        registerFuels();
    }
}
