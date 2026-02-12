package com.chesy.unstriplog;

import com.chesy.unstriplog.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.List;

public class UnstripLog implements ModInitializer {
    public static String MOD_ID = "unstriplog";

    @Override
    public void onInitialize() {
        List<Block> LOGS = new ArrayList<>();

        for (Block block : BuiltInRegistries.BLOCK) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(block);
            String path = id.getPath();

            if ((path.endsWith("_log") || path.endsWith("_wood") || path.endsWith("stem") || path.endsWith("hyphae")) && !path.startsWith("stripped_")) {
                LOGS.add(block);
            }
        }

        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (world.isClientSide()) return InteractionResult.PASS;

            if (player.getOffhandItem().getItem() == Items.SHIELD && !player.isShiftKeyDown()){
                return InteractionResult.PASS;
            }

            if (!(player.getItemInHand(hand).getItem() instanceof AxeItem)) {
                return InteractionResult.PASS;
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
                world.addFreshEntity(drop);
            }

            return InteractionResult.PASS;
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(content -> {
            content.accept(ModItems.BARK);
        });

        ModItems.initialize();
    }
}
