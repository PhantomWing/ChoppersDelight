package com.phantomwing.choppersdelight.recipe.custom;

import javax.annotation.Nonnull;

import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

public class RemoveCuttingBoardPatternRecipe extends CustomRecipe {

    public static final RecipeSerializer<RemoveCuttingBoardPatternRecipe> REMOVE_PATTERN =
            new SimpleCraftingRecipeSerializer<>(RemoveCuttingBoardPatternRecipe::new);

    public RemoveCuttingBoardPatternRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@Nonnull CraftingInput inv, @Nonnull Level level) {
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

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingInput inv, @Nonnull HolderLookup.Provider provider) {
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

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
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

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return REMOVE_PATTERN;
    }
}