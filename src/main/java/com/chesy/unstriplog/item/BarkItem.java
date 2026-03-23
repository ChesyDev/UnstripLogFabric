package com.chesy.unstriplog.item;

import com.chesy.unstriplog.component.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
