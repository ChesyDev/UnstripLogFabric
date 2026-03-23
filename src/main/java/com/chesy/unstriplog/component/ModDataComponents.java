package com.chesy.unstriplog.component;

import com.chesy.unstriplog.UnstripLog;
import com.chesy.unstriplog.config.BarkTypeConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModDataComponents {
    public static final DataComponentType<BarkTypeConfig.BarkTypeEntry> BARK_TYPE = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, UnstripLog.id("bark_type"), new DataComponentType.Builder<BarkTypeConfig.BarkTypeEntry>().persistent(BarkTypeConfig.BarkTypeEntry.CODEC).networkSynchronized(BarkTypeConfig.BarkTypeEntry.STREAM_CODEC).build());

    public static void initialize(){

    }
}
