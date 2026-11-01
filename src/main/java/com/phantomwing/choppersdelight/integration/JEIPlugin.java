package com.phantomwing.choppersdelight.integration;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

@JeiPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
    private static final Identifier ID = Identifier.fromNamespaceAndPath(ChoppersDelight.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        BlockUtils.getCuttingBoards().forEach((block) -> {
            registration.addCraftingStation(FDRecipeTypes.CUTTING, new ItemStack(block));
        });
    }
}