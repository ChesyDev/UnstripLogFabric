package com.chesy.unstriplog;

import com.chesy.unstriplog.datagen.model.BarkItemRenderer;
import com.chesy.unstriplog.item.ModItems;
import com.chesy.unstriplog.network.ClientSyncEvents;
import com.chesy.unstriplog.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class UnstripLogClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.BARK, new BarkItemRenderer());
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, ConfigSyncPayload::handle);
        ClientPlayConnectionEvents.DISCONNECT.register(ClientSyncEvents::onClientLogout);
    }
}
