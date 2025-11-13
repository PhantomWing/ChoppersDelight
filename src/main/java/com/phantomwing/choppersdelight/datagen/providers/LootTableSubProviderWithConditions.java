package com.phantomwing.choppersdelight.datagen.providers;


import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.conditions.WithConditions;

@FunctionalInterface
public interface LootTableSubProviderWithConditions {
    void generate(BiConsumer<ResourceKey<LootTable>, WithConditions<LootTable.Builder>> var1);
}