package com.chesy.unstriplog;

import com.chesy.unstriplog.datagen.model.BarkItemRenderer;
import com.chesy.unstriplog.network.ClientSyncEvents;
import com.chesy.unstriplog.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class UnstripLogClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpecialModelRenderers.ID_MAPPER.put(UnstripLog.id("bark"), BarkItemRenderer.Unbaked.MAP_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, ConfigSyncPayload::handle);
        ClientPlayConnectionEvents.DISCONNECT.register(ClientSyncEvents::onClientLogout);
    }
}
