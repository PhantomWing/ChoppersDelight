package com.phantomwing.choppersdelight.recipe.custom;

import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.recipe.ModRecipes;
import com.phantomwing.choppersdelight.tags.ModTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class AddCuttingBoardPatternRecipe extends CustomRecipe {
    public AddCuttingBoardPatternRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level level) {
        ItemStack bannerStack = ItemStack.EMPTY;
        ItemStack cuttingBoardStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
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
    public ItemStack assemble(@Nonnull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        ItemStack bannerStack = ItemStack.EMPTY;
        ItemStack cuttingBoardStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    bannerStack = stack.copy();
                    bannerStack.setCount(1);
                } else if (stack.is(ModTags.Items.CUTTING_BOARDS)) {
                    cuttingBoardStack = stack.copy();
                    cuttingBoardStack.setCount(1);
                }
            }
        }

        if (!cuttingBoardStack.isEmpty() && !bannerStack.isEmpty()) {
            ItemStack itemstack = new ItemStack(ModItems.DECORATED_CUTTING_BOARD.get());

            CompoundTag decoratedData = itemstack.getOrCreateTagElement(ModDataComponents.DECORATED_CUTTING_BOARD_DATA);
            decoratedData.put(ModDataComponents.DECORATED_CUTTING_BOARD_CUTTING_BOARD_DATA, cuttingBoardStack.save(new CompoundTag()));
            decoratedData.put(ModDataComponents.DECORATED_CUTTING_BOARD_BANNER_DATA, bannerStack.save(new CompoundTag()));

            return itemstack;
        }

        return ItemStack.EMPTY;
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