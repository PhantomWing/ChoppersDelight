package com.phantomwing.choppersdelight.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class BlockUtils {
    public static String getName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public static ResourceLocation getResourceLocation(String modId, Block block) {
        return ResourceLocation.fromNamespaceAndPath(modId, "block/" + getName(block));
    }
}
