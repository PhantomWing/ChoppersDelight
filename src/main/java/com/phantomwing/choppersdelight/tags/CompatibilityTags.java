package com.phantomwing.choppersdelight.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * References to tags belonging to other mods, which Farmer's Delight innately supports.
 * These tags are used by other mods for their own mechanics.
 */
public class CompatibilityTags
{
    // Create
    public static final String CREATE = "create";
    public static final TagKey<Block> CREATE_BRITTLE = externalBlockTag(CREATE, "brittle");

    private static TagKey<Item> externalItemTag(String modId, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, path));
    }

    private static TagKey<Block> externalBlockTag(String modId, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, path));
    }
}
