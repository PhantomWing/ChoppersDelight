package com.phantomwing.choppersdelight.recipe;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.recipe.custom.AddCuttingBoardPatternRecipe;
import com.phantomwing.choppersdelight.recipe.custom.RemoveCuttingBoardPatternRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ChoppersDelight.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AddCuttingBoardPatternRecipe>> ADD_PATTERN_RECIPE =
            registerRecipe("add_cutting_board_pattern", new SimpleCraftingRecipeSerializer<>(AddCuttingBoardPatternRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RemoveCuttingBoardPatternRecipe>> REMOVE_PATTERN_RECIPE =
            registerRecipe("remove_cutting_board_pattern", new SimpleCraftingRecipeSerializer<>(RemoveCuttingBoardPatternRecipe::new));

    private static <T extends Recipe<?>> DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> registerRecipe(String name, RecipeSerializer<T> serializer) {
        return RECIPE_SERIALIZERS.register(name, () -> serializer);
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
