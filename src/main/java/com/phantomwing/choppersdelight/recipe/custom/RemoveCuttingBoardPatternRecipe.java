package com.phantomwing.choppersdelight.recipe.custom;

import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import com.phantomwing.choppersdelight.recipe.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class RemoveCuttingBoardPatternRecipe extends CustomRecipe {
    public RemoveCuttingBoardPatternRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level level) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
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
    public ItemStack assemble(@Nonnull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
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
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList
                .withSize(inv.getContainerSize(), ItemStack.EMPTY);

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
        return ModRecipes.REMOVE_PATTERN_RECIPE.get();
    }
}