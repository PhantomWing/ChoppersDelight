package com.phantomwing.choppersdelight.tags;

import com.phantomwing.choppersdelight.ChoppersDelight;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    // Block tags
    public static class Blocks {
        public static final TagKey<Block> CUTTING_BOARDS = tag("cutting_boards");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.fromNamespaceAndPath(ChoppersDelight.MOD_ID, name));
        }
    }

    // Item tags
    public static class Items {
        public static final TagKey<Item> CUTTING_BOARDS = tag("cutting_boards");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(ChoppersDelight.MOD_ID, name));
        }
    }
}