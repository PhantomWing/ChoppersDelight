package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChoppersDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach((supplier) -> simpleBlock(supplier, ChoppersDelight.MOD_ID));

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach((supplier) -> simpleBlock(supplier, Compatibility.BIOMES_O_PLENTY_MOD_ID));
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach((supplier) -> simpleBlock(supplier, Compatibility.BIOMES_WEVE_GONE_MOD_ID));
        }
    }

    private void simpleBlock(Supplier<Block> blockSupplier, String modId) {
        Block block = blockSupplier.get();
        this.withExistingParent(modId + ":" + BlockUtils.getName(block), BlockUtils.getResourceLocation(modId, block));
    }
}