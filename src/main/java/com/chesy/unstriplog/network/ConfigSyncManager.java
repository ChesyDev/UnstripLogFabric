package com.chesy.unstriplog.network;

import com.chesy.unstriplog.config.BarkTypeConfig;
import com.chesy.unstriplog.config.UnstripDetailedConfig;
import com.chesy.unstriplog.config.UnstripLogConfig;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ConfigSyncManager {
    private ConfigSyncManager() {
    }

    public static void onRegisterPayloadHandlers() {
        PayloadTypeRegistry.clientboundPlay().register(ConfigSyncPayload.TYPE, ConfigSyncPayload.STREAM_CODEC);
    }

    public static void onPlayerLogin(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
        sendTo(listener.player);
    }

    public static void onDatapackSync(ServerPlayer player, boolean joined) {
        if (player != null) {
            sendTo(player);
            return;
        }

        player.level().getServer().getPlayerList().getPlayers().forEach(ConfigSyncManager::sendTo);
    }

    public static void syncAll(net.minecraft.server.MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(ConfigSyncManager::sendTo);
    }

    public static void sendTo(ServerPlayer player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ServerPlayNetworking.send(serverPlayer, createPayload());
    }

    private static ConfigSyncPayload createPayload() {
        Item bark = UnstripLogConfig.getBark();
        if (bark == null) {
            bark = BuiltInRegistries.ITEM.getValue(Identifier.parse(UnstripLogConfig.getConfiguredItemId()));
        }

        Identifier barkId = BuiltInRegistries.ITEM.getKey(bark);

        List<ConfigSyncPayload.SyncedLogEntry> syncedEntries = UnstripDetailedConfig.getEntries().stream()
                .map(entry -> {
                    ItemStack drop = new ItemStack(entry.drop().item(), 1, entry.drop().componentPatch());
                    return new ConfigSyncPayload.SyncedLogEntry(
                            BuiltInRegistries.BLOCK.getKey(entry.base()),
                            BuiltInRegistries.BLOCK.getKey(entry.stripped()),
                            drop,
                            entry.unstripItem().map(i -> new ItemStack(i.item(), 1, i.componentPatch()))
                    );
                })
                .toList();

        return new ConfigSyncPayload(
                UnstripLogConfig.allowUnknownLog(),
                barkId,
                List.copyOf(BarkTypeConfig.getEntries()),
                syncedEntries
        );
    }
}

