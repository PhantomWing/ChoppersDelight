package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.datagen.loot.ModBlockLootTables;
import com.phantomwing.choppersdelight.datagen.providers.LootTableProviderWithConditions;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider {
    public static LootTableProviderWithConditions create(PackOutput output) {
        return new LootTableProviderWithConditions(
                output,
                Set.of(),
                List.of(new LootTableProviderWithConditions.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK))
        );
    }
}