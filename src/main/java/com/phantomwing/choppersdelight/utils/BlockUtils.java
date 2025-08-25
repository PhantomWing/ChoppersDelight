package com.phantomwing.choppersdelight.utils;

import com.phantomwing.choppersdelight.ChoppersDelight;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class BlockUtils {
    public static String getName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public static ResourceLocation getResourceLocation(String modId, Block block) {
        return ResourceLocation.fromNamespaceAndPath(modId, "block/" + getName(block));
    }

    public static ModelFile getModel(BlockModelProvider provider, String modId, Block block) {
        return new ModelFile.ExistingModelFile(getResourceLocation(modId, block), provider.existingFileHelper);
    }
}
