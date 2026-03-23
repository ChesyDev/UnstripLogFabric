package com.chesy.unstriplog.network;

import com.chesy.unstriplog.UnstripLog;
import com.chesy.unstriplog.config.BarkTypeConfig;
import com.chesy.unstriplog.config.SyncedConfigState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record ConfigSyncPayload(boolean allowUnknownLog, Identifier barkItem, List<BarkTypeConfig.BarkTypeEntry> barkTypes, List<SyncedLogEntry> logEntries) implements CustomPacketPayload {
    public static final Type<ConfigSyncPayload> TYPE = new Type<>(UnstripLog.id("config_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ConfigSyncPayload::allowUnknownLog,
            Identifier.STREAM_CODEC,
            ConfigSyncPayload::barkItem,
            BarkTypeConfig.BarkTypeEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ConfigSyncPayload::barkTypes,
            SyncedLogEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ConfigSyncPayload::logEntries,
            ConfigSyncPayload::new
    );

    public static void handle(ConfigSyncPayload payload, ClientPlayNetworking.Context context) {
        SyncedConfigState.apply(payload);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record SyncedLogEntry(Identifier base, Identifier stripped, ItemStack drop, Optional<ItemStack> unstripItem) {
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncedLogEntry> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC,
                SyncedLogEntry::base,
                Identifier.STREAM_CODEC,
                SyncedLogEntry::stripped,
                ItemStack.STREAM_CODEC,
                SyncedLogEntry::drop,
                ByteBufCodecs.optional(ItemStack.STREAM_CODEC),
                SyncedLogEntry::unstripItem,
                SyncedLogEntry::new
        );
    }
}

