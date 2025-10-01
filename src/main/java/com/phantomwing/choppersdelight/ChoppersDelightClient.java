package com.phantomwing.choppersdelight;

import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChoppersDelightClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Stream<Block> blockStream = ModBlocks.MINECRAFT_CUTTING_BOARDS.stream().map(Supplier::get);

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            blockStream = Stream.concat(blockStream, ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.stream().map(Supplier::get));
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            blockStream = Stream.concat(blockStream, ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.stream().map(Supplier::get));
        }

        List<Block> blocks = blockStream.toList();
        ((BlockEntityTypeAccessor) ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get())
                .getBlocks()
                .addAll(blocks);
    }
}