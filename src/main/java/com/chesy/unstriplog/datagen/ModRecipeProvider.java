package com.chesy.unstriplog.datagen;

import com.chesy.unstriplog.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            private final RegistryEntryLookup<Item> items = registries.getOrThrow(RegistryKeys.ITEM);

            @Override
            public void generate() {
                ShapelessRecipeJsonBuilder.create(items, RecipeCategory.MISC, Items.PAPER, 1)
                        .input(ModItems.BARK)
                        .input(ModItems.BARK)
                        .criterion(getHasName(ModItems.BARK), has(ModItems.BARK))
                        .offerTo(exporter);
            }

            private String getItemName(ItemConvertible item) {
                if (item.asItem() == Items.SLIME_BALL) return "slimeball";
                String name = item.asItem().getName().getString();
                return name.substring(name.lastIndexOf('.') + 1);
            }

            private AdvancementCriterion<?> has(NumberRange.IntRange pCount, ItemConvertible item) {
                return inventoryTrigger(ItemPredicate.Builder.create().items(this.items, item).count(pCount).build());
            }

            private AdvancementCriterion<?> has(ItemConvertible item) {
                return inventoryTrigger(ItemPredicate.Builder.create().items(this.items, item));
            }

            private AdvancementCriterion<?> has(TagKey<Item> pTag) {
                return inventoryTrigger(ItemPredicate.Builder.create().tag(this.items, pTag));
            }

            private AdvancementCriterion<?> inventoryTrigger(ItemPredicate.Builder... pItems) {
                return inventoryTrigger(Arrays.stream(pItems).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
            }

            private AdvancementCriterion<?> inventoryTrigger(ItemPredicate... pPredicates) {
                return Criteria.INVENTORY_CHANGED
                        .create(new InventoryChangedCriterion.Conditions(Optional.empty(), InventoryChangedCriterion.Conditions.Slots.ANY, List.of(pPredicates)));
            }

            private String getHasName(ItemConvertible pItemLike) {
                return "has_" + getItemName(pItemLike);
            }
        };
    }

    @Override
    public String getName() {
        return "";
    }
}
