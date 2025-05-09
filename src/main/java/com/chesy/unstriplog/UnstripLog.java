package com.chesy.unstriplog;

import com.chesy.unstriplog.item.ModItems;
import com.chesy.unstriplog.util.ModTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnstripLog implements ModInitializer {
    public static String MOD_ID = "unstriplog";

    @Override
    public void onInitialize() {
        List<Block> LOGS = new ArrayList<>();

        for (Block block : Registries.BLOCK) {
            Identifier id = Registries.BLOCK.getId(block);
            String path = id.getPath();

            if ((path.endsWith("_log") || path.endsWith("_wood")) || path.endsWith("stem") || path.endsWith("hyphae") && !path.startsWith("stripped_")) {
                LOGS.add(block);
            }
        }

        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (world.isClient) return ActionResult.PASS;

            if (!(player.getStackInHand(hand).getItem() instanceof AxeItem)) {
                return ActionResult.PASS;
            }

            BlockPos pos = hit.getBlockPos();
            var state = world.getBlockState(pos);
            var block = state.getBlock();

            if (LOGS.contains(block)) {
                ItemEntity drop = new ItemEntity(
                        world,
                        pos.getX() + 0.5,
                        pos.getY() + 1.0,
                        pos.getZ() + 0.5,
                        new ItemStack(ModItems.BARK)
                );
                world.spawnEntity(drop);
            }

            return ActionResult.PASS;
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(ModItems.BARK);
        });

        ModItems.initialize();
    }
}
