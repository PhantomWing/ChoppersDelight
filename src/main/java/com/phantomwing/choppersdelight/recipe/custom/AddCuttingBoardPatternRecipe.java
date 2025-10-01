package com.phantomwing.choppersdelight.recipe.custom;

import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.tags.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class AddCuttingBoardPatternRecipe extends CustomRecipe {

    public static final RecipeSerializer<AddCuttingBoardPatternRecipe> ADD_PATTERN =
            new SimpleCraftingRecipeSerializer<>(AddCuttingBoardPatternRecipe::new);

    public AddCuttingBoardPatternRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
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

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider) {
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

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ADD_PATTERN;
    }
}