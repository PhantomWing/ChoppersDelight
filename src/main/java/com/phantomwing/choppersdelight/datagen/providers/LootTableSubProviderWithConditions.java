package com.phantomwing.choppersdelight.datagen.providers;


import com.phantomwing.choppersdelight.utils.WithConditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LootTableSubProviderWithConditions {
    void generate(BiConsumer<ResourceLocation, WithConditions<LootTable.Builder>> var1);
}