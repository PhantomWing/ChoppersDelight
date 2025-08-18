package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        // Build recipes for cutting boards.
        this.buildCuttingBoardRecipe(output, ModBlocks.ACACIA_CUTTING_BOARD, Items.ACACIA_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BAMBOO_CUTTING_BOARD, Items.BAMBOO_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BIRCH_CUTTING_BOARD, Items.BIRCH_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.CHERRY_CUTTING_BOARD, Items.CHERRY_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.CRIMSON_CUTTING_BOARD, Items.CRIMSON_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.DARK_OAK_CUTTING_BOARD, Items.DARK_OAK_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.JUNGLE_CUTTING_BOARD, Items.JUNGLE_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.OAK_CUTTING_BOARD, Items.OAK_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.WARPED_CUTTING_BOARD, Items.WARPED_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.MANGROVE_CUTTING_BOARD, Items.MANGROVE_PLANKS);


        // Override existing Cutting Board recipe.
        this.buildCuttingBoardRecipe(output, vectorwing.farmersdelight.common.registry.ModBlocks.CUTTING_BOARD, Items.SPRUCE_PLANKS);
    }

    private void buildCuttingBoardRecipe(@NotNull RecipeOutput output, Supplier<Block> cuttingBoard, ItemLike planks) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, cuttingBoard.get())
                .pattern("/##")
                .pattern("/##")
                .define('/', Items.STICK)
                .define('#', planks)
                .unlockedBy(getHasName(planks), has(planks))
                .save(output);
    }
}
