package com.phantomwing.choppersdelight.recipe.custom;

import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import com.phantomwing.choppersdelight.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RemoveCuttingBoardPatternRecipe extends CustomRecipe {
    public RemoveCuttingBoardPatternRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, @NotNull Level level) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);

            if (!stack.isEmpty()) {

                if (!itemstack.isEmpty() || !(stack.getItem() instanceof DecoratedCuttingBoardItem)) {
                    return false;
                } else {
                    itemstack = stack.copy();
                }
            }
        }
        return !itemstack.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput inv, HolderLookup.@NotNull Provider provider) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);

            if (!stack.isEmpty()) {

                if (stack.getItem() instanceof DecoratedCuttingBoardItem) {
                    itemstack = stack.copy();
                    break;
                }
            }
        }

        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            return DecoratedCuttingBoardItem.getBannerStack(itemstack);
        }
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList
                .withSize(inv.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = inv.getItem(i);

            if (!item.isEmpty() && item.getItem() instanceof DecoratedCuttingBoardItem) {
                nonnulllist.set(i, DecoratedCuttingBoardItem.getCuttingBoardStack(item));
            }
        }
        return nonnulllist;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.REMOVE_PATTERN_RECIPE.get();
    }
}