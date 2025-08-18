package com.phantomwing.choppersdelight.datagen.loot;

import com.phantomwing.choppersdelight.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

public class BlockLootTables extends BlockLootSubProvider {
    public BlockLootTables(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    // Actually add our loot tables.
    @Override
    protected void generate() {
        ModBlocks.CUTTING_BOARDS.forEach((this::dropSelf));
        dropSelf(ModBlocks.DECORATED_CUTTING_BOARD);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries()
                .stream()
                .map(e -> (Block) e.value())
                .toList();
    }

    protected void dropSelf(Supplier<Block> blockSupplier) {
        var block = blockSupplier.get();
        this.dropOther(block, block);
    }
}
