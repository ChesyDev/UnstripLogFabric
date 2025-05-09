package com.chesy.unstriplog.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BarkItem extends Item {
    private static final Map<Block, Block> REVERSE_STRIPPED = new HashMap<>();

    static {
        buildReverseStrippedMap();
    }

    private static void buildReverseStrippedMap() {
        for (Block block : Registries.BLOCK) {
            Identifier id = Registries.BLOCK.getId(block);
            String path = id.getPath(); // e.g. "stripped_oak_log"

            if (path.startsWith("stripped_") && path.endsWith("_log") || path.endsWith("_wood") || path.endsWith("stem") || path.endsWith("hyphae")) {
                if (path.length() > "stripped_".length()) {
                    String originalPath = path.substring("stripped_".length());
                    Identifier originalId = Identifier.of(id.getNamespace(), originalPath);

                    Block original = Registries.BLOCK.get(originalId);
                    if (original != null && original != Blocks.AIR) {
                        REVERSE_STRIPPED.put(block, original);
                    }
                }
            }
        }
    }

    public BarkItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState targetState = world.getBlockState(pos);

        Block reversed = REVERSE_STRIPPED.get(targetState.getBlock());
        if (reversed != null) {
            if (!world.isClient) {
                world.setBlockState(pos, reversed.getDefaultState());
                world.playSound(null, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);

                // Consume 1 bark item
                context.getStack().decrement(1);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
