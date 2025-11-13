package com.phantomwing.choppersdelight.datagen.providers;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class LootTableProviderWithConditions implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final PackOutput.PathProvider pathProvider;
    private final Set<ResourceKey<LootTable>> requiredTables;
    private final List<SubProviderEntry> subProviders;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public LootTableProviderWithConditions(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.LOOT_TABLE);
        this.subProviders = subProviders;
        this.requiredTables = requiredTables;
        this.registries = registries;
    }

    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        return this.registries.thenCompose((p_323117_) -> this.run(output, p_323117_));
    }

    private CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider provider) {
        WritableRegistry<LootTable> writableregistry = new MappedRegistry<>(Registries.LOOT_TABLE, Lifecycle.experimental());
        Map<RandomSupport.Seed128bit, ResourceLocation> map = new Object2ObjectOpenHashMap<>();
        Map<ResourceKey<LootTable>, List<ICondition>> conditionsMap = new Object2ObjectOpenHashMap<>();

        this.getTables().forEach((subProviderEntry) -> subProviderEntry.provider().apply(provider).generate((resourceKey, builderWithConditions) -> {
            ResourceLocation sequencedId = sequenceIdForLootTable(resourceKey);

            ResourceLocation resourceLocation = map.put(RandomSequence.seedForKey(sequencedId), sequencedId);
            if (resourceLocation != null) {
                String var10000 = String.valueOf(resourceLocation);
                Util.logAndPauseIfInIde("Loot table random sequence seed collision on " + var10000 + " and " + String.valueOf(resourceKey.location()));
            }

            var conditions = builderWithConditions.conditions();
            conditionsMap.put(resourceKey, conditions);

            var builder = builderWithConditions.carrier();
            builder.setRandomSequence(sequencedId);
            LootTable loottable = builder.setParamSet(subProviderEntry.paramSet).build();

            writableregistry.register(resourceKey, loottable, RegistrationInfo.BUILT_IN);
        }));

        writableregistry.freeze();

        ProblemReporter.Collector problemreporter$collector = new ProblemReporter.Collector();
        HolderGetter.Provider holdergetter$provider = (new RegistryAccess.ImmutableRegistryAccess(List.of(writableregistry))).freeze().asGetterLookup();
        ValidationContext validationcontext = new ValidationContext(problemreporter$collector, LootContextParamSets.ALL_PARAMS, holdergetter$provider);

        this.validate(writableregistry, validationcontext, problemreporter$collector);

        Multimap<String, String> multimap = problemreporter$collector.get();
        if (!multimap.isEmpty()) {
            multimap.forEach((problemLoc, message) -> LOGGER.warn("Found validation problem in {}: {}", problemLoc, message));
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            return CompletableFuture.allOf(writableregistry.entrySet().stream().map((lootTableEntry) -> {
                ResourceKey<LootTable> resourceKey = lootTableEntry.getKey();
                LootTable loottable = lootTableEntry.getValue();
                Path path = this.pathProvider.json(resourceKey.location());

                // Convert LootTable to JSON.
                RegistryOps<JsonElement> registryops = provider.createSerializationContext(JsonOps.INSTANCE);
                JsonElement jsonelement = LootTable.DIRECT_CODEC.encodeStart(registryops, loottable).getOrThrow();

                JsonObject root = jsonelement.getAsJsonObject();

                // Add provided conditions to the JSON object (if there are any).
                List<ICondition> conditions = conditionsMap.get(resourceKey);
                ICondition.writeConditions(provider, root, conditions);

                // Save JSON object to file.
                return DataProvider.saveStable(output, root, path);

            }).toArray(CompletableFuture[]::new));
        }
    }

    public List<SubProviderEntry> getTables() {
        return this.subProviders;
    }

    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        for (ResourceKey<LootTable> resourcekey : Sets.difference(this.requiredTables, writableregistry.registryKeySet())) {
            problemreporter$collector.report("Missing built-in table: " + resourcekey.location());
        }

        writableregistry.holders().forEach((p_335195_) -> p_335195_.value().validate(validationcontext.setParams(p_335195_.value().getParamSet()).enterElement("{" + String.valueOf(p_335195_.key().location()) + "}", p_335195_.key())));
    }

    private static ResourceLocation sequenceIdForLootTable(ResourceKey<LootTable> lootTable) {
        return lootTable.location();
    }

    public final @NotNull String getName() {
        return "Loot Tables With Conditions";
    }

    public record SubProviderEntry(Function<HolderLookup.Provider, LootTableSubProviderWithConditions> provider, LootContextParamSet paramSet) {
    }
}
