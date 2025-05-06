package com.chesy.unstriplog;

import com.chesy.unstriplog.item.ModItems;
import net.fabricmc.api.ModInitializer;

public class UnstripLog implements ModInitializer {
    public static String MOD_ID = "unstriplog";

    @Override
    public void onInitialize() {
        ModItems.initialize();
    }
}
