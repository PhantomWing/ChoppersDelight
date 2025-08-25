package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.block.BasketBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {
    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ChoppersDelight.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach((supplier) -> cuttingBoardBlock(supplier, ChoppersDelight.MOD_ID));
        ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach((supplier) -> cuttingBoardBlock(supplier, Compatibility.BIOMES_O_PLENTY_MOD_ID));
        ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach((supplier) -> cuttingBoardBlock(supplier, Compatibility.BIOMES_WEVE_GONE_MOD_ID));

        cuttingBoardBlock(ModBlocks.DECORATED_CUTTING_BOARD, ChoppersDelight.MOD_ID);
    }

    private void cuttingBoardBlock(Supplier<Block> supplier, String modId) {
        Block block = supplier.get();
        customHorizontalBlock(block,
                $ -> BlockUtils.getModel(models(), modId, block), BasketBlock.WATERLOGGED);
    }

    // Helper functions.
    private void customHorizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
                        .build(), ignored);
    }
}
