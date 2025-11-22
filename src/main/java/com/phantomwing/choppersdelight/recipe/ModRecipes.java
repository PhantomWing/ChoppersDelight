package com.phantomwing.choppersdelight.recipe;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.recipe.custom.AddCuttingBoardPatternRecipe;
import com.phantomwing.choppersdelight.recipe.custom.RemoveCuttingBoardPatternRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ChoppersDelight.MOD_ID);

    public static final RegistryObject<RecipeSerializer<AddCuttingBoardPatternRecipe>> ADD_PATTERN_RECIPE =
            registerRecipe("add_cutting_board_pattern", () -> new SimpleCraftingRecipeSerializer<>(AddCuttingBoardPatternRecipe::new));

    public static final RegistryObject<RecipeSerializer<RemoveCuttingBoardPatternRecipe>> REMOVE_PATTERN_RECIPE =
            registerRecipe("remove_cutting_board_pattern", () -> new SimpleCraftingRecipeSerializer<>(RemoveCuttingBoardPatternRecipe::new));

    private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> registerRecipe(String name, Supplier<? extends RecipeSerializer<T>> supplier) {
        return RECIPE_SERIALIZERS.register(name, supplier);
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
