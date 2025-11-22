package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;

public class ModBlockModelProvider extends BlockModelProvider {
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChoppersDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach((supplier) -> this.cuttingBoardModel(supplier, ChoppersDelight.MOD_ID));

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach((supplier) -> this.cuttingBoardModel(supplier, Compatibility.BIOMES_O_PLENTY_MOD_ID));
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach((supplier) -> this.cuttingBoardModel(supplier, Compatibility.BIOMES_WEVE_GONE_MOD_ID));
        }
    }

    private BlockModelBuilder cuttingBoardModel(Supplier<Block> blockSupplier, String modId) {
        Block block = blockSupplier.get();
        ResourceLocation blockTexture = BlockUtils.getResourceLocation(modId, blockSupplier.get());
        String blockModelName = modId + ":" + BlockUtils.getName(block);
        ResourceLocation parentModel = BlockUtils.getResourceLocation(FarmersDelight.MODID, vectorwing.farmersdelight.common.registry.ModBlocks.CUTTING_BOARD.get());

        return this.withExistingParent(blockModelName, parentModel)
            .texture("particle", blockTexture)
            .texture("top", blockTexture);
    }
}
