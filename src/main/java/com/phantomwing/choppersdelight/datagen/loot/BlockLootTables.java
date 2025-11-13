package com.phantomwing.choppersdelight.datagen.loot;

import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.datagen.providers.LootTableSubProviderWithConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockLootTables implements LootTableSubProviderWithConditions {
    protected final HolderLookup.Provider registries;
    protected final Set<Item> explosionResistant;
    protected final FeatureFlagSet enabledFeatures;
    protected final Map<ResourceKey<LootTable>, WithConditions<LootTable.Builder>> map;

    public BlockLootTables(HolderLookup.Provider lookupProvider) {
        this.explosionResistant = Set.of();
        this.enabledFeatures = FeatureFlags.REGISTRY.allFlags();
        this.map = new HashMap<>();
        this.registries = lookupProvider;
    }

    // Actually add our loot tables.
    protected void generateLootTables() {
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach(this::dropSelf);
        ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach((blockSupplier) -> this.dropSelfOptional(blockSupplier, Compatibility.BIOMES_O_PLENTY_MOD_ID));
        ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach((blockSupplier) -> this.dropSelfOptional(blockSupplier, Compatibility.BIOMES_WEVE_GONE_MOD_ID));

        dropNothing(ModBlocks.DECORATED_CUTTING_BOARD);
    }

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

    private void dropNothing(Supplier<Block> blockSupplier) {
        var block = blockSupplier.get();
        this.add(block.getLootTable(), LootTable.lootTable());
    }

    private void dropSelf(Supplier<Block> blockSupplier) {
        var block = blockSupplier.get();
        this.add(block.getLootTable(), createCuttingBoardLootTable(block));
    }

    private void dropSelfOptional(Supplier<Block> blockSupplier, String modId) {
        var block = blockSupplier.get();
        this.add(block.getLootTable(), createCuttingBoardLootTable(block), new ModLoadedCondition(modId));
    }

    public void add(ResourceKey<LootTable> modifier, LootTable.Builder instance, List<ICondition> conditions) {
        this.map.put(modifier, new WithConditions(conditions, instance));
    }

    public void add(ResourceKey<LootTable> modifier, LootTable.Builder instance, ICondition... conditions) {
        this.add(modifier, instance, Arrays.asList(conditions));
    }

    public LootTable.Builder createCuttingBoardLootTable(ItemLike item) {
        return LootTable.lootTable()
            .withPool(
                this.applyExplosionCondition(item, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(item)))
            );
    }

    protected <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike item, ConditionUserBuilder<T> conditionBuilder) {
        return !this.explosionResistant.contains(item.asItem()) ? conditionBuilder.when(ExplosionCondition.survivesExplosion()) : conditionBuilder.unwrap();
    }

    public void generate(BiConsumer<ResourceKey<LootTable>, WithConditions<LootTable.Builder>> output) {
        this.generateLootTables();

        Set<ResourceKey<LootTable>> set = new HashSet<>();

        for(Block block : this.getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                ResourceKey<LootTable> resourcekey = block.getLootTable();
                if (resourcekey != BuiltInLootTables.EMPTY && set.add(resourcekey)) {
                    WithConditions<LootTable.Builder> loottable$builder = this.map.remove(resourcekey);
                    if (loottable$builder == null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", resourcekey.location(), BuiltInRegistries.BLOCK.getKey(block)));
                    }

                    output.accept(resourcekey, loottable$builder);
                }
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
        }
    }
}
