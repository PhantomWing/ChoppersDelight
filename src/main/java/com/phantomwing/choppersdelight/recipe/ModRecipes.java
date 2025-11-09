package com.phantomwing.choppersdelight.recipe;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.recipe.custom.AddCuttingBoardPatternRecipe;
import com.phantomwing.choppersdelight.recipe.custom.RemoveCuttingBoardPatternRecipe;
import com.phantomwing.choppersdelight.utils.RegisterUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class ModRecipes {
    public static final Supplier<RecipeSerializer<AddCuttingBoardPatternRecipe>> ADD_PATTERN_RECIPE =
            registerRecipe("add_cutting_board_pattern", new SimpleCraftingRecipeSerializer<>(AddCuttingBoardPatternRecipe::new));

    public static final Supplier<RecipeSerializer<RemoveCuttingBoardPatternRecipe>> REMOVE_PATTERN_RECIPE =
            registerRecipe("remove_cutting_board_pattern", new SimpleCraftingRecipeSerializer<>(RemoveCuttingBoardPatternRecipe::new));

    private static <T extends Recipe<?>>Supplier<RecipeSerializer<T>> registerRecipe(String name, RecipeSerializer<T> serializer) {
        return registerRecipe(name, () -> serializer);
    }

    private static <T extends Recipe<?>>Supplier<RecipeSerializer<T>> registerRecipe(String name, Supplier<RecipeSerializer<T>> supplier) {
        return RegisterUtils.register(name, supplier, BuiltInRegistries.RECIPE_SERIALIZER);
    }

    public static void registerModRecipes() {
        ChoppersDelight.LOGGER.info("Registering recipes for " + ChoppersDelight.MOD_ID);
    }
}
