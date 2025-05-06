package com.chesy.unstriplog.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BarkItem extends Item {
    private static final Map<Block, Block> REVERSE_STRIPPED = new HashMap<>();

    static {
        REVERSE_STRIPPED.put(Blocks.STRIPPED_OAK_LOG, Blocks.OAK_LOG);
        REVERSE_STRIPPED.put(Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_LOG);
        REVERSE_STRIPPED.put(Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_LOG);
        REVERSE_STRIPPED.put(Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_LOG);
        REVERSE_STRIPPED.put(Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_LOG);
        REVERSE_STRIPPED.put(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_LOG);
        // Add others if needed
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
