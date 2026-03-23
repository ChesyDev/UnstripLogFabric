package com.chesy.unstriplog.item;

import java.util.HashMap;
import java.util.Map;

import com.chesy.unstriplog.component.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BarkItem extends Item {

    public BarkItem(Properties settings) {
        super(settings);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        if (itemStack.has(ModDataComponents.BARK_TYPE)){
            return Component.translatable("item.unstriplog." + itemStack.get(ModDataComponents.BARK_TYPE).name() + "_bark");
        }
        return super.getName(itemStack);
    }
}
