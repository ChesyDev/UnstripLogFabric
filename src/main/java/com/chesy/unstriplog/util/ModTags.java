package com.chesy.unstriplog.util;

import com.chesy.unstriplog.UnstripLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String id) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(UnstripLog.MOD_ID, id));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String id) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(UnstripLog.MOD_ID, id));
        }
    }
}
