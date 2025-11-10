package com.phantomwing.choppersdelight.recipe.custom;

import javax.annotation.Nonnull;

import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.recipe.ModRecipes;
import com.phantomwing.choppersdelight.tags.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AddCuttingBoardPatternRecipe extends CustomRecipe {
    public AddCuttingBoardPatternRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@Nonnull CraftingInput inv, @Nonnull Level level) {
        ItemStack bannerStack = ItemStack.EMPTY;
        ItemStack cuttingBoardStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    if (!bannerStack.isEmpty()) {
                        return false;
                    }

                    bannerStack = stack;
                } else if (stack.is(ModTags.Items.CUTTING_BOARDS)) {

                    if (!cuttingBoardStack.isEmpty()) {
                        return false;
                    }

                    cuttingBoardStack = stack;
                } else {
                    return false;
                }
            }
        }

        return !bannerStack.isEmpty() && !cuttingBoardStack.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingInput inv, @Nonnull HolderLookup.Provider provider) {
        ItemStack bannerStack = ItemStack.EMPTY;
        ItemStack cuttingBoardStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    bannerStack = stack.copy();
                    bannerStack.setCount(1);
                } else if (stack.is(ModTags.Items.CUTTING_BOARDS)) {
                    cuttingBoardStack = stack.copy();
                }
            }
        }

        if (cuttingBoardStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = new ItemStack(ModItems.DECORATED_CUTTING_BOARD.get());
            stack.set(ModDataComponents.DECORATED_CUTTING_BOARD_DATA.get(),
                new DecoratedCuttingBoardData(cuttingBoardStack.copy(), bannerStack.copy()));

            return stack;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ADD_PATTERN_RECIPE.get();
    }
}