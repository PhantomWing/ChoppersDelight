package com.phantomwing.choppersdelight.datagen.loot;

import com.phantomwing.choppersdelight.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
        Stream<Block> blocks = getKnownBlocksForRegistry(ModBlocks.BLOCKS);

        // Compatibility blocks.
        Stream<Block> biomesOPlentyBlocks = getKnownBlocksForRegistry(ModBlocks.BIOMES_O_PLENTY_BLOCKS);
        Stream<Block> biomesWeveGoneBlocks = getKnownBlocksForRegistry(ModBlocks.BIOMES_WEVE_GONE_BLOCKS);

        Stream<Block> concat = Stream.concat(biomesOPlentyBlocks, biomesWeveGoneBlocks);
        return Stream.concat(blocks, concat).toList();
    }

    private Stream<Block> getKnownBlocksForRegistry(DeferredRegister.Blocks registry) {
        return registry.getEntries()
            .stream()
            .map(e -> (Block) e.value());
    }


    private void dropSelf(Supplier<Block> blockSupplier) {
        var block = blockSupplier.get();
        this.dropOther(block, block);
    }
}
