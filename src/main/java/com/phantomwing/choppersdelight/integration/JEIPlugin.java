package com.phantomwing.choppersdelight.integration;

import com.phantomwing.choppersdelight.utils.BlockUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

@JeiPlugin
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        BlockUtils.getCuttingBoards().forEach((block) -> {
            registration.addRecipeCatalyst(new ItemStack(block), FDRecipeTypes.CUTTING);
        });
    }
}