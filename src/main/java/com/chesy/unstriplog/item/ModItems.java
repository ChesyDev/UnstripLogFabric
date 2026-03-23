package com.chesy.unstriplog.item;

import com.chesy.unstriplog.UnstripLog;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item BARK = register("bark", new BarkItem(new Item.Properties()));


    public static void registerFuels() {
        FuelRegistry.INSTANCE.add(ModItems.BARK, 150);
    }

    public static <T extends Item> T register(String id, T item){
        ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(UnstripLog.MODID, id);
        return Registry.register(BuiltInRegistries.ITEM, itemID, item); //returns Registered Item
    }

    public static void initialize() {
        registerFuels();
    }
}
