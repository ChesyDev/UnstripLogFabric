package com.chesy.unstriplog.datagen;

import com.chesy.unstriplog.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
        return new RecipeProvider(wrapperLookup, recipeExporter) {
            private final HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

            @Override
            public void buildRecipes() {
                ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, Items.PAPER, 1)
                        .pattern("##")
                        .define('#', ModItems.BARK)
                        .unlockedBy(getHasName(ModItems.BARK), has(ModItems.BARK))
                        .save(output, "paper_from_bark");
            }
        };
    }

    @Override
    public String getName() {
        return "";
    }
}