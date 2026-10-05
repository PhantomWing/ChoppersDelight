package com.phantomwing.choppersdelight.gametest;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

/**
 * The calls a test makes whose shape differs between Minecraft versions, behind one signature, so the
 * test bodies copy across lines unchanged.
 */
public final class TestCompat {
    private TestCompat() {
    }

    /** Builds a creative tab's contents with the server's registries, as a server-side mod would. */
    public static void buildContents(CreativeModeTab tab, ServerLevel level) {
        tab.buildContents(new CreativeModeTab.ItemDisplayParameters(level.enabledFeatures(), true, level.registryAccess()));
    }

    /** What a crafting grid holding these stacks in one row makes, or empty when no recipe matches. */
    public static ItemStack craft(ServerLevel level, ItemStack... stacks) {
        CraftingInput input = CraftingInput.of(stacks.length, 1, List.of(stacks));
        return level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level)
                .map(recipe -> recipe.value().assemble(input, level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }
}
