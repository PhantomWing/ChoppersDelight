package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChoppersDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModBlocks.CUTTING_BOARDS.forEach(this::simpleBlock);
    }

    private void simpleBlock(Supplier<Block> item) {
        Block block = item.get();
        this.withExistingParent(ChoppersDelight.MOD_ID + ":" + BlockUtils.getName(block), BlockUtils.getResourceLocation(block));
    }
}
