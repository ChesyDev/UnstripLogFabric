package com.chesy.unstriplog;

import com.chesy.unstriplog.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;

public class UnstripLog implements ModInitializer {
    public static String MOD_ID = "unstriplog";

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(ModItems.BARK);
        });

        ModItems.initialize();
    }
}
