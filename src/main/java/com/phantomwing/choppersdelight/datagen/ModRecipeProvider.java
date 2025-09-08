package com.phantomwing.choppersdelight.datagen;

import biomesoplenty.api.block.BOPBlocks;
import com.phantomwing.choppersdelight.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.potionstudios.biomeswevegone.world.level.block.wood.BWGWood;
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

        // Biomes O' Plenty.
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_DEAD_CUTTING_BOARD, BOPBlocks.DEAD_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_EMPYREAL_CUTTING_BOARD, BOPBlocks.EMPYREAL_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_FIR_CUTTING_BOARD, BOPBlocks.FIR_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_HELLBARK_CUTTING_BOARD, BOPBlocks.HELLBARK_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_JACARANDA_CUTTING_BOARD, BOPBlocks.JACARANDA_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_MAGIC_CUTTING_BOARD, BOPBlocks.MAGIC_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_MAHOGANY_CUTTING_BOARD, BOPBlocks.MAHOGANY_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_MAPLE_CUTTING_BOARD, BOPBlocks.MAPLE_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_PALM_CUTTING_BOARD, BOPBlocks.PALM_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_PINE_CUTTING_BOARD, BOPBlocks.PINE_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_REDWOOD_CUTTING_BOARD, BOPBlocks.REDWOOD_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_UMBRAN_CUTTING_BOARD, BOPBlocks.UMBRAN_PLANKS);
        this.buildCuttingBoardRecipe(output, ModBlocks.BOP_WILLOW_CUTTING_BOARD, BOPBlocks.WILLOW_PLANKS);

        // Oh The Biomes We've Gone.
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_ASPEN_CUTTING_BOARD, BWGWood.ASPEN.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_BAOBAB_CUTTING_BOARD, BWGWood.BAOBAB.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_BLUE_ENCHANTED_CUTTING_BOARD, BWGWood.BLUE_ENCHANTED.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_CIKA_CUTTING_BOARD, BWGWood.CIKA.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_CYPRESS_CUTTING_BOARD, BWGWood.CYPRESS.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_EBONY_CUTTING_BOARD, BWGWood.EBONY.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_FIR_CUTTING_BOARD, BWGWood.FIR.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_FLORUS_CUTTING_BOARD, BWGWood.FLORUS.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_GREEN_ENCHANTED_CUTTING_BOARD, BWGWood.GREEN_ENCHANTED.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_HOLLY_CUTTING_BOARD, BWGWood.HOLLY.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_IRONWOOD_CUTTING_BOARD, BWGWood.IRONWOOD.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_JACARANDA_CUTTING_BOARD, BWGWood.JACARANDA.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_MAHOGANY_CUTTING_BOARD, BWGWood.MAHOGANY.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_MAPLE_CUTTING_BOARD, BWGWood.MAPLE.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_PALM_CUTTING_BOARD, BWGWood.PALM.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_PINE_CUTTING_BOARD, BWGWood.PINE.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD, BWGWood.RAINBOW_EUCALYPTUS.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_REDWOOD_CUTTING_BOARD, BWGWood.REDWOOD.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_SAKURA_CUTTING_BOARD, BWGWood.SAKURA.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_SKYRIS_CUTTING_BOARD, BWGWood.SKYRIS.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_SPIRIT_CUTTING_BOARD, BWGWood.SPIRIT.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_WHITE_MANGROVE_CUTTING_BOARD, BWGWood.WHITE_MANGROVE.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_WILLOW_CUTTING_BOARD, BWGWood.WILLOW.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_WITCH_HAZEL_CUTTING_BOARD, BWGWood.WITCH_HAZEL.planks());
        this.buildCuttingBoardRecipe(output, ModBlocks.BWG_ZELKOVA_CUTTING_BOARD, BWGWood.ZELKOVA.planks());

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
