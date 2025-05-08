package com.chesy.unstriplog.item;

import com.chesy.unstriplog.UnstripLog;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item BARK = register("bark", new BarkItem(new Item.Settings()));


    public static void registerFuels() {
        FuelRegistry.INSTANCE.add(ModItems.BARK, 150);
    }

    public static <T extends Item> T register(String id, T item){
        Identifier itemID = Identifier.of(UnstripLog.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item); //returns Registered Item
    }

    public static void initialize() {
        registerFuels();
    }
}
