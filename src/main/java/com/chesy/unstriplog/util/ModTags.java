package com.chesy.unstriplog.util;

import com.chesy.unstriplog.UnstripLog;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String id) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(UnstripLog.MODID, id));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String id) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(UnstripLog.MODID, id));
        }
    }
}
