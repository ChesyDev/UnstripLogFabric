package com.chesy.unstriplog;

import com.chesy.unstriplog.component.ModDataComponents;
import com.chesy.unstriplog.config.RuntimeConfigAccess;
import com.chesy.unstriplog.config.UnstripLogConfig;
import com.chesy.unstriplog.handler.LogHandler;
import com.chesy.unstriplog.item.ModItems;
import com.chesy.unstriplog.network.ConfigSyncManager;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

public class UnstripLog implements ModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static String MODID = "unstriplog";
    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModDataComponents.initialize();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER = null);
        LogHandler.onCommonSetup();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(this::addCreative);
        ConfigSyncManager.onRegisterPayloadHandlers();
        ServerPlayConnectionEvents.JOIN.register(ConfigSyncManager::onPlayerLogin);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(ConfigSyncManager::onDatapackSync);
        UnstripLogConfig.init();

        UseBlockCallback.EVENT.register(LogHandler::onUnstrip);
        UseBlockCallback.EVENT.register(LogHandler::onStrip);
    }

    private void addCreative(FabricItemGroupEntries output) {
        RuntimeConfigAccess.barkTypes().forEach(entry -> {
            ItemStack stack = ModItems.BARK.getDefaultInstance();
            stack.set(ModDataComponents.BARK_TYPE, entry);
            output.accept(stack);
        });
    }

    public static ResourceLocation id(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
