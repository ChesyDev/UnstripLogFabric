package com.chesy.unstriplog.mixin;

import com.chesy.unstriplog.item.ModItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"))
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {}

    @Inject(method = "useOnBlock", at = @At("RETURN"))
    private void afterUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = cir.getReturnValue();

        if (result == ActionResult.SUCCESS) {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();

            if (!world.isClient) {
                ItemStack drop = new ItemStack(ModItems.BARK, 1);

                ItemEntity droppedItem = new ItemEntity(world,
                        pos.getX(), pos.getY() + 1.0, pos.getZ(),
                        drop);

                world.spawnEntity(droppedItem);
            }
        }
    }
}
