package com.phantomwing.choppersdelight.datagen.providers;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.phantomwing.choppersdelight.utils.WithConditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class LootTableProviderWithConditions implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final PackOutput.PathProvider pathProvider;
    private final Set<ResourceLocation> requiredTables;
    private final List<LootTableProviderWithConditions.SubProviderEntry> subProviders;

    public LootTableProviderWithConditions(PackOutput output, Set<ResourceLocation> requiredTables, List<SubProviderEntry> subProviders) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
        this.subProviders = subProviders;
        this.requiredTables = requiredTables;
    }

    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        final Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        Map<RandomSupport.Seed128bit, ResourceLocation> map1 = new Object2ObjectOpenHashMap<>();
        Map<ResourceLocation, List<ICondition>> conditionsMap = new Object2ObjectOpenHashMap<>();

        this.getTables().forEach((subProviderEntry) -> {
            subProviderEntry.provider().get().generate((key, builderWithConditions) -> {
                ResourceLocation resourcelocation1 = map1.put(RandomSequence.seedForKey(key), key);
                if (resourcelocation1 != null) {
                    Util.logAndPauseIfInIde("Loot table random sequence seed collision on " + resourcelocation1 + " and " + key);
                }

                builderWithConditions.carrier().setRandomSequence(key);
                if (map.put(key, builderWithConditions.carrier().setParamSet(subProviderEntry.paramSet).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + key);
                }

                var conditions = builderWithConditions.conditions();
                conditionsMap.put(resourcelocation1, conditions);
            });
        });

        ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, new LootDataResolver() {
            @Nullable
            public <T> T getElement(@NotNull LootDataId<T> p_279283_) {
                return (T) (p_279283_.type() == LootDataType.TABLE ? map.get(p_279283_.location()) : null);
            }
        });

        validate(map, validationcontext);

        Multimap<String, String> multimap = validationcontext.getProblems();
        if (!multimap.isEmpty()) {
            multimap.forEach((p_124446_, p_124447_) -> {
                LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_);
            });
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            return CompletableFuture.allOf(map.entrySet().stream().map((lootTableEntry) -> {
                ResourceLocation resourcelocation1 = lootTableEntry.getKey();
                LootTable loottable = lootTableEntry.getValue();
                Path path = this.pathProvider.json(resourcelocation1);

                // Convert LootTable to JSON.
                JsonElement jsonelement = LootDataType.TABLE.parser().toJsonTree(loottable);

                JsonObject root = jsonelement.getAsJsonObject();

                // Add provided conditions to the JSON object (if there are any).
                List<ICondition> conditions = conditionsMap.get(resourcelocation1);
                WithConditions.writeConditions(root, conditions);


                return DataProvider.saveStable(pOutput, root, path);
            }).toArray(CompletableFuture[]::new));
        }
    }

    public List<LootTableProviderWithConditions.SubProviderEntry> getTables() {
        return this.subProviders;
    }

    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {
        for(ResourceLocation resourcelocation : Sets.difference(this.requiredTables, map.keySet())) {
            validationcontext.reportProblem("Missing built-in table: " + resourcelocation);
        }

        map.forEach((p_278897_, p_278898_) -> {
            p_278898_.validate(validationcontext.setParams(p_278898_.getParamSet()).enterElement("{" + p_278897_ + "}", new LootDataId<>(LootDataType.TABLE, p_278897_)));
        });
    }

    public final @NotNull String getName() {
        return "Loot Tables With Conditions";
    }

    public record SubProviderEntry(Supplier<LootTableSubProviderWithConditions> provider, LootContextParamSet paramSet) {
    }
}
