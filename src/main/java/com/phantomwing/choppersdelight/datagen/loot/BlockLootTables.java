package com.phantomwing.choppersdelight.datagen.loot;

import com.phantomwing.choppersdelight.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

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
            .map(DeferredHolder::value);
    }

    private void dropSelf(Supplier<Block> blockSupplier) {
        var block = blockSupplier.get();
        this.map.put(block.getLootTable(), createCuttingBoardLootTable(block));
    }

    public LootTable.Builder createCuttingBoardLootTable(ItemLike item) {
        return LootTable.lootTable()
            .withPool(
                this.applyExplosionCondition(item, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(item)))
            );
    }
}
