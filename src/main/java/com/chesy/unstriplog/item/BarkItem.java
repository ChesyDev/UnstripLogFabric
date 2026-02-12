package com.chesy.unstriplog.item;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BarkItem extends Item {
    private static final Map<Block, Block> REVERSE_STRIPPED = new HashMap<>();

    static {
        buildReverseStrippedMap();
    }

    private static void buildReverseStrippedMap() {
        for (Block block : BuiltInRegistries.BLOCK) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(block);
            String path = id.getPath(); // e.g. "stripped_oak_log"

            if (path.startsWith("stripped_") && (path.endsWith("_log") || path.endsWith("_wood") || path.endsWith("stem") || path.endsWith("hyphae"))) {
                // Make sure the length of the path is greater than "stripped_" to avoid substring error
                if (path.length() > "stripped_".length()) {
                    String originalPath = path.substring("stripped_".length());
                    Identifier originalId = Identifier.fromNamespaceAndPath(id.getNamespace(), originalPath);

                    Block original = BuiltInRegistries.BLOCK.getValue(originalId);
                    if (original != null && original != Blocks.AIR) {
                        REVERSE_STRIPPED.put(block, original);
                    }
                }
            }
        }
    }

    public BarkItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState targetState = world.getBlockState(pos);

        Block reversed = REVERSE_STRIPPED.get(targetState.getBlock());
        if (reversed != null) {
            if (!world.isClientSide()) {
                BlockState newState = reversed.defaultBlockState();

                if (newState.hasProperty(RotatedPillarBlock.AXIS) && targetState.hasProperty(RotatedPillarBlock.AXIS)) {
                    Direction.Axis axis = targetState.getValue(RotatedPillarBlock.AXIS);
                    newState = newState.setValue(RotatedPillarBlock.AXIS, axis);
                }

                world.setBlock(pos, newState, 3);
                world.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

                // Consume 1 bark item
                context.getItemInHand().shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
