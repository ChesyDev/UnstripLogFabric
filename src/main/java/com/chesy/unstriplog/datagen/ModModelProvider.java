package com.chesy.unstriplog.datagen;

import com.chesy.unstriplog.UnstripLog;
import com.chesy.unstriplog.datagen.model.BarkItemRenderer;
import com.chesy.unstriplog.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModels) {
        itemModels.itemModelOutput.accept(ModItems.BARK, ItemModelUtils.specialModel(UnstripLog.id("item/bark"), new BarkItemRenderer.Unbaked()));
    }
}