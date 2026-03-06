package com.phantomwing.choppersdelight.utils;

import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.EveryCompatSetup;
import com.phantomwing.choppersdelight.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockUtils {
    public static String getName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public static ResourceLocation getResourceLocation(String modId, Block block) {
        return ResourceLocation.fromNamespaceAndPath(modId, "block/" + getName(block));
    }

    public static ModelFile getModel(BlockModelProvider provider, String modId, Block block) {
        return new ModelFile.ExistingModelFile(getResourceLocation(modId, block), provider.existingFileHelper);
    }

    public static Stream<Block> getCuttingBoards() {
        Stream<Block> blocks = ModBlocks.MINECRAFT_CUTTING_BOARDS.stream().map(Supplier::get);

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            blocks = Stream.concat(blocks, ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.stream().map(Supplier::get));
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            blocks = Stream.concat(blocks, ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.stream().map(Supplier::get));
        }

        if (Compatibility.IsEveryCompatLoaded()) {
            blocks = Stream.concat(blocks, EveryCompatSetup.getCuttingBoardBlocks());
        }

        return blocks;
    }
}
