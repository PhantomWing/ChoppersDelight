package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.datagen.loot.BlockLootTables;
import com.phantomwing.choppersdelight.datagen.providers.LootTableProviderWithConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider {
    public static LootTableProviderWithConditions create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProviderWithConditions(
            output,
            Set.of(),
            List.of(new LootTableProviderWithConditions.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)),
            lookupProvider
        );
    }
}
