package com.phantomwing.choppersdelight.datagen.loot;

import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.datagen.providers.LootTableSubProviderWithConditions;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import com.phantomwing.choppersdelight.utils.WithConditions;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModBlockLootTables implements LootTableSubProviderWithConditions {
    protected final Set<Item> explosionResistant;
    protected final FeatureFlagSet enabledFeatures;
    protected final Map<ResourceLocation, WithConditions<LootTable.Builder>> map;

    public ModBlockLootTables() {
        this.explosionResistant = Set.of();
        this.enabledFeatures = FeatureFlags.REGISTRY.allFlags();
        this.map = new HashMap<>();
    }

    // Actually add our loot tables.
    protected void generateLootTables() {
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach(this::dropSelf);

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach((blockSupplier) -> this.dropSelfOptional(blockSupplier, Compatibility.BIOMES_O_PLENTY_MOD_ID));
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach((blockSupplier) -> this.dropSelfOptional(blockSupplier, Compatibility.BIOMES_WEVE_GONE_MOD_ID));
        }

        dropNothing(ModBlocks.DECORATED_CUTTING_BOARD);
    }

    protected @NotNull Iterable<Block> getKnownBlocks() {
        Stream<Block> blocks = getKnownBlocksForRegistry(ModBlocks.BLOCKS);

        // Compatibility blocks.
        if (Compatibility.IsBiomesOPlentyLoaded()) {
            Stream<Block> biomesOPlentyBlocks = getKnownBlocksForRegistry(ModBlocks.BIOMES_O_PLENTY_BLOCKS);
            blocks = Stream.concat(blocks, biomesOPlentyBlocks);
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            Stream<Block> biomesWeveGoneBlocks = getKnownBlocksForRegistry(ModBlocks.BIOMES_WEVE_GONE_BLOCKS);
            blocks = Stream.concat(blocks, biomesWeveGoneBlocks);
        }

        return blocks.toList();
    }

    private Stream<Block> getKnownBlocksForRegistry(DeferredRegister<Block> registry) {
        return registry.getEntries()
                .stream()
                .map(RegistryObject::get);
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

    public void add(ResourceLocation modifier, LootTable.Builder instance) {
        this.map.put(modifier, new WithConditions(instance));
    }

    public void add(ResourceLocation modifier, LootTable.Builder instance, ICondition... conditions) {
        this.map.put(modifier, new WithConditions(instance, conditions));
    }

    public LootTable.Builder createCuttingBoardLootTable(ItemLike item) {
        return LootTable.lootTable()
            .withPool(
                this.applyExplosionCondition(item, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(item)))
            );
    }

    protected <T extends ConditionUserBuilder<T>> @NotNull T applyExplosionCondition(ItemLike item, ConditionUserBuilder<T> conditionBuilder) {
        return !this.explosionResistant.contains(item.asItem()) ? conditionBuilder.when(ExplosionCondition.survivesExplosion()) : conditionBuilder.unwrap();
    }

    public void generate(BiConsumer<ResourceLocation, WithConditions<LootTable.Builder>> output) {
        this.generateLootTables();

        Set<ResourceLocation> set = new HashSet<>();

        for(Block block : this.getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                ResourceLocation resourcekey = block.getLootTable();
                if (resourcekey != BuiltInLootTables.EMPTY && set.add(resourcekey)) {
                    WithConditions<LootTable.Builder> loottable$builder = this.map.remove(resourcekey);
                    if (loottable$builder == null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", resourcekey, ForgeRegistries.BLOCKS.getKey(block)));
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